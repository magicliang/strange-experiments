package experiment.jmh;

/**
 * @author liangchuan
 */
public class SinglethreadCalculator implements Calculator {
    public long sum(int[] numbers) {
        long total = 0L;
        for (int i : numbers) {
            total += i;
        }
        return total;
    }

    @Override
    public void shutdown() {
        // nothing to do
    }
}
