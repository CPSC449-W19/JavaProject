
package Structure;

//library imports
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Arrays;

//package imports
import IO.Parser;

/**
 *  BruteForce takes a parser object as input.
 *  To solve the problem the following steps are taken:
 *  all possible solutions are generated
 *  the solutions are then narrowed down based on the forced/forbidden assignments hard constraint
 *  the solutions are further narrowed down based on the too near tasks hard constraint
 *  then the cost of the remaing possible solutions are calculated and the lowest one is the solution
 *
 * @author Elzanne Venter
 * @since February 2019
 */
public class BruteForce{

  //containers
  private LinkedList<Pair<Integer,String>> forcedPartialAssignments;
    private LinkedList<Pair<Integer,String>> forbiddenMachines;
    private LinkedList<Pair<String,String>> tooNearTasks;
    private LinkedList<LinkedList<Integer>> machinePenalties;
    private LinkedList<Pair<Pair<String,String>,Integer>> tooNearPenalties;
    LinkedList<LinkedList<String>> all_possible = new LinkedList<LinkedList<String>>();
    LinkedList<String> x = new LinkedList<String>();

    //maybe don't use?
    LinkedList<String> finalsolution = new LinkedList<>();


    private String [] tasks = {"A","B","C","D","E","F","G","H"};
    private int min_cost = Integer.MAX_VALUE;


    /**
     * receives information from parser object
     * @param parser
     * @author Elzanne Venter
     * @since February 2019
     */
    public BruteForce ( Parser parser) {

  		this.forcedPartialAssignments = parser.getForcedPartialAssignments();
  		this.forbiddenMachines = parser.getForbiddenMachines();
  		this.tooNearTasks = parser.getTooNearTasks();
  		this.machinePenalties = parser.getMachinePenalties();
  		this.tooNearPenalties = parser.getTooNearPenalties();

      }

      //NEXT THREE METHODS USE HEAPS ALGORITHM TO GENERATE ALL POSSIBLE SOLUTIONS

      //getter for the generated list of all solutions
    public LinkedList<LinkedList<String>> getAllPossible(){
      return all_possible;
    }

    public LinkedList<LinkedList<String>> createAll(){
        all_possible.clear();
        generateAll(tasks,8,0);
        return all_possible;
    }

    //REFERENCE: HEAP'S algorithm for finding all permutations
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
     * Narrows down the list of all possible solutions by the forced/ forbidden constraints
     * @param forced_partial
     * @param forbidden
     * @param all_possible
     * @author Elzanne Venter
     * @since February 2019
     */
     public LinkedList<LinkedList<String>> hardConstraints_assignments(LinkedList<LinkedList<String>> all_possible
    , LinkedList<Pair<Integer,String>> forced_partial, LinkedList<Pair<Integer,String>> forbidden){

      //forced assignments
      LinkedList<LinkedList<String>> newAllPossible = new LinkedList<LinkedList<String>>();


      boolean partialSatisfied = true;

      //check all assignments to see if they have all required forced pairs
      for (LinkedList<String> k : all_possible){
        for (Pair<Integer,String> pair : forced_partial){
          if (!(k.get(pair.getX()-1).equals(pair.getY()))){
            partialSatisfied = false;
          }
        }
        if (partialSatisfied == true) {
          newAllPossible.add(k); //adding an assignment to the narrowed down solutions if it contains the forced
        }
        else {
          partialSatisfied = true;
        }
      }

      //forbidden assignments
      LinkedList<LinkedList<String>> newAllPossible2 = new LinkedList<LinkedList<String>>();
      partialSatisfied = true;
      for (LinkedList<String> k : newAllPossible){
        for (Pair<Integer,String> pair : forbidden){
          if ((k.get(pair.getX()-1).equals(pair.getY()))){
            partialSatisfied = false;
          }
        }
        if (partialSatisfied == true) {
          newAllPossible2.add(k); //adding an assignment to the narrowed down solutions if it does not contain the forbidden
        }
        else {
          partialSatisfied = true;
        }
      }

      return newAllPossible2;

    }

    /**
     * Narrows down the list of all possible solutions by the too near tasks constraints
     * @param too_near
     * @param all_possible
     * @author Elzanne Venter
     * @since February 2019
     */
    public LinkedList<LinkedList<String>> hardConstraints_too_near(LinkedList<LinkedList<String>> all_possible,
    LinkedList<Pair<String,String>> too_near){


      LinkedList<LinkedList<String>> newAllPossible = new LinkedList<LinkedList<String>>();

      boolean partialSatisfied = true;

      for (int i = 0; i<all_possible.size();i++){
        for (int j = 0; j<too_near.size();j++){
          for (int k = 0; k<8; k++){
            if ((all_possible.get(i).get(k).equals(too_near.get(j).getX()))  &&
            (all_possible.get(i).get((k+1)%8).equals(too_near.get(j).getY()))){
              partialSatisfied = false;
            }
          }
        }

        if (partialSatisfied == true){
          newAllPossible.add(all_possible.get(i)); //adds to narrowed down list only if no too near tasks in assignment
        }

        else {
          partialSatisfied = true;
        }
      }


      return newAllPossible;


    }

    /**
     * calculates the quality of assignments based on soft constraints
     * @param assignment
     * @param machine_cost
     * @param tooNearPen
     * @author Elzanne Venter
     * @since February 2019
     */

    public int get_quality(LinkedList<String> assignment, LinkedList<LinkedList<Integer>> machine_cost,
    LinkedList<Pair<Pair<String,String>,Integer>> tooNearPen){
      int cost = 0;
      int task_number = -1;

      for (int i = 0; i< assignment.size() ; i++ ){
        //converting task into number for index of matrix

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


    /**
     * finds solution
     * @author Elzanne Venter
     * @since February 2019
     */
    public LinkedList<String> findSolution(){

         LinkedList<String> solution = new LinkedList<>();

         int index = -1;


         //create all possible solutions
         LinkedList<LinkedList<String>> all_solutions = createAll();

         //narrows down solution based on forced/ forbidden
         all_solutions = hardConstraints_assignments(all_solutions,
         forcedPartialAssignments, forbiddenMachines);

         //only do tooNearTasks if it isn't empty
         if (!tooNearTasks.isEmpty()){

           all_solutions = hardConstraints_too_near(all_solutions,tooNearTasks);
       }


        //return if no valid solution after hard constraints
         if (all_solutions.isEmpty()){
           return new LinkedList<String> ();
         }

         //find lowest penalty
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

        return solution;

       }


       //output of solution
       public String solutiontoString(LinkedList<String> solution){
         String output;
         if (!solution.isEmpty()){


         output = "Solution";

         for (int i = 0; i<solution.size();i++){
           output = output + " " + solution.get(i);
         }

         output = output + "; Quality: " + min_cost;
         return output;
       }

       else{
         output = "No valid solution possible!";
         return output;
       }

       }





}
