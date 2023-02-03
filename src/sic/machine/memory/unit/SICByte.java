package sic.machine.memory.unit;

public class SICByte {
    //Primary unit for the Machine, String value has leading 0 if needed
    public static final int LHalf = 0;
    public static final int RHalf = 1;
    private String value;

    public SICByte(String value) {
        this.value = value;
    }

    public SICByte(Integer value) {
        this.value = toHexByte(value);
    }
    public Integer getValue(){
        return Integer.valueOf(this.value,16);
    }
    public String getHexValue(){
        return this.value;
    }
    public Integer getHalfByte(int position){
        StringBuilder half = new StringBuilder(value);
        return Integer.valueOf(half.substring(position,position+1),16);
    }
    public String getHalfByteAsHex(int position){
        StringBuilder half = new StringBuilder(value);
        return half.substring(position,position+1);
    }
    public void setValue(Integer value){
        this.value = toHexByte(value);
    }
    public void setValue(String value){this.value = value;}

    //Used to create a string of 2 CHARS to represent the hex value. Adds leading 0 if needed!
    public static String toHexByte(Integer value){
        return String.format("%1$02X",value);
    }
}
