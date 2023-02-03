package sic.machine.devices;

import sic.machine.memory.unit.SICByte;

public abstract class Device {
    // TD-test device, RD-read device and WD-write device
    public boolean test(){
        return true;
    }
    public SICByte read(){
        return new SICByte("0x0");
    }
    public void write(SICByte sicByte){
    }
}
