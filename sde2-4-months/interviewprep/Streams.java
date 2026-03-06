import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Streams {

    public static void main(String[] args) {

        List<Integer> list = new ArrayList<>(List.of(7, 1, 23, 1, 41, 5, 15, 15, 24, 1, 62, 51));

        List<String> strList = new ArrayList<>(List.of("Udit", "rat", "Rohit", "cat", "Rohan", "Meghalaya"));

        String str = "arjunkumardabasay";

        //1. Find all even numbers from a list of integers.
        // ex1
        List<Integer> evenNumbers = list
                .stream()
                .filter(x -> x % 2 == 0)
                .toList();

        //2. Convert a list of strings to uppercase.

        List<String> upperCased = strList
                .stream()
                .map(x -> x.toUpperCase())
                .toList();

        //3. Find the sum of all elements in a list.
        int sumList = list
                .stream()
                .mapToInt(x -> x.intValue())
                .sum();

        Integer sumList2 = list
                .stream()
                .reduce(0, (a, b) -> a + b);

        //4. Find the maximum number in a list.
        Optional<Integer> maxList = list
                .stream()
                .max((a, b) -> Integer.compare(a, b));

        // 5. Find duplicate elements in a list.
        List<Integer> dupList = list
                .stream()
                .collect(Collectors.groupingBy(
                        x -> x,
                        Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() >= 2)
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());

        // 6. Count the frequency of each element in a list.
        Map<Integer, Long> elementFreq = list
                .stream()
                .collect(Collectors.groupingBy(
                        x -> x,
                        Collectors.counting()));

        // 7. Sort a list of integers in descending order.
        List<Integer> sortedList = list
                .stream()
                .sorted((a, b) -> Integer.compare(b, a))
                .toList();

        

        // 8. Find the first non-repeated character in a string.
        Optional<String> firstNonRepeated = str.chars()
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.groupingBy(
                        c -> c,
                        LinkedHashMap::new,
                        Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue()==1)
                .map(entry -> entry.getKey())
                .findFirst();

        // 9. Group a list of strings by their length.
        Map<Integer, List<String>> groupByLength = strList
                .stream()
                .collect(Collectors.groupingBy(x -> x.length()));

        // 10. Find the second highest number in a list
        Optional<Integer> secondHighest = list
                .stream()
                .sorted((a, b) -> Integer.compare(b, a))
                .skip(1)
                .findFirst();

        System.out.println(secondHighest);


        System.out.println(list);

    }
}
