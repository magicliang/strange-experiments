package experiment.proxy.cglib;

/**
 * Created by LC on 2017/6/20.
 */
public class SampleBean implements TestInterface {
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int enhancedOperation() {
        return 0;
    }

    @Override
    public int normalOperation() {
        return 0;
    }

    @Override
    public String echo(String str) {
        return null;
    }
}
