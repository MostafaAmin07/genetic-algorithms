package com.company;

import javafx.util.Pair;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class GeneticsAlgorithm {
    private ArrayList<ArrayList<Double>> population;
    private Random random;
    private Integer minimumErrorIndex;
    private Double minimumError;
    private double[] xData, yData;
    private static final int UPPER_BOUND = 10, LOWER_BOUND = -10;
    private static final int POPULATION_SIZE = 50;
    private static final int MAX_GENERATION = 1000;
    private static final double B = 1.5;//dependency factor .5 -> 5
    private static final double CROSSOVER_PROBABILITY = .7;//.4->.7
    private static final double MUTATION_PROBABILITY = .01;//.001->.1
//    private static final int FITNESS_CONSTANT = 1;

    GeneticsAlgorithm() {
        random = new Random();
        population = new ArrayList<>();
        minimumError = Double.MAX_VALUE;
        minimumErrorIndex = -1;
        xData = new double[MAX_GENERATION];
        yData = new double[MAX_GENERATION];
    }

    public Double getMinimumError() {
        return minimumError;
    }

    public ArrayList<Double> getBestSolution() {
        return population.get(minimumErrorIndex);
    }

    public void evolvePopulation(Integer numberOfCoff) {
//generate random Population
        generateRandomPopulation(numberOfCoff);
        for (int i = 0; i < MAX_GENERATION; i++) {
//            System.out.println(population);
            //calculate fitness
            ArrayList<Double> fitnessValues = getFitness(numberOfCoff);
//            System.out.println(fitnessValues);
            //selection
            ArrayList<ArrayList<Double>> selected = selectParents(fitnessValues);
//            System.out.println("->"+selected);
            //crossover
//            ArrayList<ArrayList<Double>> newPopulation = applyCrossover(selected);
            ArrayList<ArrayList<Double>> newPopulation = applyCrossover(selected, numberOfCoff);
            //mutation
            applyMutation(newPopulation, i);
//            System.out.println(newPopulation);
            population = newPopulation;
            yData[i] = Collections.max(getFitness(numberOfCoff));
            xData[i] = i + 1;
        }
        ArrayList<Double> fitnessValues = getFitness(numberOfCoff);
//        System.out.println(population);
        minimumError = Collections.max(fitnessValues);
        minimumErrorIndex = fitnessValues.indexOf(minimumError);
        XYChart chart = QuickChart.getChart("Evolution", "population number", "max value", null, xData, yData);
        new SwingWrapper(chart).displayChart();
    }

    private void generateRandomPopulation(Integer numberOfCoff) {
        population.clear();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add(generateRandomChromosome(numberOfCoff));
        }
    }

    private ArrayList<Double> generateRandomChromosome(Integer numberOfCoff) {
        ArrayList<Double> chromosome = new ArrayList<>();
        int choice;
        for (int i = 0; i < numberOfCoff; i++) {
            choice = random.nextDouble() > .5 ? 1 : -1;
            chromosome.add(Double.valueOf(choice * random.nextInt(11)));
//            chromosome.add(choice * 10 * random.nextDouble() + choice);
        }
        return chromosome;
    }

    private ArrayList<Double> getFitness(Integer coffNumber) {
        ArrayList<Double> fitnessValues = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            fitnessValues.add(calculateFitness(population.get(i), coffNumber));
        }
        return fitnessValues;
    }

    private Double calculateFitness(ArrayList<Double> chromosome, Integer coffNumber) {
        Double value = Double.valueOf(1.0 / Double.valueOf(Main.points.size()));
        ArrayList<Double> yCalculated = new ArrayList<>();
        Double tempY, sum = 0.0;
        for (int i = 0; i < Main.points.size(); i++) {
            tempY = chromosome.get(0);
            for (int j = 1; j < coffNumber; j++) {
                tempY += chromosome.get(j) * Math.pow(Main.points.get(i).getKey(), Double.valueOf(j));
            }
            yCalculated.add(tempY);
        }
        for (int i = 0; i < Main.points.size(); i++) {
            sum += Math.pow(yCalculated.get(i) - Main.points.get(i).getValue(), 2);
        }
        return value * sum;
    }

    private ArrayList<ArrayList<Double>> selectParents(ArrayList<Double> fitnessValues) {
        ArrayList<ArrayList<Double>> selected = new ArrayList<>();
        ArrayList<Integer> cumulativeFitness = new ArrayList<>();
        cumulativeFitness.add(0);
        for (int i = 0; i < fitnessValues.size(); i++) {
            cumulativeFitness.add((int) (cumulativeFitness.get(i) + fitnessValues.get(i)));
        }
//        System.out.println("-->"+ cumulativeFitness);
        int maxValue = Collections.max(cumulativeFitness), selectionVariable;
        for (int i = 0; i < POPULATION_SIZE; i++) {
            selectionVariable = random.nextInt(maxValue);
            for (int j = 1; j < cumulativeFitness.size(); j++) {
                if (selectionVariable >= cumulativeFitness.get(j - 1) && selectionVariable < cumulativeFitness.get(j)) {
                    selected.add(population.get(j-1));
                    break;
                }
            }
        }
        return selected;
    }

    //choosing the best 2 of the parents and their children
    private ArrayList<ArrayList<Double>> applyCrossover(ArrayList<ArrayList<Double>> parents, Integer numberOfCoff) {
        ArrayList<ArrayList<Double>> newPopulation = new ArrayList<>();
        int crossoverPoint;
        for (int i = 0; i < POPULATION_SIZE - 1; i += 2) {
            crossoverPoint = random.nextInt(parents.size() - 2) + 1;
            if (random.nextDouble() <= CROSSOVER_PROBABILITY) {
                ArrayList<Double> firstChild = new ArrayList<>();
                ArrayList<Double> secondChild = new ArrayList<>();
                formulateChildren(parents, i, firstChild, secondChild, numberOfCoff, crossoverPoint);
                ArrayList<Pair<ArrayList<Double>, Double>> pickupLine = new ArrayList<>();
                pickupLine.add(new Pair<>(firstChild, calculateFitness(firstChild, numberOfCoff)));
                pickupLine.add(new Pair<>(secondChild, calculateFitness(secondChild, numberOfCoff)));
                pickupLine.add(new Pair<>(parents.get(i), calculateFitness(parents.get(i), numberOfCoff)));
                pickupLine.add(new Pair<>(parents.get(i + 1), calculateFitness(parents.get(i + 1), numberOfCoff)));
                Collections.sort(pickupLine, new Comparator<Pair<ArrayList<Double>, Double>>() {
                    @Override
                    public int compare(Pair<ArrayList<Double>, Double> o1, Pair<ArrayList<Double>, Double> o2) {
                        if (o1.getValue() > o2.getValue()) {
                            return 1;
                        } else if (o1.getValue().equals(o2.getValue())) {
                            return 0;
                        } else {
                            return -1;
                        }
                    }
                });
                newPopulation.add(pickupLine.get(0).getKey());
                newPopulation.add(pickupLine.get(1).getKey());
            } else {
                newPopulation.add(parents.get(i));
                newPopulation.add(parents.get(i + 1));
            }
        }
        if (newPopulation.size() < POPULATION_SIZE) {
            newPopulation.add(population.get(POPULATION_SIZE - 1));
        }
        return newPopulation;
    }

    private void formulateChildren(ArrayList<ArrayList<Double>> parents, Integer parentIndex, ArrayList<Double> firstChild, ArrayList<Double> secondChild, Integer chromosomeSize, Integer crossoverPoint) {
        for (int j = 0; j < chromosomeSize; j++) {
            if (j < crossoverPoint) {
                firstChild.add(parents.get(parentIndex).get(j));
                secondChild.add(parents.get(parentIndex + 1).get(j));
            } else {
                firstChild.add(parents.get(parentIndex + 1).get(j));
                secondChild.add(parents.get(parentIndex).get(j));
            }
        }
    }

    private void applyMutation(ArrayList<ArrayList<Double>> newPopulation, int currentGeneration) {
        for (int i = 0; i < newPopulation.size(); i++) {
            for (int j = 0; j < newPopulation.get(i).size(); j++) {
                double delta;
                if (random.nextBoolean())
                    delta = UPPER_BOUND - newPopulation.get(i).get(j);
                else
                    delta = newPopulation.get(i).get(j) - LOWER_BOUND;
                double power = Math.pow(1 - currentGeneration / MAX_GENERATION, B);
                newPopulation.get(i).set(j, delta * (1 - Math.pow(random.nextDouble(), power)));
            }
        }
    }

}
