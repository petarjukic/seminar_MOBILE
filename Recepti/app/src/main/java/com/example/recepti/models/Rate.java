package com.example.recepti.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Rate {
    private int mark;
    private String user;
    private String recipeId;

    public Rate() {}

    public Rate(int mark, String recipeId, String user) {
        this.mark = mark;
        this.recipeId = recipeId;
        this.user = user;
    }

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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
