public class Main {
    public static void main(String[] args) {
        String path = "C:\\Users\\vaheg\\IdeaProjects\\CPU\\src\\commands.txt";
        CPU cpu = new CPU();
        cpu.execute(path);
        cpu.printRegisters();
        System.out.println(cpu.readMemory((byte)5));
        System.out.println(cpu.readMemory((byte)10));
    }
}
