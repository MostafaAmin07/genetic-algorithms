public class Premiss {
    private Variable variable;
    private String fuzzySet;
    private Double value;

    public Premiss() {
    }

    public Premiss(Variable variable, String fuzzySet) {
        this.variable = variable;
        this.fuzzySet = fuzzySet;
        this.value = value;
    }

    public Variable getVariable() {
        return variable;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    public String getFuzzySet() {
        return fuzzySet;
    }

    public void setFuzzySet(String fuzzySet) {
        this.fuzzySet = fuzzySet;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(){
        value = variable.getFuzzySetValue(fuzzySet);
    }

    public void setValue(Double result, Boolean isOutputPremiss){
        if(isOutputPremiss)
            value = variable.getFuzzySetValue(fuzzySet, result);
        else
            value = result;
    }
}
