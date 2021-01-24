package com.example.stufa.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.stufa.R;
import com.example.stufa.app_utilities.Utilities;
import com.example.stufa.data_models.Booking;
import com.example.stufa.data_models.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CreateRequest extends AppCompatActivity {

    Button btnViewFinancialStatement, btnRequestFinancialClearance, btnCancel;
    private ArrayList<Request> requests;
    Request request;
    int i = 0;
    boolean rIsAnswered = false, cleared = false;
    String userID, id, staffEmail, requestType, fCleared, rDate, rStudentNumber, studentNumber, studEmail;

//    String DateFormat = "yyyy-MM-dd HH:mm:ss";
//    currentDateString = new SimpleDateFormat(DateFormat, Locale.getDefault()).format(new Date());

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    Date date = new Date();

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;

    DatabaseReference requestReff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        btnViewFinancialStatement = findViewById(R.id.btnViewFinancialStatement);
        btnRequestFinancialClearance = findViewById(R.id.btnRequestFinancialClearance);
        btnCancel = findViewById(R.id.btnCancel);

        requestReff = Utilities.getDatabaseRefence().child("Requests");

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        userID = firebaseAuth.getCurrentUser().getUid();

        rDate = dateFormat.format(date);

        //Reads the data entered when the user registered just to check if we can read back the data
        DocumentReference documentReference = firestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                assert value != null;
                studentNumber = value.getString("studentNumber");
                studEmail = value.getString("email");

            }
        });

        requestReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Request details = snapshot1.getValue(Request.class);
                    assert details != null;

                    //gets the student number that the request was accompanied with
                    //rStudentNumber = details.getStudNum();

                }

                if(snapshot.exists())
                {
                    //increments the value when data has changes or updated
                    i = (int)snapshot.getChildrenCount();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

        btnViewFinancialStatement.setOnClickListener(v -> {

            requestType = "Financial Statement";
            rStudentNumber = studentNumber;
            //id = String.valueOf(i);
            request = new Request(rIsAnswered, rDate, cleared, requestType, rStudentNumber, staffEmail, studEmail);

            //requestReff.child(String.valueOf(i + 1)).setValue(request);
            requestReff.push().setValue(request);

            CreateRequest.this.finish();
            Utilities.show(CreateRequest.this, getString(R.string.fs_sent));
        });

        btnRequestFinancialClearance.setOnClickListener(v -> {

            requestType = "Financial Clearance";
            rStudentNumber = studentNumber;
            //id = String.valueOf(i);
            request = new Request(/*id,*/ rIsAnswered, rDate, cleared, requestType, rStudentNumber, staffEmail, studEmail);

            //requestReff.child(String.valueOf(i + 1)).setValue(request);
            requestReff.push().setValue(request);

            CreateRequest.this.finish();
            Utilities.show(CreateRequest.this, getString(R.string.fc_sent));
        });

        btnCancel.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), StudentHomePage.class));
            CreateRequest.this.finish();
        });

    }

}