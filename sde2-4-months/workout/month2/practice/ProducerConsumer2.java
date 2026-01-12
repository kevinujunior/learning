import java.util.*;



    class ProducerThread implements Runnable {
        ProducerConsumer2 producerConsumer;
        ProducerThread(ProducerConsumer2 producerConsumer){
            this.producerConsumer = producerConsumer;
        }

        @Override
        public void run() {
            try {
                producerConsumer.produce();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }


     class ConsumerThread implements Runnable {
        ProducerConsumer2 producerConsumer;
        ConsumerThread(ProducerConsumer2 producerConsumer){
            this.producerConsumer = producerConsumer;
        }

        @Override
        public void run() {
            try {
                producerConsumer.consume();
            } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
            }
        }

    }



public class ProducerConsumer2 {

    Queue<Integer> buffer = new LinkedList<>();
    static int val = 0;
    int CAPACITY = 5;

    public synchronized void produce() throws InterruptedException {
        while (true) {
            while (buffer.size() == CAPACITY) {
                System.out.println("Producer is waiting");
                wait();
            }
            buffer.offer(val);
            System.out.println("Produced : " + val++);
            Thread.sleep(500);
            notifyAll();
        }

    }

    public synchronized void consume() throws InterruptedException {

        while (true) {
            while (buffer.isEmpty()) {
                System.out.println("Consumer is waiting");
                wait();
            }
            var v = buffer.poll();
            System.out.println("Consumed : " + v);
            Thread.sleep(500);
            notifyAll();
        }

    }


    public static void main(String[] args) {

        ProducerConsumer2 pc = new ProducerConsumer2();

        Thread producerThread = new Thread(new ProducerThread(pc),"Producer");

        Thread consumerThread = new Thread(new ConsumerThread(pc),"Consumer");

        System.out.println("Starting Consumer Producer");
        producerThread.start();
        consumerThread.start();

    }

}
