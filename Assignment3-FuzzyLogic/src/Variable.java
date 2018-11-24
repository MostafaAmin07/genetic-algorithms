import javafx.util.Pair;

import java.util.ArrayList;

public class Variable {
//    public enum Type{
//        Input,
//        Output
//    }
    private String name;
//    private Type type;
    private ArrayList<Pair<FuzzySet, Double>> fuzzySets;
    private Double crisp;

    public Variable() {
    }

    public Variable(String name,/* Type type,*/ ArrayList<Pair<FuzzySet, Double>> fuzzySets, Double crisp) {
        this.name = name;
//        type = type;
        this.fuzzySets = fuzzySets;
        this.crisp = crisp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public Type getType() {
//        return type;
//    }
//
//    public void setType(Type type) {
//        this.type = type;
//    }

    public ArrayList<Pair<FuzzySet, Double>> getFuzzySets() {
        return fuzzySets;
    }

    public void setFuzzySets(ArrayList<Pair<FuzzySet, Double>> fuzzySets) {
        this.fuzzySets = fuzzySets;
    }

    public Double getCrisp() {
        return crisp;
    }

    public void setCrisp(Double crisp) {
        this.crisp = crisp;
    }

    public void AddFuzzySet(FuzzySet fuzzySet){
        fuzzySets.add(new Pair<>(fuzzySet, fuzzySet.intersectionPoint(crisp)));
    }

    public Double getFuzzySetValue(String fuzzySet){
        Double value=0.0;
        for(int i=0;i<fuzzySets.size();i++){
            if(fuzzySets.get(i).getKey().getName().equals(fuzzySet)){
                value = fuzzySets.get(i).getValue();
            }
        }
        return value;
    }

    public Double getFuzzySetValue(String fuzzySet, Double value){
        Double centroid = 0.0;
        //TODO calculate centroid
        return value * centroid;
    }
}
