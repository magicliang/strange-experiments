package experiment.requet_limit;

/**
 * @author liangchuan
 */
public class LeakyDemo {
    public long timeStamp = System.currentTimeMillis();
    public int capacity = 100; // 桶的容量
    public int rate = 1; // 水漏出的速度，和 qps 相关
    public volatile long water; // 当前水量(当前累积请求数)

    // 注意，这个 grant 函数可能可以并发执行
    public boolean grant() {
        long now = System.currentTimeMillis();
        // 假定有一个请求到达桶内，应该先确认是不是还可以进入这个桶
        water = max(0l, (long) (water - (now - timeStamp) * rate)); // 所以应该先执行漏水，计算剩余水量
        timeStamp = now;
        // 在现有的容量上如果可以加水成功，意味着这一滴水可以按照当前的 QPS 落入桶中。
        // 我们可以想象它满足了这个约束，未来也必然可以以相同的速率离开这个桶。所以此处可以认为它拿到了 permit。
        // 而其他并发调用这个 grant 函数的其他请求，总会超过这个 QPS 的约束。因而无法得到 permit，也就保证了 QPS。
        if ((water + 1) < capacity) {
            // 尝试加水,并且水还未满
            water += 1;
            return true;
        } else {
            // 水满，拒绝加水
            return false;
        }
    }

    private long max(long a, long b) {
        return a > b ? a : b;
    }
}
