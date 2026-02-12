public class W8_T2_P4 {
     static int binarySearch(int[] arr, int i, int j, int el){
        if(i>j) return -1;
        int mid = i + (j-i)/2;
        if(arr[mid]==el) return mid;
        if(arr[mid]>el) return binarySearch(arr,i,mid-1,el);
        return binarySearch(arr,mid+1,j,el);
    }

    public static void main(String[] args) {
        //???????????
        int[] arr = {  16, 59, 87, 0, 2, 3, 5, 9, 12};
        System.out.println(binarySearch(arr,0,arr.length-1,3));
    }
    
}
