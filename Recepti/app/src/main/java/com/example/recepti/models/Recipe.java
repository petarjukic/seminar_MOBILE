package com.example.recepti.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Recipe {
    private String category;
    private String title;
    private String image;
    private int grade;
    private String description;
    private String user;

    public Recipe() {}

    public Recipe(String category, String title, String image, String description, String user) {
        this.category = category;
        this.title = title;
        this.image = image;
        this.description = description;
        this.user = user;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
