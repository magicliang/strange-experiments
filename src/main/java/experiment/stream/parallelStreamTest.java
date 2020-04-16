package experiment.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by LC on 2017/6/26.
 */
public class parallelStreamTest {
    public static void main(String[] args) {
        for (int i : IntStream.range(0, 5).parallel().map(x -> x * 2).toArray()) {
            System.out.println(i);
        }

        List<String> list = new ArrayList<String>();
        list.add("java");
        list.add("php");
        list.add("python");
        list.add("perl");
        list.add("c");
        list.add("lisp");
        list.add("c#");
        Stream<String> wordStream = list.stream();

        int s = wordStream.reduce(0, (x, y) -> x + y.length(), (x, y) -> x + y);
        System.out.println(s);

    }
}
