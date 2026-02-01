import java.util.*;
import java.util.stream.*;

class Product {
    String name;
    double price;
    String category;

    Product(String name, String category, double price) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getCategory() { return category; }
    public String getName() { return name; }
    public double getPrice() { return price; }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{ name = " + this.name
                + ", price = " + this.price
                + ", category = " + this.category + "}");

        return sb.toString();
    }

}

public class W7_T2_P4 {

    public static void main(String[] args) {
        List<Product> products = List.of(
                new Product("Laptop", "Electronics", 1200.00),
                new Product("Keyboard", "Electronics", 75.00),
                new Product("Book", "Books", 25.00),
                new Product("Mouse", "Electronics", 30.00),
                new Product("Novel", "Books", 15.00)
            );

    
        Map<String, List<Product>> groupedByCategory  = products
                                    .stream()
                                    .collect(Collectors.groupingBy(Product::getCategory));



        System.out.println(groupedByCategory);
    
    }
}
