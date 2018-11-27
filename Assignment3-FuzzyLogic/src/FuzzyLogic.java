import java.util.ArrayList;
import java.util.Scanner;

public class FuzzyLogic {
    private ArrayList<Variable> inputVariables;
    private ArrayList<Rule> rules;
    private Variable outputVariable;

    public FuzzyLogic() {
        inputVariables = new ArrayList<>();
        rules = new ArrayList<>();
    }

    public FuzzyLogic(ArrayList<Variable> inputVariables, ArrayList<Rule> rules, Variable outputVariable) {
        this.inputVariables = inputVariables;
        this.rules = rules;
        this.outputVariable = outputVariable;
    }

    public ArrayList<Variable> getInputVariables() {
        return inputVariables;
    }

    public void setInputVariables(ArrayList<Variable> inputVariables) {
        this.inputVariables = inputVariables;
    }

    public ArrayList<Rule> getRules() {
        return rules;
    }

    public void setRules(ArrayList<Rule> rules) {
        this.rules = rules;
    }

    public Variable getOutputVariable() {
        return outputVariable;
    }

    public void setOutputVariable(Variable outputVariable) {
        this.outputVariable = outputVariable;
    }

    public void constructProblem(Scanner input){
//        System.out.println("Number of Input Variables");
        Integer numberOfVariables = input.nextInt();
        for(int i=0;i<numberOfVariables;i++){
            inputVariables.add(new Variable());
            inputVariables.get(i).constructVariable(input, false);
        }
        outputVariable = new Variable();
        outputVariable.constructVariable(input, true);
//        System.out.println("Number of Rules");
        Integer numberOfRules = input.nextInt();
        for(int i=0;i<numberOfRules;i++){
            Rule temp = new Rule();
            temp.constructRule(inputVariables, outputVariable ,input);
            rules.add(temp);
        }
    }

    public void predict(){
        Double value=0.0, denomerator=0.0;
        //Fuzzification output
        System.out.println(",,,,,,,,,,,,,Fuzzification,,,,,,,,,,,,,,");
        for (int i=0;i<inputVariables.size();i++){
            System.out.println(inputVariables.get(i).getName());
            for (int j=0;j<inputVariables.get(i).getFuzzySets().size();j++){
                System.out.println(inputVariables.get(i).getFuzzySets().get(j).getKey().getName() +" "+
                                    inputVariables.get(i).getFuzzySets().get(j).getValue());
            }
        }
        System.out.println(",,,,,,,,,,,,,Rule inference,,,,,,,,,,,,,,");
        String defuzzificationValue = "", denomenatorValue = "";
        for(int i=0;i<rules.size();i++){
            rules.get(i).calculateRule();
            for(int j=0;j<rules.get(i).getNumberOfPremises();j++){
                System.out.println(rules.get(i).getPremises().get(j).toString()+ " " +
                        ((j < rules.get(i).getNumberOfPremises()-1) ? rules.get(i).getOperations().get(j).toString(): ""));
            }
            System.out.println("then");
            System.out.println(rules.get(i).getResult().toString());
            defuzzificationValue += rules.get(i).getDefuzzify() + ((i<rules.size()-1)?" + ":"");
            denomenatorValue += rules.get(i).getResult().getValue()+((i<rules.size()-1)?" + ":"");
            value+=rules.get(i).getResult().getValueForOutput();
            denomerator+=rules.get(i).getResult().getValue();
        }
        System.out.println(",,,,,,,,,,,,,Defuzzification,,,,,,,,,,,,,,");
        System.out.println(defuzzificationValue);
        System.out.println("____________________");
        System.out.println(denomenatorValue);
        System.out.println("-->"+value/denomerator);
    }
}
