package sic.machine.regs;

//Register class, holds name and value(as Integer) with simple getter and setter methods
public class Register {

    private String name;
    private Integer value;

    public Register(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }
    public Integer getValue() {
        return value;
    }
    public void setValue(Integer value) {
        this.value = value;
    }
}
