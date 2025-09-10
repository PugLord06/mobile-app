package com.tripbuddy.app.models;

public class Activity {
    private int id;
    private String name;
    private double cost;
    private boolean isSelected;
    private String category;

    public Activity() {
        this.isSelected = false;
    }

    public Activity(String name, double cost, String category) {
        this.name = name;
        this.cost = cost;
        this.category = category;
        this.isSelected = false;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}