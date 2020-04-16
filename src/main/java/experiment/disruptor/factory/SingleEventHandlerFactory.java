package experiment.disruptor.factory;

import com.lmax.disruptor.EventHandler;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @param <T> any valid type
 * @author liangchuan
 */
public class SingleEventHandlerFactory<T> {

    private SingleEventHandlerFactory() {
        throw new AssertionError("No SingleEventHandlerFactory for you!");
    }

    /**
     * Accept several Consumer functional interface instance and make them event handlers.
     * A method factory method.
     *
     * @param consumers Consumer interface instance.
     * @param <T>       valid type parameter.
     * @return EventHandlers array.
     */
    public static <T> EventHandler<ValueEvent<T>>[] getEventHandler(Consumer<T>... consumers) {
        // Turn varargs into stream.
        Stream<Consumer<T>> consumerStream = Stream.of(consumers);
        @SuppressWarnings("unchecked")
        EventHandler<ValueEvent<T>>[] eventHandlers = consumerStream.map((consumer) -> {
            EventHandler<ValueEvent<T>> eventHandler = (event, sequence, endOfBatch) -> {
                // like a closure.
                consumer.accept(event.get());
            };
            return eventHandler;
            // Turn stream into arrays.
        }).toArray(EventHandler[]::new);
        return eventHandlers;
    }
}
