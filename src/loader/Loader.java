package loader;

import sic.machine.memory.Memory;
import sic.machine.memory.unit.SICByte;

import java.io.FileReader;
import java.util.Scanner;
import java.util.TreeMap;

//Simple loader, reads the provided .obj file and loads it into the memory
public class Loader {
    private TreeMap<Integer, SICByte> memory;
    private Integer codeLength;
    private Integer codeAddress;
    private Integer startAddress;

    public Loader() {
        this.memory = new TreeMap<Integer, SICByte>();
    }

    public Integer read(String file){
        Scanner sc = null;
        try {
            sc = new Scanner(new FileReader(file));

        }catch (Exception e){
            e.printStackTrace();
        }
        //Reads header
        readHeader(sc.nextLine());

        //Starts reading body, ends with End line
        while(sc.hasNext()){
            readBody(sc.nextLine());
        }

        sc.close();

        //Files the reserved memory
        for(int i = memory.lastKey() + 1; i < codeLength; i++){
            memory.put(i,new SICByte(0));
        }

        System.out.println("Finished loading");
        return startAddress;
    }
    private void readBody(String line){
        Integer codeAddressT = 0;
        //Reads Body
        if(line.charAt(0) == 'E'){
            readEnd(line);
            return;
        } else if (line.charAt(0) != 'T') {
            System.err.println(".obj not formatted correctly");
            return;
        }

        codeAddressT = Integer.parseInt(line.substring(1,7),16);
        Integer len = Integer.parseInt(line.substring(7,9),16);

        int start = 9;
        while (len > 0){
            String substring = line.substring(start,start + 2);
            Integer code = Integer.parseInt(line.substring(start,start + 2),16);
            memory.put(codeAddressT,new SICByte(code));
            codeAddressT += 1;
            start += 2;
            len -= 1;
        }

    }
    private void readEnd(String line){
        //Reads END
        if(line.charAt(0) != 'E'){
            System.err.println(".obj not formatted correctly");
            return;
        }
        startAddress = Integer.parseInt(line.substring(1,7),16);
    }
    private void readHeader(String line){
        //Reads Header
        if(line.charAt(0) != 'H'){
            System.err.println(".obj not formatted correctly");
            return;
        }
        //Code Address: 6nibls
        codeAddress = Integer.parseInt(line.substring(7,13),16);
        codeLength = Integer.parseInt(line.substring(13,19),16);

    }
    public Integer getCodeLength(){
        return this.codeLength;
    }
    public Integer getFirstKey(){
        return this.memory.firstKey();
    }
    public Memory returnMemory(){
        return new Memory(memory);
    }

}
