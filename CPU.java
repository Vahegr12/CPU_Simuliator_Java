//Problem 1: add jump functions
//Problem 2: programm is reading directly from file, first fill memory from file, after exicute programm from memory
//Problem 3: exexute function must test if jump or Not write only 1 operand


import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CPU {
    private final Map<String, Byte> registers; // values

    private final Map<String, Byte> regs; // IDs
    private final Map<String, Byte> insts;
    private final Map<Character, Byte> oper_t;
    private final byte[] memory;
//    private final byte programCounter;

    public CPU() {
        // registers - values
        registers = new HashMap<>();
        registers.put("AYB", (byte) 0);
        registers.put("BEN", (byte) 0);
        registers.put("GIM", (byte) 0);
        registers.put("DA", (byte) 0);
        registers.put("EC", (byte) 0);
        registers.put("ZA", (byte) 0);


        // instructions - IDs
        insts = new HashMap<>();
        insts.put("MOV", (byte)0);
        insts.put("ADD", (byte)1);
        insts.put("SUB", (byte)2);
        insts.put("MUL", (byte)3);
        insts.put("DIV", (byte)4);
        insts.put("AND", (byte)5);
        insts.put("OR", (byte)6);
        insts.put("XOR", (byte)7);
        insts.put("CMP", (byte)8);
        insts.put("JMP", (byte)9);
        insts.put("JG", (byte)10);
        insts.put("JL", (byte)11);
        insts.put("JE", (byte)12);
        insts.put("NOT", (byte)13);

        // oper types in id
        oper_t = new HashMap<>();
        oper_t.put('R', (byte) 0);
        oper_t.put('A', (byte) 2);
        oper_t.put('L', (byte) 3);

        // regis
        regs = new HashMap<>();
        regs.put("AYB", (byte) 0);
        regs.put("BEN", (byte) 1);
        regs.put("GIM", (byte) 2);
        regs.put("DA", (byte) 3);
        regs.put("EC", (byte) 4);
        regs.put("ZA", (byte) 5);

        memory = new byte[32]; // Memory size of 32 bytes
//        programCounter = 0;
    }

    public String corrLValue(String in) {
        if (in.matches("[A-Z]+") && registers.containsKey(in)){
            return "R:" + in;
        }
        else if (in.charAt(0) == '[' && in.charAt(in.length()-1) == ']'){
            byte addr_byte = 34;
            try {
                in = in.substring(1, in.length() - 1);
                if (!in.matches("\\d+")){
                    return null;
                }
                addr_byte = Byte.parseByte(in);
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid input: " + in + " is not an address");
            }
            if (addr_byte > (byte) 4 && addr_byte <= (byte) 32) {
                return "A:" + addr_byte;
            }

            else {
                System.out.println("Invalid input: " + in + " is out of bounds");
            }
        }
        else if (in.matches("\\d+")){
            byte value = Byte.parseByte(in);
            return "L:" + in;
        }
        return null;
    }
    public String corrRValue(String in){
        if (in.matches("[A-Z]+") && registers.containsKey(in)){
            return "R:" + in;
        }
        else if (in.charAt(0) == '[' && in.charAt(in.length()-1) == ']'){
            byte addr_byte = 34;
            try {
                in = in.substring(1, in.length() - 1);
                if (!in.matches("\\d+")){
                    return null;
                }
                addr_byte = Byte.parseByte(in);
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid input: " + in + " is not an address");
            }
            if (addr_byte > (byte) 4 && addr_byte <= (byte) 32) {
                return "A:" + addr_byte;
            }
            else {
                System.out.println("Invalid input: " + in + " is out of bounds");
            }
        }
        else if (in.matches("\\d+")){
            byte value = Byte.parseByte(in);
            return "L:" + in;
        }
        return null;
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
        if (address > 4 && address < memory.length) {
            memory[address] = value;
        } else {
            System.out.println("Invalid memory address: " + address);
        }
    }

    public byte readMemory(byte address) {
        if (address > 4 && address < memory.length) {
            return memory[address];
        } else {
            System.out.println("Invalid memory address: " + address);
            return 0;
        }
    }

    public boolean MOV(String L, String R) {
        L = corrLValue(L);
        R = corrRValue(R);
        if (L == null || R == null){
            System.out.println("Invalid operation!");
        }
        else{
            char L_type = L.charAt(0);
            L = L.substring(2, L.length());
            char R_type = R.charAt(0);
            R = R.substring(2, R.length());
            switch (L_type){
                case 'R':
                    // code for MOV to register
                    if(R_type == 'L') {
                        registers.put(L, Byte.parseByte(R));
                        return registers.get(L) == Byte.parseByte(R);
                    }
                    if(R_type == 'A'){
                        registers.put(L, readMemory(Byte.parseByte(R)));
                        return registers.get(L) == readMemory(Byte.parseByte(R));
                    }
                    if(R_type == 'R'){
                        registers.put(L, registers.get(R));
                        return registers.get(L) == registers.get(R);
                    }
                    break;
                case 'A':
                    if(R_type == 'L') {
                        writeMemory(Byte.parseByte(L),Byte.parseByte(R));
                        return readMemory(Byte.parseByte(L)) == Byte.parseByte(R);
                    }
                    if(R_type == 'A'){
                        writeMemory(Byte.parseByte(L),readMemory(Byte.parseByte(R)));
                        return readMemory(Byte.parseByte(L)) == readMemory(Byte.parseByte(R));
                    }
                    if(R_type == 'R'){
                        writeMemory(Byte.parseByte(L), registers.get(R));
                        return readMemory(Byte.parseByte(L)) == registers.get(R);
                    }
                    break;

            }
        }
        return false;
    }

    public boolean ADD(String L, String R){
        L = corrLValue(L);
        R = corrRValue(R);
        if (L == null || R == null){
            System.out.println("Invalid operation!");
            return false;
        }
            char L_type = L.charAt(0);
            L = L.substring(2, L.length());
            char R_type = R.charAt(0);
            R = R.substring(2, R.length());
            switch (L_type){
                case 'R':
                    // code for MOV to register
                    if(R_type == 'L') {
                        registers.put(L, (byte)(Integer.parseInt(R) + registers.get(L)));
                        registers.put("AYB", (byte)(Integer.parseInt(R) + registers.get(L)));

                        return registers.get(L) == (byte) (Integer.parseInt(R) + registers.get(L));
                    }
                    if(R_type == 'A'){
                        registers.put(L, (byte)(readMemory(Byte.parseByte(R)) + registers.get(L)));
                        registers.put("AYB", (byte)(readMemory(Byte.parseByte(R)) + registers.get(L)));
                        return registers.get(L) == (byte) (readMemory(Byte.parseByte(R)) + registers.get(L));
                    }
                    if(R_type == 'R'){
                        registers.put(L, (byte)(registers.get(R) + registers.get(L)));
                        registers.put("AYB", (byte)(registers.get(R) + registers.get(L)));
                        return registers.get(L) == (byte) (registers.get(R) + registers.get(L));
                    }
                    break;
                case 'A':
                    if(R_type == 'L') {
                        writeMemory(Byte.parseByte(L),(byte)(Integer.parseInt(R) + readMemory(Byte.parseByte(L))));
                        registers.put("AYB", (byte)(Integer.parseInt(R) + readMemory(Byte.parseByte(L))));
                        return readMemory(Byte.parseByte(L)) == (byte) (Integer.parseInt(R) + readMemory(Byte.parseByte(L)));
                    }
                    if(R_type == 'A'){
                        writeMemory(Byte.parseByte(L),(byte)(readMemory(Byte.parseByte(R)) + readMemory(Byte.parseByte(L))));
                        registers.put("AYB",(byte)(readMemory(Byte.parseByte(R)) + readMemory(Byte.parseByte(L))));
                        return readMemory(Byte.parseByte(L)) == (byte) (readMemory(Byte.parseByte(R)) + readMemory(Byte.parseByte(L)));
                    }
                    if(R_type == 'R'){
                        writeMemory(Byte.parseByte(L), (byte)(registers.get(R) + readMemory(Byte.parseByte(L))));
                        registers.put("AYB", (byte)(registers.get(R) + readMemory(Byte.parseByte(L))));
                        return readMemory(Byte.parseByte(L)) == (byte) (registers.get(R) + readMemory(Byte.parseByte(L)));
                    }
                    break;

            }
        return false;
    }

    public boolean SUB(String L, String R){
        L = corrLValue(L);
        R = corrRValue(R);
        if (L == null || R == null){
            System.out.println("Invalid operation!");
            return false;
        }
        char L_type = L.charAt(0);
        L = L.substring(2, L.length());
        char R_type = R.charAt(0);
        R = R.substring(2, R.length());
        switch (L_type){
            case 'R':
                // code for MOV to register
                if(R_type == 'L') {
                    registers.put(L, (byte)(Integer.parseInt(R) - registers.get(L)));
                    registers.put("AYB", (byte)(Integer.parseInt(R) - registers.get(L)));
                    return registers.get(L) == (byte) (Integer.parseInt(R) - registers.get(L));
                }
                if(R_type == 'A'){
                    registers.put(L, (byte)(readMemory(Byte.parseByte(R)) - registers.get(L)));
                    registers.put("AYB", (byte)(readMemory(Byte.parseByte(R)) - registers.get(L)));
                    return registers.get(L) == (byte) (readMemory(Byte.parseByte(R)) - registers.get(L));
                }
                if(R_type == 'R'){
                    registers.put(L, (byte)(registers.get(R) - registers.get(L)));
                    registers.put("AYB", (byte)(registers.get(R) - registers.get(L)));
                    return registers.get(L) == (byte) (registers.get(R) - registers.get(L));
                }
                break;
            case 'A':
                if(R_type == 'L') {
                    writeMemory(Byte.parseByte(L),(byte)(Integer.parseInt(R) - readMemory(Byte.parseByte(L))));
                    registers.put("AYB",(byte)(Integer.parseInt(R) - readMemory(Byte.parseByte(L))));
                    return readMemory(Byte.parseByte(L)) == (byte) (Integer.parseInt(R) - readMemory(Byte.parseByte(L)));
                }
                if(R_type == 'A'){
                    writeMemory(Byte.parseByte(L),(byte)(readMemory(Byte.parseByte(R)) - readMemory(Byte.parseByte(L))));
                    registers.put("AYB", (byte)(readMemory(Byte.parseByte(R)) - readMemory(Byte.parseByte(L))));
                    return readMemory(Byte.parseByte(L)) == (byte) (readMemory(Byte.parseByte(R)) - readMemory(Byte.parseByte(L)));
                }
                if(R_type == 'R'){
                    writeMemory(Byte.parseByte(L), (byte)(registers.get(R) - readMemory(Byte.parseByte(L))));
                    registers.put("AYB", (byte)(registers.get(R) - readMemory(Byte.parseByte(L))));
                    return readMemory(Byte.parseByte(L)) == (byte) (registers.get(R) - readMemory(Byte.parseByte(L)));
                }
                break;

        }
        return false;
    }

    public boolean MUL(String L, String R){
        L = corrLValue(L);
        R = corrRValue(R);
        if (L == null || R == null){
            System.out.println("Invalid operation!");
            return false;
        }
        char L_type = L.charAt(0);
        L = L.substring(2, L.length());
        char R_type = R.charAt(0);
        R = R.substring(2, R.length());
        switch (L_type){
            case 'R':
                // code for MOV to register
                if(R_type == 'L') {
                    registers.put(L, (byte)(Integer.parseInt(R) * registers.get(L)));
                    registers.put("AYB", (byte)(Integer.parseInt(R) * registers.get(L)));
                    return registers.get(L) == (byte) (Integer.parseInt(R) * registers.get(L));
                }
                if(R_type == 'A'){
                    registers.put(L, (byte)(readMemory(Byte.parseByte(R)) * registers.get(L)));
                    registers.put("AYB", (byte)(readMemory(Byte.parseByte(R)) * registers.get(L)));
                    return registers.get(L) == (byte) (readMemory(Byte.parseByte(R)) * registers.get(L));
                }
                if(R_type == 'R'){
                    registers.put(L, (byte)(registers.get(R) * registers.get(L)));
                    registers.put("AYB", (byte)(registers.get(R) * registers.get(L)));
                    return registers.get(L) == (byte) (registers.get(R) * registers.get(L));
                }
                break;
            case 'A':
                if(R_type == 'L') {
                    writeMemory(Byte.parseByte(L),(byte)(Integer.parseInt(R) * readMemory(Byte.parseByte(L))));
                    registers.put("AYB", (byte)(Integer.parseInt(R) * readMemory(Byte.parseByte(L))));
                    return readMemory(Byte.parseByte(L)) == (byte) (Integer.parseInt(R) * readMemory(Byte.parseByte(L)));
                }
                if(R_type == 'A'){
                    writeMemory(Byte.parseByte(L),(byte)(readMemory(Byte.parseByte(R)) * readMemory(Byte.parseByte(L))));
                    registers.put("AYB", (byte)(readMemory(Byte.parseByte(R)) * readMemory(Byte.parseByte(L))));
                    return readMemory(Byte.parseByte(L)) == (byte) (readMemory(Byte.parseByte(R)) * readMemory(Byte.parseByte(L)));
                }
                if(R_type == 'R'){
                    writeMemory(Byte.parseByte(L), (byte)(registers.get(R) * readMemory(Byte.parseByte(L))));
                    registers.put("AYB", (byte)(registers.get(R) * readMemory(Byte.parseByte(L))));
                    return readMemory(Byte.parseByte(L)) == (byte) (registers.get(R) * readMemory(Byte.parseByte(L)));
                }
                break;

        }
        return false;
    }

    public boolean DIV(String L, String R){
        L = corrLValue(L);
        R = corrRValue(R);
        if (L == null || R == null){
            System.out.println("Invalid operation!");
            return false;
        }
        char L_type = L.charAt(0);
        L = L.substring(2, L.length());
        char R_type = R.charAt(0);
        R = R.substring(2, R.length());
        switch (L_type){
            case 'R':
                // code for MOV to register
                if(R_type == 'L') {
                    registers.put(L, (byte)(Integer.parseInt(R) / registers.get(L)));
                    registers.put("AYB", (byte)(Integer.parseInt(R) / registers.get(L)));
                    return registers.get(L) == (byte) (Integer.parseInt(R) / registers.get(L));
                }
                if(R_type == 'A'){
                    registers.put(L, (byte)(readMemory(Byte.parseByte(R)) / registers.get(L)));
                    registers.put("AYB", (byte)(readMemory(Byte.parseByte(R)) / registers.get(L)));
                    return registers.get(L) == (byte) (readMemory(Byte.parseByte(R)) / registers.get(L));
                }
                if(R_type == 'R'){
                    registers.put(L, (byte)(registers.get(R) / registers.get(L)));
                    registers.put("AYB", (byte)(registers.get(R) / registers.get(L)));
                    return registers.get(L) == (byte) (registers.get(R) / registers.get(L));
                }
                break;
            case 'A':
                if(R_type == 'L') {
                    writeMemory(Byte.parseByte(L),(byte)(Integer.parseInt(R) / readMemory(Byte.parseByte(L))));
                    registers.put("AYB", (byte)(Integer.parseInt(R) / readMemory(Byte.parseByte(L))));
                    return readMemory(Byte.parseByte(L)) == (byte) (Integer.parseInt(R) / readMemory(Byte.parseByte(L)));
                }
                if(R_type == 'A'){
                    writeMemory(Byte.parseByte(L),(byte)(readMemory(Byte.parseByte(R)) / readMemory(Byte.parseByte(L))));
                    registers.put("AYB", (byte)(readMemory(Byte.parseByte(R)) / readMemory(Byte.parseByte(L))));
                    return readMemory(Byte.parseByte(L)) == (byte) (readMemory(Byte.parseByte(R)) / readMemory(Byte.parseByte(L)));
                }
                if(R_type == 'R'){
                    writeMemory(Byte.parseByte(L), (byte)(registers.get(R) / readMemory(Byte.parseByte(L))));
                    registers.put("AYB", (byte)(registers.get(R) / readMemory(Byte.parseByte(L))));
                    return readMemory(Byte.parseByte(L)) == (byte) (registers.get(R) / readMemory(Byte.parseByte(L)));
                }
                break;

        }
        return false;
    }

    public boolean AND(String L, String R){
        L = corrLValue(L);
        R = corrRValue(R);
        if (L == null || R == null){
            System.out.println("Invalid operation!");
            return false;
        }
        char L_type = L.charAt(0);
        L = L.substring(2, L.length());
        char R_type = R.charAt(0);
        R = R.substring(2, R.length());
        switch (L_type){
            case 'R':
                if(R_type == 'L') {
                    registers.put(L, (byte)(Integer.parseInt(R) & registers.get(L)));
                    return registers.get(L) == (byte) (Integer.parseInt(R) & registers.get(L));
                }
                if(R_type == 'A'){
                    registers.put(L, (byte)(readMemory(Byte.parseByte(R)) & registers.get(L)));
                    return registers.get(L) == (byte) (readMemory(Byte.parseByte(R)) & registers.get(L));
                }
                if(R_type == 'R'){
                    registers.put(L, (byte)(registers.get(R) & registers.get(L)));
                    return registers.get(L) == (byte) (registers.get(R) & registers.get(L));
                }
                break;
            case 'A':
                if(R_type == 'L') {
                    writeMemory(Byte.parseByte(L),(byte)(Integer.parseInt(R) & readMemory(Byte.parseByte(L))));
                    return readMemory(Byte.parseByte(L)) == (byte) (Integer.parseInt(R) & readMemory(Byte.parseByte(L)));
                }
                if(R_type == 'A'){
                    writeMemory(Byte.parseByte(L),(byte)(readMemory(Byte.parseByte(L)) & readMemory(Byte.parseByte(L))));
                    return readMemory(Byte.parseByte(L)) == (byte) (readMemory(Byte.parseByte(L)) & readMemory(Byte.parseByte(L)));
                }
                if(R_type == 'R'){
                    writeMemory(Byte.parseByte(L), (byte)(registers.get(R) & readMemory(Byte.parseByte(L))));
                    return readMemory(Byte.parseByte(L)) == (byte) (registers.get(R) & readMemory(Byte.parseByte(L)));
                }
                break;
            case 'L': // edit
                if(R_type == 'L') {
                    registers.put(L, (byte)(Integer.parseInt(R) & Integer.parseInt(L)));
                    return registers.get(L) == (byte) (Integer.parseInt(R) & Integer.parseInt(L));
                }
                if(R_type == 'A'){
                    registers.put(L, (byte)(readMemory(Byte.parseByte(R)) & Integer.parseInt(L)));
                    return registers.get(L) == (byte) (readMemory(Byte.parseByte(R)) & Integer.parseInt(L));
                }
                if(R_type == 'R'){
                    registers.put(L, (byte)(registers.get(R) & Integer.parseInt(L)));
                    return registers.get(L) == (byte) (registers.get(R) & Integer.parseInt(L));
                }
                break;
        }
        return false;
    }

    public boolean OR(String L, String R){
        L = corrLValue(L);
        R = corrRValue(R);
        if (L == null || R == null){
            System.out.println("Invalid operation!");
            return false;
        }
        char L_type = L.charAt(0);
        L = L.substring(2, L.length());
        char R_type = R.charAt(0);
        R = R.substring(2, R.length());
        switch (L_type){
            case 'R':
                // code for MOV to register
                if(R_type == 'L') {
                    registers.put(L, (byte)(Integer.parseInt(R) | registers.get(L)));
                    return registers.get(L) == (byte) (Integer.parseInt(R) | registers.get(L));
                }
                if(R_type == 'A'){
                    registers.put(L, (byte)(readMemory(Byte.parseByte(R)) | registers.get(L)));
                    return registers.get(L) == (byte) (readMemory(Byte.parseByte(R)) | registers.get(L));
                }
                if(R_type == 'R'){
                    registers.put(L, (byte)(registers.get(R) | registers.get(L)));
                    return registers.get(L) == (byte) (registers.get(R) | registers.get(L));
                }
                break;
            case 'A':
                if(R_type == 'L') {
                    writeMemory(Byte.parseByte(L),(byte)(Integer.parseInt(R) | readMemory(Byte.parseByte(L))));
                    return readMemory(Byte.parseByte(L)) == (byte) (Integer.parseInt(R) | readMemory(Byte.parseByte(L)));
                }
                if(R_type == 'A'){
                    writeMemory(Byte.parseByte(L),(byte)(readMemory(Byte.parseByte(L)) | readMemory(Byte.parseByte(L))));
                    return readMemory(Byte.parseByte(L)) == (byte) (readMemory(Byte.parseByte(L)) | readMemory(Byte.parseByte(L)));
                }
                if(R_type == 'R'){
                    writeMemory(Byte.parseByte(L), (byte)(registers.get(R) | readMemory(Byte.parseByte(L))));
                    return readMemory(Byte.parseByte(L)) == (byte) (registers.get(R) | readMemory(Byte.parseByte(L)));
                }
                break;
            case 'L': // edit
                if(R_type == 'L') {
                    registers.put(L, (byte)(Integer.parseInt(R) | Integer.parseInt(L)));
                    return registers.get(L) == (byte) (Integer.parseInt(R) | Integer.parseInt(L));
                }
                if(R_type == 'A'){
                    registers.put(L, (byte)(readMemory(Byte.parseByte(R)) | Integer.parseInt(L)));
                    return registers.get(L) == (byte) (readMemory(Byte.parseByte(R)) | Integer.parseInt(L));
                }
                if(R_type == 'R'){
                    registers.put(L, (byte)(registers.get(R) | Integer.parseInt(L)));
                    return registers.get(L) == (byte) (registers.get(R) & Integer.parseInt(L));
                }
                break;
        }
        return false;
    }

    public boolean XOR(String L, String R){
        L = corrLValue(L);
        R = corrRValue(R);
        if (L == null || R == null){
            System.out.println("Invalid operation!");
            return false;
        }
        char L_type = L.charAt(0);
        L = L.substring(2, L.length());
        char R_type = R.charAt(0);
        R = R.substring(2, R.length());
        switch (L_type){
            case 'R':
                // code for MOV to register
                if(R_type == 'L') {
                    registers.put(L, (byte)(Integer.parseInt(R) ^ registers.get(L)));
                    return registers.get(L) == (byte) (Integer.parseInt(R) ^ registers.get(L));
                }
                if(R_type == 'A'){
                    registers.put(L, (byte)(readMemory(Byte.parseByte(R)) ^ registers.get(L)));
                    return registers.get(L) == (byte) (readMemory(Byte.parseByte(R)) ^ registers.get(L));
                }
                if(R_type == 'R'){
                    registers.put(L, (byte)(registers.get(R) ^ registers.get(L)));
                    return registers.get(L) == (byte) (registers.get(R) ^ registers.get(L));
                }
                break;
            case 'A':
                if(R_type == 'L') {
                    writeMemory(Byte.parseByte(L),(byte)(Integer.parseInt(R) ^ readMemory(Byte.parseByte(L))));
                    return readMemory(Byte.parseByte(L)) == (byte) (Integer.parseInt(R) ^ readMemory(Byte.parseByte(L)));
                }
                if(R_type == 'A'){
                    writeMemory(Byte.parseByte(L),(byte)(readMemory(Byte.parseByte(L)) ^ readMemory(Byte.parseByte(L))));
                    return readMemory(Byte.parseByte(L)) == (byte) (readMemory(Byte.parseByte(L)) ^ readMemory(Byte.parseByte(L)));
                }
                if(R_type == 'R'){
                    writeMemory(Byte.parseByte(L), (byte)(registers.get(R) ^ readMemory(Byte.parseByte(L))));
                    return readMemory(Byte.parseByte(L)) == (byte) (registers.get(R) ^ readMemory(Byte.parseByte(L)));
                }
                break;
            case 'L': // edit
                if(R_type == 'L') {
                    registers.put(L, (byte)(Integer.parseInt(R) ^ Integer.parseInt(L)));
                    return registers.get(L) == (byte) (Integer.parseInt(R) ^ Integer.parseInt(L));
                }
                if(R_type == 'A'){
                    registers.put(L, (byte)(readMemory(Byte.parseByte(R)) ^ Integer.parseInt(L)));
                    return registers.get(L) == (byte) (readMemory(Byte.parseByte(R)) ^ Integer.parseInt(L));
                }
                if(R_type == 'R'){
                    registers.put(L, (byte)(registers.get(R) ^ Integer.parseInt(L)));
                    return registers.get(L) == (byte) (registers.get(R) ^ Integer.parseInt(L));
                }
                break;

        }
        return false;
    }

    public boolean CMP(String L, String R){
        L = corrLValue(L);
        R = corrRValue(R);
        if (L == null || R == null){
            System.out.println("Invalid operation!");
            return false;
        }
        char L_type = L.charAt(0);
        L = L.substring(2, L.length());
        char R_type = R.charAt(0);
        R = R.substring(2, R.length());
        switch (L_type){
            case 'R':
                // code for MOV to register
                if(R_type == 'L') {
                    if((Byte.parseByte(R) > registers.get(L))){
                        registers.put("DA", (byte)-1);
                    }
                    else if ((Integer.parseInt(R) < registers.get(L))){
                        registers.put("DA", (byte)1);
                    }
                    else if ((Integer.parseInt(R) == registers.get(L))){
                        registers.put("DA", (byte)0);
                    }
                    return false;

                }
                if(R_type == 'A'){
                    if(readMemory(Byte.parseByte(R)) > registers.get(L)){
                        registers.put("DA", (byte)-1);
                    }
                    else if (readMemory(Byte.parseByte(R)) < registers.get(L)){
                        registers.put("DA", (byte)1);
                    }
                    else if (readMemory(Byte.parseByte(R)) == registers.get(L)){
                        registers.put("DA", (byte)0);
                    }
                    return false;

                }
                if(R_type == 'R'){
                    if(registers.get(R) > registers.get(L)){
                        registers.put("DA", (byte)-1);
                    }
                    else if (registers.get(R) < registers.get(L)){
                        registers.put("DA", (byte)1);
                    }
                    else if (registers.get(R) == registers.get(L)){
                        registers.put("DA", (byte)0);
                    }
                    return false;

                }
                break;
            case 'A':
                if(R_type == 'L') {
                    if(Byte.parseByte(R) > readMemory(Byte.parseByte(L))){
                        registers.put("DA", (byte)-1);
                    }
                    else if (Byte.parseByte(R) < readMemory(Byte.parseByte(L))){
                        registers.put("DA", (byte)1);
                    }
                    else if (Byte.parseByte(R) == readMemory(Byte.parseByte(L))){
                        registers.put("DA", (byte)0);
                    }
                    return false;

                }
                if(R_type == 'A'){
                    if(readMemory(Byte.parseByte(R)) > readMemory(Byte.parseByte(L))){
                        registers.put("DA", (byte)-1);
                    }
                    else if (readMemory(Byte.parseByte(R)) < readMemory(Byte.parseByte(L))){
                        registers.put("DA", (byte)1);
                    }
                    else if (readMemory(Byte.parseByte(R)) == readMemory(Byte.parseByte(L))){
                        registers.put("DA", (byte)0);
                    }
                    return false;

                }
                if(R_type == 'R'){
                    if(registers.get(R) > readMemory(Byte.parseByte(L))){
                        registers.put("DA", (byte)-1);
                    }
                    else if (registers.get(R) < readMemory(Byte.parseByte(L))){
                        registers.put("DA", (byte)1);
                    }
                    else if (registers.get(R) == readMemory(Byte.parseByte(L))){
                        registers.put("DA", (byte)0);
                    }
                    return false;

                }
                break;
            case 'L':
                if(R_type == 'L') {
                    if(Byte.parseByte(R) > Byte.parseByte(L)){
                        registers.put("DA", (byte)-1);
                    }
                    else if (Byte.parseByte(R) < Byte.parseByte(L)){
                        registers.put("DA", (byte)1);
                    }
                    else if (Byte.parseByte(R) == Byte.parseByte(L)){
                        registers.put("DA", (byte)0);
                    }
                    return false;

                }
                if(R_type == 'A'){
                    if(readMemory(Byte.parseByte(R)) > Byte.parseByte(L)){
                        registers.put("DA", (byte)-1);
                    }
                    else if (readMemory(Byte.parseByte(R)) < Byte.parseByte(L)){
                        registers.put("DA", (byte)1);
                    }
                    else if (readMemory(Byte.parseByte(R)) == Byte.parseByte(L)){
                        registers.put("DA", (byte)0);
                    }
                    return false;

                }
                if(R_type == 'R'){
                    if(registers.get(R) > Byte.parseByte(L)){
                        registers.put("DA", (byte)-1);
                    }
                    else if (registers.get(R) < Byte.parseByte(L)){
                        registers.put("DA", (byte)1);
                    }
                    else if (registers.get(R) == Byte.parseByte(L)){
                        registers.put("DA", (byte)0);
                    }
                    return false;

                }
                break;
        }
        return false;
    }

    public void JMP(int line){
        // cheack DA register state;
        // make cursor location` the start of file
        // read line (forexample 4 time)
        // exit, let exicuteprogramm() do its work from 4 line;
    }
    public void JL(int line){}
    public void JG(int line){}
    public void JE(int line){}


    public boolean decode(String line) throws IOException {

        String[] parts = line.split(" ");
        String inst = parts[0];

        String L_oper = corrLValue(parts[1]);
        char L_type = L_oper.charAt(0);
        String L_value = corrLValue(parts[1]).substring(2, L_oper.length());

        //werite instruction to memory
        memory[0] = insts.get(inst); // instruction

        memory[1] = oper_t.get(L_type); // left type
        switch (L_type){ // left value
            case 'R':
                memory[2] = regs.get(L_value);
                break;
            case 'A':
            case 'L':
                memory[2] = Byte.parseByte(L_value);
                break;
        }

        if (parts.length == 2 && insts.get(inst) >= 9 && insts.get(inst) >= 13) {
            return true;
        }

        else if (parts.length == 3) {

            String R_oper = corrRValue(parts[2]);
            char R_type = R_oper.charAt(0);
            String R_value = corrRValue(parts[2]).substring(2, R_oper.length());

            memory[3] = oper_t.get(R_type); // right type
            switch (R_type){ // right value
                case 'R':
                    memory[4] = regs.get(R_value);
                    break;
                case 'A':
                case 'L':
                    memory[4] = Byte.parseByte(R_value);
                    break;
            }
            return true;
        }

        else{
            System.out.println("Invalid Instruction");
            return false;
        }
    }
    public void execute(String path){
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 2){
                    String instruction = parts[0];
                    String value = parts[1];

// code for jumps and Not operations
                }
                else if (parts.length == 3){
                    String instruction = parts[0];
                    String lvalue = parts[1];
                    String rvalue = parts[2];

// code for mov, ariphmetics and logic operation

                    switch (instruction){
                        case "MOV":
                            if (MOV(lvalue,rvalue)){
                                System.out.println(line + ConCol.GREEN + " DONE" + ConCol.RESET);
                            }
                            else {
                                System.out.println(line + ConCol.RED + " FAILED" + ConCol.RESET);
                            }
                            break;
                        case "ADD":
                            if (ADD(lvalue,rvalue)){
                                System.out.println(line + ConCol.GREEN + " DONE" + ConCol.RESET);
                            }
                            else {
                                System.out.println(line + ConCol.RED + " FAILED" + ConCol.RESET);
                            }
                            break;
                        case "SUB":
                            if (SUB(lvalue,rvalue)){
                                System.out.println(line + ConCol.GREEN + " DONE" + ConCol.RESET);
                            }
                            else {
                                System.out.println(line + ConCol.RED + " FAILED" + ConCol.RESET);
                            }
                            break;
                        case "MUL":
                            if (MUL(lvalue,rvalue)){
                                System.out.println(line + ConCol.GREEN + " DONE" + ConCol.RESET);
                            }
                            else {
                                System.out.println(line + ConCol.RED + " FAILED" + ConCol.RESET);
                            }
                            break;
                        case "DIV":
                            if (DIV(lvalue,rvalue)){
                                System.out.println(line + ConCol.GREEN + " DONE" + ConCol.RESET);
                            }
                            else {
                                System.out.println(line + ConCol.RED + " FAILED" + ConCol.RESET);
                            }
                            break;
                        case "AND":
                            if (AND(lvalue,rvalue)){
                                System.out.println(line + ConCol.GREEN + " DONE" + ConCol.RESET);
                            }
                            else {
                                System.out.println(line + ConCol.RED + " FAILED" + ConCol.RESET);
                            }
                            break;
                        case "OR":
                            if (OR(lvalue,rvalue)){
                                System.out.println(line + ConCol.GREEN + " DONE" + ConCol.RESET);
                            }
                            else {
                                System.out.println(line + ConCol.RED + " FAILED" + ConCol.RESET);
                            }
                            break;
                        case "XOR":
                            if (XOR(lvalue,rvalue)){
                                System.out.println(line + ConCol.GREEN + " DONE" + ConCol.RESET);
                            }
                            else {
                                System.out.println(line + ConCol.RED + " FAILED" + ConCol.RESET);
                            }
                            break;
                        case "CMP":
                            CMP(lvalue,rvalue);
                            System.out.println(line + ConCol.GREEN + " DONE" + ConCol.RESET);
                            break;
                    }
                }
            }
        }
        catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }
}
