#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define SIZE 9

int sodoko[SIZE][SIZE];
int outcome[27] = {0}; // Array to hold outcome for each thread
pthread_mutex_t m = PTHREAD_MUTEX_INITIALIZER; // Mutex for thread safety

typedef struct {
    int r;
    int col;
} Parameters;

// Declaring functions
void* RowChecking(void* par);
void* ColumnChecking(void* par);
void* checkSubGrid(void* par);
void readsodokoFile(const char* filename);
void* RowChecking(void* par) {
    Parameters* p = (Parameters*) par;
    int r = p->r;
    int s[SIZE] = {0};

    for (int i = 0; i < SIZE; i++) {
        int n = sodoko[r][i];
        if (n < 1 || n > SIZE || s[n - 1] == 1) {
            pthread_mutex_lock(&m);
            outcome[r] = 0;
            pthread_mutex_unlock(&m);
            return NULL;
        }
        s[n - 1] = 1;
    }

    pthread_mutex_lock(&m);
    outcome[r] = 1;
    pthread_mutex_unlock(&m);
    return NULL;
}
void* ColumnChecking(void* par) {
    Parameters* p = (Parameters*) par;
    int col = p->col;
    int s[SIZE] = {0};

    for (int i = 0; i < SIZE; i++) {
        int n = sodoko[i][col];
        if (n < 1 || n > SIZE || s[n - 1] == 1) {
            pthread_mutex_lock(&m);
            outcome[9 + col] = 0;
            pthread_mutex_unlock(&m);
            return NULL;
        }
        s[n - 1] = 1;
    }

    pthread_mutex_lock(&m);
    outcome[9 + col] = 1;
    pthread_mutex_unlock(&m);
    return NULL;
}
void* checkSubGrid(void* par) {
    Parameters* p = (Parameters*) par;
    int Srow = p->r;
    int Scol = p->col;
    int s[SIZE] = {0};

    for (int i = Srow; i < Srow + 3; i++) {
        for (int j = Scol; j < Scol + 3; j++) {
            int n = sodoko[i][j];
            if (n < 1 || n > SIZE || s[n - 1] == 1) {
                pthread_mutex_lock(&m);
                outcome[18 + (Srow / 3) * 3 + (Scol / 3)] = 0;
                pthread_mutex_unlock(&m);
                return NULL;
            }
            s[n - 1] = 1;
        }
    }

    pthread_mutex_lock(&m);
    outcome[18 + (Srow / 3) * 3 + (Scol / 3)] = 1;
    pthread_mutex_unlock(&m);
    return NULL;
}
void readsodokoFile(const char* filename) {
    FILE* file = fopen(filename, "r");
    if (!file) {
        printf("Error: file %s cannot be opened\n", filename);
        exit(EXIT_FAILURE);
    }
    for (int i = 0; i < SIZE; i++) {
        for (int j = 0; j < SIZE; j++) {
            
            if (fscanf(file, "%d%*[^0-9]", &sodoko[i][j]) != 1) {
                printf("Error: Invalid format in file %s\n", filename);
                fclose(file);
                exit(EXIT_FAILURE);
            }
        }
    }

    fclose(file);
}

int main() {
    char filename[100];
    printf("Enter the input file name: ");
    scanf("%s", filename);

    readsodokoFile(filename);

    pthread_t threads[27];
    Parameters p[27];
    int threadIndex = 0;
    for (int i = 0; i < SIZE; i++) {
        p[threadIndex].r = i;
        p[threadIndex].col = 0;
        pthread_create(&threads[threadIndex], NULL, RowChecking, &p[threadIndex]);
        threadIndex++;

        p[threadIndex].r = 0;
        p[threadIndex].col = i;
        pthread_create(&threads[threadIndex], NULL, ColumnChecking, &p[threadIndex]);
        threadIndex++;
    }
    for (int i = 0; i < SIZE; i += 3) {
        for (int j = 0; j < SIZE; j += 3) {
            p[threadIndex].r = i;
            p[threadIndex].col = j;
            pthread_create(&threads[threadIndex], NULL, checkSubGrid, &p[threadIndex]);
            threadIndex++;
        }
    }
    for (int i = 0; i < threadIndex; i++) {
        pthread_join(threads[i], NULL);
    }
    for (int i = 0; i < 27; i++) {
        if (outcome[i] == 0) {
            printf("The Sudoku solution is invalid.\n");
            return 0;
        }
    }

    printf("The Sudoku solution is valid.\n");
    return 0;
}
