
import java.util.*;


public class W8_T1_P2 {

      static Random rand = new Random();

    static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    //random index as pivot
    //algorithm is input independent
    // average run case is O(nlogn)
    static int partition(int[] arr, int low, int high) {

        int randomInd = low + rand.nextInt(high-low+1);

        swap(arr,randomInd,high);
        int p = arr[high];
        int i = low;

        for (int j = low; j < high; j++) {
            if (arr[j] <= p) {
                swap(arr, i, j);
                i++;
            }
        }
        swap(arr, i, high);
        return i;
    }

    static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int p = partition(arr, low, high);
            quickSort(arr, low, p - 1);
            quickSort(arr, p + 1, high);
        }
    }

   

     public static void main(String[] args) {
        int[] arr = { 2, 5, 59, 3, 7, 16, 1, 87, 12, 16 };

        System.out.println(Arrays.toString(arr));
        quickSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }
    
}