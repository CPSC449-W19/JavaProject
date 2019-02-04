package generator;
import java.io.FileWriter;
import java.util.LinkedList;
import Structure.Pair;
import java.util.Random;

public class InputGenerator {
	
	String option;
	static boolean debug = true;
	private Integer range;
	private String name = null;
	private String fileOut = null;
	private enum tasks {
		A, B, C, D, E, F, G, H
	}
	
	private LinkedList <Pair<Integer,String>> forcedMachines;
	private LinkedList <Pair<Integer,String>> forbiddenMachines;
	private LinkedList <Pair<Integer,String>> tooNearTasks;
	private Integer[][] penaltyMatrix = null;

	public InputGenerator(boolean debug, String option, Integer range) {
		this.setDebug(debug);
		this.setOption(option);
		this.setRange(range);
	}
	
	public void generate() {
		switch (this.getOption()) {
		case "--option": // default for figuring out how to write out
			this.setName("default");
			this.validForcedMachines();
			this.validForbiddenMachines();
			this.validPentaltyMatrix();
			this.validTooNear();
			this.fileOut();
		case "--oor": // out of range, once for machine & once for task or not enough tasks/machines
		case "--ffc": // forbidden forced machine conflict, 
		case "--tnc": // too near conflict
		case "--tmm": // type mismatch
		case "--dup": // duplicate machine/task
		}
		fileOut.concat("Name:\n "+name+"\n");
	}
	
	
	private void validTooNear() {
		// TODO Auto-generated method stub
		
	}

	private void fileOut() {
		try {
		FileWriter printer = new FileWriter(System.getProperty("user.dir")+this.name+".txt");
		printer.write("");
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
		Random RNG = new Random();
		for (int i = 0; i < 8; i ++) {
			for (int j = 0; j < 8; j++) {
				this.penaltyMatrix[i][j] = RNG.nextInt(this.range);
			}
		}
	}
	
	private tasks getRandomTask() {
		Random generator = new Random();
		return tasks.values()[generator.nextInt(tasks.values().length)];
	}
}