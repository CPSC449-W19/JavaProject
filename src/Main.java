import IO.Output;
import IO.Parser;
import Structure.BranchAndBound;

import java.io.File;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {
      

        LinkedList<String> filesToRead = new LinkedList<>();
        LinkedList<String> filesToWrite = new LinkedList<>();
        if (args.length == 3) {
            filesToRead.add(args[1]);
            filesToWrite.add(args[2]);
        } else {
            File directory = new File("src/InputFiles");
            File[] listOfFiles = directory.listFiles();
            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    filesToRead.add(file.getName());
                    filesToWrite.add(file.getName());
                }
            }
        }

        Parser parser;
        BranchAndBound branchAndBound;
        Output output;
        //for (String fileName : filesToRead) {
            parser = new Parser("InputFiles/machpen1.txt",false);
            output = new Output("OutputFiles/machpen1.txt");
            if (parser.isException()) {
                output.write(parser.getMessage());
                output.close();
            } else {
                branchAndBound = new BranchAndBound(parser, false);
                branchAndBound.findSolution();
                output.write(branchAndBound.getMessage());
                output.close();
            }
        //}
    }
}
