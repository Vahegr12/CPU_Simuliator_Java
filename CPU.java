import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CPU {
    private Map<String, Byte> registers;
    private byte[] memory;
    private byte programCounter;

    public CPU() {
        registers = new HashMap<>();
        registers.put("AYB", (byte) 0);
        registers.put("BEN", (byte) 0);
        registers.put("GIM", (byte) 0);
        registers.put("DA", (byte) 0);
        registers.put("EC", (byte) 0);
        registers.put("ZA", (byte) 0);

        memory = new byte[32]; // Memory size of 32 bytes
        programCounter = 0;
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
            registers.put("DA", (byte) 1); // Greater than
        } else if (value1 == value2) {
            registers.put("DA", (byte) 0); // Equal
        } else {
            registers.put("DA", (byte) -1); // Less than
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
        if (address >= 4 && address < memory.length) {
            memory[address] = value;
        } else {
            System.out.println("Invalid memory address: " + address);
        }
    }

    public byte readMemory(byte address) {
        if (address >= 4 && address < memory.length) {
            return memory[address];
        } else {
            System.out.println("Invalid memory address: " + address);
            return 0;
        }
    }

    public void executeProgramFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 3) {
                    String instruction = parts[0];
                    String operand1 = parts[1];
                    String operand2 = parts[2];

                    if(operand2.matches("\\d+")) {
                        byte value = Byte.parseByte(operand2);
                        switch (instruction) {
                            case "MOV":
                                loadValueIntoRegister(operand1, value);
                                break;
                            case "ADD":
                                performArithmeticOperation("ADD", operand1, value);
                                break;
                            case "SUB":
                                performArithmeticOperation("SUB", operand1, value);
                                break;
                            case "MUL":
                                performArithmeticOperation("MUL", operand1, value);
                                break;
                            case "DIV":
                                performArithmeticOperation("DIV", operand1, value);
                                break;
                            case "JMP":
                            case "JG":
                            case "JL":
                            case "JE":
                                jump(instruction, value);
                                break;
                            case "MOV_MEM":
                                writeMemory(value, registers.get(operand1));
                                break;
                            default:
                                System.out.println("Invalid instruction: " + instruction);
                                break;
                        }
                    }
                    else if () {

                    } else{
                        switch (instruction) {
                            case "MOV":
                                loadValueIntoRegister(operand1, registers.get(operand2));
                                break;
                            case "ADD":
                                performArithmeticOperation("ADD", operand1, registers.get(operand2));
                                break;
                            case "SUB":
                                performArithmeticOperation("SUB", operand1, registers.get(operand2));
                                break;
                            case "MUL":
                                performArithmeticOperation("MUL", operand1, registers.get(operand2));
                                break;
                            case "DIV":
                                performArithmeticOperation("DIV", operand1, registers.get(operand2));
                                break;
                            case "JMP":
                            case "JG":
                            case "JL":
                            case "JE":
                                jump(instruction, registers.get(operand2));
                                break;
                            case "MOV_MEM":
                                writeMemory(registers.get(operand2), registers.get(operand1));
                                break;
                            default:
                                System.out.println("Invalid instruction: " + instruction);
                                break;
                        }
                    }

                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }
}
