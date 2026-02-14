import java.util.*;


public class SelectionSortPrac {

    //Repeatedly selects the smallest element and puts it at the beginning.
    static void selectionSort(int[] arr){

        int n = arr.length;

        for(int i=0;i<n-1;i++){
            
            int min_ind = i;
            for(int j=i+1;j<n;j++){
                if(arr[j]<arr[min_ind]){
                    min_ind = j;
                }
            }

            int temp = arr[i];
            arr[i] = arr[min_ind];
            arr[min_ind] = temp;
        }
    }
    public static void main(String[] args) {


        int[] arr = {2,5,59,3,7,16,1,87,12,16};

        System.out.println(Arrays.toString(arr));
        selectionSort(arr);
        System.out.println(Arrays.toString(arr));
        

    }   
}
