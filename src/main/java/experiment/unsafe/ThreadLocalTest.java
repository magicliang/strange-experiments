package experiment.unsafe;

import java.util.stream.IntStream;

/**
 * 一个从 SO 上看来的问题。如果想要跨线程传递变量，应该把“main”当做局部变量通过闭包的方式设值。
 * ThreadLocal本身就是为了线程隔离准备的。除了初始值以外，最好不要试图跨线程共享。
 * 或者考虑使用  InheritableThreadLocal。它会天然继承父线程的threadlocal 的值。
 * 也可以考虑用闭包的方式对initValue赋值。
 * Created by LC on 2017/7/2.
 */
public class ThreadLocalTest {
    public static void main(String[] args) {
        ThreadContext.set("MAIN");
        System.out.printf("Main Thread: %s\n", ThreadContext.get());
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        IntStream.range(0, 8).boxed().parallel().forEach(n -> {
            // 到底有多少个输出会输出main，其实是无从知晓的。但凡是输出main的，并不是当前的threadloacal 被set了，而恰巧是parallelStream 借用了主线程。
            System.out.printf("CurrentThread is: %s. Parallel Consumer - %d: %s\n", Thread.currentThread().getName(), n, ThreadContext.get());
        });
    }

    private static class ThreadContext {
        private static ThreadLocal<String> val = ThreadLocal.withInitial(() -> "empty");

        public ThreadContext() {
        }

        public static String get() {
            return val.get();
        }

        public static void set(String x) {
            ThreadContext.val.set(x);
        }
    }
}

