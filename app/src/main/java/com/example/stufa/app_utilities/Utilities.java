package com.example.stufa.app_utilities;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.stufa.data_models.Announcement;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Utilities
{
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static ArrayList<Announcement> DataCache = new ArrayList<>();


    public static void show(Context c, String message){
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
    }
    //This method will validate all the edit texts
    public static boolean validate(EditText... editTexts){

        return true;

    }
    //this method is for opening up a new sctivity

    public static void openActivity(Context c,Class clazz){
        Intent intent = new Intent(c, clazz);
        c.startActivity(intent);
    }

    //Converting string to date
    public static Date giveMeDate(String stringDate){
        try {
            SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT);
            return sdf.parse(stringDate);
        }catch ( ParseException e){
            e.printStackTrace();
            return null;
        }
    }

    // Sending Serialized Object(announcement) to another activity
    public static void sendAnnouncementToActivity(Context c, Announcement announcement,
                                               Class clazz){
        Intent i=new Intent(c,clazz);
        i.putExtra("ANNOUNCEMENT_KEY",announcement);
        c.startActivity(i);
    }

    //Receiving a Serialized Object(Announcement) then deserializing it
    public  static Announcement receiveAnnouncement(Intent intent, Context c){
        try {
            return (Announcement) intent.getSerializableExtra("ANNOUNCEMENT_KEY");
        }catch (Exception e){
            e.printStackTrace();
            show(c,"RECEIVING-ANNOUNCEMENT ERROR: "+e.getMessage());
        }
        return null;
    }

    //This is for showing or hiding the progress bar if need be
    public static void showProgressBar(ProgressBar pb){
        pb.setVisibility(View.VISIBLE);
    }
    public static void hideProgressBar(ProgressBar pb){
        pb.setVisibility(View.GONE);
    }

    //getting a firebase database reference
    public static DatabaseReference getDatabaseRefence() {
        return FirebaseDatabase.getInstance().getReference();
    }
}
