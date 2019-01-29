import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Arrays;

public class Tree{

  private final int NUMBER_OF_TASKS_MACHINES = 8;

  public int addCost(Node node, LinkedList<LinkedList<Integer>> machinePenalties){
    Boolean [] unassigned = new Boolean[NUMBER_OF_TASKS_MACHINES]; //available tasks
    Arrays.fill(unassigned,true);

    int cost = 0;

        //node.getDepth()--confused what doing. help/discussion
        //getDepth + 1 because, want the options after the argument node? 
    for (int machine_position = node.getDepth()+1; machine_position < NUMBER_OF_TASKS_MACHINES; machine_position++){
      int minimum = Integer.MAX_VALUE;
      int min_task_index = -1;
      for (int task_position = 0; task_position < NUMBER_OF_TASKS_MACHINES; task_position++){

        if (unassigned[task_position] && machinePenalties.get(machine_position).get(task_position)<minimum
        && !node.getAssigned()[task_position]){

          minimum = machinePenalties.get(machine_position).get(task_position);

          min_task_index = task_position;
        }
      }
      cost += minimum;
      unassigned[min_task_index] = false;
    }

    return cost;

  }

  public void BranchBound(LinkedList<LinkedList<Integer>> machinePenalties){
    PriorityQueue<Integer> min_heap;
  }
}
