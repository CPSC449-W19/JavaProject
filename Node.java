//library imports
import java.util.LinkedList;

public class Node {

  //constants
  private final int NUMBER_OF_TASKS_MACHINES = 8;

  //variables
  private Node parent;
  private String job;
  private int machine;
  private int pathcost;
  private int cost; //cost of assignment
  private boolean [] jobsAssigned; // Keeps track of which jobs are available

  /**
   *  Setter methods
   */
  public void setJob (String givenJob) {
	  this.job = givenJob;
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
	  this.setCost = givenCost;
  }
  
  public void setAssigned (boolean[] givenJobsAssigned) {
	  this.jobsAssigned = givenJobsAssigned;
  }
  
  /**
   *  Getter methods
   */
  public String getJob(){
    return this.job;
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
    return this.assigned;
  }

} // End of Node
