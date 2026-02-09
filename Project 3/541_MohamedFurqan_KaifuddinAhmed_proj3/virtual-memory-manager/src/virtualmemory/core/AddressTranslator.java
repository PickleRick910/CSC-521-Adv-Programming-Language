package virtualmemory.core;

import virtualmemory.components.PageTable;
import virtualmemory.components.TLBCache;
import virtualmemory.components.PhysicalMemory;
import virtualmemory.utils.BackingStore;
import virtualmemory.utils.AddressResult;

public class AddressTranslator {
    private static final int OFFSET_BITS = 8; // Each page and frame is 256 bytes (2^8)
    private final PageTable pageTable;
    private final TLBCache tlb;
    private final PhysicalMemory physicalMemory;
    private final BackingStore backingStore;
    private final MemoryStatistics stats;
    private int nextFrame;

    public AddressTranslator(boolean useLimitedFrames, boolean useLRUForPages, boolean useLRUForTLB) {
        // Initialize components
        this.pageTable = new PageTable(new MemoryConfiguration(256, 256, useLimitedFrames ? 128 : 256, useLimitedFrames, useLRUForPages));
        this.tlb = new TLBCache(useLRUForTLB);
        this.physicalMemory = new PhysicalMemory(useLimitedFrames ? 128 : 256);
        this.backingStore = new BackingStore();
        this.stats = new MemoryStatistics();
        this.nextFrame = 0;
    }

    /**
     * Translates a logical address to a physical address.
     * @param logicalAddress The logical address to translate.
     * @return The result of the translation including the physical address and byte value.
     */
    public AddressResult translateAddress(int logicalAddress) {
        stats.incrementTotalAccesses();
        
        int pageNumber = (logicalAddress >> OFFSET_BITS) & 0xFF;
        int offset = logicalAddress & 0xFF;
        int frameNumber = checkTLB(pageNumber);
        boolean tlbHit = frameNumber != -1;
        
        if (!tlbHit) {
            frameNumber = pageTable.getFrame(pageNumber);
            
            // Handle page fault
            if (frameNumber == -1) {
                frameNumber = handlePageFault(pageNumber);
            }
            
            // Update TLB with the new page-to-frame mapping
            tlb.update(pageNumber, frameNumber);
        } else {
            stats.incrementTLBHits();
        }
        
        int physicalAddress = (frameNumber * 256) + offset;
        byte value = physicalMemory.readByte(frameNumber, offset);
        
        return new AddressResult(logicalAddress, physicalAddress, value);
    }

    /**
     * Checks the TLB for a page number.
     * @param pageNumber The page number to search for.
     * @return The frame number if the page is found in the TLB, otherwise -1.
     */
    private int checkTLB(int pageNumber) {
        return tlb.lookup(pageNumber);
    }

    /**
     * Handles a page fault by loading the requested page from the backing store.
     * @param pageNumber The page number that caused the fault.
     * @return The frame number where the page was loaded.
     */
    private int handlePageFault(int pageNumber) {
        stats.incrementPageFaults();
        
        // Load page from backing store
        byte[] pageData = backingStore.readPage(pageNumber);
        
        // Allocate a frame for the page
        int frameNumber = physicalMemory.allocateFrame();
        if (frameNumber == -1) {
            // If no free frames, use page replacement in PageTable
            frameNumber = pageTable.selectVictimFrame();
            pageTable.removePage(frameNumber);
        }
        
        physicalMemory.writeFrame(frameNumber, pageData);
        pageTable.mapPage(pageNumber, frameNumber);
        
        return frameNumber;
    }

    /**
     * Closes resources used by AddressTranslator.
     */
    public void close() {
        backingStore.close();
    }

    /**
     * Prints the memory statistics for this translation session.
     */
    public void printStatistics() {
        System.out.println(stats);
    }

    /**
     * Prints the current page table for debugging purposes.
     */
    public void printPageTable() {
        pageTable.display();
    }
}
