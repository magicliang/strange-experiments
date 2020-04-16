package experiment;

/**
 * Created by magicliang on 2016/8/2.
 */
public class FindANumberInCertainMatrix {
    public static void main(String[] args) {
        int[] array = {1, 2, 8, 9, 2, 4, 9, 12, 4, 7, 10, 13, 6, 8, 11, 15};
        int[][] array2 = {{1, 2, 8}};
        System.out.println(find(array, 4, 4, 2));
        System.out.println(find(array, 4, 4, 11189));
        System.out.println(find(array, 4, 9, 2));
        System.out.println(find(array, 9, 4, 2));
        System.out.println(find(null, 4, 4, 2));
    }

//    private static boolean find(int[] matrix, int rows, int columns, int number) {
//        boolean found = false;
//        if (matrix.length != 0 && rows > 0 && columns > 0) {
//            int row = 0;
//            int column = columns - 1;
//            while (row < rows && column > -1) {
//                if (matrix[row * columns + column] == number) {
//                    found = true;
//                    break;
//                } else if (matrix[row * columns + column] > number) {
//                    column--;
//                } else {
//                    row++;
//                }
//            }
//        }
//        return found;
//    }

    private static boolean find(int[] matrix, int rows, int columns, int number) {
        boolean found = false;
        if (matrix != null && matrix.length > 0 && rows > 0 && columns > 0 && rows * columns == matrix.length) {
            int row = 0;
            int column = columns - 1;
            //一定要注意迭代的while循环的条件
            while (row < rows && column >= 0) {
                if (matrix[row * columns + column] == number) {
                    //以及总要有一个突然终止的方法
                    return true;
                    //只考虑两种情况，不要考虑斜线
                } else if (matrix[row * columns + column] > number) {
                    --column;
                } else {
                    ++row;
                }
            }
        }
        return found;
    }
}
