package sic.machine.devices;

import sic.machine.memory.unit.SICByte;

import java.io.IOException;
import java.io.OutputStream;

public class OutputDevice extends Device{
    private OutputStream outputStream;
    public OutputDevice(OutputStream outputStream) {
        this.outputStream = outputStream;
    }
    @Override
    public void write(SICByte sicByte) {
        try {
            outputStream.write(sicByte.getValue());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
