import IO.Parser;
import Structure.BranchAndBound;

import java.io.File;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {

        LinkedList<String> filesToRead = new LinkedList<>();

        if (args.length > 1) {
            filesToRead.add(args[1]);
        } else {
            File directory = new File("src/InputFIles");
            File[] listOfFiles = directory.listFiles();
            for (File file : listOfFiles) {
                filesToRead.add(file.getName());
            }
        }

        Parser parser;
        BranchAndBound branchAndBound;
        parser = new Parser("src/InputFiles/optzero.txt",true);
        branchAndBound = new BranchAndBound(parser);
        branchAndBound.findSolution();
        /*
        for (String fileName : filesToRead) {
            parser = new Parser("src/InputFiles/"+fileName,false);
            branchAndBound = new BranchAndBound(parser);
            branchAndBound.findSolution();
        }
        */
    }
}