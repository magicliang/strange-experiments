package experiment.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;

/**
 * Created by LC on 2017/6/20.
 */
public class InvocationHandlerProxyFactory<T> implements ProxyInterface<T> {

    @Override
    public T getInstance(T instance) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(instance.getClass());
        enhancer.setCallback((InvocationHandler) (Object o, Method method, Object[] objects) -> {
            // Java 的这两个方法真的是鸭子类型编程方式的克星。但从侧面上看，也可以用来实现鸭子类型了。
            if (method.getDeclaringClass() != Object.class && method.getReturnType() == String.class) {
                // 不能这样调用，否则会有无限递归。和 Java 自带的 invocationHandler不能直接对 proxy 下手差不多。要做super调用要用MethodInterceptor
                //return "InvocationHandlerProxyFactory enhenced." + method.invoke(o, objects);
                return "InvocationHandlerProxyFactory enhenced.";
            } else {
                return method.invoke(o, objects);
            }
        });
        // enhancer 如果重复 create 会生成多个不同的对象？所以一对一很重要，否则会不会有状态污染？
        return (T) enhancer.create();
    }
}