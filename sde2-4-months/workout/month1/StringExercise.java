import java.util.*;

public class StringExercise {

    public static String sortString(String str){
        char[] charArray = str.toCharArray();
        int[] bucket = new int[26];
        int n = charArray.length;
        int i = 0;
        while(i<n){
            bucket[charArray[i]-'a']++;
            i++;
        }
        System.out.println(Arrays.toString(bucket));

        i = 0;
        int j = 0;
        while(j<26){
            if(bucket[j]>0){
                int k = bucket[j];
                while(k-->0){
                    charArray[i] = (char)('a' + j);
                    i++;
                }
            }
            j++;
        }
    
        return new String(charArray);
    }


    public static void main(String[] args){

        String str1 = "udit";

        System.out.println(sortString(str1));

    }
}
