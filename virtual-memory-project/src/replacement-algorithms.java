import java.util.*;

public abstract class MemoryReplacementStrategy {
    protected final int[] frameContents;
    protected int pageMisses;
    protected final int frameCount;

    public MemoryReplacementStrategy(int frameCount) {
        this.frameCount = frameCount;
        this.frameContents = new int[frameCount];
        Arrays.fill(frameContents, -1);
        this.pageMisses = 0;
    }

    public abstract void processPage(int pageNumber);

    public int getPageMisses() {
        return pageMisses;
    }

    protected boolean isPageInMemory(int pageNumber) {
        return Arrays.stream(frameContents).anyMatch(frame -> frame == pageNumber);
    }
}

public class FIFOStrategy extends MemoryReplacementStrategy {
    private int nextReplaceIndex;

    public FIFOStrategy(int frameCount) {
        super(frameCount);
        this.nextReplaceIndex = 0;
    }

    @Override
    public void processPage(int pageNumber) {
        if (!isPageInMemory(pageNumber)) {
            frameContents[nextReplaceIndex] = pageNumber;
            nextReplaceIndex = (nextReplaceIndex + 1) % frameCount;
            pageMisses++;
        }
    }
}

public class LRUStrategy extends MemoryReplacementStrategy {
    private final LinkedHashSet<Integer> pageOrder;

    public LRUStrategy(int frameCount) {
        super(frameCount);
        this.pageOrder = new LinkedHashSet<>(frameCount);
    }

    @Override
    public void processPage(int pageNumber) {
        if (!pageOrder.remove(pageNumber)) {
            pageMisses++;
            if (pageOrder.size() == frameCount) {
                int leastUsed = pageOrder.iterator().next();
                pageOrder.remove(leastUsed);
                replaceInFrames(leastUsed, pageNumber);
            } else {
                addToFrames(pageNumber);
            }
        }
        pageOrder.add(pageNumber);
    }

    private void replaceInFrames(int oldPage, int newPage) {
        for (int i = 0; i < frameCount; i++) {
            if (frameContents[i] == oldPage) {
                frameContents[i] = newPage;
                break;
            }
        }
    }

    private void addToFrames(int pageNumber) {
        for (int i = 0; i < frameCount; i++) {
            if (frameContents[i] == -1) {
                frameContents[i] = pageNumber;
                break;
            }
        }
    }
}
