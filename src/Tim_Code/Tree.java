/**
 * Tree class file for generating the Tree to be traversed for the Branch and
 * Bound algorithm implementation.
 *
 */
package Tim_Code;

public class Tree {

	// Class variables
	private Constraints con;

	private boolean isDebugging = false;

	private char[] state = new char[8]; // stores the current state in the tree

	private char[] sol = new char[8]; // stores the best solution found

	private int min; // minimum penalty encountered so far.

	private int current; // penalty of state currently being reviewed

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
	 * Constructor for the Tree class objects. Generates an intialized Tree
	 * array of Machine nodes for each machine in the systems
	 */
	public Tree(Constraints c) {

		con = c; // Set to -1 if no valid leaf has been encountered yet
		min = -1;
		debug("Creating tree...");
		for (int i = 0; i < state.length; i++) {
			char m = 'z'; // Creates a machine set to task z (ie. null)
			state[i] = m;
			sol[i] = m;
			debug("Nulled machine " + i + " added");
		}
	}

	/**
	 * Iteratively generates the tree of possible states, traverses it, and
	 * prunes it by checking if a branch
	 *
	 * violates hard constraints or exceeds the minimum leaf penalty encountered
	 */
	public void traverseTree() {
		// loop through all possible tasks for Machine 1
		for (int i0 = 0; i0 < 8; i0++) {
			state[0] = (char) (i0 + 'A'); // asign task to Machine 1
			// cycle through all tasks to find one that is unique, valid, and a penalty smaller than the most recent minimum penalty
			while (i0 < 8 && (con.hardConstraintChecker(state) == -1 || (min != -1 && min < con.computePenalty(state)))) {
				i0++; // increment task
				if (i0 == 8)
					break; // if all tasks have been cycled through, breaks loop
				state[0] = (char) (i0 + 'A'); // asign task to Machine 1
			}
			// if all task have been cycled through, reset Machine 1 and return to previous machine
			if ((i0 > 7)) {
				state[0] = 'z';
				break;
			}

			// loop through all possible tasks for Machine 2
			for (int i1 = 0; i1 < 8; i1++) {
				state[1] = (char) (i1 + 'A'); // asign task to Machine 2
				// cycle through all tasks to find one that is unique, valid, and a penalty smaller than the most recent minimum penalty
				while (i1 == i0 || (i1 < 8 && (con.hardConstraintChecker(state) == -1 || (min != -1 && min < con.computePenalty(state))))) {
					i1++; // increment task
					if (i1 == 8)
						break; // if all tasks have been cycled through, breaks loop
					state[1] = (char) (i1 + 'A'); // asign task to Machine 2
				}
				// if all task have been cycled through, reset Machine 2 and return to previous machine
				if ((i1 > 7)) {
					state[1] = 'z';
					break;
				}

				// loop through all possible tasks for Machine 3
				for (int i2 = 0; i2 < 8; i2++) {
					state[2] = (char) (i2 + 'A'); // asign task to Machine 3
					// cycle through all tasks to find one that is unique, valid, and a penalty smaller than the most recent minimum penalty
					while (i2 == i0 || i2 == i1 || (i2 < 8 && (con.hardConstraintChecker(state) == -1 || (min != -1 && min < con.computePenalty(state))))) {
						i2++; // increment task
						if (i2 == 8)
							break; // if all tasks have been cycled through, breaks loop
						state[2] = (char) (i2 + 'A'); // asign task to Machine 3
					}
					// if all task have been cycled through, reset Machine 3 and return to previous machine
					if ((i2 > 7)) {
						state[2] = 'z';
						break;
					}

					// loop through all possible tasks for Machine 4
					for (int i3 = 0; i3 < 8; i3++) {
						state[3] = (char) (i3 + 'A'); // asign task to Machine 4
						// cycle through all tasks to find one that is unique, valid, and a penalty smaller than the most recent minimum penalty
						while (i3 == i0 || i3 == i1 || i3 == i2 || (i3 < 8 && (con.hardConstraintChecker(state) == -1 || (min != -1 && min < con.computePenalty(state))))) {
							i3++; // increment task
							if (i3 == 8)
								break; // if all tasks have been cycled through, breaks loop
							state[3] = (char) (i3 + 'A'); // asign task to Machine 4
						}
						// if all task have been cycled through, reset Machine 4 and return to previous machine
						if ((i3 > 7)) {
							state[3] = 'z';
							break;
						}

						// loop through all possible tasks for Machine 5
						for (int i4 = 0; i4 < 8; i4++) {
							state[4] = (char) (i4 + 'A'); // asign task to Machine 5
							// cycle through all tasks to find one that is unique, valid, and a penalty smaller than the most recent minimum penalty
							while (i4 == i0 || i4 == i1 || i4 == i2 || i4 == i3 || (i4 < 8 && (con.hardConstraintChecker(state) == -1 || (min != -1 && min < con.computePenalty(state))))) {
								i4++; // increment task
								if (i4 == 8)
									break; // if all tasks have been cycled through, breaks loop
								state[4] = (char) (i4 + 'A'); // asign task to Machine 5
							}
							// if all task have been cycled through, reset Machine 5 and return to previous machine
							if ((i4 > 7)) {
								state[4] = 'z';
								break;
							}

							// loop through all possible tasks for Machine 6
							for (int i5 = 0; i5 < 8; i5++) {
								state[5] = (char) (i5 + 'A'); // asign task to Machine 6
								// cycle through all tasks to find one that is unique, valid, and a penalty < recent minimum penalty
								while (i5 == i0 || i5 == i1 || i5 == i2 || i5 == i3 || i5 == i4 || (i5 < 8 && (con.hardConstraintChecker(state) == -1 || (min != -1 && min < con.computePenalty(state))))) {
									i5++; // increment task
									if (i5 == 8)
										break; // if all tasks have been cycled through, breaks loop
									state[5] = (char) (i5 + 'A'); // asign task to Machine 6
								}
								// if all task have been cycled through, reset Machine 6 and return to previous machine
								if ((i5 > 7)) {
									state[5] = 'z';
									break;
								}

								// loop through all possible tasks for Machine 7
								for (int i6 = 0; i6 < 8; i6++) {
									state[6] = (char) (i6 + 'A'); // asign task to Machine 7
									// cycle through all tasks to find one that is unique, valid, and a penalty < recent minimum penalty
									while (i6 == i0 || i6 == i1 || i6 == i2 || i6 == i3 || i6 == i4 || i6 == i5 || (i6 < 8 && (con.hardConstraintChecker(state) == -1 || (min != -1 && min < con.computePenalty(state))))) {
										i6++; // increment task
										if (i6 == 8)
											break; // if all tasks have been cycled through, breaks loop
										state[6] = (char) (i6 + 'A'); // asign task to Machine 7
									}
									// if all task have been cycled through, reset Machine 7 and return to previous machine
									if ((i6 > 7)) {
										state[6] = 'z';
										break;
									}

									// loop through all possible tasks for Machine 8
									for (int i7 = 0; i7 < 8; i7++) {
										state[7] = (char) (i7 + 'A'); // asign task to Machine 8
										// cycle through all tasks to find one that is unique, valid, and a penalty < recent minimum penalty
										while (i7 == i0 || i7 == i1 || i7 == i2 || i7 == i3 || i7 == i4 || i7 == i5|| i7 == i6 || (i7 < 8 && (con.hardConstraintChecker(state) == -1 || (min != -1 && min < con.computePenalty(state))))) {
											i7++; // increment task
											if (i7 == 8)
												break; // if all tasks have been cycled through, breaks loop
											state[7] = (char) (i7 + 'A'); // assign task to Machine 8
										}
										// if all task have been cycled through, reset Machine 8 and return to previous machine
										if ((i7 > 7)) {
											state[7] = 'z';
											break;
										}
										current = con.computePenalty(state);
										// if penalty of current leaf is lower than minimum encountered or this is the first leaf encountered
										if ((current < min) || (min == -1)) {
											min = current; // set current penalty as new minimum penalty
											sol = state.clone(); // record current leaf as solution
										}
									}
									state[7] = 'z';
								}
								state[6] = 'z';
							}
							state[5] = 'z';
						}
						state[4] = 'z';
					}
					state[3] = 'z';
				}
				state[2] = 'z';
			}
			state[1] = 'z';
		}
	}

	/**
	 * Prints the current state of the state. Used for debugging purposes only.
	 *
	 * @param t Tree object to print state of
	 */
	private void printState() {
		System.out.println("Current State:");
		for (int i = 0; i < state.length; i++) {
			System.out.print(i + " -> " + state[i]);
			String s = (i + 1 != state.length) ? " || " : "\n";
			System.out.print(s);
		}
	}

	/**
	 * Returns a string representation of the final solution.
	 *
	 * @return String statement of solution
	 */
	public String printSolution() {
		if (min == -1)
			return ("No valid solution possible!");
		String result = "Solution";
		for (int i = 0; i < 8; i++)
			result += " " + sol[i];
		result += "; Quality: " + min;
		return result;
	}
}
