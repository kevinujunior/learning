import java.util.*;

public class W8_T1_P1 {


     //Repeatedly swaps adjacent elements if they are in the wrong order.
    static void bubbleSort(int[] arr){

        int n = arr.length;

        for(int i=0;i<n-1;i++){
            boolean swap = false;
            for(int j=0;j<n-1;j++){
                if(arr[j]>arr[j+1]){
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                    swap = true;
                }
            }
            if(!swap) return ;
            System.out.println(Arrays.toString(arr));
        }
    }

       public static void main(String[] args) {
        
        int[] arr = {2,5,59,3,7,16,1,87,12,16};

    

        // System.out.println(Arrays.toString(arr));
        bubbleSort(arr);
        System.out.println(Arrays.toString(arr));
    }
    
}


