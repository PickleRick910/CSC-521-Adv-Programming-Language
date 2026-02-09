// File: remove_comments_from_file.js
const fs = require('fs');

/**
 * Removes valid comments from a C program and detects invalid comments.
 * @param {string} input - The input C program as a string.
 * @returns {string} - The C program with valid comments removed.
 */
function removeComments(input) {
  // Step 1: Remove valid single-line comments (//)
  let output = input.replace(/\/\/.*$/gm, '');

  // Step 2: Remove valid multi-line comments (/* ... */)
  output = output.replace(/\/\*[\s\S]*?\*\//g, '');

  // Step 3: Check for invalid comments
  const hasInvalidSingleLine = /\/\*.*?\*\//g.test(output); // Detects invalid single-line comments
  const hasInvalidMultiLine = /\/\*|\*\//g.test(output); // Detects invalid multi-line comments

  if (hasInvalidSingleLine || hasInvalidMultiLine) {
    console.error('Error: Invalid comments detected in the C program.');
  } else {
    console.log('Valid comments removed successfully. Output:');
  }

  return output;
}

/**
 * Reads a C file, removes comments, and prints the result to the console.
 * @param {string} filePath - The path to the C file.
 */
function processCFile(filePath) {
  // Read the C file
  fs.readFile(filePath, 'utf8', (err, data) => {
    if (err) {
      console.error('Error reading the file:', err);
      return;
    }

    // Remove comments from the file content
    const output = removeComments(data);

    // Print the output to the console
    console.log(output);
  });
}

// Path to the C file (replace with your file path)
const cFilePath = 'detect.c';

// Process the C file
processCFile(cFilePath);