package experiment.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;


/**
 * JMH  Java Microbenchmark Harness
 *
 * @author liangchuan
 * <p>
 * Throughput: 整体吞吐量，例如“1秒内可以执行多少次调用”。
 * AverageTime: 调用的平均时间，例如“每次调用平均耗时xxx毫秒”。
 * SampleTime: 随机取样，最后输出取样结果的分布，例如“99%的调用在xxx毫秒以内，99.99%的调用在xxx毫秒以内”
 * SingleShotTime: 以上模式都是默认一次 iteration 是 1s，唯有 SingleShotTime 是只运行一次。往往同时把 warmup 次数设为0，用于测试冷启动时的性能。
 */

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
// 线程间共享。这个注解应该放在一个成员变量上为好。这样可以保证那个成员变量会被注入到一个 benchmark 方法里头。
// Thread  意味着每个线程有一个单独的 copy，而 benchmark 则意味着整个测试的所有线程拥有它的 copy
// 此处之所以放在类上，是为了让测试方法不是 static 的。对作为成员方法 benchmark 的方法而言。这个对象的实例，也是一个隐含着需要的 this 参数。

/**
 * Thread	Each thread running the benchmark will create its own instance of the state object.
 * Group	Each thread group running the benchmark will create its own instance of the state object.
 * Benchmark	All threads running the benchmark share the same state object.
 */
@State(Scope.Thread)
public class FirstBenchmark {


    @Benchmark
    public int sleepAWhile() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // ignore
        }
        return 0;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                // benchamark 所在类的名字，可以用正则表达式来匹配
                .include(FirstBenchmark.class.getSimpleName())
                // 跑测试的并发性。感觉只要一个线程是对 cpu 最好的测试
                .forks(1)
                // 默认的情况下，1s 内会无限次地跑这些操作
                // 预热环节跑了五轮迭代
                .warmupIterations(5)
                // 测量阶段跑了无论迭代
                .measurementIterations(5)
                .build();

        new Runner(opt).run();

        /**
         * 结果注释：
         *
         * # JMH version: 1.19
         # VM version: JDK 1.8.0_131, VM 25.131-b11 // JVM 版本
         # VM invoker: /Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/bin/java // Java 启动程序
         # VM options: -javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=50394:/Applications/IntelliJ IDEA.app/Contents/bin -Dfile.encoding=UTF-8 // JVM 参数
         # Warmup: 5 iterations, 1 s each // 五轮预热迭代，每秒一次。
         # Measurement: 5 iterations, 1 s each // 五轮测量迭代，每秒一次
         # Timeout: 10 min per iteration // 每轮迭代10分钟超时
         # Threads: 1 thread, will synchronize iterations //只有一个线程
         # Benchmark mode: Average time, time/op // 测试模式，平均时长，时间和操作次数
         # Benchmark: experiment.jmh.FirstBenchmark.sleepAWhile 基准测试的名称

         # Run progress: 0.00% complete, ETA 00:00:10
         # Fork: 1 of 1
         objc[38137]: Class JavaLaunchHelper is implemented in both /Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/bin/java (0x1018fa4c0) and /Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/libinstrument.dylib (0x1019d64e0). One of the two will be used. Which one is undefined.
         # Warmup Iteration   1: 504340.931 us/op
         # Warmup Iteration   2: 503092.365 us/op
         # Warmup Iteration   3: 503032.637 us/op
         # Warmup Iteration   4: 504435.031 us/op
         # Warmup Iteration   5: 503459.108 us/op
         Iteration   1: 502317.855 us/op
         Iteration   2: 501957.907 us/op
         Iteration   3: 501287.846 us/op
         Iteration   4: 503236.892 us/op
         Iteration   5: 503454.115 us/op


         Result "experiment.jmh.FirstBenchmark.sleepAWhile":
         502450.923 ±(99.9%) 3464.328 us/op [Average] //平均下来的基准测试性能
         (min, avg, max) = (501287.846, 502450.923, 503454.115), stdev = 899.675
         CI (99.9%): [498986.594, 505915.251] (assumes normal distribution)


         # Run complete. Total time: 00:00:12

         Benchmark                       Mode  Cnt       Score      Error  Units
         FirstBenchmark.sleepAWhile  avgt    5  502450.923 ± 3464.328  us/op
         */
    }
}
