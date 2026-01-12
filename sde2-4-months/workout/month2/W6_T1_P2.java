import java.util.*;
import java.io.*;


public class W6_T1_P2 {

    static void copyBinaryFile(String sourcePath, String destPath){
        try(FileInputStream fis = new FileInputStream(sourcePath);
            FileOutputStream fos = new FileOutputStream(destPath)){
                byte[] buffer = new byte[4096]; 

                int byteReads;
                while((byteReads = fis.read(buffer))!=-1){
                    fos.write(buffer,0,byteReads);
                }

                System.out.println("Binary file copied successfully!");
        }
        catch(IOException e){
            System.err.println("Error during file copy: " + e.getMessage());
        }
    }
    public static void main(String[] args) {

        String sourcePath = "data/input.jpg";
        String destPath = "data/output.bin";
        copyBinaryFile(sourcePath,destPath);
        
        
    }
    
}
