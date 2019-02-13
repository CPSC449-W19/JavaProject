package Structure;

/* Library imports */
import java.util.Comparator;
import java.util.LinkedList;
import IO.Parser;

/**
 * BranchAndBound will solve an instance of the machine-task scheduling problem by using the branch and bound algorithm.
 * It receives and initializes all of the constraints via Parser object that is passed into the BranchAndBound Constructor.
 * Use the findSolution method to both calculate and output the solution.
 * 
 * @author Isha Afzaal
 * @since February 2019
 */
public class BranchAndBound {

	/* Constants */
	private final int MAX_NUM = 8;
	
	// Penalty lists
	private LinkedList<Pair<Integer,String>> forcedPartialAssignments;
    private LinkedList<Pair<Integer,String>> forbiddenMachines;
    private LinkedList<Pair<String,String>> tooNearTasks;
    private LinkedList<LinkedList<Integer>> machinePenalties;
    private LinkedList<Pair<Pair<String,String>,Integer>> tooNearPenalties;
    
    // Containers
    private LinkedList<Node> assignments = new LinkedList<>();
    private int totalCost = Integer.MAX_VALUE;
    private String message = "No valid solution possible!";
    private boolean debug;

	/**
     * Constructor that sets all penalty lists using an instance of Parser.
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
	 * createNode will create a new Node instance that is instantiated using the passed in
	 * machine, task, assigned and Node parent values.
	 *
	 * @param boolean[] assigned is the task assignment array of the parent node. Given the parent's assignment array,
	 *   createNode will copy the array and mark the new node's task as true to indicate that the given task is being
	 *   assigned and becoming unavailable for future machines.
	 * @return Node newNode that assigns to the given machine the task argument.
	 */
	public Node createNode (int machine, String task, boolean[] assigned, Node parent) {

		// Create the new node instance
		Node newNode = new Node();

		// Update the new node's assignment matrix
		boolean [] temp = new boolean[MAX_NUM];

		// Copy the parent node's assignment matrix
		for (int i = 0; i < MAX_NUM; i++) {
			temp[i] = assigned[i];
		}

		// Find the new node's task value and adjust the new assignment matrix to reflect the fact that the task is being assigned/taken
		if (task.compareTo("A") == 0) {
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

		// Fill the node's initial values and return
		newNode.setParent(parent);
		newNode.setTask(task);
		newNode.setMachine(machine);
		newNode.setAssigned(temp);
		
		debug("createNode", "newNode", newNode, 0);
		return newNode;
		
	}

	/**
	 * calculatePenalty will determine the penalty value of the given Node node.
	 * Algorithm: Check if the node violates any hard constraints.  If it does, return -1 to indicate a useless node.
	 * 	Then, calculate the node's soft penalty values by using the machine penalty matrix and calculating the too-near task soft penalties.
	 *  Finally, return the cost value.
	 * 
	 * @param Node node contains the machine-task assignment whose cost will be evaluated.
	 * @return int cost will have a positive value if the given node has a valid machine-task assignment. Otherwise, it will be -1.
	 */
	public int calculatePenalty (Node node) {

		int cost = 0;
		Pair<Integer, String> currentCheck;

		/* --- Hard Constraint #1: Forbidden Machines ---
		 * If node assigns a machine machi to task taski for any i-pair in the forbidden machines
		 * list, the node assignment is invalid and -1 will be returned. Otherwise, the cost value will not be changed from zero.
		 */
		for (int i = 0; i < forbiddenMachines.size(); i++) {

			currentCheck = forbiddenMachines.get(i);

			// If the current forbidden machine pair matches the given node, return -1 for invalid assignment
			if ( (currentCheck.getX() == node.getMachine()) && (currentCheck.getY().equals(node.getTask()) == true) ) {

				return -1;

			}

		}

		/* --- Hard Constraint #2: Forced Partial Assignments ---
		 * All (machine, task) assignments in the forced partial assignments list must be part of the solution.
		 * For any machine machi in any pair in the list, if the given node has the same machine but assigns it a task other than
		 * taski, the node assignment is invalid and -1 will be returned. Otherwise, the cost value will not be changed from zero.
		 */
		for (int j = 0; j < forcedPartialAssignments.size(); j++) {

			currentCheck = forcedPartialAssignments.get(j);

			// First check if the given node's machine is in the current forced partial assignment node
			if (currentCheck.getX() == node.getMachine()) {

				// Check if the node has the required task assigned to it
				if (currentCheck.getY().equals(node.getTask()) == false) {

					// The assignment is invalid if the tasks do not match, return -1 for invalid assignment
					return -1;

				}

			}

		}

		/* --- Hard Constraint #3: Too-Near Tasks --- 
		 * Any node that, together with its parent, forms a consecutive pair that is present in the too-near tasks list is an invalid
		 * assignment. In this case, node will be compared to task2, and if it matches, the node's parent will be compared to task1. If they
		 * both match, then a too-near task pair has been found and -1 will be returned. Otherwise, the cost value will not be changed from zero.
		 */
		for (int k = 0; k < tooNearTasks.size(); k++) {

			Pair<String, String> taskPair = tooNearTasks.get(k);

			// Check if the given node is a first-level node
			if (node.getMachine() == -1 || node.getParent().getMachine() == -1 ) {

				// Leave this loop then and proceed to the next check
				break;

			}

			// Else check if the given node has the same task assigned to it as the current too-near task pair's task2
			else if (taskPair.getY().equals(node.getTask()) == true) {

				// Check if the node's parent (i.e. the machine before) holds the first task in the pair
				if (node.getParent().getTask().equals(taskPair.getX()) == true) {

					// The assignment is invalid, return -1 for invalid assignment
					return -1;

				}

			}
	
		}

		/* --- Soft Constraint #1: Machine Penalties ---
		 * Using the machine penalty matrix, use the node's machine value to determine which penalty row to search in, and then
		 * use its task value to determine which column to retrieve from.
		 */
		LinkedList<Integer> relevantPenaltyRow = null;

		// Get the penalty row only if we are in a non-root node
		if (node.getMachine() == -1) {
			cost = 0;
		}
		else {
			relevantPenaltyRow = machinePenalties.get(node.getMachine() - 1);
		}

		// Get the value in the row according to the task
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

		/* --- Soft Constraint #2: Too-Near Task Penalties ---
		 * Calculate whether the node and its parent form a too-near task penalty pair.
		 * Iterate through each penalty pair and check, for each one, whether node and its parent match (task1, task2) for any pair i.
		 * If they match, update the cost value using the last instance of the pair in the penalty list.
		 */
		LinkedList<Pair<Pair<String,String>, Integer>> pairsFound = new LinkedList<Pair<Pair<String,String>, Integer>>();
		Pair<Pair<String, String>, Integer> currentTaskPair;
		Pair<Pair<String, String>, Integer> temp;

		// Iterate through each pair, looking for a match between (task1, task2) and (node's parent's task, node's task)
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

					// A match has been found.  Check if this pair has been found before (to prevent repeated cost updates)
					if (pairsFound.indexOf(currentTaskPair) == -1) {

						// Add the current pair to the list of pairs that have been visited
						pairsFound.add(currentTaskPair);

						// Get the last occurrence of the pair
						temp = tooNearPenalties.get(tooNearPenalties.lastIndexOf(currentTaskPair));

						// Add the too-near penalty value to cost
						cost += temp.getY();
					}

				}

			}
			
			// Check if machines 8 and 1 correspond to a penalty pair; (node's task, machine 1's task) will be compared to (task1, task2)
			else if (node.getMachine() == 8 && (currentTaskPair.getX().getX().equals(node.getTask()) == true) ) {
				
				// Iterate through the node's ancestors until we find machine 1
				Node findMachineOne = node.getParent();
				int countMachines = 0;
				
				// Use the counter variable to ensure termination of the loop
				while ( (findMachineOne.getMachine() != 1) && (countMachines < 9) ) {
					
					findMachineOne = findMachineOne.getParent();
					countMachines++;
					
				}
				
				// Check that we have successfully found machine 1 alongside checking for the pair
				if (findMachineOne.getMachine() == 1 && (currentTaskPair.getX().getY().equals(findMachineOne.getTask()) == true) ) {
					
					// Then a pair has been found and the penalty value should be added if it has not been found before
					if (pairsFound.indexOf(currentTaskPair) == -1) {
						
						// Add the current pair to the list of pairs that have been visited
						pairsFound.add(currentTaskPair);
						
						// Get the last occurrence of the pair
						temp = tooNearPenalties.get(tooNearPenalties.lastIndexOf(currentTaskPair));
						
						// Add the too-near penalty to cost
						cost += temp.getY();

					}
					
				}
				
			}

		}

		debug("calculatePenalty", "possible live", node, cost);
		return cost;

	}

	/**
	 * outputSolution will take a leaf node and recursively trace its parent's and add each to the assignment/solution list.
	 */
	public void outputSolution (Node node) {
		
		// Check if the parent node is the dummy root node
		if (node.getMachine() == -1) {
			return;
		}
		
		outputSolution(node.getParent());
		debug("outputSolution", "assignment", node);
		assignments.add(node);
	}

	/**
	 * findCost will calculate the cost of a given assignment solution path.
	 * 
	 * @param Node node which is a leaf node of the solution.
	 * @return int cost which will be calculated recursively.
	 */
	public int findCost(Node node) {
		
		debug("findCost");
		
		// Check if we have reached the dummy root node
		if (node.getParent() == null) {
			return node.getCost();
		} 
		else {
			return node.getCost() + findCost(node.getParent());
		}
		
	}

	/**
	 * findSolution solves an instance of the machine-task assignment problem.
	 * Outline: Represent each machine in the solution as a level of a search tree, while unassigned tasks represent the number
	 *   of nodes at each level. At each level, view each child (next machine's possible task assignments), pick the lowest cost
	 *   and switch the active, live node to the chosen least-cost node and repeat. Run the algorithm until all levels have been reached.
	 */
	public void findSolution () {

		LinkedList<Node> liveNodes = new LinkedList<Node>();

		// Create the root node; set all tasks to false by default since nothing has been assigned yet
		boolean[] assigned = new boolean[MAX_NUM];
		Node root = createNode(-1, " ", assigned, null);
		root.setPathCost(0);
		root.setCost(0);

		// Add the root to the list of live nodes
		liveNodes.add(root);

		// leastNode is the active node that will be switched to the next machine's least costing assignment on each iteration
		Node leastNode = null;
		int leastIndex = 0;
		int currentLevel = 0;

		// Keep choosing and generating machine-task assignments until either the live nodes are exhausted or if all levels have been determined
		while (liveNodes.size() != 0) {
	
			// Find a live node that is valid (the cost is not -1)
			for (int i = 0; i < liveNodes.size(); i++) {
				if (liveNodes.get(i).getCost() != -1) {
					leastNode = liveNodes.get(i);
					leastIndex = i;
					break;
				}
			}
			
			// Find the live node that has the least cost
			for (int j = 0; j < liveNodes.size(); j++) {
				
				debug("findSolution", "liveNode", liveNodes.get(j));
				
				if ( (liveNodes.get(j).getCost() < leastNode.getCost() ) && (liveNodes.get(j).getCost() != -1) ) {

					// If the current live node's cost value is valid and less than the current leastNode, update leastNode and its index
					leastNode = liveNodes.get(j);
					leastIndex = j;

				}

			}
			
			debug("findSolution", "leastNode", leastNode);
			
			// Remove the least node from the list of live nodes
			liveNodes.remove(leastIndex);

			// Update the current level/machine
			if (leastNode.getMachine() == -1) {
				currentLevel = 1;
			}
			else {
				currentLevel = leastNode.getMachine() + 1;
			}

			// Terminate program if we have assigned tasks to all machines
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

					// Add the child to the list of liveNodes only if it has a valid, non-negative cost value
					if (calc != -1) {

						// Update the new assignment's path cost and add it to liveNodes
						nextMachine.setPathCost(leastNode.getPathCost() + calc);
						liveNodes.add(nextMachine);
						debug("findSolution", "nextMachine", nextMachine);

					}

				}

			}
			
			// Remove all nodes that were on the previous level to prevent out-dated nodes from affecting the solution
			LinkedList<Integer> removeThis = new LinkedList<Integer> ();
			
			// Find the indices to remove
			for (int m = 0; m < liveNodes.size(); m++) {
				if (liveNodes.get(m).getMachine() < currentLevel) {
					removeThis.add(m);
				}
			}
			
			// Remove the old nodes from liveNodes
			for (int n = 0; n < removeThis.size(); n++) {
				liveNodes.remove(n);
			}
			
		} // End of the liveNodes while-loop
		
	}

	/**
	 * messageBuilder will determine whether the calculated solution is valid. If it is, then it will be output.
	 */
	private void messageBuilder() {
		if (assignments.size() != 8) this.message = "No valid solution possible!";
		else {
			this.message = "Solution";
			assignments.sort(Comparator.naturalOrder());
			for (Node node : assignments) this.message += " " + node.getTask();
			this.message += "; Quality: " + this.totalCost;
		}
	}

	/**
	 * Getter for message, used for debugging purposes.
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * @Override
	 * debug is used for logging and debugging purposes.
	 */
	private void debug(String message) {
		if (isDebug()) {
			System.out.println("BranchAndBound [DEBUG]: Currently in " + message + ".\n");
		}
	}
	
	/**
	 * @Override
	 * debug will be used for indicating run-time location and node values
	 */
	private void debug(String location, String type, Node node) {
		
		if (isDebug()) {
			
			System.out.println("BranchAndBound [DEBUG]: Currently in " + location + " with the " + type + " node. "
					+ "The node's Machine: " + Integer.toString(node.getMachine()) + " assigned Task: " + node.getTask()
					+ " with Cost: " + Integer.toString(node.getCost()) + ".\n");
			
		}
		
	}

	/**
	 * @Override
	 * debug will be used when the node and cost are separate
	 */
	private void debug(String location, String type, Node node, int cost) {
		
		if (isDebug()) {
			
			System.out.println("BranchAndBound [DEBUG]: Currently in " + location + " with the " + type + " node. "
					+ "The node's Machine: " + Integer.toString(node.getMachine()) + " assigned Task: " + node.getTask()
					+ " with Cost: " + Integer.toString(cost) + ".\n");
			
		}
		
	}
	
	/**
	 * Getter for the debug flag.
	 */
	public boolean isDebug() {
		return this.debug;
	}

} // End of BranchAndBound
