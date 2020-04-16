package experiment.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import rx.Observable;
import rx.Observer;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author liangchuan
 */
public class FirstCommand extends HystrixCommand<String> {

    private final String name;

    public FirstCommand(String name) {
        //最少配置:指定命令组名(CommandGroup) 实际上也是线程池的名字
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected String run() {
        // 依赖逻辑封装在run()方法中
        return "Hello " + name + " thread:" + Thread.currentThread().getName();
    }

    public static void main(String[] args) throws Exception {
        //每个Command对象只能调用一次,不可以重复调用,
        //重复调用对应异常信息:This instance can only be executed once. Please instantiate a new instance.
        FirstCommand helloWorldCommand = new FirstCommand("Synchronous-hystrix");
        //使用execute()同步调用代码,效果等同于:helloWorldCommand.queue().get();
        String result = helloWorldCommand.execute();
        System.out.println("result=" + result);

        helloWorldCommand = new FirstCommand("Asynchronous-hystrix");

        //异步调用,可自由控制获取结果时机,
        Future<String> future = helloWorldCommand.queue();
        //get操作不能超过command定义的超时时间,默认:1秒
        result = future.get(100, TimeUnit.MICROSECONDS);
        System.out.println("result=" + result);
        System.out.println("mainThread=" + Thread.currentThread().getName());

        // 这里 get 和 queue 调用的线程是不一样的。可以认为一个 command 对象必然有一个线程隔离，不会复用？

        // 这里要用 rx 的 Observable。感觉上像是一个立即可以被 resolve 的 promise。
        Observable<String> fs = new FirstCommand("World").observe();

        // 从程序的输出我们可以看到 subscriber/observer 都是被本 command 的线程顺序执行。因为这是第三个 command，所以执行线程就是线程3.
        // Action1 只含有一个响应方法的订阅者。这里一 subscribe 了就立即获得了返回值了
        fs.subscribe((s) -> System.out.println("Action1 subscriber: " + s));

        // 如果需要使用响应多种 case 而不只是成功 case 的接口，考虑使用 observer。或者考虑使用 HystrixObservableCommand 用构造器来注册观察者。
        // 此处也是立即获得了返回值。
        fs.subscribe(new Observer<String>() {

            private String anotherName = "orginal name";

            @Override
            public void onCompleted() {
                // onNext/onError完成之后最后回调.也就是说，像 finally，总能调用。
                System.out.println("execute onCompleted: " + anotherName);

            }

            @Override
            public void onError(Throwable e) {
                // 当产生异常时回调
                System.out.println("onError " + e.getMessage());
                e.printStackTrace();
            }

            // 感觉上 next 方法像 promise 里面的被 resolve 驱动的方法。
            @Override
            public void onNext(String v) {
                // 获取结果后回调
                System.out.println("onNext: " + v);
                anotherName = "next name";
            }
        });

    }


}
