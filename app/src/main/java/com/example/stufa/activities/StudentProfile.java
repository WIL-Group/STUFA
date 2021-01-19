package com.example.stufa.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stufa.R;
import com.example.stufa.app_utilities.QueryAdapter;
import com.example.stufa.app_utilities.Utilities;
import com.example.stufa.data_models.Announcement;
import com.example.stufa.data_models.Booking;
import com.example.stufa.data_models.Form;
import com.example.stufa.data_models.Query;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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
import java.util.HashMap;
import java.util.Map;

public class StudentProfile extends AppCompatActivity implements QueryAdapter.ItemClicked {

    TextView tvStudentNumber, tvStudentNum, tvFullName, tvNameAndSurname, tvBookingType, tvDateCreated,
            tvDateRequested, tvRequestType, tvDateOfRequest, tvIsRequestAnswered;
    EditText etEmailAddress, etCourse, etCampus;
    Button btnUpdateDetails, btnDeleteBooking, btnEditBooking, btnEditRequest;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FirebaseUser user;
    DatabaseReference userRer;

    Form form;
    String userID, name, surname, studentNumber, email, course, campus, bType, date;

    RecyclerView recyclerView;
    private QueryAdapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<Query> queries;
    Query query;
    String type, message, qId;
    DatabaseReference queryReff, bookingReff, databaseReference;
    com.google.firebase.database.Query query1;
    private Booking booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_student_profile);

        tvStudentNumber = findViewById(R.id.tvStudentNumber);
        tvStudentNum = findViewById(R.id.tvStudentNum);
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
        user = firebaseAuth.getCurrentUser();
        form = new Form();
        queries = new ArrayList<>();
        booking = new Booking();


        userID = firebaseAuth.getCurrentUser().getUid();
        userRer = FirebaseDatabase.getInstance().getReference().child("users");

        recyclerView = findViewById(R.id.rvList);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(StudentProfile.this);
        recyclerView.setLayoutManager(layoutManager);

        readData(new FireBaseCallBack() {
            @Override
            public void onCallBack(ArrayList<Query> list) {
                myAdapter = new QueryAdapter(list, StudentProfile.this);
                recyclerView.setAdapter(myAdapter);
            }
        });



        //Reads the data entered when the user registered just to check if we can read back the data
        DocumentReference documentReference = firestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                assert value != null;
                /*-----------------For the profile details tab----------------*/
                studentNumber = value.getString("studentNumber");
                name = value.getString("name");
                surname = value.getString("surname");
                email = value.getString("email");
                course = value.getString("course");
                campus = value.getString("campus");


                tvStudentNumber.setText(studentNumber);
                tvFullName.setText(String.format("%s %s", name, surname));
                etEmailAddress.setText(email);
                etCampus.setText(campus);
                etCourse.setText(course);

                /*-----------------For the booking details tab----------------*/



            }
        });





        btnUpdateDetails.setOnClickListener(v -> {

            updateDetails();

        });

        btnDeleteBooking.setOnClickListener(v -> {

        });

        btnEditBooking.setOnClickListener(v -> {

        });

        btnEditRequest.setOnClickListener(v -> {

        });

        retrieveBooking(new FireCallBack() {
            @Override
            public void onFire(Booking booking) {
                bType = booking.getbType();
                date = booking.getDate();

                tvNameAndSurname.setText(String.format("%s %s", name, surname));
                tvBookingType.setText(bType);
                tvDateCreated.setText(date);
                tvStudentNum.setText(studentNumber);
            }
        });

    }

    public void updateDetails() {

        if(etEmailAddress.getText().toString().isEmpty() || etCampus.getText().toString().isEmpty()
           /*|| etCourse.getText().toString().isEmpty()*/)
        {
            Utilities.show(StudentProfile.this, "One or more fields are empty!");
            return;
        }

        String newEmail = etEmailAddress.getText().toString().trim();
        String newCampus = etCampus.getText().toString().trim();
        String newCourse = etCourse.getText().toString().trim();

        user.updateEmail(newEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                DocumentReference docRef = firestore.collection("users").document(user.getUid());
                Map<String,Object> edited = new HashMap<>();
                edited.put("email", newEmail);
                edited.put("campus", newCampus);
                edited.put("course", newCourse);

                docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Utilities.show(StudentProfile.this, "Campus and Course are updated!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Utilities.show(StudentProfile.this, "Error: " + e.getMessage());
                    }
                });

                Utilities.show(StudentProfile.this, "Profile updated!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                Utilities.show(StudentProfile.this, "Error: " + e.getMessage());
            }
        });

    }

    private void retrieveBooking(FireCallBack fireCallBack)
    {
        bookingReff = Utilities.getDatabaseRefence().child("bookings"+studentNumber);

        bookingReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                booking = snapshot.getValue(Booking.class);
                fireCallBack.onFire(booking);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentProfile.this, "Error!" + error, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void readData(FireBaseCallBack fireBaseCallBack)
    {
        queryReff = Utilities.getDatabaseRefence().child("submitted_queries");

        queryReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                queries.clear();

                for(DataSnapshot ds : snapshot.getChildren())
                {
                    query = ds.getValue(Query.class);
                    queries.add(query);
                }
                fireBaseCallBack.onCallBack(queries);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentProfile.this, "Error!" + error, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onItemClicked(int index) {

    }

    interface  FireCallBack
    {
        void onFire(Booking booking);
    }

    interface FireBaseCallBack
    {
        void onCallBack(ArrayList<Query> list);
    }

//    public void readBooking()
//    {
//        DocumentReference docRef = firestore.collection("users").document(user.getUid());
        //bookingDatabaseRef = FirebaseDatabase.getInstance().getReference().child("bookings");

        //bookingDatabaseRef.child("bookings").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String bookingType = snapshot.getValue();
//                tvBookingType.setText(bookingType);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

//    public void deleteBooking()
//    {
//
//    }

}