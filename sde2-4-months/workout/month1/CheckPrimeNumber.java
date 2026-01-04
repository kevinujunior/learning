import java.util.*;



public class CheckPrimeNumber {

    static boolean[] primes;


    static void initPrimes(int size){
        
        primes = new boolean[size+1];
        Arrays.fill(primes,true);
        
        for(int i=2;i*i<=size;i++){
            if(primes[i]==true){
                for(int p=i*i;p<=size;p+=i){
                    primes[p] = false;
                }
            }
        }
    }

    static boolean checkPrime(int num){
        return primes[num];
    }
    
    public static void main(String[] args){
        initPrimes(10000);
        for(int i=2;i<=100;i++){
            if(checkPrime(i)){
                System.out.printf("%d is %s\n",i,checkPrime(i)?"Prime":"Not Prime");
            }
        }
    }
}
