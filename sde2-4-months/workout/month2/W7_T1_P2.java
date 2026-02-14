import java.util.*;

public class W7_T1_P2 {

    // T must extend Comparable so we can compare using compareTo method, hence : T extends Comparable<X>
    // if some class say Employee extends Person (so Person is super class and it implements Comprable) then we can 
    // use it to compare Employee's objects, hence Comparable<? super T> (to set a lower bound (T is lowest bound))
    // shorter : T must be comparable to itself or to one of its supertypes
    static <T extends Comparable<? super T>> T findMax(List<T> list) {

        if (list == null || list.isEmpty()) {
            return null;
        }

    
        T max = list.get(0);

        for (T val : list) {
            if (val.compareTo(max) > 0) {
                max = val;
            }
        }

        return max;
    }

    public static void main(String[] args) {

        List<Integer> intList = List.of(1, 2, 17, 13, 3, 12);
        List<String> strList = List.of("karan", "raman", "udit", "burma");

        System.out.println(findMax(intList));
        System.out.println(findMax(strList));

    }
}
