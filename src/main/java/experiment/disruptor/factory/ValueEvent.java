package experiment.disruptor.factory;

/**
 * A data envelop for type T value.
 *
 * @param <T> any valid type
 * @author liangchuan
 */
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.EqualsAndHashCode(of = {"value"})
@lombok.ToString
public class ValueEvent<T> {
    private T value;

    public static <U> ValueEvent<U> createEvent() {
        return new ValueEvent<>();
    }

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return this.value;
    }

}
