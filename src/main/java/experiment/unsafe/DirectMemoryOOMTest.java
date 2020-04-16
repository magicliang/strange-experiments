package experiment.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Created by LC on 2017/6/18.
 */
public class DirectMemoryOOMTest {
    private static final int _1MB = 1024 * 1204;

    public static void main(String[] args) throws IllegalAccessException {
        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        // 从程序的测试结果而言，OOM 必然会导致程序中断退出。至少在没有线程恢复手段的情况下，这是一个自然的结果。
        while (true) {
            unsafe.allocateMemory(_1MB);
        }

    }
}
