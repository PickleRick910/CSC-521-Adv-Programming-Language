# CSC521 - Project 3  
### Expression Evaluation Using a Stack-Based Calculator

---

## 👨‍💻 Authors
- Khaja AnwaruddinHamza LNU,Mohammed Furqan,Yesu Venkata Bharath Anumolu
-

---

## 🧾 Project Overview

This project implements a **stack-based calculator** that:
- Accepts arithmetic expressions in **infix notation**
- Converts them to **prefix notation**
- Evaluates the expression using a **stack**
- Handles **8-bit signed integer overflow** (range: -128 to 127)
- Supports **+**, **-**, **\***, **/** operators and **parentheses**
- Handles **unary minus** (as `^` in prefix)
- Includes extra credit: **unbalanced parentheses check**

---

## 🚀 How to Run

1. Make sure you have Python 3 installed.
2. Open a terminal and navigate to the project directory:
   ```bash
   cd CSC521_Khaja_Furqan_Bharath_proj3
   ```
3. Run the calculator:
   ```bash
   python3 Calculator.py
   ```
4. Input a valid arithmetic expression when prompted. Example:
   ```
   Enter a valid arithmetic expression: (-36+107)*5
   ```

---

## 📁 Project Structure

```
APL_Proj3/
├── Calculator.py          # Python script with the main logic
├── README.md              
└── Screenshots/
    ├── Test1.png
    ├── Test2.png
    ├── ...
    └── Test10.png
```

---

## 📸 Screenshots Included

All 10 test cases are captured and included in the `Screenshots/` folder.

| Test # | Expression               | Result  | Notes                        |
|--------|--------------------------|---------|------------------------------|
| 1      | 40+50                    | 90      | Basic addition               |
| 2      | 45+(-50)                 | -5      | Addition with negative       |
| 3      | 80+80                    | -96     | Overflow                    |
| 4      | (-36+107)\*5             | 99      | Overflow on multiplication   |
| 5      | -50-122                  | 84      | Overflow on subtraction      |
| 6      | -33\*3                   | -99     | Negative multiplication      |
| 7      | 101\*61                  | 17      | Overflow on multiplication   |
| 8      | -101\*61                 | -17     | Negative overflow            |
| 9      | -70/3                    | -25     | Negative division + rounding |
| 10     | -120/(-34)              | 4       | Negatives cancel out         |

---

## ✅ Features Demonstrated

- Accurate operator precedence and associativity
- Full parenthesis support
- Stack visualization during evaluation
- Prefix expression conversion
- Overflow handling via two’s complement simulation
- Handles negative values and unary minus
- Graceful handling of invalid parenthesis (extra credit)

---

## 💡 Notes

- All expressions are assumed to be valid unless parentheses are unbalanced.
- Overflow cases wrap as per **8-bit signed integer limits**.
- For example:
  - `(-36 + 107) * 5` = 355 → overflow → `99`
  - `80 + 80` = 160 → overflow → `-96`

---