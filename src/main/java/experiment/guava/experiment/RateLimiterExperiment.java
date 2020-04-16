package experiment.guava.experiment;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

/**
 * @author liangchuan
 */
public class RateLimiterExperiment {
    public static void main(String[] args) throws InterruptedException {
        RateLimiter limiter = RateLimiter.create(5);
        // 默认的情况下是令牌桶算法，令牌桶本身是依据 next request waiting time 来获取令牌的。
        // 第0个 request 的等待时间是0.后续的请求 n ，如果可以取到积存的令牌，等待时间仍然是0.否则等待时间应该等于 1/qps。
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());

        System.out.println("");

        limiter = RateLimiter.create(5);
        // 这一个请求会等大约1秒。这种复合请求的等待时间为 token 数 * (1/qps)
        System.out.println(limiter.acquire(5));
        // 这一个请求会等大约 0.2秒。
        System.out.println(limiter.acquire(1));
        System.out.println(limiter.acquire(1));

        System.out.println("");

        limiter = RateLimiter.create(5);
        // 按照 RateLimiter 的文档的说法，RateLimiter会直接允许当前的请求获得令牌，让接下来的请求偿还令牌欠债。
        // 但实际上却是当前这个请求要等到令牌足够了以后，才继续跑下去（令牌桶算法要求令牌不足时请求被缓存）。
        System.out.println(limiter.acquire(10));
        System.out.println(limiter.acquire(1));
        System.out.println(limiter.acquire(1));

        System.out.println("");

        // 因为有两秒的令牌积攒，所以前面四个请求都可以直接通过（侧面证明了不是漏桶算法）
        limiter = RateLimiter.create(2);
        System.out.println(limiter.acquire());
        Thread.sleep(2000L);
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println("");

        // 只有用这种构造函数创造出来的 limiter 才是 leaky bucket。
        // 根据 SmoothWarmingUp 的文档，预热期的桶发令牌速度是慢于正常速度的，
        // 这个速度决定 permit 生成和发送给 acquire 操作的速度。进而起到限request的作用。
        limiter = RateLimiter.create(5, 1000, TimeUnit.MILLISECONDS);
        for (int i = 1; i < 5; i++) {
            // 这五个 acquire 一个比一个快。是慢慢加速到恒定的速度的。
            System.out.println(limiter.acquire());
        }
        Thread.sleep(1000L);
        for (int i = 1; i < 5; i++) {
            System.out.println(limiter.acquire());
        }

        System.out.println("before SmoothWarmUp aquire 10 at once!");

        System.out.println(limiter.acquire(10));//此处还可以按照之前已经做过 throttle 的速度即0.2秒的速度继续拿数据。

        System.out.println(limiter.acquire()); // 但后面的令牌就要支付10倍时间的积欠了。


    }
}
