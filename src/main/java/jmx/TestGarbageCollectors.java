package jmx;

/**
 * Created by LC on 2017/10/7.
 */

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.util.List;

public class TestGarbageCollectors {

    public static void main(String args[]) throws InterruptedException {

        List<GarbageCollectorMXBean> l = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean b : l) {
            System.out.println(b.getName());
        }

        testGetCodeCacheUsage();
        // 输出结果：
        // PS Scavenge
        // PS MarkSweep
        Thread.sleep(100000l);
    }

    public static void testGetCodeCacheUsage() {
        ManagementFactory.getPlatformMXBeans(MemoryPoolMXBean.class)
                .stream()
                .filter(e -> MemoryType.NON_HEAP == e.getType())
                .filter(e -> e.getName().startsWith("CodeHeap"))
                .forEach(e -> {
                    System.out.println(String.format("name:%s,info:%s", e.getName(), e.getUsage()));
                });
    }
}
