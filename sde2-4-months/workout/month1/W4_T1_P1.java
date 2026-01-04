
import java.util.*;

public class W4_T1_P1 {

    public static boolean isBalanced(String input){
        Deque<Character> st = new ArrayDeque<>();

        for(Character ch : input.toCharArray()){
            if(ch.equals('(')){
                st.push(ch);
            }
            else if(ch.equals(')') && !st.isEmpty()){
                st.pop();
            }
            else{
                return false;
            }
        }

        return st.isEmpty();

    }
    
    
    public static void main(String[] args){
        String input = "((()))";
        String input2 = "(()()()))";
        String input3 = "((())()()))";

        System.out.println("input isBalanced  : "  + isBalanced(input));
        System.out.println("input2 isBalanced : "  + isBalanced(input2));
        System.out.println("input3 isBalanced : "  + isBalanced(input3));

    }
}
