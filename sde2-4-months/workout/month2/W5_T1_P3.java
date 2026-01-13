import java.io.*;


public class W5_T1_P3 {

    static void readFile(String filePath){

        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;

            while( (line = reader.readLine())!=null){
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    public static void main(String[] args) {
        String inputFilePath = "data/input.txt";
        readFile(inputFilePath);

    }
}
