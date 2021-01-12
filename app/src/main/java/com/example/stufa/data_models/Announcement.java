package com.example.stufa.data_models;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Date;

public class Announcement implements Serializable
{
    private  String aId;
    private String title;
    private String message;
    private String date;
    private boolean viewed;
    private String key;

    public Announcement() {
    }

    public Announcement(String aId,String title, String message, String date, boolean viewed) {
        this.title = title;
        this.message = message;
        this.date = date;
        this.aId = aId;
        this.viewed = viewed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
