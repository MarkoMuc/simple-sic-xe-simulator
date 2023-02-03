package sic.machine.devices;

import sic.machine.memory.unit.SICByte;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;


//Used for all devices number 2 onwards(including)
public class FileDevice extends Device {
    private String name;
    private RandomAccessFile RAF;

    public FileDevice(Integer name){
        this.name = SICByte.toHexByte(name) + ".dev";
        this.RAF = initRAF();
    }

    private RandomAccessFile initRAF(){
        try{
            return new RandomAccessFile(new File("./"+name),"rwd");
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }
    public String getName(){
        return this.name;
    }
    @Override
    public SICByte read() {
        try {
            return new SICByte(this.RAF.read());
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void write(SICByte sicByte) {
        try {
            this.RAF.write(sicByte.getValue());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
