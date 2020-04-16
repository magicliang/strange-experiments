package experiment;

import java.util.Random;

/**
 * Created by magicliang on 2016/8/3.
 */
public class ShuffleTest {
    private static Random random = new Random();

    public static void main(String[] args) {
        int[] array = {9, 2, 3, 4};
        shuffle(array);
        System.out.println(array);
    }

    //所谓的yates算法，最基本的洗牌算法
    private static void shuffle(int[] array) {
        //don't shuffle the first element
        //从后倒数回来。可以证明每一个点只在前方的子集里交换的话，不会再与后方的数交换，则交换的概率始终为 1/n。
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);//Use an upper bound.
            int temp = array[j];
            array[j] = array[i];
            array[i] = temp;
        }
    }
}
