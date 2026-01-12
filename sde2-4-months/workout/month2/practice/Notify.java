import java.util.*;

class ProducerConsumer {

    int value = 0;
    private final Queue<Integer> buffer = new LinkedList<>();
    int CAPACITY = 5;

    
    public synchronized void produce() throws InterruptedException{
        while(true){
            while(buffer.size()==CAPACITY){
                System.out.println("Producer is waiting");
                wait();
            }
            buffer.add(value);
            System.out.println("Produced Value: "+ value);
            value++;
            notifyAll();    
            Thread.sleep(500);
        }
    }

    public synchronized void consume() throws InterruptedException{
        while(true){
            while(buffer.isEmpty()){
                System.out.println("Consumer is waiting");
                wait();
            }
            int val = buffer.poll();
            System.out.println("Consumed Value: "+ val);
            notifyAll(); 
            Thread.sleep(1000);
        }
    }   



}


public class Notify{
    public static void main(String[] args) throws InterruptedException {
        ProducerConsumer pc = new ProducerConsumer();
        Thread producer = new Thread(()-> {
            try{
                pc.produce();
            }
            catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
        },"Producer");


        Thread consumer = new Thread(()-> {
            try{
                pc.consume();
            }
            catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
        },"Consumer");

        System.out.println("Starting Threads");

        // Thread.sleep(500);

        producer.start();
        consumer.start();
    }
}
