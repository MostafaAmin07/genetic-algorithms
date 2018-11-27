import java.util.ArrayList;
import java.util.Scanner;

public class Rule {
    public enum Operation {
        Or,
        And,
//        Not
    }

    private Integer numberOfPremises;
    private ArrayList<Premise> premises;
    private ArrayList<Operation> operations;
    private Premise result;

    public Rule() {
        premises = new ArrayList<>();
        operations = new ArrayList<>();
    }

    public Integer getNumberOfPremises() {
        return numberOfPremises;
    }

    public void setNumberOfPremises(Integer numberOfPremises) {
        this.numberOfPremises = numberOfPremises;
    }

    public ArrayList<Premise> getPremises() {
        return premises;
    }

    public void setPremises(ArrayList<Premise> premises) {
        this.premises = premises;
    }

    public ArrayList<Operation> getOperations() {
        return operations;
    }

    public void setOperations(ArrayList<Operation> operations) {
        this.operations = operations;
    }

    public Premise getResult() {
        return result;
    }

    public void setResult(Premise result) {
        this.result = result;
    }

    public void constructRule(ArrayList<Variable> inputVariables, Variable outputVariable, Scanner input) {
//        System.out.println("Number of Premises");
        //Index 0 is null because the string starts with space
        numberOfPremises = input.nextInt();
        Integer index = 1;
//        System.out.println("Rule");
        String[] rule = input.nextLine().split(" ");
//        for (int i=0;i<rule.length;i++)
//            System.out.println('('+rule[i]+')');
        for (int i = 0; i < numberOfPremises; i++) {
            Premise tempPremise = new Premise();
            /**search for variable name then = || != then fuzzy set name and get Value**/
//            System.out.println("Premise");
            for (int j = 0; j < inputVariables.size(); j++) {
//                System.out.println("->"+index+">"+rule[index]);
                if (inputVariables.get(j).getName().equals(rule[index])) {
                    tempPremise.setVariable(inputVariables.get(j));
                    index+=2;
                    tempPremise.setFuzzySet(rule[index]);
//                    System.out.println("->"+rule[index]);
                    tempPremise.setValue();
//                    System.out.println("->"+tempPremise.getValue());
                    if (rule[index-1].equals("!=")) {
                        tempPremise.setValue(1.0 - tempPremise.getValue());
                    }
                    break;
                }
            }
            premises.add(tempPremise);
            if (!rule[++index].toLowerCase().equals("then")) {
                if (rule[index].toLowerCase().equals("and")) {
                    operations.add(Operation.And);
                } else if (rule[index].toLowerCase().equals("or")) {
                    operations.add(Operation.Or);
                }
            }
//            System.out.println("----"+index);
            index++;
        }
        result = new Premise();
        //TODO Allow more than one output variable
        index++;
        //TODO delete above
        result.setVariable(outputVariable);
//        System.out.println("---->>"+index+">"+rule[index]);
        //TODO check if you need = || !=
        result.setFuzzySet(rule[index]);
    }

    public void calculateRule() {
        /**The Not operations are handled during the input phase**/
        ArrayList<Double> values = new ArrayList<>();
        ArrayList<Operation> tempOperations = new ArrayList<>();
        int i = 0;
        for (; i < premises.size(); i++){
            values.add(premises.get(i).getValue());
//            System.out.println("va "+values.get(i));
        }
        for (i = 0; i < operations.size(); i++)
            tempOperations.add(operations.get(i));
//        System.out.println("+++++++>"+tempOperations.size()+" "+values.size());
        for (i = 0; i < tempOperations.size(); i++) {
            if (tempOperations.get(i).equals(Operation.And)) {
                values.add(i, Math.min(values.get(i), values.get(i + 1)));
                values.remove(i + 1);
                values.remove(i + 1);
                tempOperations.remove(i--);
            }
        }
        for (i = 0; i < tempOperations.size(); i++) {
            if (tempOperations.get(i).equals(Operation.Or)) {
                values.add(i, Math.max(values.get(i), values.get(i + 1)));
                values.remove(i + 1);
                values.remove(i + 1);
                tempOperations.remove(i--);
            }
        }
        result.setValue(values.get(0));
    }

    public String getDefuzzify(){
        return (result.getValue() + " * ("+result.getVariable().getFuzzySetPair(result.getFuzzySet()).getKey().calculateCentroid()+")");
    }
}
