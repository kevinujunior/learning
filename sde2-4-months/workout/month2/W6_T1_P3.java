import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.util.stream.*;

public class W6_T1_P3 {

    // static long countWords(String filePath){

    //     long count = 0;
    //     try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
    //         String line;
    //         while((line=reader.readLine())!=null){
    //             String[] words = line.trim().split("\\s+");
    //             System.out.println(Arrays.toString(words));
    //             for(var word:words){
    //                 System.out.println(word);
    //                 if(!word.isEmpty()){
    //                     count++;
    //                 }
    //             }
    //         }
    //     }
    //     catch(IOException e){
    //          System.out.println("Exception while reading file : "+ e);
    //     }

    //     return count;
    // }  
    
    
    static long countWords(String filePath){

        Path path = Path.of(filePath);
        long count = 0;

        try(Stream<String> lines = Files.lines(path)){
            return lines
                    .flatMap(line->Stream.of(line.trim().split("\\s+")))
                    .filter(word->!word.isEmpty())
                    .collect(Collectors.counting());

        }catch(IOException e){
           System.err.println("Error during file copy: " + e.getMessage());
        }
        

        return count;
    }  

    public static void main(String[] args) {
        String filePath = "data/input.txt";
        System.out.println(countWords(filePath));
    }
    
}
