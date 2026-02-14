import java.util.*;

public class W7_T2_P1_P2_P3 {

    public static void main(String[] args) {
        List<String> words = Arrays.asList("apple", "banana", "Apricot", "grape", "Avocado");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        List<String> startsWithA = words
                .stream()
                .filter(s -> (s.startsWith("a") || s.startsWith("A")))
                .map(String::toUpperCase)
                .toList();

        System.out.println(startsWithA);

        OptionalDouble avg = numbers
                .stream()
                .mapToInt(Integer::intValue)
                .average();

        System.out.println(avg);

        Integer sumOdd = numbers
                .stream()
                .filter(n->n%2!=0)
                .reduce(0,Integer::sum);
                // equivalent to (0, (a,b)->(a+b))

        
        System.out.println(sumOdd);

    }

}
