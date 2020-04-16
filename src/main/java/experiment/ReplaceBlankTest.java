package experiment;

/**
 * Created by magicliang on 2016/8/2.
 */

//有意思的问题在于，怎样在 Java 中表示空的char。想要表示空，只能用null而不能用''，所以最后只好指向一个包装器类型
public class ReplaceBlankTest {
    public static void main(String[] args) {
        testCase1();
        testCase2();
    }

    private static void testCase1() {
        System.out.println("testCase1");
        Character[] array1 = new Character[]{' ', null, null};
        replaceBlank(array1, 1);
        for (Character c : array1) {
            System.out.println(c);
        }
    }

    private static void testCase2() {
        System.out.println("testCase2");
        Character[] array1 = new Character[]{'A', ' ', 'B', null, null};
        replaceBlank(array1, 3);
        for (Character c : array1) {
            System.out.println(c);
        }
    }

    //effectiveLength is the very char array effectiveLength
    private static void replaceBlank(Character[] stringWithBlank, int effectiveLength) {
        //Step 1: count how many spaces does this string have
        if (stringWithBlank == null || stringWithBlank.length == 0 || effectiveLength == 0) {
            return;
        }
        //Step 2: count how many spaces does this string have
        int blankCount = 0;
        for (int i = 0; i < effectiveLength; i++) {
            if (stringWithBlank[i] == ' ') {
                blankCount++;
            }
        }
        //Step 3: prepare index
        int originalIndex = effectiveLength - 1;
        //Don't need to  - 1
        int newIndex = originalIndex + blankCount * 2;

        //Step 4: iterate the index
        while (originalIndex >= 0 && newIndex >= 0) {
            if (stringWithBlank[originalIndex] == ' ') {
                stringWithBlank[newIndex--] = '0';
                stringWithBlank[newIndex--] = '2';
                stringWithBlank[newIndex--] = '%';
            } else {
                stringWithBlank[newIndex--] = stringWithBlank[originalIndex];
            }
            originalIndex--;
        }
    }
}
