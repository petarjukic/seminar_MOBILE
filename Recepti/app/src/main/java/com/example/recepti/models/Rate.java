package com.example.recepti.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Rate {
    private int numberOfRates;
    private float mark;
    private String recipe;

    public Rate(int numberOfRates, float mark, String recipe) {
        this.numberOfRates = numberOfRates;
        this.mark = mark;
        this.recipe = recipe;
    }

    public int getNumberOfRates() {
        return numberOfRates;
    }

    public void setNumberOfRates(int numberOfRates) {
        this.numberOfRates = numberOfRates;
    }

    public float getMark() {
        return mark;
    }

    public void setMark(float mark) {
        this.mark = mark;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }
}
