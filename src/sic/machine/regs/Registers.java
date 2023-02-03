package sic.machine.regs;

import java.util.ArrayList;
import java.util.List;

/*
    Regs:
        A -> Accumulator used for arithmetic operations
        X -> Index reg
        L -> Linkage register(JSUB stores PC in here)
        B -> Base register
        S -> General working Register
        T -> General working Register
        F -> floating point accumulator
        PC -> Progrma Counter
        SW -> Status word, includes Conditino Code (CC)
 */

//Encapsulation of registers with simple getter and setter methods
//Both getter and setter can be used with register as an int or String

public class Registers {

    private List<Register> regs;
    private String[] names = {"A","X", "L", "B", "S", "T", "F", "PC", "SW"};
                            //0 ,  1,   2,   3 ,  4 ,  5,  6  ,  7 ,  8
    private final int REGCOUNT = 9;

    public Registers(){
        this.regs = genRegs();
    }

    private ArrayList<Register> genRegs(){
        ArrayList<Register> rez = new ArrayList<>();
        for (int i = 0; i < REGCOUNT; i++) {
                rez.add(new Register(names[i],0));
        }
        return rez;
    }
    public Register getRegister(String reg){
        switch (reg) {
            case "A": return this.regs.get(0);
            case "X": return this.regs.get(1);
            case "L": return this.regs.get(2);
            case "B": return this.regs.get(3);
            case "S": return this.regs.get(4);
            case "T": return this.regs.get(5);
            case "F": return this.regs.get(6);
            case "PC": return this.regs.get(7);
            case "SW": return this.regs.get(8);
            default:
                System.err.printf("REGISTER %s does not exist!",reg);
                return null;
        }
    }
    public Register getRegister(int regID){
        return this.regs.get(regID);
    }
    public Integer getRegValue(String regID){
        return getRegister(regID).getValue();
    }
    public Integer getRegValue(int regID){
        return getRegister(regID).getValue();
    }
    public void setReg(int regID,Integer val){
        getRegister(regID).setValue(val);
    }
    public void setReg(String regID,Integer val){
        getRegister(regID).setValue(val);
    }
    public List<Register> getRegs(){
        return regs;
    }
}
