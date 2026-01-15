
import java.util.*;
import java.util.concurrent.*;

class FactorialTask implements Callable<Long> {

    long n;

    FactorialTask(int n){
        this.n = n;
    }

    private Long calcFactorial() throws InterruptedException{
        if (n < 0) {
            throw new IllegalArgumentException("Number must be non-negative.");
        }
        long fact = 1;
        long num = this.n;
        while(num>=1 ){
            fact = fact*num;
            num--;
        }

        //to check awaitTermination
        // Thread.sleep(10000);


        System.out.println(Thread.currentThread().getName() +  " gives factorial of " + n  + " = " + fact);
        return fact;
    }

    @Override
    public Long call() throws Exception {
        return calcFactorial();
    }


    
}

public class W5_T2_P4 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        
        ExecutorService executor = Executors.newCachedThreadPool();

        List<Future<Long>> futureList = new ArrayList<>();

        
        for(int i=-3;i<=1;i++){
            futureList.add(executor.submit(new FactorialTask(i)));
        }


        System.out.println("Tasks submitted. Doing other work while tasks run...");
        Thread.sleep(1000); // Simulate main thread doing other work

        executor.shutdown(); // Initiates an orderly shutdown
        if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
            System.err.println("Executor did not terminate in time. Forcing shutdown.");
            executor.shutdownNow(); // Attempt to stop all actively executing tasks
        }

       

    }
}
