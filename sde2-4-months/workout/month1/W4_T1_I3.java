import java.util.*;

class MyQueue<T>{
    Stack<T> inStack = new Stack<>();
    Stack<T> outStack = new Stack<>();

    public void offer(T element){
        inStack.push(element);
    }

    public T poll(){
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty.");
        }
        if(!outStack.isEmpty()){
            return outStack.pop();
        }
        if(outStack.isEmpty()){
            while(!inStack.isEmpty()){
                outStack.push(inStack.pop());
            }
        }
        return outStack.pop();
    }

    public T peek(){
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty.");
        }
        if(!outStack.isEmpty()){
            return outStack.peek();
        }
        if(outStack.isEmpty()){
            while(!inStack.isEmpty()){
                outStack.push(inStack.pop());
            }
        }
        return outStack.peek();
    }

    public boolean isEmpty(){
        return inStack.isEmpty() && outStack.isEmpty();
    }

}

public class W4_T1_I3 {



    public static void main(String[] args){
        MyQueue<Integer> q = new MyQueue<>();

        q.offer(10);
        q.offer(20);
        q.offer(30);
        System.out.println(q.peek()); //10
        q.poll();
        q.offer(40);
        System.out.println(q.peek()); //20
        q.poll();
        System.out.println(q.isEmpty()); //false
        q.poll();
        System.out.println(q.peek()); //40
        q.poll();
        System.out.println(q.isEmpty()); //true



    }
    
}
