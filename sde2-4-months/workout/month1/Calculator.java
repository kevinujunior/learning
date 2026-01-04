import java.util.*;

public class Calculator {

    private int add(int a, int b) {
        return a + b;
    }

    private int sub(int a, int b) {
        return a - b;
    }

    private int multiply(int a, int b) {
        return a * b;
    }

    private double divide(int a, int b) {
        if (b != 0) {
            return a / b;
        }
        throw new IllegalArgumentException("Cannot divide by zero.");
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        System.out.println(calculator.add(5, 4));
        System.out.println(calculator.divide(1, 0));

    }

}
