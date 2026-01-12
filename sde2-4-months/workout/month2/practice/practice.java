class MySimpleTask implements Runnable {

    @Override
    public void run() {
        System.out.println("Hello from thread: " + Thread.currentThread().getName());

        for (int i = 1; i <= 5; i++) {
            System.out.printf("Thread is making %d iteration\n", i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

public class practice {

    public static void main(String[] args) {

        MySimpleTask task = new MySimpleTask();

        Thread normalThread = new Thread(task, "Thread-worker1");

        Thread daemonThread = new Thread(() -> {
            while (true) {
                System.out.println("Daemon is working....");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

        });

        daemonThread.setDaemon(true);

        normalThread.start();

        daemonThread.start();

    

    }

}

