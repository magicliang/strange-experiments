package experiment.proxy.cglib;

import net.sf.cglib.proxy.CallbackHelper;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.FixedValue;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Method;

/**
 * Created by LC on 2017/6/20.
 */
public class CallBackFilterFactory<T> implements ProxyInterface<T> {

    @Override
    public T getInstance(T instance) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(instance.getClass());
        // 有参构造器的内部类的扩展好例子。不要忘记。

        CallbackHelper callbackHelper = new CallbackHelper(instance.getClass(),// 这是 super class
                new Class[0]) {// 这是要实现的接口
            @Override
            protected Object getCallback(Method method) {
                if (method.getDeclaringClass() != Object.class && method.getReturnType() == String.class) {
                    return new FixedValue() {
                        @Override
                        public Object loadObject() throws Exception {
                            return "CallBackFilterFactory!";
                        }
                    };
                } else {
                    return NoOp.INSTANCE;
                }
            }
        };
        enhancer.setCallbackFilter(callbackHelper);
        enhancer.setCallbacks(callbackHelper.getCallbacks());

        return (T) enhancer.create();
    }
}
