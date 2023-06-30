public class Main {
    public static void main(String[] args) {
        String path = "C:\\Users\\vaheg\\IdeaProjects\\CPU\\src\\commands.txt";
        CPU cpu = new CPU();
        cpu.executeProgramFromFile(path);
        cpu.printRegisters();
    }
}
