package virtualmemory;

import virtualmemory.core.AddressTranslator;
import virtualmemory.utils.AddressResult;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java virtualmemory.Main <InputFile.txt>");
            return;
        }

        String inputFilePath = args[0];
        boolean useLimitedFrames = false; // Set to true to use 128 frames instead of 256
        boolean useLRUForPages = false;   // Set to true to use LRU for page replacement
        boolean useLRUForTLB = false;     // Set to true to use LRU for TLB replacement

        // Initialize the AddressTranslator with the desired configuration
        AddressTranslator translator = new AddressTranslator(useLimitedFrames, useLRUForPages, useLRUForTLB);

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int logicalAddress = Integer.parseInt(line.trim());
                AddressResult result = translator.translateAddress(logicalAddress);

                System.out.printf("Logical: %d, Physical: %d, Value: %d\n",
                    result.getLogicalAddress(), result.getPhysicalAddress(), result.getValue());
            }

            // Print statistics and the page table after processing all addresses
            System.out.println("\nTranslation complete. Here are the statistics:");
            translator.printStatistics();
            translator.printPageTable();

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        } finally {
            // Close the translator to release any resources (like the backing store)
            translator.close();
        }
    }
}
