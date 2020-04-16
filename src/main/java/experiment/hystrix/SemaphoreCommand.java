package experiment.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * 使用信号量的方式，适合快速返回的短请求结果。这样的开销会比使用一个独立的线程池要小一点。同时也就意味着，实际上是在复用当前线程来执行请求的。
 * 类似 Java 的 ThreadPoolExecutor.CallerRunsPolicy
 *
 * @author liangchuan
 */
public class SemaphoreCommand extends HystrixCommand<String> {

    private final String name;

    public SemaphoreCommand(String name) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("SemaphoreCommandGroup"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)));
        this.name = name;
    }

    @Override
    protected String run() throws Exception {
        return "HystrixThread:" + Thread.currentThread().getName();
    }


    public static void main(String[] args) throws Exception {
        SemaphoreCommand command = new SemaphoreCommand("semaphore");
        String result = command.execute();
        System.out.println(result);
        System.out.println("MainThread:" + Thread.currentThread().getName());
    }
}
