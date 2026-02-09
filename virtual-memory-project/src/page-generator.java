import java.util.Random;
import java.util.stream.IntStream;

public class PageSequenceCreator {
    private final Random randomGenerator;
    private final int maxPageNumber;

    public PageSequenceCreator(int maxPageNumber) {
        this.randomGenerator = new Random();
        this.maxPageNumber = maxPageNumber;
    }

    public int[] createSequence(int length) {
        return IntStream.generate(() -> randomGenerator.nextInt(maxPageNumber + 1))
                        .limit(length)
                        .toArray();
    }

    public int[] createBiasedSequence(int length, int biasPercentage) {
        int biasedPage = randomGenerator.nextInt(maxPageNumber + 1);
        return IntStream.generate(() -> {
            if (randomGenerator.nextInt(100) < biasPercentage) {
                return biasedPage;
            } else {
                return randomGenerator.nextInt(maxPageNumber + 1);
            }
        }).limit(length).toArray();
    }
}
