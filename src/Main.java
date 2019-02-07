import IO.Parser;

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


        //System.out.println("CPSC 449 Winter 2019, test");
        Parser parser;
        for (String fileName : filesToRead) {
            parser = new Parser("src/InputFiles/"+fileName,true);
            System.out.println(parser.getFileName());
            System.out.println(parser.getName());
            System.out.println(parser.getForcedPartialAssignments());
            System.out.println(parser.getForbiddenMachines());
            System.out.println(parser.getTooNearMachines());
            System.out.println(parser.getMachinePenalties());
            System.out.println(parser.getTooNearPenalties());
        }
    }
}