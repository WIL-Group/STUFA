package com.example.stufa.data_models;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

public class Announcement implements Comparator<Announcement>
{
    private  String id;
    private String title;
    private String message;
    private String date;
    private boolean viewed;
    private String staffName;

    public Announcement() { }

    public Announcement(String id, String title, String message, String date,
                        boolean viewed, String staffName) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.date = date;
        this.viewed = viewed;
        this.staffName = staffName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public static Comparator<Announcement> sort = new Comparator<Announcement>() {
        @Override
        public int compare(Announcement o1, Announcement o2) {
            return o2.getDate().compareTo(o1.getDate());
        }
    };

    @Override
    public int compare(Announcement o1, Announcement o2) {
        return o2.getDate().compareTo(o1.getDate());
    }

}
