/**
 * Producer Consumer
 * using counting semaphores as well as a mutex lock.
 */

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>
#include <unistd.h>
#include <time.h>

#define TRUE 1
#define BUFFER_SIZE 5
#define MAX_SLEEP 5

#define MIN 0  //minimum value of an element in buffer
#define MAX 1000 //maximum value of an element in buffer

int buffer[BUFFER_SIZE];

pthread_mutex_t mutex;
sem_t empty;
sem_t full;

int insertPointer = 0, removePointer = 0;

void *producer(void *param);
void *consumer(void *param);

int myRand() {
   return rand() % (MAX - MIN + 1) + MIN;
}

int print_buffer(){
    int i;
    for (i=0; i< BUFFER_SIZE; i++)
        printf("slot %d: %d\n", i, buffer[i]);
    printf("\n");    
    return 0;
}

int init_buffer(){
    int i;
    for (i=0; i< BUFFER_SIZE; i++)
        buffer[i] = -1;
    return 0;
}

int insert_item(int item, int prodID, int TryTime)
{
    sem_wait(&empty);
    pthread_mutex_lock(&mutex);

    buffer[insertPointer] = item;
    insertPointer = (insertPointer + 1) % BUFFER_SIZE;

    printf("Producer %d produced %d at time %d\n", prodID, item, (int)time(NULL));
    print_buffer();

    pthread_mutex_unlock(&mutex);
    sem_post(&full);

    return 0;
}

int remove_item(int ConsumerID, int TryTime)
{
    int item;

    sem_wait(&full);
    pthread_mutex_lock(&mutex);

    item = buffer[removePointer];
    buffer[removePointer] = -1;
    removePointer = (removePointer + 1) % BUFFER_SIZE;

    printf("Consumer %d consumed %d at time %d\n", ConsumerID, item, (int)time(NULL));
    print_buffer();

    pthread_mutex_unlock(&mutex);
    sem_post(&empty);

    return 0;
}

int main(int argc, char *argv[])
{
    int sleepTime, producerThreads, consumerThreads;
    int i, j;

    if(argc != 4)
    {
        fprintf(stderr, "Usage: <sleep time> <producer threads> <consumer threads>\n");
        return -1;
    }

    sleepTime = atoi(argv[1]);
    producerThreads = atoi(argv[2]);
    consumerThreads = atoi(argv[3]);

    /* Initialize buffer to be -1 */
    init_buffer();

    /* Initialize the synchronization tools */
    pthread_mutex_init(&mutex, NULL);
    sem_init(&empty, 0, BUFFER_SIZE);
    sem_init(&full, 0, 0);
    srand(time(NULL));

    /* Create the producer and consumer threads */
    for(i = 0; i < producerThreads; i++)
    {
        pthread_t tid;
        pthread_attr_t attr;
        pthread_attr_init(&attr);
        int *id = malloc(sizeof(int));
        *id = i;
        pthread_create(&tid, &attr, producer, id);
    }

    for(j = 0; j < consumerThreads; j++)
    {
        pthread_t tid;
        pthread_attr_t attr;
        pthread_attr_init(&attr);
        int *id = malloc(sizeof(int));
        *id = j;
        pthread_create(&tid, &attr, consumer, id);
    }

    /* Sleep for user specified time */
    sleep(sleepTime);
    return 0;
}

void *producer(void *param)
{
    int random;
    int r;
    int prodID = *(int*)param;
    free(param);

    while(TRUE)
    {
        r = rand() % MAX_SLEEP;
        sleep(r);
        random = myRand();

        int TryTime = (int)time(NULL);
        printf("Producer %d tries to insert %d at time %d\n", prodID, random, TryTime);
        if(insert_item(random, prodID, TryTime))
            fprintf(stderr, "Error");
    }
}

void *consumer(void *param)
{
    int r;
    int ConsumerID = *(int*)param;
    free(param);

    while(TRUE)
    {
        r = rand() % MAX_SLEEP;
        sleep(r);

        int TryTime = (int)time(NULL);
        printf("Consumer %d tries to consume at time %d\n", ConsumerID, TryTime);

        if(remove_item(ConsumerID, TryTime))
            fprintf(stderr, "Error Consuming");
    }
}