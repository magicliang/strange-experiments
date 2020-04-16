package experiment.exception;

/**
 * Created by LC on 2017/6/10.
 */
public class ExceptionTest {

    public static void test() throws MyException {
    }

    // 即使 MyException 是从 Exception 里派生出来的，它依然是一个 checkedException!
    public static void main(String[] args) throws MyException {
        test();
    }
}
