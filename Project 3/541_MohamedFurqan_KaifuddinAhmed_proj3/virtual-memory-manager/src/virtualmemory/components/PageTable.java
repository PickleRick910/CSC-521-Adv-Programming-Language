package virtualmemory.components;

import virtualmemory.core.MemoryConfiguration;
import java.util.*;

public class PageTable {
    private final int[] pageFrameMap;
    private final Queue<Integer> fifoQueue;
    private final Map<Integer, Long> lruTimestamps;
    private final boolean useEnhancedLRU;

    public PageTable(MemoryConfiguration config) {
        this.pageFrameMap = new int[256];
        Arrays.fill(pageFrameMap, -1);
        this.useEnhancedLRU = config.isUseEnhancedLRU();
        this.fifoQueue = new LinkedList<>();
        this.lruTimestamps = new HashMap<>();
    }

    public synchronized int getFrame(int pageNumber) {
        int frameNumber = pageFrameMap[pageNumber];
        if (frameNumber != -1 && useEnhancedLRU) {
            lruTimestamps.put(frameNumber, System.nanoTime());
        }
        return frameNumber;
    }

    public synchronized void mapPage(int pageNumber, int frameNumber) {
        pageFrameMap[pageNumber] = frameNumber;
        fifoQueue.offer(frameNumber);
        if (useEnhancedLRU) {
            lruTimestamps.put(frameNumber, System.nanoTime());
        }
    }

    public synchronized int selectVictimFrame() {
        if (useEnhancedLRU) {
            return lruTimestamps.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(-1);
        }
        return fifoQueue.poll();
    }

    public synchronized void removePage(int frameNumber) {
        for (int i = 0; i < pageFrameMap.length; i++) {
            if (pageFrameMap[i] == frameNumber) {
                pageFrameMap[i] = -1;
                break;
            }
        }
        lruTimestamps.remove(frameNumber);
    }

    public void display() {
        System.out.println("\nPage Table Contents:");
        for (int i = 0; i < pageFrameMap.length; i++) {
            if (pageFrameMap[i] != -1) {
                System.out.printf("Page %d -> Frame %d%n", i, pageFrameMap[i]);
            }
        }
    }
}
