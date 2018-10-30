package com.company;

import javafx.stage.StageStyle;
import javafx.util.Pair;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;

import java.util.ArrayList;
import java.util.Collections;
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
//            plot(geneticAlgorithm.getBestSolution());
//            ArrayList<Double> coff = new ArrayList<>();
//            coff.add(0.429163);
//            coff.add(1.18487);
//            coff.add(-0.717967);
//            coff.add(0.0854301);
            System.out.println(calculateFitness(geneticAlgorithm.getBestSolution(), 4));
            points.clear();
            testCases--;
        }
    }
    
    private static void plot(ArrayList<Double> coff){
        int points_size = points.size();
        double[] xPoints = new double[points_size], yPoints = new double[points_size],
                    yData= new double[points_size];
        for(int i=0;i<points_size;i++){
            xPoints[i] = points.get(i).getKey();
            yPoints[i] = points.get(i).getValue();
            yData[i] = coff.get(0);
            for(int j=1;j<coff.size();j++){
                yData[i] += coff.get(j) * Math.pow(points.get(i).getKey(), j);
            }
        }
        XYChart chart = QuickChart.getChart("Equation", "x", "y", "points", xPoints, yPoints);
        chart.addSeries("Generated", yData);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        new SwingWrapper(chart).displayChart();
    }
    private static Double calculateFitness(ArrayList<Double> chromosome, Integer coffNumber) {
        Double value = Double.valueOf(1.0/Double.valueOf(points.size()));
        ArrayList<Double> yCalculated = new ArrayList<>();
        Double tempY, sum = 0.0;
        for(int i=0;i<points.size();i++){
            tempY = chromosome.get(0);
            for(int j=1;j<coffNumber;j++){
                tempY += chromosome.get(j) * Math.pow(points.get(i).getKey(), Double.valueOf(j));
            }
            yCalculated.add(tempY);
        }
        for(int i=0;i<points.size();i++){
            sum += Math.pow(yCalculated.get(i) - points.get(i).getValue(), 2);
        }
        return 1.0 /( value * sum);
    }
}
