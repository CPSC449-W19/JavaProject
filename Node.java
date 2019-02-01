//library imports
import java.util.LinkedList;

public class Node{

  //constants
  private final int NUMBER_OF_TASKS_MACHINES = 8;

  //variables
  private Pair<Integer,String> assignment; //x = machine, y = task
  private Node parent;
  private LinkedList<Node> children;
  private int pathcost;
  private int cost; //cost of assignment
  private Boolean [] assigned = new Boolean[NUMBER_OF_TASKS_MACHINES];

  public Node(Pair<Integer,String> assignment, Node parent, LinkedList<Node> children, int cost, Boolean[] assigned){
    this.assignment = assignment;
    this.parent = parent;
    this.children = children;
    this.cost = cost;
    this.pathcost = (this.parent == null) ? cost : cost + parent.getPathCost();

    for (int i = 0; i< NUMBER_OF_TASKS_MACHINES; i++ ){
      this.assigned[i] = assigned[i];
    }

    //need to set correct assigned to true
  }

  public int getAssignment(){
    return this.assignment;
  }

  public Node getParent(){
    return this.parent;
  }

  public LinkedList<Node> getChildren(){
    return this.children;
  }

  public int getPathCost(){
    return this.pathcost;
  }

  public int getCost(){
    return this.cost;
  }

  public Boolean[] getAssigned(){
    return this.assigned;
  }

}
