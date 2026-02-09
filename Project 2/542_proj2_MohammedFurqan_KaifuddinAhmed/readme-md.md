# Producer-Consumer Problem Implementation

## Team Members and Contributions

1. Mohammed Furqan
   - Implemented the `insert_item()` function and producer thread logic
   - Wrote main() function and thread creation
   - Created test cases and documented results
   - Contributed to the README file

2. Kaifuddin Ahmed
   - Implemented the `remove_item()` function and consumer thread logic
   - Added producer/consumer IDs and timing information
   - Debugged synchronization issues
   - Documented observations and findings

## Compilation and Execution Instructions


1. Ensure you have a C compiler installed (e.g., MinGW).
2. Open Command Prompt and navigate to the project directory.
3. Compile the program:
   
   gcc -o buffer.exe buffer.c -lpthread

4. Run the program:
   
   buffer.exe <sleep_time> <producer_threads> <consumer_threads>

   Example: buffer.exe 20 5 5
   
5. To save output to a file:

   buffer.exe 20 5 5 > output.txt


   ```

## Program Behavior

- The program implements a producer-consumer scenario using a shared buffer.
- It uses semaphores and mutex locks for synchronization.
- Producers generate random integers and insert them into the buffer.
- Consumers remove integers from the buffer.
- The program prints the buffer state after each operation.
- Producer and consumer IDs are displayed for each action.
- Timestamps for attempts and successful operations are shown.






## Screenshots













