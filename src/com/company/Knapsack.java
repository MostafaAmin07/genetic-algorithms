package com.company;

import java.util.ArrayList;

public class Knapsack {
    public static Integer ComputeKnapsack(ArrayList<Item> items, Integer maxWeight) // Bottom-up, save maximum space
    {
        ArrayList<Integer> table = new ArrayList<>();
        for (int i = 0; i < maxWeight + 1; i++)
            table.add(0);
        for (int i = 0; i < items.size(); i++) {
            for (int j = maxWeight; j >= 0; j--) {
                int currentWeight = items.get(i).getWeight(),
                        currentValue = items.get(i).getValue();
                if (currentWeight <= j) {
                    int temp = currentValue + table.get(j - currentWeight);
                    if (temp > table.get(j)) {
                        table.set(j, temp);
                    }
                }
            }
        }
        return table.get(maxWeight);
    }

}
