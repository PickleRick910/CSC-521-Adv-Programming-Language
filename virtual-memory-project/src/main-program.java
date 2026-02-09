import java.io.*;
import java.util.Arrays;

public class MemoryManagementSimulation {
    public static void main(String[] args) {
        try {
            runVirtualMemorySimulation();
            evaluateReplacementStrategies();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void runVirtualMemorySimulation() throws IOException {
        VirtualMemorySimulator simulator = new VirtualMemorySimulator();
        try (BufferedReader reader = new BufferedReader(new FileReader("InputFile.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int logicalAddress = Integer.parseInt(line.trim());
                simulator.processAddress(logicalAddress);
            }
        }
        simulator.printStatistics();
        simulator.close();
    }

    private static void evaluateReplacementStrategies() {
        PageSequenceCreator creator = new PageSequenceCreator(9);
        int[] referenceString = creator.createSequence(1000);
        int[] frameCounts = {4, 8, 16};

        Arrays.stream(frameCounts).forEach(frameCount -> {
            System.out.printf("Testing with %d frames:%n", frameCount);
            testStrategy(new FIFOStrategy(frameCount), referenceString, "FIFO");
            testStrategy(new LRUStrategy(frameCount), referenceString, "LRU");
            System.out.println();
        });

        // Test with biased sequence
        int[] biasedSequence = creator.createBiasedSequence(1000, 70);
        System.out.println("Testing with biased sequence (70% bias):");
        testStrategy(new FIFOStrategy(8), biasedSequence, "FIFO");
        testStrategy(new LRUStrategy(8), biasedSequence, "LRU");
    }

    private static void testStrategy(MemoryReplacementStrategy strategy, int[] referenceString, String name) {
        Arrays.stream(referenceString).forEach(strategy::processPage);
        System.out.printf("%s - Page Faults: %d%n", name, strategy.getPageMisses());
    }
}
