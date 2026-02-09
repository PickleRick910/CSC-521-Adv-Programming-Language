# Virtual Memory Management Project - Setup and Execution Guide

## 1. Project Structure
First, create a project directory with the following structure:
```
virtual-memory-project/
├── src/
│   ├── PageSequenceCreator.java
│   ├── MemoryReplacementStrategy.java
│   ├── FIFOStrategy.java
│   ├── LRUStrategy.java
│   ├── VirtualMemorySimulator.java
│   └── MemoryManagementSimulation.java
├── data/
│   ├── BACKING_STORE
│   └── InputFile.txt
└── README.md
```

## 2. Creating Required Files

### 2.1 Generate BACKING_STORE
Create a Python script named `create_backing_store.py`:

```python
with open("data/BACKING_STORE", "wb") as f:
    # Create 256 pages of 256 bytes each
    for i in range(256):
        for j in range(256):
            # Write values from -128 to 127 in a repeating pattern
            f.write(((i + j) % 256 - 128).to_bytes(1, byteorder='signed'))
```

### 2.2 Generate InputFile.txt
Create a Python script named `create_input_file.py`:

```python
import random

with open("data/InputFile.txt", "w") as f:
    # Generate 1000 random logical addresses (0-65535)
    for _ in range(1000):
        address = random.randint(0, 65535)
        f.write(f"{address}\n")
```

## 3. Compilation and Execution

### 3.1 Compile the Java Files
Open a terminal in the project directory and run:
```bash
javac -d out src/*.java
```

### 3.2 Run the Program
```bash
java -cp out MemoryManagementSimulation
```

## 4. Testing Different Scenarios

### 4.1 Basic Test
The program will automatically:
- Process addresses from InputFile.txt
- Show virtual-to-physical address translations
- Display page fault and TLB hit statistics
- Compare FIFO and LRU algorithms

### 4.2 Custom Tests
You can modify the test parameters in MemoryManagementSimulation.java:
- Change frame counts in the `frameCounts` array
- Adjust the bias percentage in `createBiasedSequence()`
- Modify the reference string length

## 5. Expected Output
The program will display:
1. Address translations:
```
Logical: 16916, Physical: 20, Value: 0
Logical: 62493, Physical: 285, Value: 0
...
```

2. Statistics:
```
Total Addresses: 1000
Page Faults: 244
Page Fault Rate: 0.244
TLB Hits: 55
TLB Hit Rate: 0.055
```

3. Page Replacement Analysis:
```
Testing with 4 frames:
FIFO - Page Faults: 244
LRU - Page Faults: 238

Testing with 8 frames:
FIFO - Page Faults: 220
LRU - Page Faults: 212
...
```

## 6. Troubleshooting

### Common Issues:
1. FileNotFoundException: Ensure BACKING_STORE and InputFile.txt are in the correct location
2. OutOfMemoryError: Reduce the physical memory size or increase JVM heap size
3. IllegalArgumentException: Check that input addresses are within valid range (0-65535)

### Solutions:
1. Run with increased memory:
```bash
java -Xmx512m -cp out MemoryManagementSimulation
```

2. Debug mode with detailed output:
```bash
java -cp out MemoryManagementSimulation -debug
```

## 7. Modifications and Extensions

### 7.1 Add Custom Page Replacement Algorithm
1. Create a new class extending MemoryReplacementStrategy
2. Implement the processPage method
3. Add the new algorithm to the evaluation in main

### 7.2 Change Physical Memory Size
Modify PAGE_TABLE_ENTRIES in VirtualMemorySimulator.java

### 7.3 Adjust TLB Size
Modify TLB_ENTRIES in VirtualMemorySimulator.java
