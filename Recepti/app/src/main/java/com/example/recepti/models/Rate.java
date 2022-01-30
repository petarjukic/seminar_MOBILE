package com.example.recepti.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Rate {
    private int mark;
    private String user;
    private String recipeId;
    private String uid;

    public Rate() {}

    public Rate(String uid, int mark, String recipeId, String user) {
        this.uid = uid;
        this.mark = mark;
        this.recipeId = recipeId;
        this.user = user;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
