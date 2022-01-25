package com.example.recepti.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Rate {
    //private int numberOfRates;
    private int mark;
    //private String recipe;
    private String recipeId;

    public Rate() {}

    public Rate(int mark, String recipeId) {
        //this.numberOfRates = numberOfRates;
        this.mark = mark;
        this.recipeId = recipeId;
    }

    /*public int getNumberOfRates() {
        return numberOfRates;
    }*/

    /*public void setNumberOfRates(int numberOfRates) {
        this.numberOfRates = numberOfRates;
    }*/

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }
}
