

import java.util.*;

import java.util.concurrent.*;

class ProducerConsumer{

    Queue<Integer> buffer;
    int CAPACITY;
    int val;

    ProducerConsumer(int capacity, int val){
        this.CAPACITY = capacity;
        this.val = val;
        this.buffer = new LinkedList<>();
    }



    public synchronized void produce() throws InterruptedException{
         while(true){
            while(buffer.size()==CAPACITY){
                System.out.println("Full Buffer - Producer is waiting");
                wait();
                
            }
            buffer.offer(val);
            System.out.println(Thread.currentThread().getName() + " - Produced : "+ val++);
            Thread.sleep(200);
            notifyAll();
        }
    }


    public synchronized void consume() throws InterruptedException{
        while(true){
            while(buffer.isEmpty()){
                System.out.println("Empty Buffer - Consumer is waiting");
                wait();
            }
            int val = buffer.poll();
            System.out.println(Thread.currentThread().getName() + " - Consumed : "+ val);
            Thread.sleep(200);
            notifyAll();
        }
    }

    

}


public class W5_T2_P2 {


    static void runUsingExecutorService(ProducerConsumer pc ) throws InterruptedException{
        ExecutorService executor = Executors.newCachedThreadPool();

        executor.submit(()->{
            try {
                pc.produce();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        },"Producer Thread");

        executor.submit(()->{
            try {
                pc.consume();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        },"Consumer Thread");


        // Wait 5 seconds
        try {
            Thread.sleep(5500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Stop executor gracefully
        executor.shutdownNow(); 
    
    }

    static void runUsingThreads(ProducerConsumer pc){
        Thread pcThread = new Thread(()->{
            try {
                pc.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"Producer Thread");


        Thread csThread = new Thread(()->{
            try {
                pc.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"Consumer Thread");

        System.out.println("Starting Producer Consumer");


        //stop infinite block
        pcThread.setDaemon(true);
        csThread.setDaemon(true);


        pcThread.start();
        csThread.start();

    }


    public static void main(String[] args) throws InterruptedException {

        ProducerConsumer pc = new ProducerConsumer(5,0);   

        
        
        runUsingExecutorService(pc);
        Thread.sleep(300);


       
       // runUsingThreads(pc);
        // Thread.sleep(5000);


        System.out.println("Process Finished");
    }
}

