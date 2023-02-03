package sic.machine.devices;

import sic.machine.memory.unit.SICByte;

import java.io.IOException;
import java.io.InputStream;

public class InputDevice extends Device {
    private InputStream inputStream;
    public InputDevice(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    @Override
    public boolean test() {
        try {
            return inputStream.available() > 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public SICByte read() {
        try {
            return new SICByte(inputStream.read());

        } catch (IOException e) {
                e.printStackTrace();
        }
        return new SICByte("0");
    }

}
