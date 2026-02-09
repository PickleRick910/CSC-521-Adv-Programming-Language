This folder contains two programs that validate Sudoku solutions:

sudoku1.c - A C program using Pthreads to validate Sudoku.
sudoku2.java - A Java program using Threads to validate Sudoku.

How to Execute
1. Executing the C Program (sudoku1.c)

		Follow these steps to compile and run the C program:

				Open Command Prompt in the folder containing the sudoku1.c file.

				Compile the C program using GCC with the following command:
								gcc sudoku1.c -lpthread
				This command will compile the program and create an executable named a.exe (or just a on some systems).

				To run the executable, type:  a

				You will be prompted to enter the input file name.

					Enter the file name (e.g., input1.txt or input3.txt) and press Enter.

The program will validate the Sudoku solution and display whether the solution is valid or invalid.




2. Executing the Java Program (sudoku2.java)
		Follow these steps to compile and run the Java program:

			Open Command Prompt in the folder containing the sudoku2.java file.

			Compile the Java program using the following command:
						javac sudoku2.java

			This will compile the program and generate a sudoku2.class file.

			To run the compiled Java program, type: 
							java sudoku2
			You will be prompted to enter the input file name.

			Enter the file name (e.g., input1.txt or input3.txt) and press Enter.

		The program will validate the Sudoku solution and display whether the solution is valid or invalid.
		
		
Contribution: 

1. Mohammed Furqan: worked on the algorithm for checking rows and columns 
2. Kaifuddin Ahmed : for reading input file and checking the grids