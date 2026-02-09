# Remove Comments from a C Program

This is a JavaScript program that removes comments from a C program. It can handle both single-line (`//`) and multi-line (`/* ... */`) comments. Additionally, it detects invalid comments and provides an error message if any are found.

---

## **Features**
1. Removes valid single-line comments (`// ...`).
2. Removes valid multi-line comments (`/* ... */`).
3. Detects invalid comments (e.g., incomplete `/*` or `*/`) and prints an error message.
4. Reads a C file as input and outputs the modified program to the console.

---

## **How to Use**

### **Prerequisites**
1. **Node.js**: Ensure Node.js is installed on your system. You can download it from [here](https://nodejs.org/).
2. **C File**: Prepare a C file (e.g., `input.c`) that you want to process.

### **Steps to Run the Program**
1. **Save the JavaScript File**:
   - Save the provided JavaScript code in a file named `test1.js`.

2. **Prepare the C File**:
   - Create a C file (e.g., `input.c`) in the same directory as the JavaScript file.
   - Add your C code to this file.

3. **Run the Program**:
   - Open a terminal or command prompt.
   - Navigate to the directory where the JavaScript and C files are located.
   - Run the program using the following command:
     ```bash
     node test1.js
     ```

4. **View the Output**:
   - The program will read the C file, remove valid comments, and print the modified code to the console.
   - If invalid comments are detected, an error message will be displayed.

---
