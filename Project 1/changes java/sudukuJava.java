import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class sudukuJava {

    static int[][] suduku = new int[9][9];
    static boolean[] outcome = new boolean[27]; // array to store results

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the input file name: ");
        String fname = input.nextLine();
        ReadInput(fname);

        Thread[] t = new Thread[27];
        int t_index = 0;
        for (int i = 0; i < 9; i++) {
            t[t_index] = new Thread(new RowCheck(i, t_index));
            t[t_index].start();
            t_index++;

            t[t_index] = new Thread(new ColCheck(i, t_index));
            t[t_index].start();
            t_index++;
        }
        for (int i = 0; i < 9; i += 3) {
            for (int j = 0; j < 9; j += 3) {
                t[t_index] = new Thread(new GridCheck(i, j, t_index));
                t[t_index].start();
                t_index++;
            }
        }
        for (int i = 0; i < t_index; i++) {
            try {
                t[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (boolean result : outcome) {
            if (!result) {
                System.out.println("The Sudoku solution is invalid.");
                return;
            }
        }

        System.out.println("The Sudoku solution is valid.");
    }

    static void ReadInput(String fname) {
        try {
            Scanner fileScanner = new Scanner(new File(fname));
            for (int i = 0; i < 9; i++) {
                // Read each line of the Sudoku grid
                String line = fileScanner.nextLine();
                // Split the line by either spaces or commas
                String[] numbers = line.split("[, ]+");
                for (int j = 0; j < 9; j++) {
                    suduku[i][j] = Integer.parseInt(numbers[j]);
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot open file " + fname);
            System.exit(1);
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format in file " + fname);
            System.exit(1);
        }
    }

    static class RowCheck implements Runnable {
        private final int r;
        private final int in;

        RowCheck(int r, int in) {
            this.r = r;
            this.in = in;
        }

        public void run() {
            boolean[] checker = new boolean[9];
            for (int i = 0; i < 9; i++) {
                int numeric = suduku[r][i];
                if (numeric < 1 || numeric > 9 || checker[numeric - 1]) {
                    outcome[in] = false;
                    return;
                }
                checker[numeric - 1] = true;
            }
            outcome[in] = true;
        }
    }

    static class ColCheck implements Runnable {
        private final int col;
        private final int in;

        ColCheck(int col, int in) {
            this.col = col;
            this.in = in;
        }

        public void run() {
            boolean[] checker = new boolean[9];
            for (int i = 0; i < 9; i++) {
                int numeric = suduku[i][col];
                if (numeric < 1 || numeric > 9 || checker[numeric - 1]) {
                    outcome[in] = false;
                    return;
                }
                checker[numeric - 1] = true;
            }
            outcome[in] = true;
        }
    }

    static class GridCheck implements Runnable {
        private final int r;
        private final int col;
        private final int in;

        GridCheck(int r, int col, int in) {
            this.r = r;
            this.col = col;
            this.in = in;
        }

        public void run() {
            boolean[] checker = new boolean[9];
            for (int i = r; i < r + 3; i++) {
                for (int j = col; j < col + 3; j++) {
                    int numeric = suduku[i][j];
                    if (numeric < 1 || numeric > 9 || checker[numeric - 1]) {
                        outcome[in] = false;
                        return;
                    }
                    checker[numeric - 1] = true;
                }
            }
            outcome[in] = true;
        }
    }
}
