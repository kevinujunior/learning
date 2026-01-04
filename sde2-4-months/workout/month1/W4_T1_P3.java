import java.util.*;


public class W4_T1_P3 {

    public static int[] findNextGreater(int[] nums){
        int n = nums.length;
        Stack<Integer> st = new Stack<>();

        int[] ans = new int[n];

        Arrays.fill(ans,-1);

        for(int i=0;i<n;i++){
            while(!st.isEmpty() && nums[st.peek()]<nums[i]){
                ans[st.pop()] = nums[i];
            }
            st.push(i);
        }

        return ans;
    }

    public static void main(String[] args){
    
    int[] nums = {7,16,3,4,19,5,18};

    System.out.println(Arrays.toString(nums));
    System.out.println(Arrays.toString(findNextGreater(nums)));

    
    }


    
}
