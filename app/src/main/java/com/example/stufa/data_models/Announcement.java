package com.example.stufa.data_models;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

public class Announcement implements Comparator<Announcement>
{
    private  String Id;
    private String Title;
    private String Message;
    private String StaffName;
    private String Date;
    private boolean Viewed;
    private String key;

    public Announcement()
    {
    }

    public Announcement(String id,String Title, String Message, String StaffName, String Date, boolean Viewed) {
        this.Title = Title;
        this.Message = Message;
        this.Date = Date;
        this.Id = id;
        this.Viewed = Viewed;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getStaffName() {
        return StaffName;
    }

    public void setStaffName(String staffName) {
        StaffName = staffName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public boolean isViewed() {
        return Viewed;
    }

    public void setViewed(boolean viewed) {
        Viewed = viewed;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
