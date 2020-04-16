package experiment.concurrency;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liangchuan on 2017/6/14.
 */
public class ConcurrentHashMapTest {

    public static void main(String args[]) {
        ConcurrentHashMap<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>(1);
        for (int i = 0; i < 1000; i++) {
            concurrentHashMap.put(i, i);
        }
        System.out.println("ConcurrentHashMap is unbounded: " + concurrentHashMap);

    }
}
