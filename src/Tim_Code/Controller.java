/**
 * Timothy Brar (10131521), Nicole Ewert (10154195), Choeney Nardechen (00508866),
 * Nathan Meulenbroek (30002822) & Nathanael Carrigan (10057613)
 * CPSC449-Tut3
 *
 * Controller class file; handles file i/o, and drives the program.
 * When running, requires 2 text files as command lines arguments;
 * one for input to read from and one file to write to.
 *
 */
package Tim_Code;

import java.io.*;
import java.util.ArrayList;
import java.awt.Desktop;
//import java.io.File;

import IO.*;
import Structure.*;

public class Controller {

	static String fileInName;
	static String fileOutName;
	static String ourFileInName;
	static String ourFileOutName;

	static Constraints c;
	private static boolean isDebugging = false;

	/**
	 * Useful debugging method while developing to print out desired errors
	 *
	 * @param msg - The error message you want displayed
	 */
	public static void debug(String msg) {
		// check if debugging flag is set
		if (isDebugging)
			System.out.println("[DEBUGGING]:\t" + msg);
	}

	public static void main(String[] args) throws IOException {
		if (args.length == 2) {
			fileInName = args[0];
			fileOutName = args[1];

			ourFileInName = args[0];
			//ourFileOutName = args[2];
			readFile();
			Tree t = new Tree(c);
			t.traverseTree();
			writeFile(null, t.printSolution());

			File file = new File(fileOutName);

	        //first check if Desktop is supported by Platform or not
	        if(!Desktop.isDesktopSupported()){
	            System.out.println("Desktop is not supported");
	            return;
	        }

	        Desktop desktop = Desktop.getDesktop();
	        if(file.exists()) desktop.open(file);

			Parser parser;
			BranchAndBound branchAndBound;

			parser = new Parser(ourFileInName,false);
			branchAndBound = new BranchAndBound(parser, false);
			branchAndBound.findSolution();

		} else
			System.out.println("Missing command line arguments for input and output file");
	}

	/**
	 * Opens, reads and parses from the input file(selected by user). Will parse through
	 * line by line and if the name, and 5 constraints are valid; the input data will be
	 * saved to a constraints object. Otherwise, an appropriate error message will be
	 * written to the output file (also selected by user).
	 */
	public static void readFile() {
		String line = "";
		String name;

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileInName))) {
			// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// | Read name |
			// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			while (line.equals(""))
				line = bufferedReader.readLine().replaceAll("\\s*$", "");
			checkKeyWord(bufferedReader, line, "name:");
			debug("read label:" + line);
			line = "";
			while (line.equals(""))
				line = bufferedReader.readLine().replaceAll("\\s*$", "");
			name = line;
			line = bufferedReader.readLine().replaceAll("\\s*$", "");
			if (!line.equals(""))
				writeFile(bufferedReader, "Error while parsing input file");
			while (line.equals(""))
				line = bufferedReader.readLine().replaceAll("\\s*$", "");
			debug("name read in->" + name);

			// ~~~~~~~~~~~~ HARD CONSTRAINTS ~~~~~~~~~~~~~~~
			// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// | Read forced partial assignment constraint |
			// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			checkKeyWord(bufferedReader, line, "forced partial assignment:");
			debug("read label:" + line);
			ArrayList<Character[]> forcedPartAssn = new ArrayList<Character[]>();
			boolean emptyLine = false;
			line = bufferedReader.readLine().replaceAll("\\s*$", "");
			while (line.equals("") || line.matches("\\(.*,.*\\)")) {
				if (line.equals(""))
					emptyLine = true;
				else {
					emptyLine = false;
					String[] token = line.substring(1, line.length() - 1).split(",");
					if ((token.length < 2) || !(isMach(token[0]) && isTask(token[1])))
						writeFile(bufferedReader, "invalid machine/task");
					Character[] forcedLine = new Character[2];
					forcedLine[0] = token[0].charAt(0);
					forcedLine[1] = token[1].charAt(0);
					forcedPartAssn.add(forcedLine);
				}
				line = bufferedReader.readLine().replaceAll("\\s*$", "");
			}
			checkForcedPartialConstraint(bufferedReader, forcedPartAssn);
			if (!emptyLine)
				writeFile(bufferedReader, "Error while parsing input file");

			debug("forced done");
			if (isDebugging)
				printArrayListChar(forcedPartAssn);

			// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// | Read forbidden machine constraint |
			// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			checkKeyWord(bufferedReader, line, "forbidden machine:");
			debug("read label:" + line);
			ArrayList<Character[]> forbiddenMach = new ArrayList<Character[]>();
			emptyLine = false;
			line = bufferedReader.readLine().replaceAll("\\s*$", "");
			while (line.equals("") || line.matches("\\(.*,.*\\)")) {
				if (line.equals(""))
					emptyLine = true;
				else {
					emptyLine = false;
					String[] token = line.substring(1, line.length() - 1).split(",");
					if ((token.length < 2) || !(isMach(token[0]) && isTask(token[1])))
						writeFile(bufferedReader, "invalid machine/task");
					Character[] forbLine = new Character[2];
					forbLine[0] = token[0].charAt(0);
					forbLine[1] = token[1].charAt(0);
					forbiddenMach.add(forbLine);
				}
				line = bufferedReader.readLine().replaceAll("\\s*$", "");
			}
			if (!emptyLine)
				writeFile(bufferedReader, "Error while parsing input file");

			debug("forbidden done");
			if (isDebugging)
				printArrayListChar(forbiddenMach);

			// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// | Read too-near tasks constraint |
			// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			checkKeyWord(bufferedReader, line, "too-near tasks:");
			debug("read label:" + line);
			ArrayList<Character[]> tooNearHard = new ArrayList<Character[]>();
			emptyLine = false;
			line = bufferedReader.readLine().replaceAll("\\s*$", "");
			while (line.equals("") || line.matches("\\(.*,.*\\)")) {
				if (line.equals(""))
					emptyLine = true;
				else {
					emptyLine = false;
					String[] token = line.substring(1, line.length() - 1).split(",");
					if ((token.length < 2) || !(isTask(token[0]) && isTask(token[1])))
						writeFile(bufferedReader, "invalid machine/task");
					Character[] tooNearHardLine = new Character[2];
					tooNearHardLine[0] = token[0].charAt(0);
					tooNearHardLine[1] = token[1].charAt(0);
					tooNearHard.add(tooNearHardLine);
				}
				line = bufferedReader.readLine().replaceAll("\\s*$", "");
			}
			if (!emptyLine)
				writeFile(bufferedReader, "Error while parsing input file");

			debug("too-near done");
			if (isDebugging)
				printArrayListChar(tooNearHard);

			// ~~~~~~~~~~~~ SOFT CONSTRAINTS ~~~~~~~~~~~~~~~

			// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// | Read machine penalties |
			// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			checkKeyWord(bufferedReader, line, "machine penalties:");
			debug("read label:" + line);
			int[][] machPenalties = new int[8][8];
			emptyLine = false;
			line = bufferedReader.readLine().replaceAll("\\s*$", "");
			int j = 0;
			while ((line.equals("") || j < 9) && !line.equalsIgnoreCase("too-near penalities")) {
				if (line.equals(""))
					emptyLine = true;
				else {
					emptyLine = false;
					String[] lineStringArr = line.split("\\s+");
					// Check that line has correct number of entries
					if (j < 8 && lineStringArr.length < 8)
						writeFile(bufferedReader, "machine penalty error");
					if (j < 8 && lineStringArr.length > 8)
						writeFile(bufferedReader, "Error while parsing input file");
					lineStringArr = line.split(" ");
					// Try casting each entry in the line to an integer and put
					// it in machPenalties array
					//boolean isSpace = false;
					for (int i = 0; i < lineStringArr.length; i++) {
						try {
							if (lineStringArr[i].length() == 0)
								writeFile(bufferedReader, "Error while parsing input file");
							int penalty = Integer.parseInt(lineStringArr[i]);
							machPenalties[j][i] = penalty;
							// Must be natural number
							if (machPenalties[j][i] < 0)
								writeFile(bufferedReader, "invalid penalty");
						}
						// If something other than an integer is found, print
						// error and exit
						catch (NumberFormatException e) {
							if (j < 8)
								writeFile(bufferedReader, "invalid penalty");
							writeFile(bufferedReader, "Error while parsing input file");
						} catch (ArrayIndexOutOfBoundsException ex) {
							writeFile(bufferedReader, "machine penalty error");
						}
					}
					j++;
				}
				line = bufferedReader.readLine().replaceAll("\\s*$", "");
			}
			if (j < 8)
				writeFile(bufferedReader, "machine penalty error");

			if (!emptyLine)
				writeFile(bufferedReader, "Error while parsing input file");

			debug("mach penalties done");
			if (isDebugging)
				printArrayEight(machPenalties);

			// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// | Read too-near penalties |
			// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			checkKeyWord(bufferedReader, line, "too-near penalities");
			debug("read label:" + line);

			ArrayList<Tuple> tooNearSoft = new ArrayList<Tuple>();
			line = bufferedReader.readLine();
			while (line != null) {
				line = line.replaceAll("\\s*$", "");
				if (!line.equals("")) {
					debug(line);
					//to be valid, must be in the format ( , , )
					if (!line.matches("\\(.*,.*,.*\\)"))
						writeFile(bufferedReader, "Error while parsing input file");

					String[] token = line.substring(1, line.length() - 1).split(",");
					if (token.length<2 || !(isTask(token[0]) && isTask(token[1])))
						writeFile(bufferedReader, "invalid task");
					// Read penalty
					String nearPen = line.substring(5, line.length() - 1);
					// Check that nearPen is a natural number (including 0)
					int nearPenInt = 0;
					try {
						nearPenInt = Integer.parseInt(nearPen);
						if (nearPenInt < 0)
							writeFile(bufferedReader, "invalid penalty");
					} catch (NumberFormatException e) {
						writeFile(bufferedReader, "invalid penalty");
					}
					tooNearSoft.add(new Tuple(token[0].charAt(0), token[1].charAt(0), nearPenInt));
				}
				line = bufferedReader.readLine();
			}

			debug("too-near penalties done");
			if (isDebugging)
				tooNearSoft.toString();

			// initialize constraint class with read in values
			c = new Constraints(name, forcedPartAssn, forbiddenMach, tooNearHard, machPenalties, tooNearSoft);

		} catch (NullPointerException ex) {
			debug("Null pointer exception.");
			writeFile(null, "Error while parsing input file");
		} catch (FileNotFoundException ex) {
			debug("File not found.");
			writeFile(null, "Error while parsing input file");
		} catch (IOException ex) {
			debug("IO exception.");
			writeFile(null, "Error while parsing input file");
		}
	}

	/**
	 *  Opens and writes to file(fileName provided by user) the string argument
	 *  passed in and exits the program.
	 *
	 * @param br - the bufferedReader to close (that input was reading from)
	 * @param msg - String to output to file (either a error message or solution)
	 */
	public static void writeFile(BufferedReader br, String msg) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileOutName));
			if (br != null)
				br.close();
			bw.write(msg);
			debug("wrote to file-> " + msg);
			bw.close();
			//System.exit(0);
		} catch (IOException e) {
			debug("ERROR couldnt write to File...");
		}
	}

	/**
	 * Checks if the 2 keywords provided, match. If they do not match, writeFile
	 * is called, where the appropriate error message will be output.
	 *
	 * @param br - the bufferedReader to close(that input was reading from)
	 * @param kwActual - keyword that was read in
	 * @param kwDesired - keyword that was desired
	 */
	private static void checkKeyWord(BufferedReader br, String kwActual, String kwDesired) {
		if (!kwActual.equalsIgnoreCase(kwDesired))
			writeFile(br, "Error while parsing input file");
	}


	/**
	 * Checks whether the machine is a valid machine name (between 1-8).
	 * @param machName - the machine to check
	 * @return - value of comparison; returns true if between 1-8; or-else false.
	 */
	private static boolean isMach(String machName) {
		if (machName.length() == 1)
			return (machName.charAt(0) >= '1' && machName.charAt(0) <= '8');
		return false;
	}

	/**
	 * Checks whether the task is a valid task name (between A-H).
	 * @param taskName - the task to check
	 * @return - value of comparison; returns true if between A-H; or-else false.
	 */
	private static boolean isTask(String taskName) {
		if (taskName.length() == 1)
			return (taskName.charAt(0) >= 'A' && taskName.charAt(0) <= 'H');
		return false;
	}

	/**
	 * Checks if any of the the forced partial assignments are repeated.
	 * If any are, writeFile is called, outputting the appropriate error message
	 * and exiting the program.
	 *
	 * @param br - the bufferedReader to close(that input was reading from)
	 * @param forcedPartAssn - the FPA array to check
	 */
	// check for repeated machines or tasks
	private static void checkForcedPartialConstraint(BufferedReader br, ArrayList<Character[]> forcedPartAssn) {
		for (int r = 0; r < forcedPartAssn.size(); r++) {
			if (forcedPartAssn.get(r)[0] == 0)
				break;
			// compare the task in row r to each task below it (c), and
			// compare the machine in row r to each machine below it to
			// ensure that there are no repeated tasks or machines
			for (int c = r + 1; c < forcedPartAssn.size(); c++) {
				if (forcedPartAssn.get(r)[1].equals(forcedPartAssn.get(c)[1])
						|| forcedPartAssn.get(r)[0].equals(forcedPartAssn.get(c)[0]))
					writeFile(br, "partial assignment error");
			}
		}
	}

	/**
	 * Helper method to print each element in the array list.
	 * @param array - array to be output
	 */
	private static void printArrayListChar(ArrayList<Character[]> array) {
		System.out.print("---\n");
		for (int i = 0; i < array.size(); i++) {
			Character[] alLine = array.get(i);
			System.out.println(alLine[0] + " " + alLine[1]);
		}
	}

	/**
	 * Helper method to print each element in the int array.
	 * @param array - array to be output
	 */
	private static void printArrayEight(int[][] array) {
		System.out.print("---\n");
		for (int j = 0; j < 8; j++) {
			for (int i = 0; i < 8; i++) {
				System.out.print(array[j][i] + " ");
			}
			System.out.print("\n");
		}
	}
}
