package experiment.exception;

/**
 * @author liangchuan
 */
public class ExceptionStackTraceExperiment {

    private static void foo() {
        throw new RuntimeException();
    }

    private static void bar() {
        try {
            foo();
        } catch (Exception e) {
            throw e;
        }
    }

    public static void main(String[] args) {
        bar();
    }
}
