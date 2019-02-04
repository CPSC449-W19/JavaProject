// Library imports
import java.util.LinkedList;
import Node;

public class BranchAndBound {
	
	// Constants
	private final int MAX_NUM = 8;
	
	/**
	 * Create a node that assigns a given task to a given machine.
	 * 
	 * @return Node newNode that contains the assignment
	 */
	Node createNode (int machine, String task, boolean[] assigned, Node parent) {
		
		Node newNode = new Node(machine, task);
		
		// Update the new node's assignment matrix
		Boolean [] temp newNode.getAssigned();
		
		for (int i = 0; i < TOTAL; i++) {
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
		else {
			temp[7] = true;
		}
		
		// Set the new node's initial values
		newNode.setParent(parent);
		newNode.setTask(task);
		newNode.setMachine(machine);
		
		return newNode;
		
	}

	/**
	 * Calculate the cost once the given node is assigned to the given task.  If the node is a
	 * 	forbidden node then the return value will be -1.
	 * 
	 * @return int penalty indicates the cost/penalty value of the desired assignment
	 */
	int calculatePenalty (LinkedList<Pair<Integer,String>> forcedPartialAssignments, LinkedList<Pair<Integer,String>> forbiddenMachines, LinkedList<Pair<String,String>> tooNearTasks, LinkedList<LinkedList<Integer>> machinePenalties, LinkedList<Pair<Pair<String,String>,Integer>> tooNearPenalties, Node node, boolean[] assigned) {
		
		// Cost will be set to -1 if the given node is a forbidden node assignment
		int cost = 0;
		Pair<Integer, String> currentCheck;
		
		// Hard Constraint:  Check if the assignment fulfills the Forbidden Machines requirement
		for (int i = 0; i < forbiddenMachines.size(); i++) {
			
			currentCheck = forbiddenMachines.get(i);
			
			// If the current forbidden machine pair matches the given node, set cost to -1
			if ( (currentCheck.getX() == node.getMachine()) && (currentCheck.getY().equals(node.getTask()) == 0) ) {
				
				return -1;
				
			}
		
		}
		
		// Hard Constraint:  Check if the assignment fulfills the Forced Partial Assignments requirement
		for (int j = 0; j < forcedPartialAssignments.size(), j++) {
			
			currentCheck = forcedPartialAssignments.get(j);
			
			// First check if the given assignment's machine is in the current forced partial assignment node
			if (currentCheck.getX() == node.getMachine()) {
				
				// Check if the node has the required task assigned to it
				if (currentCheck.getY().equals(node.getTask()) == 0) {
					
					// The assignment is done correctly then, do nothing
					
				}
				else {
					
					// The assignment is invalid, set the cost and return
					return -1;
			
				}
				
			}
			
		}
		
		// Hard Constraint:  Check if the assignment fulfills the Too-Near Tasks requirements
		for (int k = 0; k < tooNearTasks.size(); k++) {
			
			Pair<String, String> taskPair = tooNearTasks.get(k);
			
			// Check if the given node has the same task assigned to it as the current too-near task pair
			if (taskPair.getX().equals(node.getTask()) == 0) {
				
				// Check if the current node is on the first level
				if (node.getParent().getTask().equals(taskPair.getY()) == 0) {
				
					// The assignment is invalid, set the cost and return
					node.setCost(-1);
					return -1;
				
				}
				
			}
			
		}
		
		// Soft Constraint:  Calculate the machine penalties from the given matrix
		LinkedList<Integer> relevantPenaltyRow = machinePenalties.get(node.getMachine() - 1);
		if (node.getTask().equals("A") == 0) {
			cost = relevantPenaltyRow.get(0);
		}
		else if (node.getTask().equals("B") == 0) {
			cost = relevantPenaltyRow.get(1);
		}
		else if (node.getTask().equals("C") == 0) {
			cost = relevantPenaltyRow.get(2);
		}
		else if (node.getTask().equals("D") == 0) {
			cost = relevantPenaltyRow.get(3);
		}
		else if (node.getTask().equals("E") == 0) {
			cost = relevantPenaltyRow.get(4);
		}
		else if (node.getTask().equals("F") == 0) {
			cost = relevantPenaltyRow.get(5);
		}
		else if (node.getTask().equals("G") == 0) {
			cost = relevantPenaltyRow.get(6);
		}
		else {
			cost = relevantPenaltyRow.get(7);
		}
		
		// Soft Constraint:  Calculate the too-near task penalties
		LinkedList<Pair<Pair<String,String>, Integer>> pairsFound = new LinkedList<Pair<Pair<String,String>, Integer>>;
		Pair<Pair<String, String>, Integer> currentTaskPair;
		Pair<Pair<String, String>, Integer> temp;
		int tooNearCost = 0;
		
		for (int l = 0; l < tooNearPenalties.size(); l++) {
			
			currentTaskPair = tooNearPenalties.get(l);
			
			// Check if the current node-parent pair is the same as the penalty pair
			if (currentTaskPair.getX().getY().equals(node.getTask()) == 0) {
				
				if (currentTaskPair.getX().getX().equals(node.parent.getTask()) == 0) {
					
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
			
		}
		
		return cost;
		
	}

	/**
	 * Output the solution to a text file.
	 * 
	 * @return String string that holds the solution.
	 */
	String outputSolution (Node node) {
		
		// Check if the parent node is the dummy root node
		if (node.getParent().getMachine() == -1) {
			return node.getTask();
		}
		
		return outputSolution(node.getParent()) + " " + node.getTask();
		
	}
	
	/**
	 * Use the Branch and Bound algorithm to find the least-cost path.
	 */
	void findSolution () {
		
		// Create data structure to hold all live nodes
		LinkedList<org.w3c.dom.Node> liveNodes = new LinkedList<Node>;
		
		// Create the dummy root node
		boolean[] assigned = new boolean[MAX_NUM];			// Set all tasks to false by default, nothing has been assigned yet
		Node root = createNode(-1, "", assigned, NULL);
		root.setPathCost(0);
		root.setCost(0);
		
		// Add the root to the list of live nodes
		liveNodes.add(root);
		
		// Placeholder variables
		Node leastNode;
		int leastIndex;
		int currentLevel;
					
		// Iterating through the live nodes now
		while (liveNodes.size() != 0) {
			
			// Find a live node that is valid (the cost is not -1)
			for (int i = 0; i < liveNodes.size(); i++) {
				if (liveNodes.get(i).getCost() != -1) {
					leastNode = liveNodes.get(i);
					break;
				}
			}
			
			// Find the live node that has the least cost
			for (int j = 0; j < liveNodes.size(); j++) {
				
				if ( (liveNodes.get(j).getCost() < leastNode.getCost() ) && (liveNodes.get(j).getCost() != -1) ) {
					
					// If the current live node's cost value is valid and less than the current leastNode, update leastNode and its index
					leastNode = liveNodes.get(j);
					leastIndex = j;

				}
				
			}
			
			// Remove the least node from the list of live nodes
			liveNodes.remove(leastIndex);
			
			// Update the current level/machine
			currentLevel = leastNode.getMachine() + 1;
			
			// Check if we have assigned tasks to all machines
			if (currentLevel = MAX_NUM) {
				
				String solution = outputSolution(leastNode);
				break;
				
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
						currentTask = "F"
					}
					else if (k == 6) {
						currentTask = "G";
					}
					else {
						currentTask = "H";
					}
					
					// Create a new node for the unassigned job
					Node nextMachine = createNode(currentLevel, currentTask, leastNode.getAssigned(), leastNode);
					
					// Update the path cost for the new node only if the next assignment is valid
					if (calculatePenalty(nextMachine) != -1) {
						
						// Update the new assignment's cost and path cost
						nextMachine.setPathCost(leastNode.getPathCost() + calculatePenalty(nextMachine));
						nextMachine.setCost(calculatePenalty(nextMachine));
						
						// Add the new machine to the list of live nodes
						liveNodes.add(nextMachine);
						
					}
					
				}
				
			}
			
		}
	}

} // End of BranchAndBound