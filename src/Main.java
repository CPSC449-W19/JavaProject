import IO.Parser;
import generator.*;

import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger("");

    public static void main(String[] args) {
        // File generator shit
        try {
        Boolean debug = (args[0].toString() == "--debug");
        String option = args[1];
        Integer range = Integer.parseInt(args[2]);
        
        InputGenerator gen = new InputGenerator(debug,option,range);
        gen.generate();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        }
}