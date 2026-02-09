package virtualmemory.components;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class TLBCache {
    private static final int TLB_SIZE = 16; // Fixed size for the TLB
    private final int[][] tlbEntries;       // Each entry holds [page number, frame number]
    private final Queue<Integer> fifoQueue;
    private final Map<Integer, Long> lruMap;
    private boolean useLRU;

    public TLBCache(boolean useLRU) {
        this.useLRU = useLRU;
        this.tlbEntries = new int[TLB_SIZE][2];
        for (int[] entry : tlbEntries) {
            entry[0] = -1; // Initialize page number as -1 (indicates empty)
            entry[1] = -1; // Initialize frame number as -1 (indicates empty)
        }
        this.fifoQueue = new LinkedList<>();
        this.lruMap = new HashMap<>();
    }

    public int lookup(int pageNumber) {
        for (int i = 0; i < TLB_SIZE; i++) {
            if (tlbEntries[i][0] == pageNumber) {
                if (useLRU) {
                    lruMap.put(i, System.nanoTime()); // Update LRU timestamp
                }
                return tlbEntries[i][1]; // Return the frame number
            }
        }
        return -1; // Page not found in TLB
    }

    public void update(int pageNumber, int frameNumber) {
        int replaceIndex = findReplacementIndex();
        
        tlbEntries[replaceIndex][0] = pageNumber;
        tlbEntries[replaceIndex][1] = frameNumber;

        if (useLRU) {
            lruMap.put(replaceIndex, System.nanoTime()); // Set LRU timestamp
        } else {
            fifoQueue.add(replaceIndex); // Add to FIFO queue
        }
    }

    private int findReplacementIndex() {
        if (useLRU) {
            return lruMap.entrySet().stream()
                    .min(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(0); // Default to the first entry if no min found
        } else {
            if (fifoQueue.size() >= TLB_SIZE) {
                return fifoQueue.poll(); // Remove oldest entry in FIFO
            } else {
                return fifoQueue.size(); // Use next available index
            }
        }
    }
}
