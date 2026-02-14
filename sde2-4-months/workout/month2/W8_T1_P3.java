import java.util.*;


public class W8_T1_P3 {

    static void swap(int[] arr, int i, int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    static void heapSort(int[] arr){
        int n = arr.length;
        //1. Build maxHeap
        // i=n/2-1 -> gives last leaf node
        // building a maxHeap means all parents node >= child node
        for(int i=n/2-1;i>=0;i--){
            heapify(arr,n,i);
        }

        System.out.println(Arrays.toString(arr));


        //put all max elements at the end
        //then heapify again(max element will be at top)
        for(int i=n-1;i>=0;i--){
            swap(arr,i,0);
            heapify(arr,i,0);
            System.out.println(Arrays.toString(arr));
        }
    }
    static void heapify(int[] arr, int n, int i){
        //heapify until parent>=left && right for all i
        int largest = i;
        int left = 2*i+1;
        int right = 2*i+2;

        if(left<n && arr[largest]<arr[left]){
            largest = left;
        }  
        
        if(right<n && arr[largest]<arr[right]){
            largest = right;
        }  
        
        if(largest!=i){
            swap(arr,i,largest);
            heapify(arr,n,largest);
        }


    }


    public static void main(String[] args) {
        int[] arr = {2,5,59,3,7,16,1,87,12,16};
        heapSort(arr);
        System.out.println(Arrays.toString(arr));
    }
    
}
