
//Package imports
import Structure.Pair;

// Library imports
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class BruteForce {
	
	// Constants
	private final String solFile = "solutionsList.txt";
	private final boolean debug;
	
	// Containers; hard/soft constraints will be filled in by the Parser class
	private LinkedList<Pair <Integer, String> > forcedPartialAssignment;
	private LinkedList<Pair <Integer, String> > forbiddenMachines;
	private LinkedList<Pair <Integer, String> > tooNearTasks;
	private LinkedList<Pair <Integer, String> > machinePenalties;
	private LinkedList<Pair <Pair <String, String>, Integer> > tooNearPenalties;
	
	// Finding all possible machine-task assignments; will store all possible solutions in a linked list of linked lists
	private LinkedList< Node <String, int, LinkedList<Pair<Integer, String >> > > solutionsList;
	
	// Constructor
	public BruteForce() {
		debug = false;
	}
	
	// Add each different solution into the solutions master list
	private int loadSolutions () {
		
		int count = 0;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(solFile))) {
			
			for (String line; (line = reader.readLine() ) != null; ) {
				
				// Add the solution into the master list
				solutionsList.add(new Node(line, 0, ))
				count++;
				
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("Input file not found.");
		}
		catch (IOException e) {
			System.out.println("Failed in opening BufferedReader.");
		}
		
		return count;
		
	}
	
	
	
	// Driver
	public static void main (String args[]) {
		
		BruteForce brute = new BruteForce();
		int count = brute.loadSolutions();
		System.out.println("Number of possibilities: " + count + ".");
		
	}
	
}  // End of Brute Force