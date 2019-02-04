import IO.Parser;

import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger("");

    public static void main(String[] args) {
        System.out.println("CPSC 449 Winter 2019, test");
        Parser parser = new Parser("src/InputFiles/normalTest.txt",true);
        System.out.println(parser.getFileName());
        System.out.println(parser.getName());
        System.out.println(parser.getForcedPartialAssignments());
        System.out.println(parser.getForbiddenMachines());
        System.out.println(parser.getTooNearMachines());
        System.out.println(parser.getMachinePenalties());
        System.out.println(parser.getTooNearPenalties());
    }
}