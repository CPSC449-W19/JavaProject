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
    private LinkedList<Pair<String,String>> tooNearTasks;
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
                    case "forced partial assignment:": readForcedPartialAssignments(fileRead); break;
                    case "forbidden machine:": readForbiddenMachines(fileRead); break;
                    case "too-near tasks:": readTooNearTasks(fileRead); break;
                    case "machine penalties:": readMachinePenalties(fileRead); break;
                    case "too-near penalities": readTooNearPenalties(fileRead); break;
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
        debug("Reading Section - Name");
        Pattern namePattern = Pattern.compile("[\\s]*[\\S]+[\\s]*");
        String temp;
        if (fileRead.hasNext(namePattern) & name == null) {
            temp = fileRead.next(namePattern);
            debug(String.format("Read Name - %s", temp));
            name = temp;
        } else {
            throw new InvalidInputException("Error while parsing input file");
    }
        debug(String.format("Assigned Name - %s", name));
    }

    private void readForcedPartialAssignments(Scanner fileRead) throws InvalidInputException {
        debug("Reading Section - Forced Partial Assignments");
        Pattern forcedPartialAssignmentPattern = Pattern.compile("[(][1-8],[A-H][)]");
        forcedPartialAssignments = new LinkedList<>();
        String temp;
        String[] temps;
        while (fileRead.hasNext()) {
            if (fileRead.hasNext(forcedPartialAssignmentPattern)) {
                temp = fileRead.next(forcedPartialAssignmentPattern).trim();
                temps = temp.substring(1,temp.length()-1).split(",");
                forcedPartialAssignments.add(new Pair(Integer.parseInt(temps[0]),temps[1]));
            } else {
                throw new InvalidInputException("Some error happened");
            }
        }
    }

    private void readForbiddenMachines(Scanner fileRead) {
        debug("Reading Section - Forbidden Machines");
        Pattern forbiddenMachinePattern = Pattern.compile("[(][1-8],[A-H][)]");
        forbiddenMachines = new LinkedList<>();
        String temp;
        String[] temps;
        while () {}
    }

    private void readTooNearTasks(Scanner fileRead) {
        debug("Reading Section - Too-Near Tasks");
        Pattern tooNearTasksPattern = Pattern.compile("[(][A-H],[A-H][)]");

    }

    private void readMachinePenalties(Scanner fileRead) {
        debug("Reading Section - Machine Penalties");
        Pattern machinePenalties = Pattern.compile("[\\d+]\\s[\\d+]\\s[\\d+]\\s[\\d+]\\s[\\d+]\\s[\\d+]\\s[\\d+]\\s[\\d+]");
    }

    private void readTooNearPenalties(Scanner fileRead) {
        debug("Reading Section - Too-Near Penalties");
        Pattern tooNearPenalties = Pattern.compile("");
    }

    private void debug(String message) {
        if (isDebug()) {
            System.out.println("Parser Debug Message: " + message);
        }
    }

    public String getFileName() {
        return this.fileName;
    }

    public boolean isDebug() {
        return this.debug;
    }

    public String getName() {
        return this.name;
    }

    public LinkedList<Pair<Integer, String>> getForcedPartialAssignments() {
        return this.forcedPartialAssignments;
    }

    public LinkedList<Pair<Integer, String>> getForbiddenMachines() {
        return this.forbiddenMachines;
    }

    public LinkedList<Pair<String, String>> getTooNearMachines() {
        return this.tooNearTasks;
    }

    public LinkedList<LinkedList<Integer>> getMachinePenalties() {
        return this.machinePenalties;
    }

    public LinkedList<Pair<Pair<String, String>, Integer>> getTooNearPenalties() {
        return this.tooNearPenalties;
    }
}
