package experiment.annotation;

/**
 * Created by liangchuan on 2017/6/8.
 */
@MyRuntimeAnnotation(age = 1)
public class TestAnnotation {

    public static void main(String[] args) {
        MyRuntimeAnnotation annotation = TestAnnotation.class.getAnnotation(MyRuntimeAnnotation.class);

        System.out.println("Get name: " + annotation.name());
        System.out.println("Get age: " + annotation.age());

    }
}
