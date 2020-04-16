package experiment.proxy.cglib;

/**
 * Created by LC on 2017/6/20.
 */
public interface ProxyInterface<T> {

    T getInstance(T instance);
}
