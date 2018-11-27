import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Scanner;

public class Variable {
    private String name;
    private ArrayList<Pair<FuzzySet, Double>> fuzzySets;
    private Double crisp;

    public Variable() {
        fuzzySets = new ArrayList<>();
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

    public void constructVariable(Scanner input, Boolean isOutput){
//        System.out.println("Variable Name");
        name = input.next();
        if(!isOutput) {
//            System.out.println("Crisp Value");
            crisp = input.nextDouble();
        }
        else {
            crisp = 0.0;
        }
//        System.out.println("Number of Fuzzy sets");
        Integer numberOfFuzzySets = input.nextInt();
        for(int i=0;i<numberOfFuzzySets;i++){
            FuzzySet temp = new FuzzySet();
            temp.constructFuzzySet(input);
            if(!isOutput) {
                fuzzySets.add(new Pair<>(temp, temp.intersectionPoint(crisp)));
            }
            else{
                fuzzySets.add(new Pair<>(temp, 0.0));
            }
        }
    }

    public Double getFuzzySetValue(String fuzzySet){
        return getFuzzySetPair(fuzzySet).getValue();
    }

    public Double getFuzzySetValue(String fuzzySet, Double value){
        //TODO calculate centroid
        Pair<FuzzySet, Double> pairTempHolder = getFuzzySetPair(fuzzySet);
        Double centroid = pairTempHolder.getKey().calculateCentroid();
        return value * centroid;
    }

    public Pair<FuzzySet, Double> getFuzzySetPair(String fuzzySetName){
        for(int i=0;i<fuzzySets.size();i++){
            if(fuzzySets.get(i).getKey().getName().equals(fuzzySetName)){
                return fuzzySets.get(i);
            }
        }
        return null;
    }

//    @Override
//    public String toString() {
//        String fuzzification = name +" "+ crisp + "\n\t";
//        for(int i=0;i<fuzzySets.size();i++)
//            fuzzification += (fuzzySets.get(i).getKey().toString() + "\n\tValue" + fuzzySets.get(i).getValue() + "\n\t");
//        return fuzzification;
//    }
}
