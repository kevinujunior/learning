import java.util.*;

public class W8_T1_P4 {
    public static void main(String[] args) {
        String[] arr = {"udit", "rahul","rohit","shivam","arjun","karan"};

        String[] arr1 = Arrays.stream(arr).sorted().toArray(String[]::new);
        System.out.println(Arrays.toString(arr1));
    }
    
}
