package sic.machine;

import sic.machine.devices.*;
import sic.machine.memory.Memory;
import sic.machine.memory.unit.SICByte;
import sic.machine.memory.unit.SICWord;
import sic.machine.opcode.OPCode;
import sic.machine.regs.Register;
import sic.machine.regs.Registers;

import java.util.Scanner;
//Machine holds all components and executes the code
public class Machine {
    private final int deviceNumber = 256;
    private Registers registers;
    private Memory  memory;
    private Integer startAddress;
    private Boolean running;
    private Integer codeLength;
    private Integer firstKey;
    private StringBuilder outputString;

    private Device[] devices;

    public Machine(Memory memory, Integer startAddress, Integer codeLength, Integer firstKey){
        this.memory = memory;
        this.registers = new Registers();
        this.devices = new Device[deviceNumber];
        this.running = false;
        this.startAddress = startAddress;
        this.registers.setReg("PC",startAddress);
        this.codeLength = codeLength;
        this.firstKey = firstKey;
        this.outputString = new StringBuilder();
        initDevices();
    }

    //Initialises stdin, stdout, stderr
    private void initDevices(){
        this.devices[0] = new InputDevice(System.in);
        this.devices[1] = new OutputDevice(System.out);
        this.devices[2] = new OutputDevice(System.err);
    }
    private void notImplemented(String mnemonic){
        System.err.println(mnemonic + " not Implemented.");
    }
    private void invalidOpcode(int opcode){
        System.err.println("OP Code: " + Integer.toHexString(opcode) + " IS INVALID");
    }
    private void invalidAddressing(){
        System.err.println("InvalidAddresing");
    }
    public void start(boolean printMem, boolean auto, int sleep){
        this.running = true;

        Scanner sc = new Scanner(System.in);
        while (running){
            clearConsole();

            System.out.print(regDump());
            if(printMem) System.out.print(memDump());
            System.out.println(outputString.toString());

            //if step is turned on, wait for step/stop
            if(!auto){
                String next = sc.nextLine();
                if(next.equalsIgnoreCase("auto")) auto = true;
                if(next.equalsIgnoreCase("stop")) break;
            }

            execute();
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
    public void execute(){
        if(!running) return;
        Integer B1 = fetch();
        if(execF1(B1)) return;
        Integer B2 = fetch();
        if(execF2(B1,B2)) return;
        Integer ni = B1 & 0b00000011;//get n and i
        B1 = B1 & 0b11111100; // OPCODE is 6bit, bit 7 and 8 are set to 0
        if(execF3F4(B1,ni,B2)) return;
    }
    private boolean execF1(Integer opcode){
        switch (opcode){
            case OPCode.NORM -> {
                Integer regA = registers.getRegValue("F");
                this.registers.setReg("PC",this.registers.getRegValue("PC")+1);
                return true;
            }
            case OPCode.FIX -> {
                this.registers.setReg("PC",this.registers.getRegValue("PC")+1);
                return true;
            }
            default -> {
                return false;
            }
        }
    }
    private boolean execF2(Integer opcode, Integer op){
        SICByte regs = new SICByte(op);
        switch (opcode){
            case OPCode.ADDR -> {
                Integer r2 = regs.getHalfByte(SICByte.RHalf);
                Integer v1 = this.registers.getRegValue(regs.getHalfByte(SICByte.LHalf));
                Integer v2 = this.registers.getRegValue(r2);

                this.registers.setReg(r2,v1+v2);
                return true;
            }
            case OPCode.CLEAR -> {
                this.registers.setReg(regs.getHalfByte(SICByte.LHalf),0);
                return true;
            }
            case OPCode.COMPR -> {
                int comparison = 0;
                Integer v1 = this.registers.getRegValue(regs.getHalfByte(SICByte.LHalf));
                Integer v2 = this.registers.getRegValue(regs.getHalfByte(SICByte.RHalf));

                if(v1 == v2){
                    comparison = 0;
                }else if(v1 > v2){
                    comparison = 1;
                } else if (v1 < v2 ) {
                    comparison = 2;
                }

                this.registers.setReg(8,comparison);
                return true;
            }
            case OPCode.DIVR -> {
                Integer r2 = regs.getHalfByte(SICByte.RHalf);
                Integer v1 = this.registers.getRegValue(regs.getHalfByte(SICByte.LHalf));
                Integer v2 = this.registers.getRegValue(r2);

                this.registers.setReg(r2,v2/v1);
                return true;
            }
            case OPCode.MULR -> {
                Integer r2 = regs.getHalfByte(SICByte.RHalf);
                Integer v1 = this.registers.getRegValue(regs.getHalfByte(SICByte.LHalf));
                Integer v2 = this.registers.getRegValue(r2);

                this.registers.setReg(r2,v2*v1);
                return true;
            }
            case OPCode.RMO -> {
                Integer v1 = this.registers.getRegValue(regs.getHalfByte(SICByte.LHalf));
                Integer r2 = regs.getHalfByte(SICByte.RHalf);

                this.registers.setReg(r2,v1);
                return true;
            }
            case OPCode.SHIFTR -> {
                Integer r1 = regs.getHalfByte(SICByte.LHalf);
                Integer v1 = this.registers.getRegValue(r1);
                Integer n = regs.getHalfByte(SICByte.RHalf) + 1;

                this.registers.setReg(r1,v1 >> n);
                return true;
            }
            case OPCode.SHIFTL -> {
                Integer r1 = regs.getHalfByte(SICByte.LHalf);
                Integer v1 = this.registers.getRegValue(r1);
                Integer n = regs.getHalfByte(SICByte.RHalf) + 1;

                this.registers.setReg(r1,v1 << n);
                return true;
            }
            case OPCode.SUBR -> {
                Integer r2 = regs.getHalfByte(SICByte.RHalf);
                Integer v1 = this.registers.getRegValue(regs.getHalfByte(SICByte.LHalf));
                Integer v2 = this.registers.getRegValue(r2);

                this.registers.setReg(r2,v2-v1);
                return true;
            }
            case OPCode.SVC -> {
                notImplemented("SVC");
                return true;
            }
            case OPCode.TIXR -> {
                notImplemented("TIXR");
                return true;
            }
            default -> {
                return false;
            }
        }
    }
    private boolean execF3F4(Integer opcode, Integer ni, Integer operand){
        Integer B3 = fetch();
        Integer address = 0;
        boolean signed = true;
        //Shift left B2 to get bits X B P E
        int xbpe = operand >> 4;
        Integer format = xbpe & 1;//e=o format3, e=1 format4

        //Defines TA calculation
        int b = xbpe & 0b0100;
        int p = xbpe & 0b0010;

        //get last 4 bits of byte2 and adds them to the top of B3 to get 12bit disp
        String disp = SICByte.toHexByte(operand & 0b00001111)
                + SICByte.toHexByte(B3);


        if(b == 0 && p == 0){
            if(format == 1){
                //format 4 has a 20bit address
                disp.concat(SICByte.toHexByte(fetch()));
            }
            address = Integer.parseInt(disp,16);
        } else if (format == 0 && b > 0 && p == 0) {
            //Base-relative
            address = registers.getRegValue("B") + Integer.parseInt(disp,16);
        } else if (format == 0 & b == 0 & p > 0) {
            //PC-relative, 2's complement
            Integer disptwos = SICWord.twosComp(disp,12);
            address = registers.getRegValue("PC") + disptwos;
        }

        //Adds X (indexed addressing) to TA
        int x = xbpe & 0b1000;
        if(x > 0){
            address += registers.getRegValue("X");
        }

        //defines use of the TA
        if(ni == 1){//BIT i on
            if(x > 1) System.err.println("Immediate and indexing cant be together");
            //Immediate
            operand = address;

        }else if(ni == 2){//BIT n on
            if(x > 1) System.err.println("Indirect and indexing cant be together");
            //Indirect -> fetches word at location, value of word is address
            address = memory.getWord(address);
            operand = memory.getWord(address);
        }else if(ni == 0 || ni == 3){
            //Simple
            if(format == 0 && ni == 0){
                //SIC INSTRUCTION, bits b, p and e are also added to disp to form address
                address = Integer.parseInt(SICByte.toHexByte(operand & 0b01111111) +
                        SICByte.toHexByte(B3),16);
            }
            if(opcode == OPCode.RD || opcode == OPCode.WD || opcode == OPCode.LDCH || opcode == OPCode.STCH ){
                signed = false;
                operand = memory.getByte(address);
            }else {
                operand = memory.getWord(address);
            }
        }

        if(signed) operand = SICWord.twosComp(SICWord.toHexWord(operand),24);

        switch (opcode){
            case OPCode.ADD -> {
                Integer A = this.registers.getRegValue("A");
                this.registers.setReg("A",A + operand);
                return true;
            }
            case OPCode.ADDF -> {
                Integer F = this.registers.getRegValue("F");
                this.registers.setReg("F",F + operand);
                return true;
            }
            case OPCode.AND -> {
                Integer A = this.registers.getRegValue("A");
                this.registers.setReg("A",A & operand);
                return true;
            }
            case OPCode.COMP -> {
                int comparison = 0;
                Integer A = this.registers.getRegValue("A");

                if(A.equals(operand)){
                    comparison = 0;
                }else if(A > operand){
                    comparison = 1;
                } else if (A < operand ) {
                    comparison = 2;
                }

                this.registers.setReg("SW",comparison);
                return true;
            }
            case OPCode.COMPF -> {
                int comparison = 0;
                Integer F = this.registers.getRegValue("F");
                if(F.equals(operand)){
                    comparison = 0;
                }else if(F > operand){
                    comparison = 1;
                } else if (F < operand ) {
                    comparison = 2;
                }

                this.registers.setReg("SW",comparison);
                return true;
            }
            case OPCode.DIV -> {
                Integer A = this.registers.getRegValue("A");
                this.registers.setReg("A",A / operand);
                return true;
            }
            case OPCode.DIVF -> {
                Integer F = this.registers.getRegValue("F");
                this.registers.setReg("F",F / operand);
                return true;
            }
            case OPCode.J -> {
                Integer PC = this.registers.getRegValue("PC") - (3 + format);
                if(PC.equals(address)){
                    running = false;
                    System.out.println("HALT");
                }else {
                    this.registers.setReg("PC", address);
                }
                return true;
            }
            case OPCode.JEQ -> {
                if(this.registers.getRegValue("SW") == 0)
                    this.registers.setReg("PC",address);
                return true;

            }
            case OPCode.JGT -> {
                if(this.registers.getRegValue("SW") == 1)
                    this.registers.setReg("PC",address);
                return true;
            }
            case OPCode.JLT -> {
                if(this.registers.getRegValue("SW") == 2)
                    this.registers.setReg("PC",address);
                return true;
            }
            case OPCode.JSUB -> {
                this.registers.setReg("L",this.registers.getRegValue("PC"));
                this.registers.setReg("PC",address);
                return true;
            }
            case OPCode.LDA -> {
                this.registers.setReg("A",operand);
                return true;
            }
            case OPCode.LDB -> {
                this.registers.setReg("B",operand);
                return true;
            }
            case OPCode.LDCH -> {
                StringBuilder A = new StringBuilder(SICWord.toHexWord(this.registers.getRegValue("A")));
                SICByte c = new SICByte(operand);

                //Set right most byte
                A.setCharAt(4,c.getHalfByteAsHex(SICByte.LHalf).charAt(0));
                A.setCharAt(5,(char)c.getHalfByteAsHex(SICByte.RHalf).charAt(0));
                this.registers.setReg("A",Integer.parseInt(A.toString(),16));
                return true;
            }
            case OPCode.LDF -> {
                this.registers.setReg("F",operand);
                return true;
            }
            case OPCode.LDL -> {
                this.registers.setReg("L",operand);
                return true;
            }
            case OPCode.LDS -> {
                this.registers.setReg("S",operand);
                return true;
            }
            case OPCode.LDT -> {
                this.registers.setReg("T",operand);
                return true;
            }
            case OPCode.LDX -> {
                this.registers.setReg("X",operand);
                return true;
            }
            case OPCode.LPS -> {
                notImplemented("LPS");
                return true;
            }
            case OPCode.MUL -> {
                Integer A = this.registers.getRegValue("A");
                this.registers.setReg("A",A * operand);
                return true;
            }
            case OPCode.MULF -> {
                Integer F = this.registers.getRegValue("F");
                this.registers.setReg("F",F * operand);
                return true;

            }
            case OPCode.OR -> {
                Integer A = this.registers.getRegValue("A");
                this.registers.setReg("A",A | operand);
                return true;
            }
            case OPCode.RD -> {
                SICByte c = null;
                if(this.devices[operand] != null){
                    c = this.devices[operand].read();
                }else{
                    this.devices[operand] = new FileDevice(operand);
                    c = this.devices[operand].read();
                }
                StringBuilder A = new StringBuilder(SICWord.toHexWord(this.registers.getRegValue("A")));
                //Set right most byte
                A.setCharAt(4,c.getHalfByteAsHex(SICByte.LHalf).charAt(0));
                A.setCharAt(5,c.getHalfByteAsHex(SICByte.RHalf).charAt(0));
                this.registers.setReg("A",Integer.parseInt(A.toString(),16));
                return true;
            }
            case OPCode.RSUB -> {
                this.registers.setReg("PC",this.registers.getRegValue("L"));
                return true;
            }
            case OPCode.STA -> {
                this.memory.setWord(address,this.registers.getRegValue("A"));
                return true;
            }
            case OPCode.STB -> {
                this.memory.setWord(address,this.registers.getRegValue("B"));
                return true;
            }
            case OPCode.STCH ->{
                StringBuilder A = new StringBuilder(SICWord.toHexWord(this.registers.getRegValue("A")));
                String val = A.substring(4,6);
                this.memory.setByte(address,Integer.parseInt(val,16));
                return true;
            }
            case OPCode.STF -> {
                this.memory.setWord(address,this.registers.getRegValue("F"));
                return true;
            }
            case OPCode.STI -> {
                notImplemented("STI");
                return true;
            }
            case OPCode.STL -> {
                this.memory.setWord(address,this.registers.getRegValue("L"));
                return true;
            }
            case OPCode.STS -> {
                this.memory.setWord(address,this.registers.getRegValue("S"));
                return true;
            }
            case OPCode.STSW -> {
                this.memory.setWord(address,this.registers.getRegValue("SW"));
                return true;
            }
            case OPCode.STT -> {
                this.memory.setWord(address,this.registers.getRegValue("T"));
                return true;
            }
            case OPCode.STX -> {
                this.memory.setWord(address,this.registers.getRegValue("X"));
                return true;
            }
            case OPCode.SUB -> {
                Integer A = this.registers.getRegValue("A");
                this.registers.setReg("A",A - operand);
                return true;
            }
            case OPCode.SUBF -> {
                Integer F = this.registers.getRegValue("F");
                this.registers.setReg("F",F - operand);
                return true;
            }
            case OPCode.TD -> {
                notImplemented("TD");
                return true;
            }
            case OPCode.WD -> {
                StringBuilder A = new StringBuilder(SICWord.toHexWord(this.registers.getRegValue("A")));
                String val = A.substring(4,6);

                SICByte c = new SICByte(val);
                if(this.devices[operand] != null){
                    this.devices[operand].write(c);
                }else{
                    this.devices[operand] = new FileDevice(operand);
                    this.devices[operand].write(c);
                }
                //Remembers STDOUT for nicer print
                if(operand == 1) outputString.append((char)c.getValue().intValue());
                return true;
            }
        }
        return false;
    }
    private Integer fetch(){
        //GET 1Byte from memory (where PC is pointing to)
        Integer PC = this.registers.getRegValue(7);
        Integer val = this.memory.getByte(PC);
        this.registers.setReg(7,PC+1);
        return val;
    }
    public String regDump(){
        StringBuilder regdump = new StringBuilder();
        int c = 0;
        for(Register reg : registers.getRegs()){
            c++;
            regdump.append(reg.getName() + ": ");
            regdump.append(SICByte.toHexByte(reg.getValue()) + " | ");
            if(c == 3) regdump.append("\n");
            c = c % 3;
        }
        return regdump.toString();
    }
    public String memDump(){
        Integer last = this.codeLength;
        StringBuilder memDump = new StringBuilder();
        int row = 0;
        int column = 0;
        int addr = this.firstKey;
        while (last > 0){
            if(column == 0) {
                memDump.append("\n" + SICWord.toHexWord(row) + " |");
                row += 16;
            }
            memDump.append(" " + SICByte.toHexByte(this.memory.getByte(addr)));

            column = (column + 1) % 16;
            addr++;
            last--;
        }
        memDump.append("\n");
        return memDump.toString();
    }
    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}