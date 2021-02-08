package com.example.stufa.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stufa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ForgotPassword extends AppCompatActivity {

    /*---------------Variables-------------------*/
    EditText etResetEmail;
    Button btnSubmit;

    View progressBarLayout, contentLayout;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot_password);

        /*------------------EditText and Button Hooks-----------------------*/
        etResetEmail = findViewById(R.id.etResetEmail);
        btnSubmit = findViewById(R.id.btnSubmit);

        /*------------------Progressbar layout Hooks-----------------------*/
        progressBarLayout = findViewById(R.id.progressBarLayout);
        contentLayout = findViewById(R.id.contentLayout);

        /*------------------Firebase authentication Hooks-----------------------*/
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        btnSubmit.setOnClickListener(v -> {

            //test if the user entered a email address or not
            if(etResetEmail.getText().toString().isEmpty())
            {
                Toast.makeText(ForgotPassword.this, getString(R.string.please_enter_your_email_address), Toast.LENGTH_SHORT).show();
            }
            else
            {
                //hides the progressbar and displays the content layout of Student Home Activity
                progressBarLayout.setVisibility(View.VISIBLE);
                contentLayout.setVisibility(View.GONE);
                //sent a request to firebase to reset the users password
                firebaseAuth.sendPasswordResetEmail(etResetEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        //if task of sending 'forgot password' request to firebase is successful,
                        //- progressbar will disappear
                        //- Student Homepage content will appear
                        //a toast will appear stating that the password is sent successfully to the users email address
                        if(task.isSuccessful())
                        {
                            progressBarLayout.setVisibility(View.GONE);
                            contentLayout.setVisibility(View.VISIBLE);
                            Toast.makeText(ForgotPassword.this, getString
                                            (R.string.password_successfully_sent_to_your_email_address),
                                    Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            //if task of sending 'forgot password' request to firebase is unsuccessful,
                            //- progressbar will disappear
                            //- Student Homepage content will appear
                            //a toast will appear stating that the password not sent to the users email address stating reason why
                            progressBarLayout.setVisibility(View.GONE);
                            contentLayout.setVisibility(View.VISIBLE);
                            Toast.makeText(ForgotPassword.this, getString
                                            (R.string.error) + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        });
    }

    //for when the user presses the back button in current activity, the activity finishes and goes back to login activity
    @Override
    public void onBackPressed() {

        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();

        super.onBackPressed();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.forgot_password, menu);
//
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        if (item.getItemId() == R.id.back) {
//            startActivity(new Intent(getApplicationContext(), Login.class));
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}