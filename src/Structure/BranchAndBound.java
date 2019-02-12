package Structure;

// Library imports
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedList;


import IO.Parser;


/**
 * BranchAndBound will use supplied penalty lists to determine the best possible machine-task assignment with lowest cost.
 * 
 * @author Isha Afzaal
 * @since February 12, 2019
 */
public class BranchAndBound {

	// Constants
	private final int MAX_NUM = 8;
	private LinkedList<Pair<Integer,String>> forcedPartialAssignments;
    private LinkedList<Pair<Integer,String>> forbiddenMachines;
    private LinkedList<Pair<String,String>> tooNearTasks;
    private LinkedList<LinkedList<Integer>> machinePenalties;
    private LinkedList<Pair<Pair<String,String>,Integer>> tooNearPenalties;
    private LinkedList<Node> assignments = new LinkedList<>();
    private int totalCost = Integer.MAX_VALUE;
    private String message = "No valid solution possible!";
    private boolean debug;
	private String location;

	/**
     * Constructor that sets all penalty lists
     */
    public BranchAndBound ( Parser parser, boolean debug ) {

    	this.forcedPartialAssignments = parser.getForcedPartialAssignments();
    	this.forbiddenMachines = parser.getForbiddenMachines();
    	this.tooNearTasks = parser.getTooNearTasks();
    	this.machinePenalties = parser.getMachinePenalties();
    	this.tooNearPenalties = parser.getTooNearPenalties();
		this.debug = debug;
    }

    /**
     * Getter for solution.
     */
    public LinkedList<Node> getAssignments(){
			return assignments;
		}


	/**
	 * Create a node that assigns a given task to a given machine.
	 *
	 * @return Node newNode that contains the assignment
	 */
	public Node createNode (int machine, String task, boolean[] assigned, Node parent) {

		// Initialize the node
		Node newNode = new Node();
		newNode.setMachine(machine);
		newNode.setTask(task);

		// Update the new node's assignment matrix
		boolean [] temp = new boolean[MAX_NUM];

		for (int i = 0; i < MAX_NUM; i++) {
			temp[i] = assigned[i];
		}

		if (task.compareTo("A") == 0) {		// Can probably optimize this
			temp[0] = true;
		}
		else if (task.compareTo("B") == 0) {
			temp[1] = true;
		}
		else if (task.compareTo("C") == 0) {
			temp[2] = true;
		}
		else if (task.compareTo("D") == 0) {
			temp[3] = true;
		}
		else if (task.compareTo("E") == 0) {
			temp[4] = true;
		}
		else if (task.compareTo("F") == 0) {
			temp[5] = true;
		}
		else if (task.compareTo("G") == 0) {
			temp[6] = true;
		}
		else if (task.compareTo("H") == 0) {
			temp[7] = true;
		}

		// Set the new node's initial values
		newNode.setParent(parent);
		newNode.setTask(task);
		newNode.setMachine(machine);
		newNode.setAssigned(temp);

		
		// ***********DEBUG*********** //
	/*	if (newNode.getMachine() > 5) {
			
			// Standard stuff
			System.out.println("---- On Level " + Integer.toString(newNode.getMachine()) + " ----");
			System.out.println("Task is " + newNode.getTask() + ".");
			
			// Check the assignment matrix
			System.out.println("The assignment matrix:");
			for (int i = 0; i < newNode.getAssigned().length; i++) {
				
				System.out.println(Integer.toString(i + 1) + " is set to " + newNode.getAssigned()[i] + ".");
				
			}
		} */
		// ********** END DEBUG ************ //
		
		return newNode;

	}

	/**
	 * Calculate the cost once the given node is assigned to the given task.  If the node is a
	 * 	forbidden node then the return value will be -1.
	 *
	 * @return int penalty indicates the cost/penalty value of the desired assignment
	 */
	public int calculatePenalty (Node node) {

		// Cost will be set to -1 if the given node is a forbidden node assignment
		int cost = 0;
		Pair<Integer, String> currentCheck;

		// Hard Constraint:  Check if the assignment fulfills the Forbidden Machines requirement
		for (int i = 0; i < forbiddenMachines.size(); i++) {

			currentCheck = forbiddenMachines.get(i);

			// If the current forbidden machine pair matches the given node, set cost to -1
			if ( (currentCheck.getX() == node.getMachine()) && (currentCheck.getY().equals(node.getTask()) == true) ) {

				return -1;

			}

		}

		// Hard Constraint:  Check if the assignment fulfills the Forced Partial Assignments requirement
		for (int j = 0; j < forcedPartialAssignments.size(); j++) {

			currentCheck = forcedPartialAssignments.get(j);

			// First check if the given node's machine is in the current forced partial assignment node
			if (currentCheck.getX() == node.getMachine()) {

				// Check if the node has the required task assigned to it
				if (currentCheck.getY().equals(node.getTask()) == false) {

					// The assignment is invalid if the tasks do not match, set the cost and return
					return -1;

				}

			}

		}

		// Hard Constraint:  Check if the assignment fulfills the Too-Near Tasks requirements
		for (int k = 0; k < tooNearTasks.size(); k++) {

			Pair<String, String> taskPair = tooNearTasks.get(k);

			// Check if the given node is a first-level node
			if (node.getMachine() == -1 || node.getParent().getMachine() == -1 ) {

				// Leave this loop then and proceed to the next check
				break;

			}

			// Check if the given node has the same task assigned to it as the current too-near task pair
			else if (taskPair.getY().equals(node.getTask()) == true) {

				// Check if the node's parent (the machine before) holds the first task in the pair
				if (node.getParent().getTask().equals(taskPair.getX()) == true) {

					// The assignment is invalid, return error
					return -1;

				}

			}
			
			// Check if machines 8 and 1 correspond to a penalty pair
			else if (node.getMachine() == 8 && (taskPair.getX().equals(node.getTask()) == true) ) {
							
				// Iterate through the node's parents until we find machine 1
				Node findMachineOne = node.getParent();
				int countMachines = 0;
							
				// Use the counter variable to ensure termination of the loop
				while ( (findMachineOne.getMachine() != 1) && (countMachines < 9) ) {
								
					findMachineOne = findMachineOne.getParent();
					countMachines++;
								
				}
							
				// Check that we have successfully found machine 1
				if (findMachineOne.getMachine() == 1 && (taskPair.getY().equals(findMachineOne.getTask()) == true) ) {
								
					// The assignment is invalid, return error
					return -1;
							
				}

				// DEBUG
				System.out.println("In too near tasks hard.");

			}
	
		}

		// Soft Constraint:  Calculate the machine penalties from the given matrix
		LinkedList<Integer> relevantPenaltyRow = null;

		if (node.getMachine() == -1) {
			cost = 0;
		}
		else {
			relevantPenaltyRow = machinePenalties.get(node.getMachine() - 1);
		}

		if (node.getTask().equals("A") == true) {
			cost = relevantPenaltyRow.get(0);
		}
		else if (node.getTask().equals("B") == true) {
			cost = relevantPenaltyRow.get(1);
		}
		else if (node.getTask().equals("C") == true) {
			cost = relevantPenaltyRow.get(2);
		}
		else if (node.getTask().equals("D") == true) {
			cost = relevantPenaltyRow.get(3);
		}
		else if (node.getTask().equals("E") == true) {
			cost = relevantPenaltyRow.get(4);
		}
		else if (node.getTask().equals("F") == true) {
			cost = relevantPenaltyRow.get(5);
		}
		else if (node.getTask().equals("G") == true) {
			cost = relevantPenaltyRow.get(6);
		}
		else if (node.getTask().equals("H") == true) {
			cost = relevantPenaltyRow.get(7);
		}

		// Soft Constraint:  Calculate the too-near task penalties
		LinkedList<Pair<Pair<String,String>, Integer>> pairsFound = new LinkedList<Pair<Pair<String,String>, Integer>>();
		Pair<Pair<String, String>, Integer> currentTaskPair;
		Pair<Pair<String, String>, Integer> temp;
		int tooNearCost = 0;

		for (int l = 0; l < tooNearPenalties.size(); l++) {

			currentTaskPair = tooNearPenalties.get(l);

			// Check if the given node is on the first level
			if (node.getMachine() == -1 || node.getParent().getMachine() == -1) {

				// Leave this loop then
				break;

			}

			// Check if the current node-parent pair is the same as the penalty pair
			else if (currentTaskPair.getX().getY().equals(node.getTask()) == true) {

				if (currentTaskPair.getX().getX().equals(node.getParent().getTask()) == true) {

					// A match has been found.  Check if this pair has been found before
					if (pairsFound.indexOf(currentTaskPair) == -1) {

						// Add the current pair to the list of pairs that have been visited
						pairsFound.add(currentTaskPair);

						// Get the last occurrence of the pair
						temp = tooNearPenalties.get(tooNearPenalties.lastIndexOf(currentTaskPair));

						// Add in the near-task penalty
						cost += temp.getY();
					}

				}

			}
			
			// Check if machines 8 and 1 correspond to a penalty pair
			else if (node.getMachine() == 8 && (currentTaskPair.getX().getX().equals(node.getTask()) == true) ) {
				
				// Iterate through the node's parents until we find machine 1
				Node findMachineOne = node.getParent();
				int countMachines = 0;
				
				// Use the counter variable to ensure termination of the loop
				while ( (findMachineOne.getMachine() != 1) && (countMachines < 9) ) {
					
					findMachineOne = findMachineOne.getParent();
					countMachines++;
					
				}
				
				// Check that we have successfully found machine 1
				if (findMachineOne.getMachine() == 1 && (currentTaskPair.getX().getY().equals(findMachineOne.getTask()) == true) ) {
					
					// Then a pair has been found and the penalty value should be added if it has not been found before
					if (pairsFound.indexOf(currentTaskPair) == -1) {
						
						// Add the current pair to the list of pairs that have been visited
						pairsFound.add(currentTaskPair);
						
						// Get the last occurrence of the pair
						temp = tooNearPenalties.get(tooNearPenalties.lastIndexOf(currentTaskPair));
						
						// Add the near-task penalty
						cost += temp.getY();

						// DEBUG
						System.out.println("In too near tasks soft.");
					}
					
				}
				
			}

		}
		
		
		// ********** DEBUG ***********
		if (node.getMachine() == 6) {
			
			// Print out the location
			System.out.println("In calculatePenalty and on level 6 for machine 6");
			
			// Print out the task
			System.out.println("We are on task " + node.getTask());
			
			// Print out the cost
			System.out.println("With the cost " + Integer.toString(cost));
			
		}
		
		// *********** END DEBUG ************

		return cost;

	}

	/**
	 * Output the solution to a text file.
	 *
	 * @return String string that holds the solution.
	 */
	public void outputSolution (Node node) {
		// Check if the parent node is the dummy root node
		if (node.getMachine() == -1) {
			return;
		}
		
		// DEBUG
		System.out.println("DEBUG: Machine # " + Integer.toString(node.getMachine()) + " with task " + node.getTask() + " with cost " + Integer.toString(node.getCost()) + ".");

		outputSolution(node.getParent());

		assignments.add(node);
	}

	public int findCost(Node node) {
		if (node.getParent() == null) {
			return node.getCost();
		} else {
			return node.getCost() + findCost(node.getParent());
		}
	}

	/**
	 * Use the Branch and Bound algorithm to find the least-cost path.
	 */
	public void findSolution () {

		// Create data structure to hold all live nodes
		LinkedList<Node> liveNodes = new LinkedList<Node>();

		// Create the dummy root node
		boolean[] assigned = new boolean[MAX_NUM];			// Set all tasks to false by default, nothing has been assigned yet
		Node root = createNode(-1, " ", assigned, null);
		root.setPathCost(0);
		root.setCost(0);

		// Add the root to the list of live nodes
		liveNodes.add(root);

		// Placeholder variables
		Node leastNode = null;
		int leastIndex = 0;
		int currentLevel = 0;

		// Iterating through the live nodes now
		while (liveNodes.size() != 0) {

			// ***** DEBUG ******** Iterate through the live nodes
			Node test;
			System.out.println("\n---- LIVE NODES -----");
			for (int g = 0; g < liveNodes.size(); g++) {
				test = liveNodes.get(g);
				System.out.println("Machine " + Integer.toString(test.getMachine()) + " Task: " + test.getTask() + " Cost: " + Integer.toString(test.getCost()) + ".");
			}
			
			
			// ******* END DEBUG *********
			
			// Find a live node that is valid (the cost is not -1)
			for (int i = 0; i < liveNodes.size(); i++) {
				if (liveNodes.get(i).getCost() != -1) {
					leastNode = liveNodes.get(i);
					leastIndex = i;
					break;
				}
			}
			
			// DEBUG
			System.out.println("--- FOUND LEAST NODE ----");
			System.out.println("Machine: " + Integer.toString(leastNode.getMachine()) + " Task: " + leastNode.getTask() + " Cost: " + Integer.toString(leastNode.getCost()) );

			// Find the live node that has the least cost
			for (int j = 0; j < liveNodes.size(); j++) {
				
				// DEBUG
				System.out.println("---- FINDING LEAST NODE ----- ");
				System.out.println("Live node current machine: " + Integer.toString(liveNodes.get(j).getMachine()) + " with task: " + liveNodes.get(j).getTask() + " with cost: " + Integer.toString(liveNodes.get(j).getCost() ) );

				if ( (liveNodes.get(j).getCost() < leastNode.getCost() ) && (liveNodes.get(j).getCost() != -1) && (liveNodes.get(j).getMachine() == currentLevel) ) {

					// If the current live node's cost value is valid and less than the current leastNode, update leastNode and its index
					leastNode = liveNodes.get(j);
					leastIndex = j;

				}

			}
			
			// DEBUG
			System.out.println("----- CHOSEN LEAST NODE ----- ");
			System.out.println("Chosen least node current machine: " + Integer.toString(leastNode.getMachine()) + " with task: " + leastNode.getTask() + " with cost: " + Integer.toString(leastNode.getCost() ) );

			// Remove the least node from the list of live nodes
			liveNodes.remove(leastIndex);

			// Update the current level/machine
			if (leastNode.getMachine() == -1) {
				currentLevel = 1;
			}
			else {
				currentLevel = leastNode.getMachine() + 1;
			}

			// Check if we have assigned tasks to all machines
			if (currentLevel > MAX_NUM) {

				outputSolution(leastNode);
				totalCost = findCost(leastNode);
				messageBuilder();
				return;

			}

			// Check for which jobs are left to be assigned
			for (int k = 0; k < MAX_NUM; k++) {

				if (leastNode.getAssigned()[k] == false) {

					// Determine which task we are on
					String currentTask;

					if (k == 0) {
						currentTask = "A";
					}
					else if (k == 1) {
						currentTask = "B";
					}
					else if (k == 2) {
						currentTask = "C";
					}
					else if (k == 3) {
						currentTask = "D";
					}
					else if (k == 4) {
						currentTask = "E";
					}
					else if (k == 5) {
						currentTask = "F";
					}
					else if (k == 6) {
						currentTask = "G";
					}
					else {
						currentTask = "H";
					}

					// Create a new node for the unassigned job
					Node nextMachine = createNode(currentLevel, currentTask, leastNode.getAssigned(), leastNode);
					
					// Calculate the machine's penalty
					int calc = calculatePenalty(nextMachine);
					nextMachine.setCost(calc);

					// Update the path cost for the new node only if the next assignment is valid
					if (calc != -1) {

						// Update the new assignment's path cost
						nextMachine.setPathCost(leastNode.getPathCost() + calc);
						
						// DEBUG
						System.out.println("----- ADDING CHILDREN -----");
						System.out.println("Adding machine " + Integer.toString(nextMachine.getMachine()) + " with task " + nextMachine.getTask() + " with cost " + Integer.toString(nextMachine.getCost()) );

						// Add the new machine to the list of live nodes
						liveNodes.add(nextMachine);

					}

				}

			}
			
			// Remove all nodes that were on the previous level
			LinkedList<Integer> removeThis = new LinkedList<Integer> ();
			for (int m = 0; m < liveNodes.size(); m++) {
				if (liveNodes.get(m).getMachine() < currentLevel) {
					removeThis.add(m);
				}
			}
			
			for (int n = 0; n < removeThis.size(); n++) {
				System.out.println("---- REMOVING -----");
				System.out.println("Removing machine " + Integer.toString(liveNodes.get(n).getMachine()) + " with the task " + liveNodes.get(n).getTask() + " and the cost " + Integer.toString(liveNodes.get(n).getCost()));
				liveNodes.remove(n);
				
			}
		}
	}

	private void messageBuilder() {
		if (assignments.size() != 8) this.message = "No valid solution possible!";
		else {
			this.message = "Solution";
			assignments.sort(Comparator.naturalOrder());
			for (Node node : assignments) this.message += " " + node.getTask();
			this.message += "; Quality: " + this.totalCost;
		}
	}

	public String getMessage() {
		return this.message;
	}

	private void debug(String message) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss");
		if (isDebug()) System.err.println(String.format("%s [DEBUG] %s - %s", LocalDateTime.now().format(formatter),location,message));
	}

	public boolean isDebug() {
		return this.debug;
	}
} // End of BranchAndBound
