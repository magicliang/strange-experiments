package experiment.proxy.cglib;

/**
 * Created by LC on 2017/6/18.
 */
public class ConcreteImpl implements TestInterface {

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int enhancedOperation() {
        System.out.println("增强方法...");
        return 1;
    }

    @Override
    public int normalOperation() {
        System.out.println("普通方法...");
        return 1;
    }

    @Override
    public String echo(String str) {
        return str;
    }
}
