/**
 * Constraints class file. Handles all of the constraints and the respective
 * error checking against our working solution.
 *
 */

package Tim_Code;

import java.util.ArrayList;

public class Constraints {
	private boolean isDebugging = false;

	private String name;

	// hard constraints
	ArrayList<Character[]> forced;
	ArrayList<Character[]> forbidden;
	ArrayList<Character[]> tooNearHard;

	// soft constraints
	private int[][] machPenalties;
	private ArrayList<Tuple> tooNearSoft = new ArrayList<Tuple>();

	/**
	 * Useful debugging method while developing to print out desired errors
	 *
	 * @param msg The error message you want displayed
	 */
	public void debug(String msg) {
		// check if debugging flag is set
		if (isDebugging)
			System.out.println("[DEBUGGING]:\t" + msg);
	}

	/**
	 * Constructor for constraints - initializes all 5 constraints.
	 * @param n - string representing name
	 * @param fpa - forced partial assignments ArrayList<Character[]>
	 * @param fm - forbidden machines ArrayList<Character[]>
	 * @param tnt - too near tasks ArrayList<Character[]>
	 * @param mp - machine penalty int array
	 * @param tnp - too near penalty arrayList<Tuple>
	 */
	public Constraints(String n, ArrayList<Character[]> fpa, ArrayList<Character[]> fm, ArrayList<Character[]> tnt, int[][] mp, ArrayList<Tuple> tnp) {
		name = n;
		forced = fpa;
		forbidden = fm;
		tooNearHard = tnt;
		machPenalties = mp;
		tooNearSoft = tnp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Character[]> getForced() {
		return forced;
	}

	public void setForced(ArrayList<Character[]> forced) {
		this.forced = forced;
	}

	public ArrayList<Character[]> getForbidden() {
		return forbidden;
	}

	public void setForbidden(ArrayList<Character[]> forbidden) {
		this.forbidden = forbidden;
	}

	public ArrayList<Character[]> getTooNearHard() {
		return tooNearHard;
	}

	public void setTooNearHard(ArrayList<Character[]> tooNearHard) {
		this.tooNearHard = tooNearHard;
	}

	public int[][] getMachPenalties() {
		return machPenalties;
	}

	public void setMachPenalties(int[][] machPenalties) {
		this.machPenalties = machPenalties;
	}

	public ArrayList<Tuple> getTooNearSoft() {
		return tooNearSoft;
	}

	public void setTooNearSoft(ArrayList<Tuple> tooNearSoft) {
		this.tooNearSoft = tooNearSoft;
	}

	/**
	 * Check the Forced Partial Assignments (Hard Constraint) against our current assignments. Will consist of up to
	 * 8 pairs, if a assignment is found in the Forced Partial Assignment that is not apart of state(our current
	 * solution) then the assignment is invalid (will ignore FPA when state has 'z' element).
	 *
	 * @param state- char[] of our current assignment (stores tasks at index = mach - 1)
	 * @return Value for the success of the input comparison. 0 if all forced partial assignments are met, -1 otherwise.
	 */
	public int checkForcedPartial(char[] state) {
		// Iterate through forcedPartAssn and compare appropriately with state
		for (int i = 0; i < forced.size(); i++) {
			int mach = Character.getNumericValue(forced.get(i)[0]) - 1;
			if (state[mach] != 'z' && forced.get(i)[1] != state[mach]) {
				// forcedPartAssn constraint not met, return error
				debug("Forced Part Assignment not met for machine " + (mach+1));
				debug("FPA -> " + (mach+1) + ", " + forced.get(i)[1] + "\t\tourAssn -> " + (mach+1) + ", " + state[mach]);
				return -1;
			}
		}
		return 0;
	}

    /**
     * Checks the Forbidden Machines (Hard Constraint) against our current assignments. If a assignment in the Forbidden
     * Machines matches one that is apart of state (our current solution) then our assignment is invalid.
     *
     * @param state- char[] of our current assignment (stores tasks at index = mach - 1)
     * @return Value for the success of the comparison. 0 if no matches with Forbidden Machine Assigns, -1 otherwise.
     */
	public int checkForbidden(char[] state) {
		// Iterate over the forbidden constraint array
		for (int i = 0; i < this.forbidden.size(); i++) {
			int forbMach = this.forbidden.get(i)[0]-'0';

			if (state[forbMach-1] == this.forbidden.get(i)[1]) {
				debug("Forbidden Machine Assignment violated for machine " + forbMach);
				debug("Forbidden -> " + forbMach + ", " + forbidden.get(i)[1] + "\t\tourAssn -> " + forbMach + ", " + state[forbMach-1]);
				return -1;
			}
		}
		return 0;
	}

    /**
     * Checks the Too Near Tasks (Hard Constraint) against our current assignments. If two assignments in state (our
     * current solution) are on neighboring machines and match a pair in Too Near Tasks, our assignment is invalid.
     *
     * @param state- char[] of our current assignment (stores tasks at index = mach - 1)
     * @return Value for the success of the comparison. 0 if no matches with Too Near Tasks, -1 otherwise.
     */
	public int checkTooNearHard(char[] state) {
		// Assumes that the same task is not assigned to two machines
		int index = 0;

		for (int i = 0; i < this.tooNearHard.size(); i++) {
			index = indexOf(state, tooNearHard.get(i)[0]);
			if (index != -1 && (state[((index+1) % 8)] == tooNearHard.get(i)[1])) { // search forward by 1 only
				debug("Too Near Task violated: " + tooNearHard.get(i)[0] + tooNearHard.get(i)[1]);
				debug("Assigned Machine/Task Pair that fails-> " + (indexOf(state, tooNearHard.get(i)[1])+1) + state[indexOf(state, tooNearHard.get(i)[1])]+" and "+ (index+1)  + state[index]);
				return -1;
			}
		}
		return 0;
	}

    /**
     * Computes the Machine and Too Near Penalties (Soft Constraints) of state (our current
     * assignments solution).
     *
     * @param state- char[] of our current assignment (stores tasks at index = mach - 1)
     * @return Value for the total too near penalties
     */
	public int computePenalty(char[] state) {
		// Assumes that the same task is not assigned to two machines
		int penalty = 0;
		int index = 0;

		// compute machine penalties
		debug("Computing penalties...");
		for (int machineNum = 0; machineNum < 8; machineNum++) {
			int taskNum;
			if (state[machineNum] != 'z') { // no penalty for 'z'
				taskNum = state[machineNum] - 'A'; // state stores tasks(chars) so subtract 'A' to find related int array position
				penalty = penalty + machPenalties[machineNum][taskNum];
				debug("Array: [" + machineNum + "][" + taskNum + "] =" + machPenalties[machineNum][taskNum]+ " for Machine:" + (machineNum + 1) + " Task:" + (char) (taskNum + 'A'));
			}
		}

		// compute too near soft penalties by looping through constraints
		for (int i = 0; i < state.length; i++) {
			index = indexOf(tooNearSoft, state[i], state[((i + 1) % 8)]); // search forward for exact pattern
			debug("Searching for: " + state[i] + state[((i + 1) % 8)] + ". Returned index " + index);

			if (index != -1) {
				penalty += (this.tooNearSoft.get(index).getPenalty()); // if task exists and constraint is directly adjacent, add penalty
				debug("Too Near Penalty -> " + tooNearSoft.get(index).getTask1() + tooNearSoft.get(index).getTask2()+ " " + tooNearSoft.get(index).getPenalty());
			}
		}
		debug("Total computed penalty: " + penalty);
		return penalty;
	}

	/**
	 * Computes the validity of a state and all future states in the tree with regards to a current minimum
	 * penalty state
	 *
	 * @param state - char[] of our current assignment (stores tasks at index = mach - 1)
	 * @return either 0 if hard constraints are valid; -1 if not
	 */
	public int hardConstraintChecker(char[] state) {
		if (-1 == checkTooNearHard(state)) {
			return -1;
		} else if (-1 == checkForcedPartial(state)) {
			return -1;
		} else if (-1 == checkForbidden(state)) {
			return -1;
		}

		return 0;
	}

    /**
     * Helper method to find the index of a element in a character array.
     *
     * @param searchable- ArrayList<Tuple> to search through
     * @param task1 - char in first slot to look for
     * @param task2 - char in second slot to look for
     * @return Value of array index if 'finding' is located in searchable[], -1 otherwise.
     */
	private int indexOf(ArrayList<Tuple> searchable, char task1, char task2) {
		int i = searchable.size() - 1;
		// Loop until found or end of searchable
		while (i >= 0 && (searchable.get(i).getTask1() != task1 || searchable.get(i).getTask2() != task2)) {
			debug("Current constraint: " + searchable.get(i).getTask1() + searchable.get(i).getTask2());
			i--;
		}

		return i;
	}

    /**
     * Helper method to find the index of a element in a character array.
     *
     * @param searchable- char[] to search through
     * @param finding - char element to look for
     * @return Value of array index if 'finding' is located in searchable[], -1 otherwise.
     */
	private int indexOf(char[] searchable, char finding) {
		int i = 0;

		// Loop until found or end of searchable
		while (i < searchable.length && searchable[i] != finding)
			i++;

		// If loop got to end, return not found. Else, return index
		return (i == searchable.length) ? -1 : i;
	}
}
