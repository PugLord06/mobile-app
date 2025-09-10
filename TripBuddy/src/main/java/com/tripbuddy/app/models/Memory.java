package com.tripbuddy.app.models;

public class Memory {
    private int id;
    private int userId;
    private String imagePath;
    private String location;
    private String date;
    private String mood;
    private String musicPath;

    public Memory() {}

    public Memory(int userId, String imagePath, String location, String date, String mood, String musicPath) {
        this.userId = userId;
        this.imagePath = imagePath;
        this.location = location;
        this.date = date;
        this.mood = mood;
        this.musicPath = musicPath;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }
}