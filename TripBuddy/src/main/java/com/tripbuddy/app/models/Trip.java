package com.tripbuddy.app.models;

import java.util.ArrayList;
import java.util.List;

public class Trip {
    private int id;
    private int userId;
    private String destination;
    private String startDate;
    private String endDate;
    private String notes;
    private double totalCost;
    private List<Activity> activities;
    private List<CustomExpense> customExpenses;
    private double discount;

    public Trip() {
        activities = new ArrayList<>();
        customExpenses = new ArrayList<>();
        discount = 0.0;
    }

    public static class CustomExpense {
        private String name;
        private double cost;

        public CustomExpense(String name, double cost) {
            this.name = name;
            this.cost = cost;
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
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<CustomExpense> getCustomExpenses() {
        return customExpenses;
    }

    public void setCustomExpenses(List<CustomExpense> customExpenses) {
        this.customExpenses = customExpenses;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double calculateTotal() {
        double total = 0;
        for (Activity activity : activities) {
            if (activity.isSelected()) {
                total += activity.getCost();
            }
        }
        for (CustomExpense expense : customExpenses) {
            total += expense.getCost();
        }
        return total;
    }

    public double calculateFinalTotal() {
        double total = calculateTotal();
        return total - (total * discount / 100);
    }
}