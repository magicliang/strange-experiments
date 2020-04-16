package experiment.proxy.cglib;

import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.beans.BeanMap;
import net.sf.cglib.beans.BulkBean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 基本按照这里的例子可以实现自己的 AOP 了。特别是可以用正则表达式来写 if-else。
 * Created by LC on 2017/6/18.
 */
public class CGLIBProxyTest {
    public static void main(String[] args) {


        MethodInterceptorProxyFactory<ConcreteImpl> methodInterceptorProxyFactory = new MethodInterceptorProxyFactory<>();
        ConcreteImpl newProxy = methodInterceptorProxyFactory.getInstance(new ConcreteImpl());
        System.out.println(newProxy.enhancedOperation());
        System.out.println(newProxy.normalOperation());

        FixValuedProxyFactory<ConcreteImpl> fixValuedProxyFactory = new FixValuedProxyFactory<>();
        newProxy = fixValuedProxyFactory.getInstance(new ConcreteImpl());
        System.out.println("Echo agc: " + newProxy.echo("agc"));
        System.out.println("Echo 123: " + newProxy.echo("123"));

        InvocationHandlerProxyFactory<ConcreteImpl> invocationHandlerProxyFactory = new InvocationHandlerProxyFactory<>();
        newProxy = invocationHandlerProxyFactory.getInstance(new ConcreteImpl());
        System.out.println("Echo agc: " + newProxy.echo("agc"));

        CallBackFilterFactory<ConcreteImpl> callBackFilterFactory = new CallBackFilterFactory<>();
        newProxy = callBackFilterFactory.getInstance(new ConcreteImpl());
        System.out.println("Echo agc: " + newProxy.echo("agc"));

        ImmutableBeanFactory<SampleBean> immutableBeanFactory1 = new ImmutableBeanFactory<>();
        SampleBean immutableBean1 = immutableBeanFactory1.getInstance(new SampleBean());
        try {
            immutableBean1.setValue("agc");
        } catch (IllegalStateException ex) {
            System.out.println(ex);
        }

        ImmutableBeanFactory<ConcreteImpl> immutableBeanFactory2 = new ImmutableBeanFactory<>();
        ConcreteImpl concrete = new ConcreteImpl();
        /**
         * 注意，这一句和
         *         ConcreteImpl newProxy = methodInterceptorProxyFactory.getInstance(new ConcreteImpl());
         *         System.out.println(newProxy.enhancedOperation());
         *         这两句矛盾。只要执行了这个enhancedOperation，这个类型的任何实例都不能生成 ImmutableBean 了。奇怪得很。不知道是不是bug？
         *         简而言之， MethodInterceptor 和
         * */
//        ConcreteImpl immutableBean2 = immutableBeanFactory2.getInstance(concrete);
//        try {
//            immutableBean2.setValue("agc");
//        } catch (IllegalStateException ex) {
//            System.out.println(ex);
//        }

        BeanGeneratorFactory<SampleBean> beanGeneratorFactory = new BeanGeneratorFactory<>();
        SampleBean sampleBean = beanGeneratorFactory.getInstance(new SampleBean());
        Method setter = null;
        try {
            setter = sampleBean.getClass().getMethod("setHelloWord", String.class);
            setter.invoke(sampleBean, "Hello cglib");

            Method getter = sampleBean.getClass().getMethod("getHelloWord");
            System.out.println("beanGeneratorFactory: " + getter.invoke(sampleBean));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        BeanCopier copier = BeanCopierFactory.getBeanCopier(SampleBean.class, OtherSampleBean.class);
        SampleBean sampleBean1 = new SampleBean();
        sampleBean1.setValue("娃哈哈啊娃哈哈");

        OtherSampleBean otherSampleBean = new OtherSampleBean();
        copier.copy(sampleBean1, otherSampleBean, null);

        System.out.println(otherSampleBean.getValue());

        /**
         *      相比于BeanCopier，BulkBean将整个Copy的动作拆分为getPropertyValues，setPropertyValues的两个方法，允许自定义处理的属性。
         * */

        BulkBean bulkBean = BulkBean.create(SampleBean.class,
                new String[]{"getValue"},
                new String[]{"setValue"},
                new Class[]{String.class});

        SampleBean bean = new SampleBean();
        bean.setValue("sample bean");

        System.out.println("How many properties this bean has: " + bulkBean.getPropertyValues(bean).length);

        System.out.println("First property is: " + bulkBean.getPropertyValues(bean)[0]);

        bulkBean.setPropertyValues(bean, new Object[]{"bulk bean"});

        System.out.println("Bean value now is: " + bean.getValue());


        SampleBean bean2 = new SampleBean();

        BeanMap map = BeanMap.create(bean2);
        bean2.setValue("5456576");

        System.out.println("Bean map now is synchronized: " + map.get("value"));
    }
}
