import java.util.*;


public class ReverseString{

    public static String reverse(String input){
        char[] str = input.toCharArray();
        int i=0, j=str.length - 1;
        while(i<j){
            char temp = str[i];
            str[i] = str[j];
            str[j] = temp;
            i++;
            j--;
        }
        return new String(str);
    }
    public static void main(String[] args){
        String input = "I am Udit Kumar";
        String output =  reverse(input);
        System.out.println(output);
    }
    
}