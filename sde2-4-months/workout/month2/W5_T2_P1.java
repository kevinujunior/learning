import java.util.concurrent.*;
import java.util.*;
import java.util.stream.*;

class ArraySumTask implements Callable<Long> {

    int[] arr;
    int start;
    int end;

    ArraySumTask(int[] arr, int start, int end) {
        this.arr = arr;
        this.start = start;
        this.end = end;
    }

    @Override
    public Long call() {
        Long sum = 0L;
        for (int j = start; j <= end; j++) {
            sum += arr[j];
        }
        System.out.println(Thread.currentThread().getName() + " - Sum" + "(" + start + "," + end + ")" + "=" + sum);
        return sum;
    }

}

public class W5_T2_P1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int[] arr = new int[100];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = i + 1;
        }

        int nThreads = 5;

        ExecutorService executor = Executors.newFixedThreadPool(nThreads);

        int paritionSize = arr.length / nThreads;

        // Arrays.stream(arr).forEach(val -> System.out.println(val));

        List<Future<Long>> futureList = new ArrayList<>();

        for (int i = 0; i < nThreads; i++) {
            int start = i * paritionSize;
            int end = (i == nThreads - 1) ? arr.length - 1 : start + paritionSize - 1;

            // ArraySumTask task = new ArraySumTask(arr,start,end);
            // Future<Long> future = executor.submit(task);

            //more readible
            Future<Long> future = executor.submit(() -> {
                Long sum = 0L;
                for (int j = start; j <= end; j++) {
                    sum += arr[j];
                }
                System.out.println(Thread.currentThread().getName() + " - Sum" + "(" + start + "," + end + ")" + "=" + sum);
                return sum;
            });

            futureList.add(future);

        }

        Long result = 0L;

        for (var future : futureList) {
            result += future.get();
        }

        executor.shutdown();

        System.out.println("Our final result is : " + result);

    }
}
