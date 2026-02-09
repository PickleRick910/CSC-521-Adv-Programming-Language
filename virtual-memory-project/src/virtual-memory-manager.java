import java.io.*;
import java.util.*;

public class VirtualMemorySimulator {
    private static final int PAGE_SIZE = 256;
    private static final int PAGE_TABLE_ENTRIES = 256;
    private static final int TLB_ENTRIES = 16;
    private static final int FRAME_SIZE = 256;
    private static final int OFFSET_BITS = 8;
    private static final int PAGE_BITS = 8;

    private final byte[] physicalMemory;
    private final int[] pageTable;
    private final int[][] tlb;
    private int pageFaultCount;
    private int tlbHitCount;
    private int totalAddresses;
    private final RandomAccessFile backingStore;

    public VirtualMemorySimulator() throws IOException {
        physicalMemory = new byte[FRAME_SIZE * PAGE_TABLE_ENTRIES];
        pageTable = new int[PAGE_TABLE_ENTRIES];
        Arrays.fill(pageTable, -1);
        tlb = new int[TLB_ENTRIES][2];
        for (int[] row : tlb) {
            Arrays.fill(row, -1);
        }
        backingStore = new RandomAccessFile("BACKING_STORE", "r");
    }

    public void processAddress(int logicalAddress) throws IOException {
        totalAddresses++;
        int pageNumber = (logicalAddress >>> OFFSET_BITS) & ((1 << PAGE_BITS) - 1);
        int offset = logicalAddress & ((1 << OFFSET_BITS) - 1);
        int frameNumber = lookupTLB(pageNumber);

        if (frameNumber == -1) {
            frameNumber = pageTable[pageNumber];
            if (frameNumber == -1) {
                frameNumber = handlePageFault(pageNumber);
            }
            updateTLB(pageNumber, frameNumber);
        } else {
            tlbHitCount++;
        }

        int physicalAddress = (frameNumber << OFFSET_BITS) | offset;
        byte value = physicalMemory[(frameNumber * PAGE_SIZE) + offset];

        System.out.printf("Logical: %d, Physical: %d, Value: %d%n", 
                          logicalAddress, physicalAddress, value);
    }

    private int lookupTLB(int pageNumber) {
        for (int[] entry : tlb) {
            if (entry[0] == pageNumber) {
                return entry[1];
            }
        }
        return -1;
    }

    private void updateTLB(int pageNumber, int frameNumber) {
        System.arraycopy(tlb, 1, tlb, 0, TLB_ENTRIES - 1);
        tlb[TLB_ENTRIES - 1][0] = pageNumber;
        tlb[TLB_ENTRIES - 1][1] = frameNumber;
    }

    private int handlePageFault(int pageNumber) throws IOException {
        pageFaultCount++;
        int frameNumber = findFreeFrame();
        if (frameNumber == -1) {
            frameNumber = 0;  // Simple replacement strategy
        }

        backingStore.seek((long) pageNumber * PAGE_SIZE);
        backingStore.read(physicalMemory, frameNumber * PAGE_SIZE, PAGE_SIZE);
        pageTable[pageNumber] = frameNumber;
        return frameNumber;
    }

    private int findFreeFrame() {
        return Arrays.stream(pageTable)
                     .filter(frame -> frame == -1)
                     .findFirst()
                     .orElse(-1);
    }

    public void printStatistics() {
        System.out.printf("Total Addresses: %d%n", totalAddresses);
        System.out.printf("Page Faults: %d%n", pageFaultCount);
        System.out.printf("Page Fault Rate: %.3f%n", (double) pageFaultCount / totalAddresses);
        System.out.printf("TLB Hits: %d%n", tlbHitCount);
        System.out.printf("TLB Hit Rate: %.3f%n", (double) tlbHitCount / totalAddresses);
    }

    public void close() throws IOException {
        backingStore.close();
    }
}
