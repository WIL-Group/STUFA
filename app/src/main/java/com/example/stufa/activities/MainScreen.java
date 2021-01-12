package com.example.stufa.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stufa.R;

public class MainScreen extends AppCompatActivity {

    /*------------for calling the Login activity after 4 seconds------------*/
    final private static int SPLASH_SCREEN = 4000;

    /*-----------------Variables-------------------*/
    Animation topAnimation, bottomAnimation;
    ImageView ivLogo;
    TextView tvLogo, tvSlogan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_screen);

        /*----------------Animation Hooks---------------------*/
        topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        /*----------------ImageView and TextView Hooks---------------------*/
        ivLogo = findViewById(R.id.ivLogo);
        tvLogo = findViewById(R.id.tvLogo);
        tvSlogan = findViewById(R.id.tvSlogan);

        /*-----------------Assigning animations to my image and text------------------*/
        ivLogo.setAnimation(topAnimation);
        tvLogo.setAnimation(bottomAnimation);
        tvSlogan.setAnimation(bottomAnimation);

        /*----------or transitioning the content from main activity to login activity----------*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent (MainScreen.this, Login.class);
//                finish();

                Pair[] pairs = new Pair[3];
                pairs[0] = new Pair<View,String>(ivLogo, "logo_image");
                pairs[1] = new Pair<View,String>(tvLogo, "logo_text");
                pairs[2] = new Pair<View,String>(tvSlogan, "slogan_text");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainScreen.this, pairs);
                    startActivity(intent, options.toBundle());

                }
            }
        },SPLASH_SCREEN);

    }



}