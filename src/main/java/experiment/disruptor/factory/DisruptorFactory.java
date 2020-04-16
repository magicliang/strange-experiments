package experiment.disruptor.factory;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ThreadFactory;

/**
 * A
 *
 * @author liangchuan
 */
public class DisruptorFactory {

    private DisruptorFactory() {
        throw new AssertionError("No DisruptorFactory for you!");
    }

    /**
     * create disruptor according to specified parameters.
     *
     * @param ringBufferSize How big the ring buffer should be. Must be power of 2.
     * @param threadFactory  standard ThreadPoolFactory instance.
     * @param <T>            valid type.
     * @return configured disruptor instance.
     */
    public static <T> Disruptor<ValueEvent<T>> createDisruptor(int ringBufferSize, ThreadFactory threadFactory) {

        WaitStrategy waitStrategy = new BusySpinWaitStrategy();
        // Here we can use diamond expression, use type witness.
        Disruptor<ValueEvent<T>> disruptor = new Disruptor<>(
                ValueEvent::createEvent,
                ringBufferSize,
                threadFactory,
                ProducerType.SINGLE,
                waitStrategy);
        return disruptor;
    }
}
