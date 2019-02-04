package IO;

// library imports
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.File;
import java.util.logging.*;
import java.util.regex.Pattern;

// package imports
import Exception.*;
import Structure.Pair;

/**
 * Parser does some stuff...
 *
 * @author Andrew Burton
 * @see Pair
 * @since January 21, 2019
 */

public class Parser {

    private final static Logger LOGGER = Logger.getLogger(Parser.class.getName());

    // constants
    private final String fileName;
    private final boolean debug;

    // shared regular expressions
    private final String SECTION = "(Name:|(forced[\\s+]partial[\\s+]assignment:)|(forbidden[\\s+]machine:)|(too-near[\\s+]tasks:)|(machine[\\s+]penalties:)|(too-near penalities))";

    // containers
    private String name;
    private LinkedList<Pair<Integer,String>> forcedPartialAssignments;
    private LinkedList<Pair<Integer,String>> forbiddenMachines;
    private LinkedList<Pair<String,String>> tooNearTasks;
    private LinkedList<LinkedList<Integer>> machinePenalties;
    private LinkedList<Pair<Pair<String,String>,Integer>> tooNearPenalties;

    /**
     * Constructs lists of data read from a file
     * @param fileName name of the file to read from
     * @param debug the debug flag status determines whether or not to print debug messages
     */
    public Parser(String fileName, boolean debug) {
        LogManager.getLogManager().reset();
        LOGGER.setLevel(Level.ALL);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.FINER);
        LOGGER.addHandler(consoleHandler);

        LOGGER.severe("severe");
        LOGGER.warning("warning");
        LOGGER.info("info");
        LOGGER.config("config");
        LOGGER.fine("fine");
        LOGGER.finer("finer");
        LOGGER.finest("finest");
        LOGGER.entering("s","a");
        LOGGER.exiting("s","a");

        this.fileName = fileName;
        this.debug = false;
        debug("Debugging Parser");
        debug(String.format("File Name Attribute Assigned As - %s", this.fileName));
        debug(String.format("Initializing Parser To Read - %s", fileName));
        initialize();
    }

    private void initialize() {

        LOGGER.entering(getClass().getName(),new Object(){}.getClass().getEnclosingMethod().getName());

        Scanner fileRead;
        String lineRead;

        try {

            fileRead = new Scanner(new File(getFileName())).useDelimiter("\\n");

            while (fileRead.hasNext()) {
                lineRead = fileRead.next().trim();
                debug(String.format("Current Line In File - %s", lineRead));
                switch (lineRead) {
                    case "Name:": readName(fileRead); break;
                    case "forced partial assignment:": readForcedPartialAssignments(fileRead); break;
                    case "forbidden machine:": readForbiddenMachines(fileRead); break;
                    case "too-near tasks:": readTooNearTasks(fileRead); break;
                    case "machine penalties:": readMachinePenalties(fileRead); break;
                    case "too-near penalities": readTooNearPenalties(fileRead); break;
                    default: if (!lineRead.isEmpty()) throw new ParsingInputException("Error while parsing input file");
                }
            }
            fileRead.close();
        } catch (FileNotFoundException fileNotFoundException) {
            debug(String.format("Could Not Find The File - %s", fileName));
            System.exit(-1);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            System.exit(-1);
        }
    }

    private void readName(Scanner fileRead) throws ParsingInputException {
        debug("Reading Section - Name");
        Pattern namePattern = Pattern.compile("[\\s]*[\\S]+[\\s]*");
        String temp = "";
        if (fileRead.hasNext(namePattern) & name == null) {
            temp = fileRead.next(namePattern);
            debug(String.format("Read Name - %s", temp));
            name = temp;
        } else {
            debug(String.format("Throwing Exception From Name - %s", temp));
            throw new ParsingInputException("Error while parsing input file");
        }
        debug(String.format("Assigned Name - %s", name));
    }

    /**
     * Add Stuff for checking partial assignment error,
     * Also Check the InvalidMachineTaskException
     * @param fileRead
     * @throws PartialAssignmentException
     * @throws InvalidMachineTaskException
     */
    private void readForcedPartialAssignments(Scanner fileRead) throws PartialAssignmentException, InvalidMachineTaskException {
        debug("Reading Section - Forced Partial Assignments");
        forcedPartialAssignments = new LinkedList<>();
        String temp;
        String[] temps;
        while (fileRead.hasNext() && !fileRead.hasNext(SECTION)) {
            temp = fileRead.next().trim();
            if (temp.matches("[(][1-8],[A-H][)]")) {
                temps = temp.substring(1,temp.length()-1).split(",");
                debug(String.format("Read Forced Partial Assignment - %s",temp));
                forcedPartialAssignments.add(new Pair<>(Integer.parseInt(temps[0]),temps[1]));
            } else if (!temp.isEmpty()) {
                debug(String.format("Throwing Invalid Machine/Task Exception From Forced Partial Assignments - %s",temp));
                throw new InvalidMachineTaskException("invalid machine/task");
            }
        }
        debug(String.format("Assigned Forced Partial Assignments - %s",forcedPartialAssignments));
    }

    private void readForbiddenMachines(Scanner fileRead) throws InvalidInputException {
        debug("Reading Section - Forbidden Machines");
        forbiddenMachines = new LinkedList<>();
        String temp;
        String[] temps;
        while (fileRead.hasNext() && !fileRead.hasNext(SECTION)) {
            temp = fileRead.next().trim();
            if (temp.matches("[(][1-8],[A-H][)]")) {
                temps = temp.substring(1,temp.length()-1).split(",");
                debug(String.format("Read Forbidden Machine - %s",temp));
                forbiddenMachines.add(new Pair<>(Integer.parseInt(temps[0]),temps[1]));
            } else if (!temp.isEmpty()) {
                debug(String.format("Throwing Exception From Forbidden Machines - %s",temp));
                throw new InvalidInputException("Some Error Happened");
            }
        }
        debug(String.format("Assigned Forbidden Machines - %s",forbiddenMachines));
    }

    private void readTooNearTasks(Scanner fileRead) throws InvalidInputException {
        debug("Reading Section - Too-Near Tasks");
        tooNearTasks = new LinkedList<>();
        String temp;
        String[] temps;
        while (fileRead.hasNext() && !fileRead.hasNext(SECTION)) {
            temp = fileRead.next().trim();
            if (temp.matches("[(][A-H],[A-H][)]")) {
                temps = temp.substring(1,temp.length()-1).split(",");
                debug(String.format("Read Too-Near Task - %s",temp));
                tooNearTasks.add(new Pair<>(temps[0],temps[1]));
            } else if (!temp.isEmpty()) {
                debug(String.format("Throwing Exception From Too-Near Tasks - %s",temp));
                throw new InvalidInputException("Some Error Happened");
            }
        }
        debug(String.format("Assigned Forbidden Machines - %s",tooNearTasks));
    }

    private void readMachinePenalties(Scanner fileRead) throws InvalidInputException {
        debug("Reading Section - Machine Penalties");
        machinePenalties = new LinkedList<>();
        String temp;
        String[] temps;
        Integer[] results;
        while (fileRead.hasNext() && !fileRead.hasNext(SECTION)) {
            temp = fileRead.next().trim();
            if (temp.matches("[\\d+]\\s[\\d+]\\s[\\d+]\\s[\\d+]\\s[\\d+]\\s[\\d+]\\s[\\d+]\\s[\\d+]")) {
                temps = temp.split("\\s+");
                results = new Integer[temps.length];
                for (int index = 0; index < temps.length; index++) results[index] = Integer.parseInt(temps[index]);
                debug(String.format("Read Machine Penalties - %s", temp));
                machinePenalties.add(new LinkedList<>(Arrays.asList(results)));
            } else if (!temp.isEmpty()) {
                debug(String.format("Throwing Exception From Machine Penalties - %s",temp));
                throw new InvalidInputException("Some Error Happened");
            }
        }
        debug(String.format("Assigned Machine Penalties - %s",machinePenalties));
    }

    private void readTooNearPenalties(Scanner fileRead) throws InvalidTaskException {
        debug("Reading Section - Too-Near Penalties");
        tooNearPenalties = new LinkedList<>();
        String temp;
        String[] temps;
        while (fileRead.hasNext() && !fileRead.hasNext(SECTION)) {
            temp = fileRead.next().trim();
            if (temp.matches("[(][A-H],[A-H],[\\d+][)]")) {
                temps = temp.substring(1,temp.length()-1).split(",");
                debug(String.format("Read Too-Near Penalties - %s",temp));
                tooNearPenalties.add(new Pair<>(new Pair<>(temps[0], temps[1]), Integer.parseInt(temps[2])));
            } else if (!temp.isEmpty()) {
                debug(String.format("Throwing Exception From Too-Near Penalties - %s",temp));
                throw new InvalidTaskException("Some Error Happened");
            }
        }
        debug(String.format("Assigned Too-Near Penalties - %s",tooNearPenalties));
    }

    private void checkValidity() {

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
