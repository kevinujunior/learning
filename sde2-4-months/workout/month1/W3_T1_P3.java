import java.util.*;

public class W3_T1_P3{

    public static String reverseWords(String sentence){
        StringBuilder stringBuilder = new StringBuilder();

        String[] words = sentence.trim().split("\\s+");

        int n = words.length;
        for(int i=n-1;i>=0;i--){
            stringBuilder.append(words[i]);
            if(i>0){
                stringBuilder.append(" ");
            }
        }

        return stringBuilder.toString();

    }

    public static void main(String[] args){
        String sentence = "Hello World I am Udit";
        System.out.println("Reversed Sentence:" + reverseWords(sentence));
    }

}