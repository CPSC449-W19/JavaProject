package Structure;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Arrays;
import IO.Parser;


/**
 * BruteForce uses a brute force approach to solving a given instance of the machine-task assignment problem.
 * It receives all hard and soft constraints via Parser object that is passed into the BruteForce constructor.
 * Call the findSolution method to solve the machine-task assignment problem.
 * 
 * @author Elzanne Venter
 * @since February 2019
 */
public class BruteForce{
	
	// Penalties
	private LinkedList<Pair<Integer,String>> forcedPartialAssignments;
    private LinkedList<Pair<Integer,String>> forbiddenMachines;
    private LinkedList<Pair<String,String>> tooNearTasks;
    private LinkedList<LinkedList<Integer>> machinePenalties;
    private LinkedList<Pair<Pair<String,String>,Integer>> tooNearPenalties;
  //  private boolean debug;
    LinkedList<LinkedList<String>> all_possible = new LinkedList<LinkedList<String>>();
    LinkedList<String> x = new LinkedList<String>();


    private String [] tasks = {"A","B","C","D","E","F","G","H"};
    private int min_cost = Integer.MAX_VALUE;

    /**
     * Constructor.
     * @param parser is used to initialize the penalty lists
     */
    public BruteForce ( Parser parser) {

  		this.forcedPartialAssignments = parser.getForcedPartialAssignments();
  		this.forbiddenMachines = parser.getForbiddenMachines();
  		this.tooNearTasks = parser.getTooNearTasks();
  		this.machinePenalties = parser.getMachinePenalties();
  		this.tooNearPenalties = parser.getTooNearPenalties();

      }


    /**
     * Getter for all possible solutions.
     */
    public LinkedList<LinkedList<String>> getAllPossible(){
      return all_possible;
    }

    
    /**
     * Create all possible solutions.
     */
    public LinkedList<LinkedList<String>> createAll(){
        all_possible.clear();
        generateAll(tasks,8,0);
        System.out.println(all_possible.size());
        return all_possible;
    }

    
    /**
     * Generate all possible solutions. Should have 40320 permutations.
     */
    public void generateAll(String[] tasks,int size, int count){

      if (size == 1){
        x = new LinkedList<String>();
        for (int i = 0; i<8; i++){
          x.add(tasks[i]);
        }
        all_possible.add(x);
      }

      for (int i = 0; i<size; i++){
        generateAll(tasks,size-1,count+1);

        if (size%2 ==1){
          String temp = tasks[0];
          tasks[0] = tasks[size-1];
          tasks[size-1] = temp;
        }

        else{
          String temp = tasks[i];
          tasks[i] = tasks[size-1];
          tasks[size-1] = temp;
        }
      }

    }

    /**
     * hardConstraints_assignments 
     */
    public LinkedList<LinkedList<String>> hardConstraints_assignments (LinkedList<LinkedList<String>> all_possible
    , LinkedList<Pair<Integer,String>> forced_partial, LinkedList<Pair<Integer,String>> forbidden){

      //System.out.println(all_possible.get(0).get(forced_partial.get(1).getX()-1));
      //System.out.println(forced_partial.get(0).getY());
      //forced assignments
      LinkedList<LinkedList<String>> newAllPossible = new LinkedList<LinkedList<String>>();

      boolean partialSatisfied = true;
      for (LinkedList<String> k : all_possible){
        for (Pair<Integer,String> pair : forced_partial){
          if (!(k.get(pair.getX()-1).equals(pair.getY()))){
            partialSatisfied = false;
          }
        }
        if (partialSatisfied == true) {
          newAllPossible.add(k);
        }
        else {
          partialSatisfied = true;
        }
      }

      LinkedList<LinkedList<String>> newAllPossible2 = new LinkedList<LinkedList<String>>();
      partialSatisfied = true;
      for (LinkedList<String> k : newAllPossible){
        for (Pair<Integer,String> pair : forbidden){
          if ((k.get(pair.getX()-1).equals(pair.getY()))){
            partialSatisfied = false;
          }
        }
        if (partialSatisfied == true) {
          newAllPossible2.add(k);
        }
        else {
          partialSatisfied = true;
        }
      }

      return newAllPossible2;

    }


    public LinkedList<LinkedList<String>> hardConstraints_too_near(LinkedList<LinkedList<String>> all_possible,
    LinkedList<Pair<String,String>> too_near){
      for (int i = 0; i<too_near.size(); i++){
        Pair<String,String> current_too_near = too_near.get(i);
        for (int j = 0; j< all_possible.size(); j++){
          LinkedList<String> current_assignment = all_possible.get(j);
          for (int k = 0; k<current_assignment.size();k++){
            if (current_assignment.get(k) == current_too_near.getX() && current_assignment.get(
            (k+1)%8) == current_too_near.getY()){
              all_possible.remove(j);
            }
          }
        }
      }

      return all_possible;

    }

    public int get_quality(LinkedList<String> assignment, LinkedList<LinkedList<Integer>> machine_cost,
    LinkedList<Pair<Pair<String,String>,Integer>> tooNearPen){
      int cost = 0;
      int task_number = -1;

      for (int i = 0; i< assignment.size() ; i++ ){

        String current_task = assignment.get(i);
        switch(current_task){
          case "A": task_number = 0;
                    break;
          case "B": task_number = 1;
                    break;
          case "C": task_number = 2;
                  break;
          case "D": task_number = 3;
                  break;

        case "E": task_number = 4;
        break;

        case "F": task_number = 5;
        break;

        case "G": task_number = 6;
        break;

        case "H": task_number = 7;
        break;

        }

        cost = cost + machine_cost.get(i).get(task_number); //[machine number][task number]

      }


      for (int i = 0; i< tooNearPen.size(); i++){
        Pair<Pair<String,String>,Integer> current_too_near = tooNearPen.get(i);
        for (int j = 0; j<assignment.size();j++){
          if ((assignment.get(j).equals(current_too_near.getX().getX())) && (assignment.get((j+1)%8).equals(
          current_too_near.getX().getY()))){
            cost = cost + current_too_near.getY();
          }
        }
      }

      return cost;

    }


    public LinkedList<String> findSolution(){

         LinkedList<String> solution = new LinkedList<>();

         int index = -1;

         LinkedList<LinkedList<String>> all_solutions = createAll();

        //System.out.println(all_solutions);

         all_solutions = hardConstraints_assignments(all_solutions,
         forcedPartialAssignments, forbiddenMachines);

         all_solutions = hardConstraints_too_near(all_solutions,tooNearTasks);

         System.out.println(all_solutions.size());

         for (int i = 0; i< all_solutions.size(); i++){
           LinkedList<String> current_solution = all_solutions.get(i);
           int current_cost = get_quality(current_solution, machinePenalties, tooNearPenalties);
           if (current_cost < min_cost){
             index = i;
             min_cost = current_cost;
           }
         }
         if (index != -1){

          solution = all_solutions.get(index);


        }

        //System.out.println(solution);
        return solution;

       }


       public String solutiontoString(LinkedList<String> solution){
         String output = "Solution";

         for (int i = 0; i<solution.size();i++){
           output = output + " " + solution.get(i);
         }

         output = output + "; Quality: " + min_cost;
         System.out.println(output);
         return output;

       }





}
