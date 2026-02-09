import os
import random

def create_directory_structure():
    directories = ['src', 'data', 'out']
    for directory in directories:
        os.makedirs(directory, exist_ok=True)

def create_backing_store():
    print("Creating BACKING_STORE file...")
    with open("data/BACKING_STORE", "wb") as f:
        for i in range(256):
            for j in range(256):
                f.write(((i + j) % 256 - 128).to_bytes(1, byteorder='signed'))
    print("BACKING_STORE created successfully")

def create_input_file():
    print("Creating InputFile.txt...")
    with open("data/InputFile.txt", "w") as f:
        for _ in range(1000):
            address = random.randint(0, 65535)
            f.write(f"{address}\n")
    print("InputFile.txt created successfully")

def main():
    print("Setting up Virtual Memory Management Project...")
    create_directory_structure()
    create_backing_store()
    create_input_file()
    print("\nSetup completed successfully!")
    print("\nNext steps:")
    print("1. Copy your Java source files to the 'src' directory")
    print("2. Compile: javac -d out src/*.java")
    print("3. Run: java -cp out MemoryManagementSimulation")

if __name__ == "__main__":
    main()
