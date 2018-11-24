import javafx.util.Pair;
import jdk.nashorn.internal.runtime.OptimisticReturnFilters;

import java.util.ArrayList;
import java.util.Scanner;

public class Rule {
    public enum Operation{
        Or,
        And,
        Not
    }
    private Integer numberOfPremisses;
    private ArrayList<Premiss> premisses;
    private ArrayList<Operation> operations;
    private Premiss result;

    public Rule(){
        //TODO initialization for all
    }

    public void calculateRule(){
        //The Not operations are handled during the input phase
        ArrayList<Double> values = new ArrayList<>();
        ArrayList<Operation> tempOperations = new ArrayList<>();
        int i=0;
        for(;i<premisses.size();i++)
            values.add(premisses.get(i).getValue());
        for (i=0;i<operations.size();i++)
            tempOperations.add(operations.get(i));
        for (i=0;i<tempOperations.size();i++){
            if(tempOperations.get(i).equals(Operation.And)){
                values.add(i ,Math.min(values.get(i), values.get(i+1)));
                values.remove(i+1);
                values.remove(i+2);
                tempOperations.remove(i--);
            }
        }
        for(i=0;i<tempOperations.size();i++){
            if(tempOperations.get(i).equals(Operation.Or)){
                values.add(i, Math.max(values.get(i), values.get(i+1)));
                values.remove(i+1);
                values.remove(i+2);
                tempOperations.remove(i--);
            }
        }
        result.setValue(values.get(0), true);
    }
//    public void constructRule(){
//        Scanner input = new Scanner(System.in);
//        numberOfPremisses = input.nextInt();
//        for(int i=0;i<numberOfPremisses;i++){
//
//            premisses.add(new Premiss())
//        }
//        input.close();
//    }
}
