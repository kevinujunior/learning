
class ThreadSafeSingleTon {
    private static volatile ThreadSafeSingleTon instance;

    private ThreadSafeSingleTon() {
        // Prevent instantiation via reflection
        if (instance != null) {
            throw new IllegalStateException("Singleton already initialized.");
        }

        // this is called only from getInstance method
        System.out.println(Thread.currentThread().getName() + ": Creating ThreadSafeSingleton instance.");

    }

    public static ThreadSafeSingleTon getInstance() {

        if (instance == null) {// do not lock if instance is created

            synchronized (ThreadSafeSingleTon.class) {// lock only when instance is null
                if (instance == null) { // second check because if T1 gets before T2, T1 will make this non-null and it
                                        // will be craeted again
                    instance = new ThreadSafeSingleTon();
                }
            }
        }
        return instance;
    }

    public void showMessage() {
        System.out.println(Thread.currentThread().getName() + ": Hello from Singleton!");
    }

}

public class W5_T2_P3 {

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            ThreadSafeSingleTon s1 = ThreadSafeSingleTon.getInstance();
            s1.showMessage();
        }, "Thread-A");

        Thread t2 = new Thread(() -> {
            ThreadSafeSingleTon s2 = ThreadSafeSingleTon.getInstance();
            s2.showMessage();
        }, "Thread-B");

        Thread t3 = new Thread(() -> {
            ThreadSafeSingleTon s3 = ThreadSafeSingleTon.getInstance();
            s3.showMessage();
        }, "Thread-C");

        t1.start();
        t2.start();
        t3.start();
    }

}
