import java.util.HashMap;
import java.util.Map;

public class CPU {
    private Map<String, Integer> registers;
    private int[] memory;

    public CPU() {
        registers = new HashMap<>();
        registers.put("AYB", 0);
        registers.put("BEN", 0);
        registers.put("GIM", 0);
        registers.put("DA", 0);
        registers.put("EC", 0);
        registers.put("ZA", 0);

        memory = new int[1000];
    }

    public void loadValueIntoRegister(String registerName, int value) {
        if (registers.containsKey(registerName)) {
            registers.put(registerName, value);
        } else {
            System.out.println("Invalid register name: " + registerName);
        }
    }

    public void performArithmeticOperation(String operation, String registerName, int value) {
        if (!registers.containsKey(registerName)) {
            System.out.println("Invalid register name: " + registerName);
            return;
        }

        int registerValue = registers.get(registerName);
        int result;

        switch (operation) {
            case "ADD":
                result = registerValue + value;
                break;
            case "SUB":
                result = registerValue - value;
                break;
            case "MUL":
                result = registerValue * value;
                break;
            case "DIV":
                if (value != 0) {
                    result = registerValue / value;
                } else {
                    System.out.println("Division by zero!");
                    return;
                }
                break;
            default:
                System.out.println("Invalid arithmetic operation: " + operation);
                return;
        }

        registers.put("AYB", result);
    }

    public void compareRegisters(String register1, String register2) {
        if (!registers.containsKey(register1) || !registers.containsKey(register2)) {
            System.out.println("Invalid register name");
            return;
        }

        int value1 = registers.get(register1);
        int value2 = registers.get(register2);

        if (value1 > value2) {
            registers.put("DA", 1); // Greater than
        } else if (value1 == value2) {
            registers.put("DA", 0); // Equal
        } else {
            registers.put("DA", -1); // Less than
        }
    }

    public void printRegisters() {
        System.out.println("Registers:");
        for (Map.Entry<String, Integer> entry : registers.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public void writeMemory(int address, int value) {
        if (address >= 0 && address < memory.length) {
            memory[address] = value;
        } else {
            System.out.println("Invalid memory address: " + address);
        }
    }

    public int readMemory(int address) {
        if (address >= 0 && address < memory.length) {
            return memory[address];
        } else {
            System.out.println("Invalid memory address: " + address);
            return 0;
        }
    }


}
