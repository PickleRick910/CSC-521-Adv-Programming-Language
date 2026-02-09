#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define SIZE 9

int sudoku[SIZE][SIZE];
int results[27] = {0}; // Array to hold results for each thread
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER; // Mutex for thread safety

typedef struct {
    int row;
    int col;
} Parameters;

// Function to check a row
void* checkRow(void* param) {
    Parameters* params = (Parameters*) param;
    int row = params->row;
    int seen[SIZE] = {0};

    for (int i = 0; i < SIZE; i++) {
        int num = sudoku[row][i];
        if (num < 1 || num > SIZE || seen[num - 1] == 1) {
            pthread_mutex_lock(&mutex);
            results[row] = 0;
            pthread_mutex_unlock(&mutex);
            return NULL;
        }
        seen[num - 1] = 1;
    }

    pthread_mutex_lock(&mutex);
    results[row] = 1;
    pthread_mutex_unlock(&mutex);
    return NULL;
}

// Function to check a column
void* checkColumn(void* param) {
    Parameters* params = (Parameters*) param;
    int col = params->col;
    int seen[SIZE] = {0};

    for (int i = 0; i < SIZE; i++) {
        int num = sudoku[i][col];
        if (num < 1 || num > SIZE || seen[num - 1] == 1) {
            pthread_mutex_lock(&mutex);
            results[9 + col] = 0;
            pthread_mutex_unlock(&mutex);
            return NULL;
        }
        seen[num - 1] = 1;
    }

    pthread_mutex_lock(&mutex);
    results[9 + col] = 1;
    pthread_mutex_unlock(&mutex);
    return NULL;
}

// Function to check a 3x3 grid
void* checkSubGrid(void* param) {
    Parameters* params = (Parameters*) param;
    int startRow = params->row;
    int startCol = params->col;
    int seen[SIZE] = {0};

    for (int i = startRow; i < startRow + 3; i++) {
        for (int j = startCol; j < startCol + 3; j++) {
            int num = sudoku[i][j];
            if (num < 1 || num > SIZE || seen[num - 1] == 1) {
                pthread_mutex_lock(&mutex);
                results[18 + (startRow / 3) * 3 + (startCol / 3)] = 0;
                pthread_mutex_unlock(&mutex);
                return NULL;
            }
            seen[num - 1] = 1;
        }
    }

    pthread_mutex_lock(&mutex);
    results[18 + (startRow / 3) * 3 + (startCol / 3)] = 1;
    pthread_mutex_unlock(&mutex);
    return NULL;
}

// Function to read the Sudoku from a file
void readSudokuFile(const char* filename) {
    FILE* file = fopen(filename, "r");
    if (!file) {
        printf("Error: Cannot open file %s\n", filename);
        exit(EXIT_FAILURE);
    }

    for (int i = 0; i < SIZE; i++) {
        for (int j = 0; j < SIZE; j++) {
            fscanf(file, "%d", &sudoku[i][j]);
        }
    }

    fclose(file);
}

int main() {
    char filename[100];
    printf("Enter the input file name: ");
    scanf("%s", filename);

    readSudokuFile(filename);

    pthread_t threads[27];
    Parameters params[27];
    int threadIndex = 0;

    // Create threads to check rows and columns
    for (int i = 0; i < SIZE; i++) {
        params[threadIndex].row = i;
        params[threadIndex].col = 0;
        pthread_create(&threads[threadIndex], NULL, checkRow, &params[threadIndex]);
        threadIndex++;

        params[threadIndex].row = 0;
        params[threadIndex].col = i;
        pthread_create(&threads[threadIndex], NULL, checkColumn, &params[threadIndex]);
        threadIndex++;
    }

    // Create threads to check each 3x3 grid
    for (int i = 0; i < SIZE; i += 3) {
        for (int j = 0; j < SIZE; j += 3) {
            params[threadIndex].row = i;
            params[threadIndex].col = j;
            pthread_create(&threads[threadIndex], NULL, checkSubGrid, &params[threadIndex]);
            threadIndex++;
        }
    }

    // Join all threads
    for (int i = 0; i < threadIndex; i++) {
        pthread_join(threads[i], NULL);
    }

    // Check the results
    for (int i = 0; i < 27; i++) {
        if (results[i] == 0) {
            printf("The Sudoku solution is invalid.\n");
            return 0;
        }
    }

    printf("The Sudoku solution is valid.\n");
    return 0;
}
