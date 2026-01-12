import java.util.*;
import java.io.*;

public class W6_T1_P1 {


    static void copyFile(String sourcePath, String destPath){

        try(BufferedReader reader = new BufferedReader(new FileReader(sourcePath));
            BufferedWriter writer = new BufferedWriter(new FileWriter(destPath))){
            
            String line;
            while((line = reader.readLine())!=null){
                writer.write(line);
                writer.newLine();
            }   
            System.out.println("File copied successfully!");
        }
        catch (IOException e) {
            System.err.println("Error during file copy: " + e.getMessage());
        }

    }

    public static void main(String[] args){
        String sourcePath = "data/input.txt";
        String destPath = "data/output.txt";
        copyFile(sourcePath,destPath);
        
    }
    
}
