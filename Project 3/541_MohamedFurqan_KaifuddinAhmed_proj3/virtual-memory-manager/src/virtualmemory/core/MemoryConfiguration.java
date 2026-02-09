package virtualmemory.core;

public class MemoryConfiguration {
    private final int pageSize;
    private final int frameSize;
    private final int totalFrames;
    private final boolean useLimitedFrames;
    private final boolean useEnhancedLRU;

    public MemoryConfiguration(int pageSize, int frameSize, int totalFrames, boolean useLimitedFrames, boolean useEnhancedLRU) {
        this.pageSize = pageSize;
        this.frameSize = frameSize;
        this.totalFrames = totalFrames;
        this.useLimitedFrames = useLimitedFrames;
        this.useEnhancedLRU = useEnhancedLRU;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getFrameSize() {
        return frameSize;
    }

    public int getTotalFrames() {
        return totalFrames;
    }

    public boolean isUseLimitedFrames() {
        return useLimitedFrames;
    }

    public boolean isUseEnhancedLRU() {
        return useEnhancedLRU;
    }

    @Override
    public String toString() {
        return "MemoryConfiguration{" +
                "pageSize=" + pageSize +
                ", frameSize=" + frameSize +
                ", totalFrames=" + totalFrames +
                ", useLimitedFrames=" + useLimitedFrames +
                ", useEnhancedLRU=" + useEnhancedLRU +
                '}';
    }
}
