package com.company;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;

import java.util.ArrayList;
import java.util.Collections;
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

    public void evolvePopulation(ArrayList<Item> items, Integer maxWeight) {
        int bestPossibleValue = Knapsack.ComputeKnapsack(items, maxWeight);
        for (int i = 0; i < MAX_GENERATION; i++)
            yData2[i] = bestPossibleValue;
        //generate random Population
        generateRandomPopulation(items.size());
        for (int i = 0; i < MAX_GENERATION; i++) {
//            System.out.println(population);
            //calculate fitness
            ArrayList<Integer> fitnessValues = getFitness(items, maxWeight);
//            System.out.println(fitnessValues);
            //selection
            ArrayList<ArrayList<Boolean>> selected = selectParents(fitnessValues);
            //crossover
            ArrayList<ArrayList<Boolean>> newPopulation = applyCrossover(selected);
            //mutation
            applyMutation(newPopulation);
//            System.out.println(newPopulation);
            population = newPopulation;
            yData[i] = Collections.max(getFitness(items, maxWeight));
            xData[i] = i + 1;
        }
        ArrayList<Integer> fitnessValues = getFitness(items, maxWeight);
//        System.out.println(population);
        bestValue = Collections.max(fitnessValues);
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

    private ArrayList<Integer> getFitness(ArrayList<Item> items, Integer maxWeight) {
        ArrayList<Integer> fitnessValues = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            fitnessValues.add(calculateFitness(items, population.get(i), maxWeight));
        }
        return fitnessValues;
    }

    private Integer calculateFitness(ArrayList<Item> items, ArrayList<Boolean> chromosome, Integer maxWeight) {
        Integer value = 0, weight = 0;
        for (int i = 0; i < chromosome.size(); i++) {
            if (chromosome.get(i)) {
                value += items.get(i).getValue();
                weight += items.get(i).getWeight();
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
            if (maxValue > 0) {
                selectionVariable = random.nextInt(maxValue);
                for (int j = 1; j < cumulativeFitness.size(); j++) {
                    if (selectionVariable >= cumulativeFitness.get(j - 1) && selectionVariable < cumulativeFitness.get(j)) {
                        selected.add(population.get(i));
                    }
                }
            } else {
                selected = population;
                break;
            }
        }
        return selected;
    }

    private ArrayList<ArrayList<Boolean>> applyCrossover(ArrayList<ArrayList<Boolean>> parents) {
        ArrayList<ArrayList<Boolean>> newPopulation = new ArrayList<>();
        int crossoverPoint, chromosomeSize = parents.get(0).size();
        for (int i = 0; i < POPULATION_SIZE - 1; i += 2) {
            crossoverPoint = random.nextInt(parents.size() - 2) + 1;
            if (random.nextDouble() <= CROSSOVER_PROBABILITY) {
                ArrayList<Boolean> firstChild = new ArrayList<>();
                ArrayList<Boolean> secondChild = new ArrayList<>();
                for (int j = 0; j < chromosomeSize; j++) {
                    if (j < crossoverPoint) {
                        firstChild.add(parents.get(i).get(j));
                        secondChild.add(parents.get(i + 1).get(j));
                    } else {
                        firstChild.add(parents.get(i + 1).get(j));
                        secondChild.add(parents.get(i).get(j));
                    }
                }
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
