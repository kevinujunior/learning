import java.util.Arrays;
import java.util.OptionalInt;
import java.util.*;

public record CountingSortPrac() {



    //Counts how many times each value appears and uses that to sort the array.
    // Prefix array maintains the stability in algorithm (Preserves the relative order of equal elements.)
    // prefix[x] = number of elements ≤ x
     static int[] countingSort(int[] arr){

        int n = arr.length;

        int maxm =   Arrays
                    .stream(arr)
                    .max()
                    .getAsInt();

    
        int[] count = new int[maxm+2];
        int[] prefix = new int[maxm+2];

        //count all element's freq
        for(int num: arr){
            count[num]++;
        }
    
        int sum = 0;
        for(int i = 0;i<maxm+2;i++){
            sum += count[i];
            count[i] = sum;
        }


        int[] ans = new int[n];

        //prefix[x] = position for ans
        for(int i=n-1;i>=0;i--){
            int ind = count[arr[i]];
            ans[ind-1] = arr[i];
            count[arr[i]]--;
        }

        return ans;

                   
    }
    


    public static void main(String[] args) {
         int[] arr = {2,5,9,3,7,16,1,7,12,16};

        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.toString(countingSort(arr)));
    
    }
}
