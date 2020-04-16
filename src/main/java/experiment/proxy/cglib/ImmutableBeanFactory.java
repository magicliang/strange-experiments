package experiment.proxy.cglib;

import net.sf.cglib.beans.ImmutableBean;

/**
 * Created by LC on 2017/6/20.
 */
public class ImmutableBeanFactory<T> implements ProxyInterface<T> {

    @Override
    public T getInstance(T instance) {
        return (T) ImmutableBean.create(instance);
    }
}
