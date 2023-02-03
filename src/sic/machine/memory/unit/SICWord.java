package sic.machine.memory.unit;

//SICWord is mostly used just to format hexWord
//twosComp is also here since its only used for operand calculation(other than PC TA-calculation)
public class SICWord {
    public static final int ALL = 0;
    public static final int FIRST = 2;
    public static final int LENGTH = 3;
    private SICByte[] word;

    public SICWord(SICByte[] word){
        Exception SICWORDException = new Exception("SICWORDException length");
        try {
            if(word.length > LENGTH) throw SICWORDException;
        }catch (Exception e){
            e.printStackTrace();
        }
        this.word = word;
    }

    public Integer getValue(int num){
        StringBuilder rez = new StringBuilder();
        for(int i = num; i < LENGTH;i++) {
             rez.append(word[i].getHexValue());
        }
        return Integer.valueOf(rez.toString(),16);
    }
    public void setValue(Integer value){
        for(SICByte b : this.word){
            b.setValue(value);
        }
    }
    public static String toHexWord(Integer value){
        return String.format("%1$06X",value);
    }
    public static Integer twosComp(String str,int bin) {
        if(bin == 12 && str.length() > 3) str = str.substring(1,str.length());
        if(str.length() > 6) str = str.substring(2,8);

        Integer num = Integer.valueOf(str, 16);

        return (num > (int)Math.pow(2,bin-1)-1) ? num - (int)Math.pow(2,bin) : num;
    }
}
