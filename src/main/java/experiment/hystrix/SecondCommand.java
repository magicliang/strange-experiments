package experiment.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;

import java.util.concurrent.TimeUnit;

/**
 * @author liangchuan
 */
public class SecondCommand extends HystrixCommand<String> {

    private final String name;

    public SecondCommand(String name) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("SecondCommandGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("SecondCommand"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        // 超时本身已经不只是适用于线程隔离了（也适用于信号量隔离？）
                        .withExecutionTimeoutInMilliseconds(500)));
        //.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("HelloWorldPool"))) //默认的线程池名字是 CommandGroup 一起走的，但其实也可以分开定义。
        this.name = name;
    }

    @Override
    protected String getFallback() {
        // 隔离依赖依赖本身并没有影响 exception 的线程池和线程名称
        return "current thread is: " + Thread.currentThread().getName() + ", execute Failed";
    }

    @Override
    protected String run() throws Exception {
        //sleep 1 秒,调用会超时
        TimeUnit.MILLISECONDS.sleep(1000);// 注意看 Sleep 的新方法。
        return "Hello " + name + " thread:" + Thread.currentThread().getName();

    }

    public static void main(String[] args) throws Exception {

        // NOTE: 除了HystrixBadRequestException异常之外，所有从run()方法抛出的异常都算作失败，并触发降级getFallback()和断路器逻辑。
        // HystrixBadRequestException用在非法参数或非系统故障异常等不应触发回退逻辑的场景。
        // 超时也算异常，这有点像声明式事务。
        SecondCommand command = new SecondCommand("test-Fallback");
        String result = command.execute();
        System.out.println(" result is: " + result);
    }

}
