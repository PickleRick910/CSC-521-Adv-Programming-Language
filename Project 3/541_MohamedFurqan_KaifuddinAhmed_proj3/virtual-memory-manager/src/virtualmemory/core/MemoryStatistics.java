package virtualmemory.core;

public class MemoryStatistics {
    private int totalAccesses;
    private int pageFaults;
    private int tlbHits;

    public void incrementTotalAccesses() {
        totalAccesses++;
    }

    public void incrementPageFaults() {
        pageFaults++;
    }

    public void incrementTLBHits() {
        tlbHits++;
    }

    public double getPageFaultRate() {
        return totalAccesses > 0 ? (double) pageFaults / totalAccesses * 100 : 0;
    }

    public double getTlbHitRate() {
        return totalAccesses > 0 ? (double) tlbHits / totalAccesses * 100 : 0;
    }

    @Override
    public String toString() {
        return "MemoryStatistics{" +
                "totalAccesses=" + totalAccesses +
                ", pageFaults=" + pageFaults +
                ", tlbHits=" + tlbHits +
                ", pageFaultRate=" + getPageFaultRate() +
                "%, tlbHitRate=" + getTlbHitRate() +
                "%}";
    }
}
