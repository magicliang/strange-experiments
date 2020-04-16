package experiment.proxy.cglib;

import net.sf.cglib.beans.BeanGenerator;

/**
 * Created by LC on 2017/6/21.
 */
public class BeanGeneratorFactory<T> implements ProxyInterface<T> {

    @Override
    public T getInstance(T instance) {
        BeanGenerator beanGenerator = new BeanGenerator();
        beanGenerator.setSuperclass(instance.getClass());
        beanGenerator.addProperty("helloWord", String.class);
        return (T) beanGenerator.create();
    }

}
