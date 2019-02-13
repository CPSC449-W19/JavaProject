import IO.Output;
import IO.Parser;
import Structure.BruteForce;

import java.io.File;
import java.util.LinkedList;

/**
 * Driver for assignment
 */
public class Main {

    public static void main(String[] args) {

        if (args.length != 2) {
        	System.out.println("Invalid number of arguments.");
            System.exit(0);
        } 
        
        Parser parser;
        Output output;
        parser = new Parser(args[0],false);
        output = new Output(args[1]);
        
        if (parser.isException()) {
             output.write(parser.getMessage());
             output.close();
         } else {
             BruteForce bruteforce = new BruteForce(parser);
             LinkedList<String> solution = bruteforce.findSolution();
             output.write(bruteforce.solutiontoString(solution));
             output.close();
         }

    }
    
} // End of Main
