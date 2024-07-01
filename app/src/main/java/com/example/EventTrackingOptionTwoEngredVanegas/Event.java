package com.example.EventTrackingOptionTwoEngredVanegas;

public class Event {
    private int id;
    private String title;
    private String date;
    private String time;
    private String description;

    public Event(int id, String title, String date, String time, String description) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
        this.description = description;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {

        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
