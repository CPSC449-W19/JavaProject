//library imports
import java.util.LinkedList;

public class Node<T>{

  //constants
  private final int NUMBER_OF_TASKS = 8;

  //variables
  private T key;
  private Node parent;
  private LinkedList<Node> children;
  private int pathcost;
  private int cost;
  private Boolean [] assigned = new Boolean[NUMBER_OF_TASKS];

  public Node(T key, Node parent, LinkedList<Node> children, int cost, int pathcost, Boolean[] assigned){
    this.key = key;
    this.parent = parent;
    this.children = children;
    this.cost = cost;
    this.pathcost = pathcost;
    this.assigned = assigned; 
  }

  public T getKey(){
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
}
