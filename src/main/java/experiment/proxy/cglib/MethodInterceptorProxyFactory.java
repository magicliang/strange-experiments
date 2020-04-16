package experiment.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 这个类比 InvocationHandler 和 FixedValue 更加完善。而且不容易触发无限递归问题。
 * Created by LC on 2017/6/18.
 */
public class MethodInterceptorProxyFactory<T> implements MethodInterceptor, ProxyInterface<T> {

    @Override
    public T getInstance(T instance) {

        Enhancer enhancer = new Enhancer();
        // 用 Enhancer 来当一个 dynamic proxy。
        enhancer.setSuperclass(instance.getClass());
        // set callback 方法本身就是为了把 proxy 套在原 instance 外面。enhancer 的具体增强逻辑，应该都是 Callback 的子类型。
        enhancer.setCallback(this);
        // T 可以当做类型来用，通配符就不行
        return (T) enhancer.create();
    }

    // 泛型方法不能更改这个方法的签名，协变逆变都没什么卵用。
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        // 怎样只增强指定的方法？靠反射获取方法名的形式？是不是一定有返回值的必要呢？
        int result = 0;
        if ("enhancedOperation".equals(method.getName())) {
            System.out.println("开始增强方法");
            result = (Integer) methodProxy.invokeSuper(o, objects);
            System.out.println("方法增强完毕");
        }
        // 不需要 return 任何东西？
        return ++result;
    }
}
