package experiment.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.FixedValue;

/**
 * Created by LC on 2017/6/20.
 */
public class FixValuedProxyFactory<T> implements ProxyInterface<T> {

    @Override
    public T getInstance(T instance) {
        //这个地方的 Enhancer 真是召之即来挥之即去。又不能做成单例，不然会有各种状态错乱问题。和 InvocationHandler 一样，用局部匿名的类型会好得多。
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(instance.getClass());
        // 把 statement lambda 变成 expresion lambda
        enhancer.setCallback((FixedValue) () -> "Hello cglib!");
        return (T) enhancer.create();
    }
}
