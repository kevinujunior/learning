import java.util.LinkedList;
import java.util.Queue;

class ProducerConsumer {
    private final Queue<Integer> buffer = new LinkedList<>();
    private final int CAPACITY = 5;

    public void produce() throws InterruptedException {
        int value = 0;
        while (true) {
            synchronized (this) { // Lock on the ProducerConsumer instance
                while (buffer.size() == CAPACITY) {
                    System.out.println("Buffer is full. Producer waiting.");
                    wait(); // Releases lock and waits
                }
                buffer.add(value);
                System.out.println("Produced: " + value);
                value++;
                notifyAll(); // Notifies waiting consumers
                Thread.sleep(500); // Simulate work
            }
        }
    }

    public void consume() throws InterruptedException {
        while (true) {
            synchronized (this) { // Lock on the ProducerConsumer instance
                while (buffer.isEmpty()) {
                    System.out.println("Buffer is empty. Consumer waiting.");
                    wait(); // Releases lock and waits
                }
                int val = buffer.remove();
                System.out.println("Consumed: " + val);
                notifyAll(); // Notifies waiting producers
                Thread.sleep(1000); // Simulate work
            }
        }
    }
}

public class WaitNotifyDemo {
    public static void main(String[] args) {
        ProducerConsumer pc = new ProducerConsumer();

        Thread producerThread = new Thread(() -> {
            try {
                pc.produce();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Producer");

        Thread consumerThread = new Thread(() -> {
            try {
                pc.consume();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Consumer");

        producerThread.start();
        consumerThread.start();
    }
}