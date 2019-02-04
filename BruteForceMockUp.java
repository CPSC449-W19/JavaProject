package IO;

// Library imports
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

// Package imports
import Structure.Pair;

public class BruteForce {
	
	// Constants
	private final String possibleSolutionsFile;
	private final boolean debug;
	
	// Containers; hard/soft constraints will be filled in by the Parser class
	private LinkedList<Pair <Integer, String> > forcedPartialAssignment;
	private LinkedList<Pair <Integer, String> > forbiddenMachines;
	private LinkedList<Pair <Integer, String> > tooNearTasks;
	private LinkedList<Pair <Integer, String> > machinePenalties;
	private LinkedList<Pair <Pair <String, String>, Integer> > tooNearPenalties;
	
	// Finding all possible machine-tasks assignments; will store all possible solutions in a linked list of linked lists
	private LinkedList< LinkedList <Pair <Integer, String> > > solutionsList;
	
	for (int a = 1; a < 9; a++) {
		for (int b = 1; b < 9; b++) {
			for (int c = 1; c < 9; c++) {
				for (int d = 1; d < 9; d++) {
					for (int e = 1; e < 9; e++) {
						for (int f = 1; f < 9; f++) {
							for (int g = 1; g < 9; g++) {
								for (int h = 1; h < 9; h++) {
									
									// If all the number are distinct, add it to the list of solutions
									if (a != b && a != c & a != d && a != e && a != f && a != g && a != h 
											&& b != c && b != d && b != e && b != f && b != g && b != h 
											&& c != d && c != e && c != f && c != g && c != h
											&& d != e && d != f && d != g && d != h
											&& e != f && e != g && e != h
											&& f != g && f != h
											&& g != h) {
										
										// Put the possible combination into the list of solutions
										// Output into text file here
										System.out.println(a + b + c + d + e + f + g + h);
										
									}
										
								}
							}
						}
					}
				}
			}
		}
	}
	
	// For each solution:  Hard Constraint - Make sure that all forced partial assignment pairs are present
	/* 
	 * for (each solution in solutionList) {
	 * 
	 * 		for (each pair in forcedPartialAssignment) {
	 * 
	 * 			if (this forced pair is not in the current solution candidate) {
	 * 		
	 * 				delete this solution from the list;
	 * 
	 * 			}
	 * 
	 * 		}
	 * 
	 * }
	 * 
	 */
	
	// For each solution:  Hard Constraint - Make sure all forbidden machines are taken care of
	/*
	 * for (each solution in solutionList) {
	 * 
	 * 		for (each pair in the forbiddenMachines) {
	 * 
	 * 			if (this forbidden pair is in the current solution candidate) {
	 * 
	 * 				delete this solution from the list;
	 * 
	 * 			}
	 * 
	 * 		}
	 * 
	 * }
	 * 
	 */
	
	// For each solution:  Hard Constraint - Ensure that the too-near tasks are taken out
	/*
	 * for (each solution in solutionList) {
	 * 
	 * 		for (each task pair in tooNearTasks) {
	 * 
	 * 			if (machine for task2 == machine for task1 + 1) {
	 * 
	 * 				delete this solution from the list;
	 * 
	 * 			}
	 * 
	 * 		}
	 * 
	 * }
	 * 
	 */
	
	// Soft Constraint - Calculate all machine penalties
	/*
	 * for (each solution in solutionList) {
	 * 
	 * 		for (each machine in the solution) {
	 * 
	 * 			solutionPenaltyValue += penaltyValue;
	 * 
	 * 		}
	 * 
	 * }
	 * 
	 */
	
	// Soft Constraint - Calculate all too-near penalties
	/*
	 * for (each solution in solutionList) {
	 * 
	 * 		for (each pair in tooNearPenalties) {
	 * 
	 * 			if (machine for task2 == machine for task1 + 1) {
	 * 
	 * 				solutionPenaltyValue += penaltyValue;
	 * 
	 * 			}
	 * 
	 * 		}
	 * 
	 * }
	 * 
	 */
	
	// Find the solution with the lowest penalty value
	/*
	 * int min = 1000000000;
	 * String solution;
	 * 
	 * for (each solution in solutionList) {
	 * 
	 * 		if (solutionPenaltyValue < min) {
	 * 
	 * 			min = solutionPenaltyValue;
	 * 			solution = solutionString;
	 * 
	 * 		}
	 * 
	 * }
	 */
	
	// Output the solution to the appropriate output txt file
	
	
}  // End of Brute Force