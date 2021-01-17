package com.example.stufa.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stufa.R;
import com.example.stufa.app_utilities.QueryAdapter;
import com.example.stufa.app_utilities.Utilities;
import com.example.stufa.data_models.Form;
import com.example.stufa.data_models.Query;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class StudentProfile extends AppCompatActivity /*implements QueryAdapter.ItemClicked*/ {

    TextView tvStudentNumber, tvFullName, tvNameAndSurname, tvBookingType, tvDateCreated,
             tvDateRequested, tvRequestType, tvDateOfRequest, tvIsRequestAnswered ;
    EditText etEmailAddress, etCourse, etCampus;
    Button btnUpdateDetails, btnDeleteBooking, btnEditBooking , btnEditRequest;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    DatabaseReference formRer;

    Form form;
    String userID, name, surname, studentNumber, email, course, campus;

    RecyclerView recyclerView;
    private QueryAdapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<Query> queries;
    Query query;
    String type, message,qId;
    DatabaseReference queryReff,submittedQueryReff,databaseReference;
    com.google.firebase.database.Query query1;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_student_profile);

        tvStudentNumber = findViewById(R.id.tvStudentNumber);
        tvFullName = findViewById(R.id.tvFullName);
        tvNameAndSurname = findViewById(R.id.tvNameAndSurname);
        tvBookingType = findViewById(R.id.tvBookingType);
        tvDateCreated = findViewById(R.id.tvDateCreated);
        tvDateRequested = findViewById(R.id.tvDateRequested);
        tvRequestType = findViewById(R.id.tvRequestType);
        tvDateOfRequest = findViewById(R.id.tvDateOfRequest);
        tvIsRequestAnswered = findViewById(R.id.tvIsRequestAnswered);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        etCourse = findViewById(R.id.etCourse);
        etCampus = findViewById(R.id.etCampus);
        btnUpdateDetails = findViewById(R.id.btnUpdateDetails);
        btnDeleteBooking = findViewById(R.id.btnDeleteBooking);
        btnEditBooking = findViewById(R.id.btnEditBooking);
        btnEditRequest = findViewById(R.id.btnEditRequest);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        form = new Form();

        userID = firebaseAuth.getCurrentUser().getUid();
        formRer = FirebaseDatabase.getInstance().getReference().child("Profiles");

        recyclerView = findViewById(R.id.rvList);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(StudentProfile.this);
        recyclerView.setLayoutManager(layoutManager);

        //sets the query list to my adapter
//        readData(list -> {
//            myAdapter = new QueryAdapter(list,StudentProfile.this);
//            recyclerView.setAdapter(myAdapter);
//        });

        //Reads the data entered when the user registered just to check if we can read back the data
        DocumentReference documentReference = firestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                assert value != null;
                studentNumber = value.getString("studentNumber");
                name = value.getString("name");
                surname = value.getString("surname");
                email = value.getString("email");
                //course = value.getString("course");
                campus = value.getString("campus");

                tvStudentNumber.setText(studentNumber);
                tvFullName.setText(String.format("%s %s", name, surname));
                etEmailAddress.setText(email);
                etCampus.setText(campus);
                //etCourse.setText(course);
            }
        });

        btnUpdateDetails.setOnClickListener(v -> {

        });

        btnDeleteBooking.setOnClickListener(v -> {

        });

        btnEditBooking.setOnClickListener(v -> {

        });

        btnEditRequest.setOnClickListener(v -> {

        });

    }


//    private void readData(CreateQuery.FireBaseCallBack fireBaseCallBack)
//    {
//        queryReff = Utilities.getDatabaseRefence().child("submitted_queries");
//
//        queryReff.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                //queries.clear();
//
//                for(DataSnapshot ds : snapshot.getChildren())
//                {
//                    query = ds.getValue(Query.class);
//                    //queries.add(query);
//                }
//                fireBaseCallBack.onCallBack(queries);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(StudentProfile.this, "Error!" + error, Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }
//
//    @Override
//    public void onItemClicked(int index) {
//        query = queries.get(index);
//    }
//
//    private interface FireBaseCallBack
//    {
//        void onCallBack(ArrayList<Query> list);
//    }

}