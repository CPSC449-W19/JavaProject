package IO;

// library imports
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.File;
import java.util.regex.Pattern;

// package imports
import Structure.Pair;

/**
 * Parser object takes in a filename as a parameter and a debug state, to toggle debug messages.
 * Upon initialization of the constructor the parser will read the provided file or quit if the
 * file does not exist in the specified directory(src/InputFiles).  After the object is created the
 * Data from the file should be appropriately stored in the getter methods for external use.
 *
 * Note: Also see isException method to validate whether en exception was thrown by the parser from
 * a defined error.  Should only be possible for testing purposes (remove for final draft).
 *
 * @author Andrew Burton
 * @see Pair
 * @since January 2019
 */

public class Parser {

    // constants
    private final String fileName;
    private final boolean debug;
    private String location;
    private boolean exception = false;
    private String message = "";

    // shared regular expressions
    private final String SECTION = "[\\s]*(Name:|(forced[\\s]+partial[\\s]+assignment:)|(forbidden[\\s]+machine:)|(too-near[\\s]+tasks:)|(machine[\\s]+penalties:)|(too-near penalities))[\\s]*";

    // containers
    private String name;
    private LinkedList<Pair<Integer,String>> forcedPartialAssignments = new LinkedList<>();
    private LinkedList<Pair<Integer,String>> forbiddenMachines = new LinkedList<>();
    private LinkedList<Pair<String,String>> tooNearTasks = new LinkedList<>();
    private LinkedList<LinkedList<Integer>> machinePenalties = new LinkedList<>();
    private LinkedList<Pair<Pair<String,String>,Integer>> tooNearPenalties = new LinkedList<>();

    /**
     * Constructs lists of data read from a file
     * @param fileName name of the file to read from
     * @param debug the debug flag status determines whether or not to print debug messages
     * @author Andrew Burton
     * @since February 2019
     */
    public Parser(String fileName, boolean debug) {
        this.location = String.format("%s %s",getClass().getName(), "Parser");
        debug("ENTER");
        this.fileName = fileName;
        this.debug = debug;
        debug("this.fileName = " + this.fileName);
        initialize();
        this.location = String.format("%s %s",getClass().getName(), "Parser");
        debug("RETURN");
    }

    /**
     * reads the file, searching for section titles (labels), then passes to the appropriate
     * method in order to ensure that the correct regex is used for parsing.  checks specified
     * error messages while in this method, as well as file io exceptions and if a section is skipped
     * (label is missed).
     * @author Andrew Burton
     * @since February 2019
     */
    private void initialize() {
        this.location = String.format("%s %s",getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        debug("ENTER");
        Scanner fileRead;
        String lineRead;
        boolean[] visited = new boolean[6];
        try {

            fileRead = new Scanner(new File(getFileName())).useDelimiter("[\\r]?[\\n]");

            while (fileRead.hasNext()) {
                lineRead = fileRead.next().trim();
                debug("Reading - " + lineRead);
                switch (lineRead) {
                    case "Name:": name(fileRead); visited[0] = true; break;
                    case "forced partial assignment:": forcedPartialAssignments(fileRead); visited[1] = true; break;
                    case "forbidden machine:": forbiddenMachines(fileRead); visited[2] = true; break;
                    case "too-near tasks:": tooNearTasks(fileRead); visited[3] = true; break;
                    case "machine penalties:": machinePenalties(fileRead); visited[4] = true; break;
                    case "too-near penalities": tooNearPenalties(fileRead); visited[5] = true; break;
                    default: if (!lineRead.isEmpty()) throw new Exception("Error while parsing input file");
                }
                this.location = String.format("%s %s",getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
            }
            for (boolean visitor : visited) {
                if (!visitor) throw new Exception("Error while parsing input file");
            }
            fileRead.close();
        } catch (FileNotFoundException fileNotFoundException) {
            debug("File Not Found - " + fileName);
            System.exit(-1);
        } catch (Exception exception) {
            debug(exception.getMessage());
            this.message = exception.getMessage();
            this.exception = true;
        }
        debug("RETURN");
    }

    /**
     * Reads name section with the given regex specified in the assignment description.
     * String type is inferred so no type checking is enabled in this section reader.
     * if regex is violated (either no name in the section or name includes whitespace characters)
     * then an exception is thrown to display the appropriate error message.
     * @param fileRead scanner object parsed to the current line to read in the file
     * @throws Exception with the specified error message to display to user as exception.getMessage()
     * @author Andrew Burton
     * @since February 2019
     */
    private void name(Scanner fileRead) throws Exception {
        this.location = String.format("%s %s",getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        debug("ENTER");
        Pattern namePattern = Pattern.compile("[\\s]*[\\S]+[\\s]*");
        String temp = "";
        if (fileRead.hasNext(namePattern) & name == null) {
            temp = fileRead.next(namePattern);
            debug("Read Name: " + temp);
            name = temp;
        } else {
            debug("Throwing Exception From Name - " + temp);
            throw new Exception("Error while parsing input file");
        }
        debug("this.name = " + this.name);
        debug("RETURN");
    }

    /**
     * Reads forced partial assignments section with given regex patterns to validate
     * format and data type of the information pulled from the input file. loops through to
     * grab numbers in the format (mach, task) where mach is {1,2,3,4,5,6,7,8} and task is {A,B,C,D,E,F,G,H}.
     * Throws errors for invalid format, invalid type and duplicate entry.
     * @param fileRead scanner object parsed to the current line to read in the file
     * @throws Exception with the specified error message to display to user as exception.getMessage()
     * @author Andrew Burton
     * @since February 2019
     */
    private void forcedPartialAssignments(Scanner fileRead) throws Exception {
        this.location = String.format("%s %s",getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        debug("ENTER");
        String temp;
        String[] temps;
        while (fileRead.hasNext() && !fileRead.hasNext(SECTION)) {
            temp = fileRead.next().trim();
            if (temp.matches("[(]\\S+,\\S+[)]")) {
                temps = temp.substring(1,temp.length()-1).split(",");
                debug("Read Forced Partial Assignment: " + temp);
                if (!temps[0].matches("[1-8]") || !temps[1].matches("[A-H]")) {
                    debug("Throwing Exception From Forced Partial Assignments - " + temp);
                    throw new Exception("invalid machine/task");
                }
                for (Pair<Integer,String> entity : forcedPartialAssignments) {
                    if (entity.getX() == Integer.parseInt(temps[0]) || entity.getY().equals(temps[1])) {
                        debug("Throwing Exception From Forced Partial Assignments - " + temp);
                        throw new Exception("partial assignment error");
                    }
                }
                forcedPartialAssignments.add(new Pair<>(Integer.parseInt(temps[0]),temps[1]));
            } else if (!temp.isEmpty()) {
                debug("Throwing Exception From Forced Partial Assignments - " + temp);
                throw new Exception("Error while parsing input file");
            }
        }
        debug("this.forcedPartialAssignments = " + this.forcedPartialAssignments);
        debug("RETURN");
    }

    /**
     * Reads forced partial assignments section with given regex patterns to validate
     * format and data type of the information pulled from the input file. loops through to
     * grab numbers in the format (mach, task) where mach is {1,2,3,4,5,6,7,8} and task is {A,B,C,D,E,F,G,H}.
     * Throws errors for invalid format, invalid type and duplicate entry.
     * @param fileRead scanner object parsed to the current line to read in the file
     * @throws Exception with the specified error message to display to user as exception.getMessage()
     * @author Andrew Burton
     * @since February 2019
     */
    private void forbiddenMachines(Scanner fileRead) throws Exception {
        this.location = String.format("%s %s",getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        debug("ENTER");
        String temp;
        String[] temps;
        while (fileRead.hasNext() && !fileRead.hasNext(SECTION)) {
            temp = fileRead.next().trim();
            if (temp.matches("[(]\\S+,\\S+[)]")) {
                temps = temp.substring(1,temp.length()-1).split(",");
                debug("Read Forbidden Machine: " + temp);
                if (!temps[0].matches("[1-8]") || !temps[1].matches("[A-H]")) {
                    debug("Throwing Exception From Forbidden Machines - " + temp);
                    throw new Exception("invalid machine/task");
                }
                forbiddenMachines.add(new Pair<>(Integer.parseInt(temps[0]),temps[1]));
            } else if (!temp.isEmpty()) {
                debug("Throwing Exception From Forbidden Machines - " + temp);
                throw new Exception("Error while parsing input file");
            }
        }
        debug("this.forbiddenMachines = " + this.forbiddenMachines);
        debug("RETURN");
    }

    /**
     * Reads forced partial assignments section with given regex patterns to validate
     * format and data type of the information pulled from the input file. loops through to
     * grab numbers in the format (mach, task) where mach is {1,2,3,4,5,6,7,8} and task is {A,B,C,D,E,F,G,H}.
     * Throws errors for invalid format, invalid type and duplicate entry.
     * @param fileRead scanner object parsed to the current line to read in the file
     * @throws Exception with the specified error message to display to user as exception.getMessage()
     * @author Andrew Burton
     * @since February 2019
     */
    private void tooNearTasks(Scanner fileRead) throws Exception {
        this.location = String.format("%s %s",getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        debug("ENTER");
        String temp;
        String[] temps;
        while (fileRead.hasNext() && !fileRead.hasNext(SECTION)) {
            temp = fileRead.next().trim();
            if (temp.matches("[(]\\S+,\\S+[)]")) {
                temps = temp.substring(1,temp.length()-1).split(",");
                debug("Read Too-Near Task: " + temp);
                if (!temps[0].matches("[A-H]") || !temps[1].matches("[A-H]")) {
                    debug("Throwing Exception From Too-Near Tasks - " + temp);
                    throw new Exception("invalid machine/task");
                }
                tooNearTasks.add(new Pair<>(temps[0],temps[1]));
            } else if (!temp.isEmpty()) {
                debug("Throwing Exception From Too-Near Tasks - " + temp);
                throw new Exception("Error while parsing input file");
            }
        }
        debug("this.tooNearTasks = " + this.tooNearTasks);
        debug("RETURN");
    }

    /**
     * Reads forced partial assignments section with given regex patterns to validate
     * format and data type of the information pulled from the input file. loops through to
     * grab numbers in the format (mach, task) where mach is {1,2,3,4,5,6,7,8} and task is {A,B,C,D,E,F,G,H}.
     * Throws errors for invalid format, invalid type and duplicate entry.
     * @param fileRead scanner object parsed to the current line to read in the file
     * @throws Exception with the specified error message to display to user as exception.getMessage()
     * @author Andrew Burton
     * @since February 2019
     */
    private void machinePenalties(Scanner fileRead) throws Exception {
        this.location = String.format("%s %s",getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        debug("ENTER");
        String temp = "";
        String[] temps;
        Integer[] results;
        int counter = 0;
        while (fileRead.hasNext() && !fileRead.hasNext(SECTION)) {
            temp = fileRead.next().trim();
            if (temp.matches("([\\S+]([\\s]+)?)+")) {
                temps = temp.split("\\s+");
                results = new Integer[temps.length];
                if (temps.length != 8) {
                    debug("Throwing Exception From Machine Penalties - " + temp);
                    throw new Exception("machine penalty error");
                }
                for (int index = 0; index < temps.length; index++) {
                    if (!temps[index].matches("(0|[1-9][0-9]*)")) {
                        debug("Throwing Exception From Machine Penalties - " + temp);
                        throw new Exception("invalid penalty");
                    }
                    results[index] = Integer.parseInt(temps[index]);
                }
                debug("Read Machine Penalties - " + temp);
                machinePenalties.add(new LinkedList<>(Arrays.asList(results)));
                counter++;
            } else if (!temp.isEmpty()) {
                debug("Throwing Exception From Machine Penalties - " + temp);
                throw new Exception("Error while parsing input file");
            }
        }
        if (counter != 8) {
            debug("Throwing Exception From Machine Penalties - " + temp);
            throw new Exception("machine penalty error");
        }
        debug("this.machinePenalties = " + this.machinePenalties);
        debug("RETURN");
    }

    /**
     * Reads forced partial assignments section with given regex patterns to validate
     * format and data type of the information pulled from the input file. loops through to
     * grab numbers in the format (mach, task) where mach is {1,2,3,4,5,6,7,8} and task is {A,B,C,D,E,F,G,H}.
     * Throws errors for invalid format, invalid type and duplicate entry.
     * @param fileRead scanner object parsed to the current line to read in the file
     * @throws Exception with the specified error message to display to user as exception.getMessage()
     * @author Andrew Burton
     * @since February 2019
     */
    private void tooNearPenalties(Scanner fileRead) throws Exception {
        this.location = String.format("%s %s",getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        debug("ENTER");
        String temp;
        String[] temps;
        while (fileRead.hasNext() && !fileRead.hasNext(SECTION)) {
            temp = fileRead.next().trim();
            if (temp.matches("[(][\\S+],[\\S+],[\\S+][)]")) {
                temps = temp.substring(1,temp.length()-1).split(",");
                if (!temps[2].matches("(0|[1-9][0-9]*)")) throw new Exception("invalid penalty");
                if (!temps[0].matches("[A-H]") || !temps[1].matches("[A-H]")) throw new Exception("invalid task");
                debug("Read Too-Near Penalties: " + temp);
                boolean isAssigned = false;
                int index = 0;
                while (index < tooNearPenalties.size()) {
                    if (tooNearPenalties.get(index).getX().getX().equals(temps[0]) && tooNearPenalties.get(index).getX().getY().equals(temps[1])) {
                        isAssigned = true;
                        break;
                    }
                    index++;
                }
                if (isAssigned) {
                    tooNearPenalties.get(index).setY(Integer.parseInt(temps[2]));
                } else {
                    tooNearPenalties.add(new Pair<>(new Pair<>(temps[0], temps[1]), Integer.parseInt(temps[2])));
                }
            } else if (!temp.isEmpty()) {
                debug("Throwing Exception From Too-Near Penalties - " + temp);
                throw new Exception("Error while parsing input file");
            }
        }
        debug("this.tooNearPenalties = " + this.tooNearPenalties);
        debug("RETURN");
    }

    /**
     * Prints yy/MM/dd HH:mm:ss [DEBUG] {executing class} {executing method} {debug message}
     * when debug mode is enabled, set by the boolean flag in the constructor
     * pass in false in constructor to disable all debug statements (print statements are slow this will impact performance).
     * Print messages are done with system.err so all debug messages will be in red, as iff from an error message.
     * @param message debug message to print when debug mode is enabled
     * @author Andrew Burton
     * @since February 2019
     */
    private void debug(String message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss");
        if (isDebug()) System.err.println(String.format("%s [DEBUG] %s - %s",LocalDateTime.now().format(formatter),location,message));
    }

    // below are getters for the program privacy leaks are all handled here so does not need to be handled externally

    public boolean isException() {
        return this.exception;
    }

    private String getFileName() {
        return this.fileName;
    }

    private boolean isDebug() {
        return this.debug;
    }

    public String getMessage() {
        return this.message;
    }

    public String getName() {
        return this.name;
    }

    public LinkedList<Pair<Integer, String>> getForcedPartialAssignments() {
        return new LinkedList<>(this.forcedPartialAssignments);
    }

    public LinkedList<Pair<Integer, String>> getForbiddenMachines() {
        return new LinkedList<>(this.forbiddenMachines);
    }

    public LinkedList<Pair<String, String>> getTooNearTasks() {
        return new LinkedList<>(this.tooNearTasks);
    }

    public LinkedList<LinkedList<Integer>> getMachinePenalties() {
        return new LinkedList<>(this.machinePenalties);
    }

    public LinkedList<Pair<Pair<String, String>, Integer>> getTooNearPenalties() {
        return new LinkedList<>(this.tooNearPenalties);
    }
}
