package experiment;

import java.util.ArrayList;

/**
 * Created by magicliang on 2016/8/2.
 */
//use enum to represent singleton
public class SingletonTest {

    private static volatile SingletonTest instance;
    private ArrayList<String> arrayList;

    private SingletonTest() {
    }

    public static void main(String[] args) {
        //These 2 instances are the same instances
        System.out.println(getSingletonByStaticHolder());
        System.out.println(getSingletonByStaticHolder());
        //These 2 instances are the same instances
        System.out.println(getSingletonByHungeryMan());
        System.out.println(getSingletonByHungeryMan());
    }

    public static SingletonTest getSingletonByStaticHolder() {
        return SingletonHolder.instance;
    }

    //Approach 2: use double check
    public static SingletonTest getSingletonByHungeryMan() {
        SingletonTest result = instance;
        if (result == null) {
            //Must lock class
            synchronized (SingletonTest.class) {
                result = instance;
                if (result == null) {
                    System.out.println("Only initialize instance once");
                    result = instance = new SingletonTest();
                }
            }
        }
        return result;
    }

    // Approach 1: use static holder
    private static class SingletonHolder {
        // This is loaded only at first time it is accessed.
        // 一个小技巧，因为类已经是私有的了，所以这个变量就可以是公有的
        public static SingletonTest instance = new SingletonTest();
    }
}
