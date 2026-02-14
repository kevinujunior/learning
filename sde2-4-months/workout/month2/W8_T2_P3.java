public class W8_T2_P3 {


    //we can avoid recursion overhead using an iterative version
    //but I don't want to lose my cool
    static int findFirst(int[] arr, int i, int j, int el,int found){
        if(i>j) return found;
        int mid = i + (j-i)/2;
        if(arr[mid]==el) return findFirst(arr,i,mid-1,el,mid);
        if(arr[mid]>el) return findFirst(arr,i,mid-1,el,found);
        return findFirst(arr,mid+1,j,el,found);
    }

     static int findLast(int[] arr, int i, int j, int el,int found){
        if(i>j) return found;
        int mid = i + (j-i)/2;
        if(arr[mid]==el)return findLast(arr,mid+1,j,el,mid);
        if(arr[mid]>el) return findLast(arr,i,mid-1,el,found);
        return findLast(arr,mid+1,j,el,found);
    }
    
    public static void main(String[] args) {
        int[] arr = { 0, 2, 3, 5, 9, 12, 16,16,16,16,16,16, 59, 87 };
        System.out.println(findFirst(arr,0,arr.length-1,16,-1));
         System.out.println(findLast(arr,0,arr.length-1,16,-1));
    }   
    
}



