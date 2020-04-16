package experiment.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;

/**
 * @author liangchuan
 */
public class CommandWithFallbackViaNetwork extends HystrixCommand<String> {
    private final int id;

    protected CommandWithFallbackViaNetwork(int id) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceX"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("GetValueCommand")));
        this.id = id;
    }

    // 注意，此处的 String 是与类型参数的 String 匹配的
    @Override
    protected String run() {
        // RemoteService.getValue(id);
        throw new RuntimeException("force failure for example");
    }

    @Override
    protected String getFallback() {
        return new FallbackViaNetwork(id).execute();
    }

    private static class FallbackViaNetwork extends HystrixCommand<String> {
        private final int id;

        public FallbackViaNetwork(int id) {
            // 共用了同一个 CommandGroupKey
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceX"))
                    // 但使用了不同的 CommandKey 此处应该有执行逻辑的隔离
                    .andCommandKey(HystrixCommandKey.Factory.asKey("GetValueFallbackCommand"))
                    // 使用不同的线程池做隔离，防止上层线程池跑满，影响降级逻辑.
                    // 只要用了一个新的线程池名字，就能启动一个新的线程池，挺不错
                    .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("RemoteServiceXFallback")));
            this.id = id;
        }

        @Override
        protected String run() {
            return "" + id;
        }

        @Override
        protected String getFallback() {
            return null;
        }
    }

    public static void main(String[] args) {
        CommandWithFallbackViaNetwork commandWithFallbackViaNetwork = new CommandWithFallbackViaNetwork(123);
        String result = commandWithFallbackViaNetwork.execute();
        System.out.println("result is: " + result);
    }
}
