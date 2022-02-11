package jmx;

/**
 * Created by LC on 2017/10/7.
 */

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

public class TestGarbageCollectors {
    public static void main(String args[]) throws InterruptedException {

        List<GarbageCollectorMXBean> l = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean b : l) {
            System.out.println(b.getName());
        }
        // 输出结果：
        // PS Scavenge
        // PS MarkSweep
        Thread.sleep(100000l);
    }
    
    public void testGetCodeCacheUsage(){
        ManagementFactory.getPlatformMXBeans(MemoryPoolMXBean.class)
                .stream()
                .filter(e -> MemoryType.NON_HEAP == e.getType())
                .filter(e -> e.getName().startsWith("CodeHeap"))
                .forEach(e -> {
                    LOGGER.info("name:{},info:{}",e.getName(),e.getUsage());
                });
    }
}
