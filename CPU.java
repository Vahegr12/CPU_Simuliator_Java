import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CPU {
    private Map <String, Byte> registers;
    private byte[] memory;
    private byte programCounter;
    String path = "commands.txt";
    private File file;
    private Scanner scanner;

    public CPU() throws FileNotFoundException {
        registers = new HashMap<>();
        registers.put("AYB", (byte)0);
        registers.put("BEN", (byte)0);
        registers.put("GIM", (byte)0);
        registers.put("DA", (byte)0);
        registers.put("EC", (byte)0);
        registers.put("ZA", (byte)0);


        memory = new byte[32];
        programCounter = 0;

        file = new File(path);
        scanner = new Scanner(file);
    }

    private byte getOperatorID(String in) {
        byte out = 0;
        switch (in) {
            case "ADD":
                out = (byte) (1);
                break;
            case "SUB":
                out = (byte) (2);
                break;
            case "MUL":
                out = (byte) (3);
                break;
            case "DIV":
                out = (byte) (4);
                break;
        }
        return out;
    }
    public void readFromFile(){

        String operator;
        operator = scanner.nextLine();
        int i = 0;
        String[] operators = operator.split(" ");
        while(operator != null){
            memory[i] = (getOperatorID(operators[0]));
            memory[i+1] = (byte)Integer.parseInt(operators[1]);
            memory[i+2] = (byte)Integer.parseInt(operators[2]);
            operator = scanner.nextLine();
            i += 3;
        }

    }
    public void loadValueIntoRegister(String registerName, byte value) {
        if (registers.containsKey(registerName)) {
            registers.put(registerName, value);
        } else {
            System.out.println("Invalid register name: " + registerName);
        }
    }

    public void performArithmeticOperation(String operation, String registerName, byte value) {
        if (!registers.containsKey(registerName)) {
            System.out.println("Invalid register name: " + registerName);
            return;
        }

        byte registerValue = registers.get(registerName);
        byte result;

        switch (operation) {
            case "ADD":
                result = (byte) (registerValue + value);
                break;
            case "SUB":
                result = (byte) (registerValue - value);
                break;
            case "MUL":
                result = (byte) (registerValue * value);
                break;
            case "DIV":
                if (value != 0) {
                    result = (byte) (registerValue / value);
                } else {
                    System.out.println("Division by zero!");
                    return;
                }
                break;
            default:
                System.out.println("Invalid arithmetic operation: " + operation);
                return;
        }

        registers.put(registerName, result);
    }

    public void compareRegisters(String register1, String register2) {
        if (!registers.containsKey(register1) || !registers.containsKey(register2)) {
            System.out.println("Invalid register name");
            return;
        }

        byte value1 = registers.get(register1);
        byte value2 = registers.get(register2);

        if (value1 > value2) {
            registers.put("DA", (byte)1); // Greater than
        } else if (value1 == value2) {
            registers.put("DA", (byte)0); // Equal
        } else {
            registers.put("DA", (byte)-1); // Less than
        }
    }

    public void jump(String instruction, byte address) {
        switch (instruction) {
            case "JMP":
                programCounter = address;
                break;
            case "JG":
                if (registers.get("DA") == 1) {
                    programCounter = address;
                }
                break;
            case "JL":
                if (registers.get("DA") == -1) {
                    programCounter = address;
                }
                break;
            case "JE":
                if (registers.get("DA") == 0) {
                    programCounter = address;
                }
                break;
            default:
                System.out.println("Invalid jump instruction: " + instruction);
                break;
        }
    }

    public void dumpMemory() {
        System.out.println("Memory dump:");
        for (int i = 0; i < memory.length; i++) {
            System.out.println("Address " + i + ": " + memory[i]);
        }
    }

    public void printRegisters() {
        System.out.println("Registers:");
        for (Map.Entry<String, Byte> entry : registers.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public void writeMemory(byte address, byte value) {
        if (address >= 0 && address < memory.length) {
            memory[address] = value;
        } else {
            System.out.println("Invalid memory address: " + address);
        }
    }

    public byte readMemory(byte address) {
        if (address >= 0 && address < memory.length) {
            return memory[address];
        } else {
            System.out.println("Invalid memory address: " + address);
        }
        return 0;
    }

}
