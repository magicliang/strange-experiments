package experiment.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

// 用 futuretask 的例子，除了thread和 runnable意外的第三种方法。可以获取任务运行状态。
public class FutureTaskDemo {

    public static void main(String... args) {

        ACallAble callAble = new ACallAble();

        FutureTask<String> futureTask = new FutureTask<>(callAble);

        Thread thread = new Thread(futureTask);

        thread.start();

        do {

        } while (!futureTask.isDone());

        try {
            String result = futureTask.get();

            System.out.println("Result:" + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

}

class ACallAble implements Callable<String> {

    @Override
    public String call() throws Exception {
        Thread.sleep(1000);
        return "Thread-Name:" +
                Thread.currentThread().getName();
    }
}