package com.company;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static ArrayList<Item> items;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Integer testCases = input.nextInt();
        Integer numberOfItems, maxWeight;
        items = new ArrayList<>();
        int index = 1;
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
        while (testCases > 0) {
            numberOfItems = input.nextInt();
            maxWeight = input.nextInt();
            for (int i = 0; i < numberOfItems; i++) {
                items.add(new Item(input.nextInt(), input.nextInt()));
            }
            geneticAlgorithm.evolvePopulation(maxWeight);
            System.out.println("Case: " + index++ + " " + geneticAlgorithm.getBestValue());
            items.clear();
            testCases--;
        }
    }
}
