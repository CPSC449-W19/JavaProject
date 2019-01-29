//library imports
import java.util.LinkedList;

public class Node{

  //constants
  private final int NUMBER_OF_TASKS = 8;

  //variables
  private int key; // task. (0-A,1-B etc.)
  private Node parent;
  private LinkedList<Node> children;
  private int pathcost;
  private int cost;
  private int depth; //machine
  private Boolean [] assigned = new Boolean[NUMBER_OF_TASKS];

  public Node(int key, Node parent, LinkedList<Node> children, int cost, Boolean[] assigned){
    this.key = key;
    this.parent = parent;
    this.children = children;
    this.cost = cost;
    this.pathcost = (this.parent == null) ? cost : cost + parent.getPathCost();
    this.depth = (this.parent == null) ? 0 : this.parent.getDepth() + 1;

    for (int i = 0; i< NUMBER_OF_TASKS; i++ ){
      this.assigned[i] = assigned[i];
    }

    this.assigned[key-1] = true;
  }

  public int getKey(){
    return this.key;
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

  public int getDepth(){
    return this.depth;
  }
}
