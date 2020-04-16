package experiment.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author liangchuan
 */
public class InterruptedExceptionTest {

    public static void main(String[] args) throws Exception {
        final ExecutorService exService = Executors.newFixedThreadPool(1);//newSingleThreadExecutor();

        // 本來想在 java.lang.function 包里面找寻一个无参无返回值的函数式接口，现在看来只要用 runnable 就好了。
        Runnable runnable = () -> {
            System.out.println("runnable: Current thread name is: " + Thread.currentThread().getName());
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("I am interrupted, I run in abnormal flow and exit suddenly");
                return;
            }
            System.out.println("I haven't been interrupted, I run in normal flow and exit normally");
        };

        exService.submit(runnable);

        Runnable runnableInterruptsCurrentThread = () -> {
            System.out.println("runnableInterruptsCurrentThread: Current thread name is: " + Thread.currentThread().getName());
            Thread.currentThread().interrupt();
        };

        Runnable runnableClearsInterruptedFlag = () -> {
            System.out.println("runnableClearsInterruptedFlag: Current thread name is: " + Thread.currentThread().getName());
            try {
                throw new InterruptedException("I throw a InterruptedException to clear worker threads's interrupted flag. So next " +
                        "runnable in this worker thread will get wrong interrupted flags");
            } catch (InterruptedException ex) {
                System.out.println("I just log the error and don't turn the flag back. So the interrupted flag is clear by exception.");
            }
        };
        exService.submit(runnableInterruptsCurrentThread);
        exService.submit(runnable);
        exService.submit(runnableClearsInterruptedFlag);
        Thread.sleep(2000l);
        exService.submit(runnable);

        exService.shutdown();

//        System.out.println("Experiment phase 2");
//
//        runnable.run();
//        interrupteCurrentThread();
//        runnable.run();
//        runnableClearsInterruptedFlag.run();
//        runnable.run();

    }

    private static void interrupteCurrentThread() {
        Thread.currentThread().interrupt();
    }

}
