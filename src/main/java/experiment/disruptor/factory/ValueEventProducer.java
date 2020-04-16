package experiment.disruptor.factory;

import com.lmax.disruptor.RingBuffer;

/**
 * @author liangchuan
 */
public class ValueEventProducer<T> {

    private final RingBuffer<ValueEvent<T>> ringBuffer;

    public ValueEventProducer(RingBuffer<ValueEvent<T>> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(T eventValue) {
        long sequence = ringBuffer.next();  // Grab the next sequence
        try {
            ValueEvent<T> event = ringBuffer.get(sequence); // Get the entry in the Disruptor
            // for the sequence
            event.set(eventValue);  // Fill with data
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
