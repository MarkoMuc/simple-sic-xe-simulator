package sic.machine.memory;

import sic.machine.memory.unit.SICByte;
import sic.machine.memory.unit.SICWord;

import java.util.TreeMap;

//Memory is mapped Integer - SICByte,

public class Memory {
    private final Integer MAX_ADDRESS = 0x8000;
    TreeMap<Integer, SICByte> memory;

    public Memory(TreeMap<Integer, SICByte> memory){
        this.memory = memory;
    }
    public Integer getByte(Integer addr){
        if(!memory.containsKey(addr)) memory.put(addr,new SICByte(0));
        return memory.get(addr).getValue();
    }
    public void setByte(Integer addr, Integer val){
        memory.get(addr).setValue(val);
    }
    public Integer getWord(Integer addr){
        StringBuilder res = new StringBuilder();
        for(int i=0; i < SICWord.LENGTH ; i++){
            if(!memory.containsKey(addr + i)) memory.put(addr + i, new SICByte(0));
            res.append(memory.get(addr+i).getHexValue());
        }
        //this adds AAA in form A->A->A and not A<-A<-A
        return Integer.valueOf(res.toString(),16);
    }
    public void setWord(Integer addr, Integer val){
        StringBuilder bVal = new StringBuilder(SICWord.toHexWord(val));
        for(int i=0; i < 3 ; i++){
            setByte(addr+i, Integer.valueOf(bVal.substring(0,2),16));
            bVal.delete(0,2);
        }
    }
}