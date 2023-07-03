import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String path = "C:\\Users\\vaheg\\IdeaProjects\\CPU\\src\\commands.txt";
        CPU cpu = new CPU();

        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line;
        while ((line = reader.readLine()) != null) {
            cpu.decode(line);
            cpu.dumpMemory();
            // cpu.execute(); // modify execute function to read instructon from (memory[0] to memory[4])
        }
    }
}
