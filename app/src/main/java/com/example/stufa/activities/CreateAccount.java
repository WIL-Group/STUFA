package com.example.stufa.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stufa.R;
import com.example.stufa.data_models.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends AppCompatActivity {

    /*-------------------------Variables------------------------*/
    EditText etName, etSurname, etEmail, etFundingType, etBursar, etStudentNumber, etPassword, etConfirmPassword;
    Button btnSignUp;
    TextView tvLogin;

    SwitchCompat switchPos;
    View progressBarLayout, createAccountContentLayout;
    ProgressBar progressBar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    String userID, name, surname, password, confirmPassword, email, id, studentNumber, fundingType, bursar, campus;
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_account);

        /*-------------------------Hooks------------------------*/
        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etEmail = findViewById(R.id.etEmail);
        etFundingType = findViewById(R.id.etFundingType);
        etBursar = findViewById(R.id.etBursar);
        etStudentNumber = findViewById(R.id.etStudentNumber);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvLogin = findViewById(R.id.tvLogin);

        switchPos = findViewById(R.id.switchPos);

        progressBarLayout = findViewById(R.id.progressBarLayout);
        createAccountContentLayout = findViewById(R.id.createAccountContentLayout);

        progressBar = findViewById(R.id.progressBar);

        createAccountContentLayout.setVisibility(View.VISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        //tests if there is a user already in the firebase database
//        if(firebaseAuth.getCurrentUser() != null)
//        {
//            startActivity(new Intent(getApplicationContext(),StudentHomePage.class));
//            finish();
//        }


        switchPos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

             //checks if the switch position state is true
               if(isChecked)
               {
                   switchPos.setText(R.string.welkom_campus);

               }
               else
               {
                   switchPos.setText(R.string.bloemfontein_campus);
               }
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validations();

                createAccountContentLayout.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.VISIBLE);

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("Students");

                //will now register the use into Firebase
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        progressBarLayout.setVisibility(View.GONE);
//                        createAccountContentLayout.setVisibility(View.VISIBLE);

                        if(task.isSuccessful())
                        {
                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful())
                                    {
                                        userID = firebaseAuth.getCurrentUser().getUid();
                                        String id = userID;

                                        //saving the user with realtime database
                                        Student student = new Student(id, name, surname, email, studentNumber,
                                                fundingType, bursar, campus);

                                        reference.child(id).setValue(student);

                                        Intent intent = new Intent();
                                        intent.putExtra("email", email);
                                        intent.putExtra("password", password);
                                        setResult(RESULT_OK, intent);

                                        Toast.makeText(CreateAccount.this, "Successfully created. Please check your email for verification", Toast.LENGTH_SHORT).show();
                                        clearEntries();
                                        startActivity(new Intent(getApplicationContext(), Login.class));
                                        CreateAccount.this.finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(CreateAccount.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                            //saving the user using firestore
//                            DocumentReference documentReference = firestore.collection("users").document(userID);
//
//                            Map<String, Object> user = new HashMap<>();
//
//
//                            user.put("name", name);
//                            user.put("surname", surname);
//                            user.put("email", email);
//                            user.put("studentNumber", studentNumber);
//                            user.put("campus", campus);
//
//                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//
//                                    Toast.makeText(CreateAccount.this, "Profile successfully created!", Toast.LENGTH_LONG).show();
//
//                                }
//                            });
                        }
                        else //if the user already exists in the database an error message will show
                        {
                            Toast.makeText(CreateAccount.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBarLayout.setVisibility(View.GONE);
                            createAccountContentLayout.setVisibility(View.VISIBLE);
                            //startActivity(new Intent(getApplicationContext(),CreateAccount.class));
                        }
                    }
                });
            }
        });


        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccount.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void validations()
    {
        //final String id = student.getId();
        name = etName.getText().toString().trim();
        surname = etSurname.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        fundingType = etFundingType.getText().toString().trim();
        bursar = etBursar.getText().toString().trim();
        studentNumber = etStudentNumber.getText().toString().trim();
        campus = switchPos.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        confirmPassword = etConfirmPassword.getText().toString().trim();

        Intent intent = new Intent();
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        setResult(RESULT_OK, intent);

        if(TextUtils.isEmpty(name))
        {
            etName.setError("Name address is Required");
            return;
        }
        else if(TextUtils.isEmpty(surname))
        {
            etSurname.setError("Surname address is Required");
            return;
        }
        else if(TextUtils.isEmpty(email))
        {
            etEmail.setError("Email address is Required");
            return;
        }
        else if(TextUtils.isEmpty(fundingType))
        {
            etFundingType.setError("Funding Type is Required");
            return;
        }
        else if(TextUtils.isEmpty(bursar))
        {
            etBursar.setError("Bursar is Required");
            return;
        }

        else if(TextUtils.isEmpty(campus))
        {
            Toast.makeText(CreateAccount.this,
                    getString(R.string.please_choose_your_campus),
                    Toast.LENGTH_SHORT).show();;
            return;
        }

        else if(TextUtils.isEmpty(studentNumber))
        {
            etStudentNumber.setError("Student number is Required");
            return;
        }
        else if(TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword))
        {
            etPassword.setError("Password is Required");
            etConfirmPassword.setError("Password is Required");
            return;
        }
        else if(password.length() < 6)
        {
            etPassword.setError("Password has to be 6 characters or more");
            return;
        }
        else if(!etConfirmPassword.getText().toString().equals(etPassword.getText().toString()))
        {
            etConfirmPassword.setError("Passwords do not match");
            return;
        }
    }

    public void clearEntries()
    {
        etName.setText("");
        etSurname.setText("");
        etEmail.setText("");
        etFundingType.setText("");
        etBursar.setText("");
        etStudentNumber.setText("");
        switchPos.setText("");
        etPassword.setText("");
         etConfirmPassword.setText("");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.forgot_password, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.back) {
            startActivity(new Intent(getApplicationContext(), Login.class));
        }

        return super.onOptionsItemSelected(item);
    }

}