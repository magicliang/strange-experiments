package experiment;

/**
 * @author liangchuan
 */
public class FinalizeMethodExperiment {

    // 这个方法只有 GC 才会触发执行， JVM shutdown 不会执行。
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("1234");
    }

    public static void main(String[] args) throws InterruptedException {
        new FinalizeMethodExperiment();
        Thread.sleep(1000000000);
    }
}
