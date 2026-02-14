
import java.util.*;

public class MergeSortPractice {

    static void merge(int[] arr, int[] temp, int l, int m, int r) {
        
        for (int i = l; i <= r; i++) {
            temp[i] = arr[i];
        }

        int i = l;
        int j = m + 1;
        int k = l;

        while (i <= m && j <= r) {
            if (temp[i] <= temp[j]) {
                arr[k++] = temp[i++];
            } else {
                arr[k++] = temp[j++];
            }
        }


        while (i <= m) {
            arr[k++] = temp[i++];
        }

        // no need to copy right half of the array it's already in-place
        // while (j <= r) {
        //     arr[k++] = temp[j++];
        // }


    }

    static void mergeSort(int[] arr, int[] temp, int l, int r) {
        if (l < r) {
            int mid = l + (r - l) / 2;
            mergeSort(arr, temp, l, mid);
            mergeSort(arr, temp, mid + 1, r);
            merge(arr, temp, l, mid, r);
        }
    }

    static void mergeSort(int[] arr) {

        int n = arr.length;
        int[] temp = new int[n];
        mergeSort(arr, temp, 0, n - 1);
    }

    public static void main(String[] args) {

        int[] arr = { 5, 6, 8, 1, 2, 3, 9 };

        System.out.println(Arrays.toString(arr));

        mergeSort(arr);

        System.out.println(Arrays.toString(arr));

    }

}
