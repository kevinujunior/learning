import java.util.*;


public class InsertionSortPrac {


    //Builds the sorted array by inserting each element into its correct position.
     static void insertionSort(int[] arr){

        int n = arr.length;


        for(int i=1;i<n;i++){
            int key = arr[i];
            int j = i-1;

            while(j>=0 && arr[j]>key){
                arr[j+1] = arr[j];
                j--;
            }
            arr[j+1] = key;
        }

        
    }
     public static void main(String[] args) {


        int[] arr = {2,5,59,3,7,16,1,87,12,16};

        System.out.println(Arrays.toString(arr));
        insertionSort(arr);
        System.out.println(Arrays.toString(arr));
        

    }   
    
}
