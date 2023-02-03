import loader.Loader;
import sic.machine.Machine;

import java.util.Scanner;

//Main method, runs the setup of simulation
public class SicSim {
    private static Scanner sc = new Scanner(System.in);
    private static Machine machine;
    private static Loader loader= new Loader();
    public static void main(String[] args) {
        if(args.length < 1){
            System.err.println("no file");
            return;
        }

        //Loads into memory and inits machine
        Integer startAddress = loader.read(args[0]);
        machine = new Machine(loader.returnMemory(),startAddress, loader.getCodeLength(), loader.getFirstKey());

        boolean printMem = false;
        System.out.print("\nPrint Memory [y/N]: ");
        if(sc.nextLine().equalsIgnoreCase("y")) printMem = true;

        boolean auto = true;
        System.out.print("\nStep [y/N]: ");
        if(sc.nextLine().equalsIgnoreCase("y")) auto = false;

        int sleep = 10;
        System.out.print("\nSet sleep [default is 10]: ");
        if(sc.nextLine().isEmpty()) ;

        System.out.print("\nStart [s]: ");
        if(sc.nextLine().equalsIgnoreCase("s")) machine.start(printMem, auto, sleep);
        System.exit(1);
    }
}
