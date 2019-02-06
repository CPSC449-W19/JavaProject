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
 * Parser does some stuff...
 *
 * @author Andrew Burton
 * @see Pair
 * @since January 21, 2019
 */

public class Parser {

    // constants
    private final String fileName;
    private final boolean debug;
    private String location;

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
        this.location = String.format("%s %s",getClass().getName(), "Parser");
        this.fileName = fileName;
        this.debug = true;
        debug("Debugging Parser");
        debug(String.format("File Name Attribute Assigned As - %s", this.fileName));
        debug(String.format("Initializing Parser To Read - %s", fileName));
        initialize();
    }

    private void initialize() {
        this.location = String.format("%s %s",getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        Scanner fileRead;
        String lineRead;

        try {

            fileRead = new Scanner(new File(getFileName())).useDelimiter("\\n");

            while (fileRead.hasNext()) {
                lineRead = fileRead.next().trim();
                debug(String.format("Current Line In File %s", lineRead));
                switch (lineRead) {
                    case "Name:": name(fileRead); break;
                    case "forced partial assignment:": forcedPartialAssignments(fileRead); break;
                    case "forbidden machine:": forbiddenMachines(fileRead); break;
                    case "too-near tasks:": tooNearTasks(fileRead); break;
                    case "machine penalties:": machinePenalties(fileRead); break;
                    case "too-near penalities": tooNearPenalties(fileRead); break;
                    default: if (!lineRead.isEmpty()) throw new Exception("Error while parsing input file");
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

    private void name(Scanner fileRead) throws Exception {
        this.location = String.format("%s %s",getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        debug("ENTER");
        Pattern namePattern = Pattern.compile("[\\s]*[\\S]+[\\s]*");
        String temp = "";
        if (fileRead.hasNext(namePattern) & name == null) {
            temp = fileRead.next(namePattern);
            debug(String.format("Read Name: %s", temp));
            name = temp;
        } else {
            debug(String.format("Throwing Exception From Name: %s", temp));
            throw new Exception("Error while parsing input file");
        }
        debug(String.format("Assigned Name - %s", name));
        debug("RETURN");
    }

    /**
     * Add Stuff for checking partial assignment error,
     * Also Check the InvalidMachineTaskException
     * @param fileRead
     * @throws Exception
     */
    private void forcedPartialAssignments(Scanner fileRead) throws Exception {
        this.location = String.format("%s %s",getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        debug("Reading Section - Forced Partial Assignments");
        forcedPartialAssignments = new LinkedList<>();
        String temp;
        String[] temps;
        while (fileRead.hasNext() && !fileRead.hasNext(SECTION)) {
            temp = fileRead.next().trim();
            if (temp.matches("[(]\\S+,\\S+[)]")) {
                temps = temp.substring(1,temp.length()-1).split(",");
                debug(String.format("Read Forced Partial Assignment - %s",temp));
                if (!temps[0].matches("[1-8]") || !temps[1].matches("[A-H]"))
                    throw new Exception("invalid machine/task");
                for (Pair<Integer,String> entity : forcedPartialAssignments)
                    if (entity.getX() == Integer.parseInt(temps[0]) || entity.getY().equals(temps[1]))
                        throw new Exception("partial assignment error");
                forcedPartialAssignments.add(new Pair<>(Integer.parseInt(temps[0]),temps[1]));
            } else if (!temp.isEmpty()) {
                debug(String.format("Throwing Invalid Machine/Task Exception From Forced Partial Assignments - %s",temp));
                throw new Exception("Error while parsing input file");
            }
        }
        debug(String.format("Assigned Forced Partial Assignments - %s",forcedPartialAssignments));
    }

    private void forbiddenMachines(Scanner fileRead) throws Exception {
        this.location = String.format("%s %s",getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        debug("Reading Section - Forbidden Machines");
        forbiddenMachines = new LinkedList<>();
        String temp;
        String[] temps;
        while (fileRead.hasNext() && !fileRead.hasNext(SECTION)) {
            temp = fileRead.next().trim();
            if (temp.matches("[(]\\S+,\\S+[)]")) {
                temps = temp.substring(1,temp.length()-1).split(",");
                debug(String.format("Read Forbidden Machine - %s",temp));
                if (!temps[0].matches("[1-8]") || !temps[1].matches("[A-H]"))
                    throw new Exception("invalid machine/task");
                forbiddenMachines.add(new Pair<>(Integer.parseInt(temps[0]),temps[1]));
            } else if (!temp.isEmpty()) {
                debug(String.format("Throwing Exception From Forbidden Machines - %s",temp));
                throw new Exception("Error while parsing input file");
            }
        }
        debug(String.format("Assigned Forbidden Machines - %s",forbiddenMachines));
    }

    private void tooNearTasks(Scanner fileRead) throws Exception {
        this.location = String.format("%s %s",getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        debug("Reading Section - Too-Near Tasks");
        tooNearTasks = new LinkedList<>();
        String temp;
        String[] temps;
        while (fileRead.hasNext() && !fileRead.hasNext(SECTION)) {
            temp = fileRead.next().trim();
            if (temp.matches("[(]\\S+,\\S+[)]")) {
                temps = temp.substring(1,temp.length()-1).split(",");
                debug(String.format("Read Too-Near Task - %s",temp));
                if (!temps[0].matches("[A-H]") || !temps[1].matches("[A-H]"))
                    throw new Exception("invalid machine/task");
                tooNearTasks.add(new Pair<>(temps[0],temps[1]));
            } else if (!temp.isEmpty()) {
                debug(String.format("Throwing Exception From Too-Near Tasks - %s",temp));
                throw new Exception("Error while parsing input file");
            }
        }
        debug(String.format("Assigned Forbidden Machines - %s",tooNearTasks));
    }

    private void machinePenalties(Scanner fileRead) throws Exception {
        this.location = String.format("%s %s",getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        debug("Reading Section - Machine Penalties");
        machinePenalties = new LinkedList<>();
        String temp;
        String[] temps;
        Integer[] results;
        int counter = 0;
        while (fileRead.hasNext() && !fileRead.hasNext(SECTION)) {
            temp = fileRead.next().trim();
            if (temp.matches("([\\S+]([\\s]+)?)+")) {
                temps = temp.split("\\s+");
                results = new Integer[temps.length];
                if (temps.length != 8) throw new Exception("machine penalty error");
                for (int index = 0; index < temps.length; index++) {
                    if (!temps[index].matches("(0|[1-9][0-9]*)")) throw new Exception("invalid penalty");
                    results[index] = Integer.parseInt(temps[index]);
                }
                debug(String.format("Read Machine Penalties - %s", temp));
                machinePenalties.add(new LinkedList<>(Arrays.asList(results)));
                counter++;
            } else if (!temp.isEmpty()) {
                debug(String.format("Throwing Exception From Machine Penalties - %s",temp));
                throw new Exception("Error while parsing input file");
            }
        }
        if (counter != 8) throw new Exception("machine penalty error");
        debug(String.format("Assigned Machine Penalties - %s",machinePenalties));
    }

    private void tooNearPenalties(Scanner fileRead) throws Exception {
        this.location = String.format("%s %s",getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
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
                throw new Exception("Error while parsing input file");
            }
        }
        debug(String.format("Assigned Too-Near Penalties - %s",tooNearPenalties));
    }

    private void debug(String message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss");
        if (isDebug()) System.err.println(String.format("%s [DEBUG] %s - %s",LocalDateTime.now().format(formatter),location,message));
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
