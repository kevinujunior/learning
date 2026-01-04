
import java.util.*;

class CustomStack<T>{

    List<T> list;

    public CustomStack(){
        list = new ArrayList<>();
    }


    public void push(T element){
        list.add(element);
    }

    public void pop() {
        
        list.remove(list.size() - 1);
        
    }

    public boolean isEmpty(){
        return list.size()>0;
    }

    public T peek() {
        return list.get(list.size() - 1);
    }
}

public class StackUsingArrayList {
    

    public static void main(String[] args){
        CustomStack<Integer> stack = new CustomStack();
        stack.push(1);
        System.out.println("After push 1 :" +  stack.peek() );
        stack.push(2);
        System.out.println("After push 2 :" +  stack.peek() );
        stack.pop();
        System.out.println("After pop 2  :" +  stack.peek() );
    

    }
}
