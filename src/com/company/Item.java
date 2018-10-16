package com.company;

public class Item {
    private Integer weight;
    private Integer value;

    public Item() {
    }

    public Item(Integer weight, Integer value) {
        this.weight = weight;
        this.value = value;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
