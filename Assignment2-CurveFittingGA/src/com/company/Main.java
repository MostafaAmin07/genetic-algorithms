package com.company;

import javafx.stage.StageStyle;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static ArrayList<Pair<Double, Double>> points;

    public static void main(String[] args) {
        points = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        Integer testCases = input.nextInt();
        Integer degree, numberOfPoints, index =1;
        Double x, y;
        GeneticsAlgorithm geneticAlgorithm = new GeneticsAlgorithm();
        while (testCases > 0) {
            numberOfPoints = input.nextInt();
            degree = input.nextInt();
            for (int i = 0; i < numberOfPoints; i++) {
                x = input.nextDouble();
                y = input.nextDouble();
                points.add(new Pair<>(x, y));
            }
            geneticAlgorithm.evolvePopulation(degree + 1);
            System.out.println("Case: " + index++);
            System.out.println(geneticAlgorithm.getBestSolution());
            System.out.println(geneticAlgorithm.getMinimumError());
            points.clear();
            testCases--;
        }
    }

//    public static void main(String[] args){
//        points = new ArrayList<>();
//        points.add(new Pair<>(2.0, 8.0));
//        points.add(new Pair<>(3.0, 27.0));
//        points.add(new Pair<>(4.0, 64.0));
//        ArrayList<Double> coff = new ArrayList<>();
//        coff.add(0.0);
//        coff.add(0.0);
//        coff.add(0.0);
//        coff.add(0.5);
//        System.out.println(calculateFitness(coff, 4));
//    }
//
//    private static Double calculateFitness(ArrayList<Double> chromosome, Integer coffNumber) {
//        Double value = Double.valueOf(1.0/Double.valueOf(points.size()));
//        ArrayList<Double> yCalculated = new ArrayList<>();
//        Double tempY, sum = 0.0;
//        for(int i=0;i<points.size();i++){
//            tempY = chromosome.get(0);
//            for(int j=1;j<coffNumber;j++){
//                tempY += chromosome.get(j) * Math.pow(points.get(i).getKey(), Double.valueOf(j));
//            }
//            yCalculated.add(tempY);
//        }
//        for(int i=0;i<points.size();i++){
//            sum += Math.pow(yCalculated.get(i) - points.get(i).getValue(), 2);
//        }
//        return value * sum;
//    }
}
