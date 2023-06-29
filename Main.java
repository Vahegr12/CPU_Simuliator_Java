class Main {
        public static void main(String[]args){
        CPU cpu = new CPU();

        cpu.loadValueIntoRegister("AYB",5);
        cpu.performArithmeticOperation("ADD","AYB",3);
        cpu.compareRegisters("AYB","BEN");
        cpu.printRegisters();

        cpu.writeMemory(0,10);
        int value=cpu.readMemory(0);
        System.out.println("Memory value: " + value);
        }
    }
