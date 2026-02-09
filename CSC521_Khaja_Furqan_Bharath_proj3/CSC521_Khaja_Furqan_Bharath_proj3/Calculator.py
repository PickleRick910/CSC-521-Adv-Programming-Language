import math

# Displays the current state of the stack visually
def show_stack(contents):
    """Display stack contents or indicate if empty, with a box-like visual format."""
    if len(contents) == 0:
        # Show empty stack
        print("-> Stack is empty and initialized")
        print("┌─────┐")
        print(f"|{'':^5}|")
        print("└─────┘")
    else:
        # Show stack elements, top to bottom
        print(f"-> Stack contents: {contents}")
        print("┌─────┐")
        for item in contents[::-1]:  # Reverse to display top of stack first
            print(f"|{item:^5}|")
            print("└─────┘")
    print("")  # Add spacing after display

# Tokenizes an infix expression into numbers, operators, and parentheses
def tokenize_formula(formula):
    """Split infix expression into a list of tokens (numbers, operators, parentheses)."""
    token_list = []
    current = ""
    for char in formula:
        # Build number tokens, including negative numbers
        if char.isdigit() or (char == '-' and (not current or current[-1] in "(/")):
            current += char
        else:
            # Save completed number token
            if current:
                token_list.append(current)
                current = ""
            # Save operators or parentheses as single tokens
            if char in "()+-*/":
                token_list.append(char)
    # Append any remaining number token
    if current:
        token_list.append(current)
    return token_list

# Converts an infix expression to prefix notation
def infix_to_prefix(formula):
    """Convert infix expression to prefix notation using a stack."""
    # Tokenize input expression
    tokens = tokenize_formula(formula)

    # Check if a character is an operator
    def is_op(char):
        """Return True if char is an operator (+, -, *, /)."""
        return (not char.isalpha()) and (not char.isdigit())

    # Define operator precedence
    def op_priority(char):
        """Return precedence level of operator (1 for +, -, 2 for *, /)."""
        if char == '-' or char == '+':
            return 1
        elif char == '*' or char == '/':
            return 2
        return 0

    # Reverse tokens for prefix processing
    reversed_tokens = tokens[::-1]

    # Swap parentheses in reversed expression
    for i in range(len(reversed_tokens)):
        if reversed_tokens[i] == '(':
            reversed_tokens[i] = ')'
        elif reversed_tokens[i] == ')':
            reversed_tokens[i] = '('

    # Enclose expression in parentheses for consistent processing
    expr = ['('] + reversed_tokens + [')']
    op_stack = []  # Stack for operators
    result = []    # List for prefix expression

    # Process each token
    for i in range(len(expr)):
        # Handle numbers, including negatives
        if expr[i].isdigit() or (expr[i][0] == '-' and len(expr[i]) > 1):
            num = int(expr[i])
            if num < 0:
                expr[i] = '^' + str(abs(num))  # Mark negative numbers with ^
            result.append(expr[i])
        # Push opening parenthesis to stack
        elif expr[i] == '(':
            op_stack.append(expr[i])
        # Pop operators until matching parenthesis
        elif expr[i] == ')':
            while op_stack[-1] != '(':
                result.append(op_stack.pop())
            op_stack.pop()  # Remove '('
        # Handle operators based on precedence
        else:
            while op_stack and op_stack[-1] != '(' and op_priority(expr[i]) < op_priority(op_stack[-1]):
                result.append(op_stack.pop())
            op_stack.append(expr[i])

    # Pop any remaining operators
    while op_stack:
        result.append(op_stack.pop())

    return result[::-1]  # Reverse to get final prefix expression

# Calculator class for stack-based prefix evaluation
class PrefixCalculator:
    """Class to evaluate arithmetic expressions in prefix notation with 8-bit constraints."""
    def __init__(self):
        self.has_overflow = False  # Flag to track overflow

    # Evaluates a prefix expression using a stack
    def compute_prefix(self, expr):
        """Evaluate prefix expression, handling 8-bit overflow and displaying stack states."""
        # Prepare tokens, converting ^n to -n
        processed = []
        expr = expr[::-1]  # Reverse for right-to-left scanning
        for token in expr:
            if token[0] == '^':
                processed.append('-' + token[1:])  # Convert negative number format
            else:
                processed.append(token)
        tokens = processed

        num_stack = []  # Stack for operands

        # Process each token
        for token in tokens:
            show_stack(num_stack)  # Display current stack state
            # Push numbers to stack
            if token.isdigit() or (token[0] == '-' and len(token) > 1):
                num_stack.append(int(token))
            else:
                # Pop operands for operator (right first, then left)
                right_val = num_stack.pop()
                left_val = num_stack.pop() if num_stack else right_val

                # Compute operation result
                result = self.perform_op(left_val, right_val, token)

                # Check for 8-bit overflow
                if result > 127 or result < -128:
                    result = result & 0xFF  # Keep lower 8 bits
                    self.has_overflow = True
                    if result > 127:
                        result -= 256  # Adjust for positive overflow
                    elif result < -128:
                        result += 256  # Adjust for negative overflow

                # Display operation details
                print(
                    f"-> Operator found: '{token}'\n"
                    f"   Popped values: {right_val} and {left_val}\n"
                    f"   Pushed result: {result}\n"
                )
                num_stack.append(result)

        # Return final result from stack
        return num_stack.pop() if num_stack else 0

    # Performs arithmetic operation based on operator
    def perform_op(self, left, right, op):
        """Apply operator to operands, handling division rounding and errors."""
        if op == '+':
            return right + left
        elif op == '-':
            return right - left
        elif op == '*':
            return right * left
        elif op == '/':
            if left == 0:
                print("## Division by Zero Error ##")
                exit(1)
            
            # Handle division with remainder adjustment
            quotient = right // left
            remainder = right % left
            
            # Adjust quotient for non-zero remainder based on signs
            if remainder != 0:
                if (left < 0) != (right < 0):  # Different signs
                    quotient -= 1  # Round down for negative results
                else:  # Same signs
                    quotient += 1  # Round up for positive results
                    
            return quotient

    # Checks for balanced parentheses
    def check_parens(self, formula):
        """Verify if parentheses in expression are balanced."""
        paren_stack = []
        for char in formula:
            if char == '(':
                paren_stack.append(char)
            elif char == ')':
                if not paren_stack:
                    return False  # No matching opening parenthesis
                paren_stack.pop()
        return not paren_stack  # True if balanced

    # Main method to process and evaluate expression
    def process(self, formula):
        """Process infix expression, convert to prefix, evaluate, and display results."""
        # Check for balanced parentheses
        if not self.check_parens(formula):
            print("Error: Parentheses are unbalanced.")
            return

        # Convert to prefix and evaluate
        prefix_expr = infix_to_prefix(formula)
        print(f"Prefix Notation: {' '.join(prefix_expr)}")
        final_result = self.compute_prefix(prefix_expr)

        # Report overflow if occurred
        if self.has_overflow:
            print("Overflow occurs!!")
        print(f"Result: {final_result}")

# Entry point for program
if __name__ == "__main__":
    """Create calculator instance and run evaluation."""
    calc = PrefixCalculator()
    user_input = input('Enter a valid arithmetic expression: ')
    print(f'Input expression: {user_input}')
    calc.process(user_input)
