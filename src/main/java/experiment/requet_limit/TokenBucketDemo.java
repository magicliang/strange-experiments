package experiment.requet_limit;

/**
 * @author liangchuan
 */
public class TokenBucketDemo {
    public long timeStamp = System.currentTimeMillis();
    public int capacity; // 桶的容量
    public int rate; // 令牌放入速度
    public int tokens; // 当前令牌数量

    public boolean grant() {
        long now = System.currentTimeMillis();
        // 先添加令牌
        //tokens = min(capacity, tokens + (now - timeStamp) * rate);
        timeStamp = now;
        if (tokens < 1) {
            // 若不到1个令牌,则拒绝
            return false;
        } else {
            // 还有令牌，领取令牌
            tokens -= 1;
            return true;
        }
    }


}
