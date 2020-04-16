package experiment.proxy.invocation_handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by liangchuan on 2017/6/8.
 */

interface Person {
    void walk();

    void sayHello(String name);
}


class Man implements Person {
    @Override
    public void walk() {
        System.out.println("Walking");
    }

    @Override
    public void sayHello(String name) {
        System.out.println("Hello: " + name);
    }
}


/**
 * 这个 invocation handler 才是把原始的 concrete 实例封装的地方。 需要增强现实类就有现实类的成员变量，否则就没有。
 */
class MyInvokationHandler implements InvocationHandler {

    private Person person;

    // 如果用构造函数，就是 composite，如果用 setter 就是 aggregate
    public MyInvokationHandler(Person p) {
        this.person = p;
    }

    /**
     * 在这里 proxy 是被动态代理生成的 proxy 对象。但如果我们用 ((Person) proxy).walk() 调用就会引起无限递归，因为这方法又会触发 InvocationHandler。也就是我们只能对它对属性取值了，纯粹成为一个摆设。
     * 这是一个 visitor 的模式吧。 proxy 和 invocation handler 可以相互嵌套访问。
     * 这里一个 invoke 方法代理了这个 Person里的所有方法，算是一个 aop 的 delegate 吧。
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("---正在执行的方法：" + method);
        if (args != null) {
            System.out.println("实参为" + args);
        }


        // 注意， proxy 和 person 都是 person 接口的类型。 只不过 proxy是空虚类型的接口实例，而 person 是真的实例。这等于整个系统内有一个多余的对象才能实现方法的增强效果。
        method.invoke(person, args);
        //((Person) proxy).sayHello("inside");
        return null;
    }
}


public class InvocationHandlerProxyTest {

    public static void main(String[] args) {
        Person man = new Man();

        InvocationHandler handler = new MyInvokationHandler(man);

        Person p = (Person) Proxy.newProxyInstance(Person.class.getClassLoader(), // 当前的类加载器
                // 注意，这最后一个参数必须是接口，否则将报错：Exception in thread "main" java.lang.IllegalArgumentException: experiment.proxy.invocation_handler.Man is not an interface
                new Class[]{Person.class}, //或者 man.getClass().getInterfaces(); 需要代理的接口
                handler); // 真正补完增强逻辑的地方都应该是我们自己做的，然后传进来就行类。
        p.walk();
        p.sayHello("chuan");

        /**
         *  用闭包的方式获取 engine，可以不用自己实现 invocationHandler 的接口，也不用自己 set 实际的 delegate 对象。这种做法最好。
         *     Proxy.newProxyInstance(null, new Class[] { descriptor.getListenerType() },
         *     (proxy, method, args) ->
         *     {
         *     engine.eval(scriptCode);
         *     return null;
         *     }
         *     ));
         */
    }
}
