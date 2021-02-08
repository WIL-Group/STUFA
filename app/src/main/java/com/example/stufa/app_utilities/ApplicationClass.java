package com.example.stufa.app_utilities;

import android.app.Application;

import com.example.stufa.activities.StudentHomePage;


public class ApplicationClass extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();

//        if(firebaseUser != null && firebaseUser.isEmailVerified())
//        {
//            startActivity(StudentHomePage.this, StudentHomePage.class);
//        }

    }
}
