import java.util.HashMap;
import java.util.Map;
import java.io.*;

class Product implements Serializable{
    String name;
    int price;
    
    Product(String name, int price){
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("{ name = " + this.name + ", price = " + this.price + "}");
        
        return sb.toString();
    }

    
}


public class W6_T2_P1 {
    private static final String FILE_NAME = "data/products_map.ser";
    
    public static void main(String[] args) {

        
        Map<String, Product> originalMap = new HashMap<>();

        originalMap.put("1" , new Product("Powder",100));
        originalMap.put("2" , new Product("Oil",47));
        originalMap.put("3" , new Product("Brush",20));
        originalMap.put("4" , new Product("Soap",300));

        System.out.println("Original HashMap: " + originalMap);
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(originalMap);
            System.out.println("\nHashMap serialized to " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Serialization error: " + e.getMessage());
        }


        Map<String, Product> deserializedMap = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            deserializedMap = (Map<String, Product>) ois.readObject();
            System.out.println("HashMap deserialized from " + FILE_NAME);
            System.out.println("Deserialized HashMap: " + deserializedMap);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Deserialization error: " + e.getMessage());
        }


        
    }
    
}
