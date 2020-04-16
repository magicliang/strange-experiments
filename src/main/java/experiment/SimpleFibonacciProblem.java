package experiment;

/**
 * Created by magicliang on 2016/8/2.
 */
public class SimpleFibonacciProblem {
    public static void main(String[] args) {
        System.out.println(fibonacci(7));
    }

    private static int fibonacci(int n) {
        if (n < 1) {
            throw new RuntimeException("Out of range");
        } else if (n == 1 || n == 2) {
            return 1;
        } else {
            return fibonacci(n - 1) + fibonacci(n - 2);
        }

    }

}
