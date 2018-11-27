public class Premise {
    private Variable variable;
    private String fuzzySet;
    private Double value;

    public Premise() {
    }

    public Premise(Variable variable, String fuzzySet) {
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

    public Double getValueForOutput() {
        return variable.getFuzzySetValue(fuzzySet, value);
    }

    /**
     * For inputPremisses when having no initial value
     **/
    public void setValue() {
        value = variable.getFuzzySetValue(fuzzySet);
    }

    /**
     * For input and outputPremisses using the value result
     **/
    public void setValue(Double result) {
        value = result;
    }

    @Override
    public String toString() {
        return variable.getName()+" = "+value+" "+fuzzySet;
    }
}
