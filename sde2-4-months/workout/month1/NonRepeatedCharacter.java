import java.util.*;

public class NonRepeatedCharacter {

    public static Character getNonRepeatedCharacter(String str){
        Map<Character,Integer> map = new HashMap<>();

        for(var ch:str.toCharArray()){
            map.put(ch,map.getOrDefault(ch,0)+1);
        }

        for(var ch:str.toCharArray()){
            if(map.get(ch)==1){
                return ch;
            }
        }
        return '0';

    }


    public static void main(String[] args){
        String input = "aabeeffuuaabkkjjmmllnn";

        System.out.println("output=" + getNonRepeatedCharacter(input));
    }
    
}
