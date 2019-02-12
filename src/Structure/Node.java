package Structure;

public class Node implements Comparable<Node> {

  //variables
  private Node parent;
  private String task;
  private int machine;
  private int pathcost;
  private int cost; //cost of assignment
  private boolean [] jobsAssigned; // Keeps track of which jobs are available

  public int compareTo(Node node) {
    return this.machine - node.machine;
  }

  /**
   *  Setter methods
   */
  public void setTask (String givenTask) {
	  this.task = givenTask;
  }

  public void setMachine (int givenMachine) {
	  this.machine = givenMachine;
  }

  public void setParent (Node givenParent) {
	  this.parent = givenParent;
  }

  public void setPathCost (int givenPathCost) {
	  this.pathcost = givenPathCost;
  }

  public void setCost (int givenCost) {
	  this.cost = givenCost;
  }

  public void setAssigned (boolean[] givenJobsAssigned) {
	  this.jobsAssigned = givenJobsAssigned;
  }

  /**
   *  Getter methods
   */
  public String getTask(){
    return this.task;
  }

  public int getMachine() {
	  return this.machine;
  }

  public Node getParent(){
    return this.parent;
  }

  public int getPathCost(){
    return this.pathcost;
  }

  public int getCost(){
    return this.cost;
  }

  public boolean[] getAssigned(){
    return this.jobsAssigned;
  }

} // End of Node
