package generator;
import Structure.Pair;
import Structure.Triple;
import java.util.LinkedList;
import java.util.Random;
import java.io.FileWriter;

/**
 * @author Dylan
 * @since January 31st, 2019
 */

public class InputGenerator {
	
	String option;
	static boolean debug = true;
	private Integer range;
	private String name;
	private String fileOut;
	private enum tasks {
		A, B, C, D, E, F, G, H
	}
	
	private LinkedList <Pair<Integer,String>> forcedMachines;
	private LinkedList <Pair<Integer,String>> forbiddenMachines;
	private LinkedList <Pair<String ,String>> tooNearContraints;
	private LinkedList <Triple<String, String, Integer>> tooNearPenalties;
	private Integer[][] penaltyMatrix = null;

	/**
	 * Constructor for InputGenerator Class, set's values as passed from string args[] when called from command line
	 * @param debug: if set to "--true", program will print the execution of the program in the console, otherwise any string will do
	 * @param option: depending on use case, this flag will generate a file that throws the selected exception, if not set to "--option", which generates a valid test file 
	 * @param range: for the penalty matrix, this value will bound all possible values in the penalty matrix.
	 */
	public InputGenerator(boolean debug, String option, Integer range) {
		this.setDebug(debug);
		this.setOption(option);
		this.setRange(range);
	}
	
	/**
	 * depending on the selected --option flag, this will call the appropriate private methods to initialize the values to be printed to the file.
	 */
	public void generate() {
		switch (this.getOption()) {
		case "--option": // default option for generating valid files
			this.setName("default");
			this.validForcedMachines();
			this.validForbiddenMachines();
			this.validPentaltyMatrix();
			this.validTooNearConstraints();
			this.validTooNearPenalties();
			this.fileOut();
		case "--oor": // out of range, once for machine & once for task or not enough tasks/machines
		case "--ffc": // forbidden forced machine conflict, 
		case "--tnc": // too near conflict
		case "--tmm": // type mismatch
		case "--dup": // duplicate machine/task
		}
	}
		
	private void fileOut() {
		try {
		FileWriter printer = new FileWriter(System.getProperty("user.dir")+this.name+".txt");
		
		this.fileOut.concat("name: \r"+this.name+"\r"); // or /r? check default file for control characters
		this.fileOut.concat("");
		for (Pair<Integer,String> p : this.forcedMachines) {
			this.fileOut.concat("");	
		}		
		printer.write(this.fileOut);
		printer.close();
		} catch (Exception e) {
			System.out.println("Unable to write file");
		}
	}

	private void setOption(String op) {
		this.option = op.toString();
	}
	
	private String getOption() {
		String toReturn = new String(this.option);
		return toReturn;
	}

	private void setDebug(Boolean debug) {
		InputGenerator.debug = Boolean.valueOf(debug);
	}
	
	private boolean debugOn() {
		return Boolean.valueOf(InputGenerator.debug);
	}

	private Integer getRange() {
		return range;
	}

	private void setRange(Integer range) {
		this.range = range;
	}
	
	private String getName() {
		String name = new String(this.name);
		return name; 
	}
	
	private void setName(String name) {
		this.name = name.toString();
	}
	
	private void validForcedMachines() {
		Random RNG = new Random();		
		Integer numOfMachines = RNG.nextInt(9);
		while (this.forcedMachines.size() != numOfMachines) {
			Boolean validPair = false;
			while(!validPair) {
				Boolean validMachine = true;
				Integer machine = RNG.nextInt(8) + 1;
				for (Pair<Integer,String> pair : this.forcedMachines) {
					if (machine == pair.getX()) {
						validMachine = false;
						break;
					}
				}
				Boolean validTask = true;
				String task = this.getRandomTask().toString();
				for (Pair<Integer,String> pair : this.forcedMachines) {
					if (task == pair.getY()) {
						validTask = false;
						break;
					}
				}
				validPair = validMachine && validTask;
				if (validPair) forcedMachines.add(new Pair<Integer,String> (machine,task));
			}
		}
	}
	
	private void validForbiddenMachines() {
		Random RNG = new Random();
		Integer numOfPairs = RNG.nextInt(9);
		while (this.forbiddenMachines.size() != numOfPairs) {
			boolean validPair = false;
			while(!validPair) {
				Boolean validMachine = true;
				Integer machine = RNG.nextInt(8) + 1;
				for (Pair<Integer,String> pair : this.forbiddenMachines) {
					if (machine == pair.getX()) {
						validMachine = false;
						break;
					}
				}
				Boolean validTask = true;
				String task = this.getRandomTask().toString();
				for (Pair<Integer,String> pair : this.forbiddenMachines) {
					if (task == pair.getY()) {
						validTask = false;
						break;
					}
				}
				Pair<Integer,String> toAdd = new Pair<Integer,String>(machine,task);
 				Boolean ffConflict = false;
				for (Pair<Integer,String> pair : this.forcedMachines) {
					if (pair == toAdd) ffConflict = true;
					validPair = false;
				}
				if (validPair) forbiddenMachines.add(toAdd);
 			}
		}
		
	}
	
	private void validPentaltyMatrix() {
		// TODO find a way to create a normal distribution of values around a given range's mean for the matrix.
		Random RNG = new Random();
		for (int i = 0; i < 8; i ++) {
			for (int j = 0; j < 8; j++) {
				this.penaltyMatrix[i][j] = RNG.nextInt(this.range);
			}
		}
	}
	
	private LinkedList<Pair<String,String>> validTooNearConstraints() {
		LinkedList<Pair<String,String>> avoid = new LinkedList<Pair<String,String>>();
		
		// check each possible pair of forced machines for pairs of too near tasks
		for (Pair<Integer, String> machine : this.forcedMachines) {
			for (Pair<Integer,String> ndMachine : this.forcedMachines) {
				if (machine != ndMachine) { // if two forced machines aren't equal
					if (ndMachine.getX() == (machine.getX()+1)) { // if two forced machines have too near tasks
						avoid.add(new Pair<String,String>(machine.getY(),ndMachine.getY())); // add the pair of tasks to the avoid list
					} else if ((machine.getX() == 8)&&(ndMachine.getX() == 0)) {
						avoid.add(new Pair<String,String>(machine.getY(),ndMachine.getY())); // if two forced machines 
					}
				}
			}
		}
		
		//set values in instance variable
		
		Random RNG = new Random();
		Integer numOfPairs = RNG.nextInt(5);
		while (this.tooNearContraints.size() != numOfPairs) {
			Boolean validPair = false;
			while (!validPair) {
				tasks a = this.getRandomTask();
				tasks b = this.getRandomTask();
				
				if (a != b) {
					validPair = true;
					for (Pair<String,String> inAvoid : avoid) {
						if (inAvoid == new Pair<String,String>(a.toString(),b.toString())) {
							validPair = false;
						}
					}
				}
				if (validPair) this.tooNearContraints.add(new Pair<String,String>(a.toString(),b.toString()));
			}
		}
		return avoid;
	}
	
	private void validTooNearPenalties() {
		Random RNG = new Random();
		Integer numOfTrips = RNG.nextInt(9)+1;
		for (int i = 0; i < numOfTrips; i++ ) {
			Boolean validTrip = false;
			while(!validTrip) {
				tasks a = this.getRandomTask();
				tasks b = this.getRandomTask();
				if (a != b) {
					validTrip = true;
					this.tooNearPenalties.add(new Triple<String,String,Integer>(a.toString(),b.toString(),RNG.nextInt(this.range)));
				}
			}
		}
		
	}
	
	private tasks getRandomTask() {
		Random generator = new Random();
		return tasks.values()[generator.nextInt(tasks.values().length)];
	}
}