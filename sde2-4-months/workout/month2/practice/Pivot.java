import java.util.Arrays;

public class Pivot {

    // static void fixPivot(int[] arr){
    // int n = arr.length;

    // int p = arr[n-1];

    // int i = 0;

    // //5, 6, 8, 1, 7, 3, 2 ,4

    // for(int k = 0;k<n-1;k++){
    // if(arr[k]<p){
    // int temp = arr[i];
    // arr[i] = arr[k];
    // arr[k] = temp;
    // i++;
    // }
    // }
    // int temp = arr[i];
    // arr[i] = arr[n - 1];
    // arr[n - 1] = temp;

    // }

    static void fixPivot(int[] arr) {
        int n = arr.length;
        int p = arr[n - 1];
        int i = 0, j = 0;
        for (int k = 0; k < n - 1; k++) {
            if (arr[k] < p) {
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                j++;
            }
            i++;
        }
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {

        // int[] arr = { 5, 6, 8, 1, 7, 3, 2, 4 };

        int[] arr = { 4, 1, 3, 2 };

        // int[] arr = { 2,3,1 };

        System.out.println(Arrays.toString(arr));

        fixPivot(arr);

        System.out.println(Arrays.toString(arr));

    }

}
