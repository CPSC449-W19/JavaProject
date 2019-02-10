/**
 * Tuple class to represent a char, char, int grouping.
 *
 */
package Tim_Code;

public class Tuple {
	private char task1;
	private char task2;
	private int penalty;

	public char getTask1() {
		return task1;
	}
	public char getTask2() {
		return task2;
	}
	public int getPenalty() {
		return penalty;
	}

	/**
	 * Constructor for Tuple - initializing two tasks and resulting penalty.
	 * @param t1 - char representing task1
	 * @param t2 - char representing task2
	 * @param p - int penalty
	 */
	public Tuple(char t1, char t2, int p) {
		task1 = t1;
		task2 = t2;
		penalty = p;
	}

	public String toString() {
		return (task1+" "+task2+" "+penalty);
	}
}
