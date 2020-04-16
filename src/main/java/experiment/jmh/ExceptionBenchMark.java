package experiment.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author liangchuan
 * @BenchmarkMode @OutputTimeUnit 两个注解可以下推到 @benchmark 上。
 */
@BenchmarkMode(Mode.All)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class ExceptionBenchMark {

    @Param({"500"})
    private int recursiveDepth;

    private Object returnMethod(int recursiveDepth) {
        if (recursiveDepth > 0) {
            return returnMethod(recursiveDepth - 1);
        } else {
            return new Object();
        }
    }


    private Object throwMethod(int recursiveDepth) throws Exception {
        if (recursiveDepth > 0) {
            return throwMethod(recursiveDepth - 1);
        } else {
            throw new RuntimeException();
        }
    }

    // 一定要是公开方法
    @Benchmark
    public void benchMarkReturn() {
        // 一定要加上使用结果的代码，否则这个代价可能被 JIT消除掉。
        System.out.println(returnMethod(recursiveDepth));
    }

    @Benchmark
    public void benchMarkThrow() {
        try {
            throwMethod(recursiveDepth);
        } catch (Exception e) {
            // 一定要加上使用结果的代码，否则这个代价可能被 JIT消除掉。
            System.out.println(e);
        }
    }

    @Setup
    public void prepare() {

    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ExceptionBenchMark.class.getSimpleName())
                .forks(2)
                .warmupIterations(1)
                .measurementIterations(1)
                .build();

        new Runner(opt).run();
    }
}
