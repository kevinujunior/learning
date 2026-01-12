class EvenOddSimulator{

    static int num = 1; 


    public synchronized void printEven() throws InterruptedException{
        while(num<=10){
            if(num%2==0){
                System.out.println(Thread.currentThread().getName() + " -> " +  num);
                num++;
                Thread.sleep(100);
                notifyAll();
            }
            else{
                wait();
            }
        }
    }


    public synchronized void printOdd() throws InterruptedException{
        while(num<=10){
            if(num%2!=0){
                System.out.println(Thread.currentThread().getName() + " -> " +  num);
                num++;
                Thread.sleep(100);
                notifyAll();
            }
            else{
                wait();
            }
        }
    }
}

public class EvenOdd {

    public static void main(String[] args) throws InterruptedException {
        EvenOddSimulator obj = new EvenOddSimulator();

        Thread evenThreadPrinter = new Thread(()->{
            try {
                obj.printEven();
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }, "Even Thread");


        Thread oddThreadPrinter = new Thread(()->{
            try {
                obj.printOdd();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Odd Thread");


        System.out.println("Starting Even Odd printing using threads");
        Thread.sleep(1000);
        evenThreadPrinter.start();
        oddThreadPrinter.start();
    }
    
}



