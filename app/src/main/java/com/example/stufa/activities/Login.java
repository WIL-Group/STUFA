package com.example.stufa.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stufa.R;
import com.example.stufa.app_utilities.Utilities;
import com.example.stufa.data_models.Announcement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Login extends AppCompatActivity {

    /*-----------------Variables--------------------*/
    EditText etEmail, etPassword;
    Button btnLogin;
    ImageView ivLogo;
    TextInputLayout tilUserName, tilEnterYourPassword;
    TextView tvCreateAccount, tvForgotPassword, tvLogo, tvSlogan;
    View progressBarLayout, contentLayout;

    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    DatabaseReference databaseReference;
    ArrayList<Announcement> announcements;
    Announcement announcement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        /*-----------------Hooks--------------------*/
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvCreateAccount = findViewById(R.id.tvCreateAccount);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tilUserName = findViewById(R.id.tilUserName);
        tilEnterYourPassword = findViewById(R.id.tilEnterYourPassword);
        tvLogo = findViewById(R.id.tvLogo);
        tvSlogan = findViewById(R.id.tvSlogan);
        ivLogo = findViewById(R.id.ivLogo);

        announcements = new ArrayList<>();

        progressBarLayout = findViewById(R.id.progressBarLayout);
        contentLayout = findViewById(R.id.contentLayout);

        progressBar = findViewById(R.id.progressBar);
        firebaseAuth = FirebaseAuth.getInstance();

        readData(list -> Utilities.DataCache = list);

        btnLogin.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if(etEmail.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty())
            {
                Toast.makeText(Login.this, "Please enter all fields!", Toast.LENGTH_SHORT).show();
                if(TextUtils.isEmpty(email))
                {
                    etEmail.setError("Email address is Required");
                }
                else if(TextUtils.isEmpty(password))
                {
                    etPassword.setError("Password is Required");
                }
                else if(password.length() < 6)
                {
                    etPassword.setError("Password has to be 6 characters or above");
                }

            }
            else
            {
                contentLayout.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.VISIBLE);

                // authenticating the user using firebase authentication
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Login.this, "Successfully Logged In!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), StudentHomePage.class));
                            finish();
                        }
                        else
                        {
                            Toast.makeText(Login.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBarLayout.setVisibility(View.GONE);
                            contentLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }

        });

        tvCreateAccount.setOnClickListener(v -> {

            Intent intent = new Intent(Login.this, CreateAccount.class);

            Pair[] pairs = new Pair[8];

            pairs[0] = new Pair<View,String>(ivLogo, "logo_image");
            pairs[1] = new Pair<View,String>(tvLogo, "logo_text");
            pairs[2] = new Pair<View,String>(tvSlogan, "tv_login_to_continue_trans");
            pairs[3] = new Pair<View,String>(tilUserName, "et_enter_username_trans");
            pairs[4] = new Pair<View,String>(tilEnterYourPassword, "et_enter_password_trans");
            pairs[5] = new Pair<View,String>(btnLogin, "btn_login_trans");
            pairs[6] = new Pair<View,String>(tvForgotPassword, "tv_forgot_password_trans");
            pairs[7] = new Pair<View,String>(tvCreateAccount, "tv_create_account_trans");

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
                startActivity(intent, options.toBundle());
            }

        });

        tvForgotPassword.setOnClickListener(v -> {

            Intent intent = new Intent(Login.this, ForgotPassword.class);
            startActivity(intent);

        });

    }

    //closes the activity when the user presses the phone 'back' button
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void readData(FirebaseCallBack firebaseCallBack)
    {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Announcements");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    announcement = ds.getValue(Announcement.class);
                    announcement.setMessage("");
                    announcements.add(announcement);
                }

                firebaseCallBack.onCallBack(announcements);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private interface FirebaseCallBack
    {
        void onCallBack(ArrayList<Announcement> list);

    }

}