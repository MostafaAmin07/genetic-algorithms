package com.company;

import javafx.util.Pair;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class GeneticAlgorithm {
    private ArrayList<ArrayList<Boolean>> population;
    private double[] xData, yData, yData2;
    private Random random;
    private Integer bestValue, bestValueIndex;
    private static final int POPULATION_SIZE = 500;
    private static final int MAX_GENERATION = 10000;
    private static final double CROSSOVER_PROBABILITY = .7;//.4->.7
    private static final double MUTATION_PROBABILITY = .01;//.001->.1
    private static final int FITNESS_CONSTANT = 1;

    public GeneticAlgorithm() {
        xData = new double[MAX_GENERATION];
        yData = new double[MAX_GENERATION];
        yData2 = new double[MAX_GENERATION];
        random = new Random();
//        random.setSeed(747);
        population = new ArrayList<>();
        bestValue = 0;
        bestValueIndex = -1;
    }

    public void evolvePopulation(Integer maxWeight) {
        int bestPossibleValue = Knapsack.ComputeKnapsack(Main.items, maxWeight);
        for (int i = 0; i < MAX_GENERATION; i++)
            yData2[i] = bestPossibleValue;
        //generate random Population
        generateRandomPopulation(Main.items.size());
        for (int i = 0; i < MAX_GENERATION; i++) {
//            System.out.println(population);
            //calculate fitness
            ArrayList<Integer> fitnessValues = getFitness(maxWeight);
//            System.out.println(fitnessValues);
            //selection
            ArrayList<ArrayList<Boolean>> selected = selectParents(fitnessValues);
            //crossover
//            ArrayList<ArrayList<Boolean>> newPopulation = applyCrossover(selected);
            ArrayList<ArrayList<Boolean>> newPopulation = applyCrossover(selected, maxWeight);
            //mutation
            applyMutation(newPopulation);
//            System.out.println(newPopulation);
            population = newPopulation;
            yData[i] = Collections.max(getFitness(maxWeight)) - FITNESS_CONSTANT;
            xData[i] = i + 1;
        }
        ArrayList<Integer> fitnessValues = getFitness(maxWeight);
//        System.out.println(population);
        bestValue = Collections.max(fitnessValues) - FITNESS_CONSTANT;
        bestValueIndex = fitnessValues.indexOf(bestValue);
//        XYChart chart = QuickChart.getChart("Evolution", "population number", "max value", null, xData, yData);
        //new SwingWrapper(chart).displayChart();
        XYChart chart = QuickChart.getChart("Evolution", "population number", "max value", null, xData, yData);
        chart.addSeries("MAX POSSIBLE", yData2);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        new SwingWrapper(chart).displayChart();
    }

    public Integer getBestValue() {
        return bestValue;
    }

    public ArrayList<Boolean> getBestSolution() {
        return population.get(bestValueIndex);
    }

    private void generateRandomPopulation(Integer chromosomeSize) {
        population.clear();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add(generateRandomChromosome(chromosomeSize));
        }
    }

    private ArrayList<Boolean> generateRandomChromosome(Integer chromosomeSize) {
        ArrayList<Boolean> chromosome = new ArrayList<>();
        for (int i = 0; i < chromosomeSize; i++) {
            chromosome.add(random.nextBoolean());
        }
        return chromosome;
    }

    private ArrayList<Integer> getFitness(Integer maxWeight) {
        ArrayList<Integer> fitnessValues = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            fitnessValues.add(calculateFitness(population.get(i), maxWeight));
        }
        return fitnessValues;
    }

    private Integer calculateFitness(ArrayList<Boolean> chromosome, Integer maxWeight) {
        Integer value = 0, weight = 0;
        for (int i = 0; i < chromosome.size(); i++) {
            if (chromosome.get(i)) {
                value += Main.items.get(i).getValue();
                weight += Main.items.get(i).getWeight();
            }
        }
        if (weight > maxWeight) {
            return FITNESS_CONSTANT;
        }
        return value + FITNESS_CONSTANT;
    }

    private ArrayList<ArrayList<Boolean>> selectParents(ArrayList<Integer> fitnessValues) {
        ArrayList<ArrayList<Boolean>> selected = new ArrayList<>();
        ArrayList<Integer> cumulativeFitness = new ArrayList<>();
        cumulativeFitness.add(0);
        for (int i = 0; i < fitnessValues.size(); i++) {
            cumulativeFitness.add(cumulativeFitness.get(i) + fitnessValues.get(i));
        }
        int maxValue = Collections.max(cumulativeFitness), selectionVariable;
        for (int i = 0; i < POPULATION_SIZE; i++) {
            selectionVariable = random.nextInt(maxValue);
            for (int j = 1; j < cumulativeFitness.size(); j++) {
                if (selectionVariable >= cumulativeFitness.get(j - 1) && selectionVariable < cumulativeFitness.get(j)) {
                    selected.add(population.get(i));
                }
            }
        }
        return selected;
    }

    //Standard method (children replace parents)
    private ArrayList<ArrayList<Boolean>> applyCrossover(ArrayList<ArrayList<Boolean>> parents) {
        ArrayList<ArrayList<Boolean>> newPopulation = new ArrayList<>();
        int crossoverPoint, chromosomeSize = parents.get(0).size();
        for (int i = 0; i < POPULATION_SIZE - 1; i += 2) {
            crossoverPoint = random.nextInt(parents.size() - 2) + 1;
            if (random.nextDouble() <= CROSSOVER_PROBABILITY) {
                ArrayList<Boolean> firstChild = new ArrayList<>();
                ArrayList<Boolean> secondChild = new ArrayList<>();
                formulateChildren(parents, i, firstChild, secondChild, chromosomeSize, crossoverPoint);
                newPopulation.add(firstChild);
                newPopulation.add(secondChild);
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

    //choosing the best 2 of the parents and their children
    private ArrayList<ArrayList<Boolean>> applyCrossover(ArrayList<ArrayList<Boolean>> parents, Integer maxWeight) {
        ArrayList<ArrayList<Boolean>> newPopulation = new ArrayList<>();
        int crossoverPoint, chromosomeSize = parents.get(0).size();
        for (int i = 0; i < POPULATION_SIZE - 1; i += 2) {
            crossoverPoint = random.nextInt(parents.size() - 2) + 1;
            if (random.nextDouble() <= CROSSOVER_PROBABILITY) {
                ArrayList<Boolean> firstChild = new ArrayList<>();
                ArrayList<Boolean> secondChild = new ArrayList<>();
                formulateChildren(parents, i, firstChild, secondChild, chromosomeSize, crossoverPoint);
                ArrayList<Pair<ArrayList<Boolean>, Integer>> pickupLine = new ArrayList<>();
                pickupLine.add(new Pair<>(firstChild, calculateFitness(firstChild, maxWeight)));
                pickupLine.add(new Pair<>(secondChild, calculateFitness(secondChild, maxWeight)));
                pickupLine.add(new Pair<>(parents.get(i), calculateFitness(parents.get(i), maxWeight)));
                pickupLine.add(new Pair<>(parents.get(i + 1), calculateFitness(parents.get(i + 1), maxWeight)));
                Collections.sort(pickupLine, new Comparator<Pair<ArrayList<Boolean>, Integer>>() {
                    @Override
                    public int compare(Pair<ArrayList<Boolean>, Integer> o1, Pair<ArrayList<Boolean>, Integer> o2) {
                        if (o1.getValue() > o2.getValue()) {
                            return -1;
                        } else if (o1.getValue().equals(o2.getValue())) {
                            return 0;
                        } else {
                            return 1;
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

    private void formulateChildren(ArrayList<ArrayList<Boolean>> parents, Integer parentIndex, ArrayList<Boolean> firstChild, ArrayList<Boolean> secondChild, Integer chromosomeSize, Integer crossoverPoint) {
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

    private void applyMutation(ArrayList<ArrayList<Boolean>> newPopulation) {
        for (int i = 0; i < newPopulation.size(); i++) {
            for (int j = 0; j < newPopulation.get(i).size(); j++) {
                if (random.nextDouble() < MUTATION_PROBABILITY) {
                    newPopulation.get(i).set(j, !newPopulation.get(i).get(j));
                }
            }
        }
    }
}
