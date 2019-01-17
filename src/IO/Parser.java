package IO;

// library imports
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.File;
import java.util.regex.Pattern;

// package imports
import Exception.InvalidInputException;
import Structure.Pair;

public class Parser {

    // constants
    private final String fileName;
    private final boolean debug;

    // containers
    private String name;
    private LinkedList<Pair<Integer,String>> forcedPartialAssignments;
    private LinkedList<Pair<Integer,String>> forbiddenMachines;
    private LinkedList<Pair<String,String>> tooNearMachines;
    private LinkedList<LinkedList<Integer>> machinePenalties;
    private LinkedList<Pair<Pair<String,String>,Integer>> tooNearPenalties;

    public Parser(String fileName, boolean debug) {
        this.fileName = fileName;
        this.debug = debug;
        initialize();
    }

    private void initialize() {

        Scanner fileRead;
        String lineRead;

        try {

            fileRead = new Scanner(new File(getFileName())).useDelimiter("\\n");

            // add functionality for "Error while parsing input file error"
            int kwCounter = 0;

            while (fileRead.hasNext()) {
                lineRead = fileRead.nextLine().trim();
                debug(String.format("Current Line In File - %s", lineRead));
                switch (lineRead) {
                    case "Name:": readName(fileRead); break;
                    case "forced partial assignment:": readForcedPartialAssignments(); break;
                    case "forbidden machine:": readForbiddenMachines(); break;
                    case "too-near tasks:": readTooNearTasks(); break;
                    case "machine penalties:": readMachinePenalties(); break;
                    case "too-near penalities": readTooNearPenalties(); break;
                    default: if (!lineRead.isEmpty()) throw new InvalidInputException("Error while parsing input file");
                }
            }
            fileRead.close();
        } catch (FileNotFoundException fileNotFoundException) {
            System.exit(-1);
        } catch (InvalidInputException invalidInputException) {
            System.out.println("bad stuff happened");
            System.exit(-1);
        }
    }

    private void readName(Scanner fileRead) throws InvalidInputException {
        Pattern namePattern = Pattern.compile("[\\s]*[\\S]+[\\s]*");
        if (fileRead.hasNext(namePattern) & name == null) {
            name = fileRead.nextLine();
        } else {
            throw new InvalidInputException("Name could not be found");
        }
        debug(String.format("Assigned Name - %s", name));
    }

    private void readForcedPartialAssignments(Scanner fileRead) throws InvalidInputException {
        Pattern forcedPartialAssignmentPattern = Pattern.compile("");
        forcedPartialAssignments = new LinkedList<>();
        while (fileRead.hasNext) {
            if (fileRead.hasNext(forcedPartialAssignmentPattern)) {
                forcedPartialAssignments.add(new Pair(fileRead.nextLine()));
            } else {
                throw new InvalidInputException("Some error happened");
            }
        }
    }

    private void readForbiddenMachines() {

    }

    private void readTooNearTasks() {

    }

    private void readMachinePenalties() {

    }

    private void readTooNearPenalties() {

    }

    private void debug(String message) {
        if (getDebug()) {
            System.out.println("Parser Debug Message: " + message);
        }
    }
}
