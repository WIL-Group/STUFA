package com.example.stufa.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
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
import com.example.stufa.data_models.Request;
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
import java.util.Objects;

public class StudentProfile extends AppCompatActivity  implements QueryAdapter.ItemClicked {

    TextView tvStudentNumber, tvStudentNum, tvFullName, tvNameAndSurname, tvBookingDate, tvBookingType, tvDateCreated,
            tvIsAttendedTo, tvRequestDate, tvRequestType, tvRequestStudentNumber, tvIsRequestAnswered, tvStudentEmail;
    EditText etEmailAddress, etCourse, etCampus;
    Button btnUpdateDetails, btnDeleteBooking, btnEditBooking, btnEditRequest;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FirebaseUser user;
    DatabaseReference userRer;

    Form form;
    String userID, name, surname, studentNumber, email, course, campus, bType, date, bookingDate, studEmail,
            bookingStudentNumber, rStudentNumber, requestDate, requestType;
    Boolean requestIsAnswered, isAttendedTo;
    int i = 0;
    RecyclerView recyclerView;
    private QueryAdapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<Query> queries;
    Query query;
    Booking booking;
    DatabaseReference queryReff, submittedQueryReff, databaseReference, bookingDatabaseRef;
//    com.google.firebase.database.Bookings booking1;
    com.google.firebase.database.Query query1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_student_profile);

        tvStudentNumber = findViewById(R.id.tvStudentNumber);
        tvStudentNum = findViewById(R.id.tvStudentNum);
        tvFullName = findViewById(R.id.tvFullName);
        tvNameAndSurname = findViewById(R.id.tvNameAndSurname);
        tvBookingDate = findViewById(R.id.tvBookingDate);
        tvBookingType = findViewById(R.id.tvBookingType);
        tvDateCreated = findViewById(R.id.tvDateCreated);
        tvIsAttendedTo = findViewById(R.id.tvIsAttendedTo);
        tvRequestDate = findViewById(R.id.tvRequestDate);
        tvRequestType = findViewById(R.id.tvRequestType);
        tvRequestStudentNumber = findViewById(R.id.tvRequestStudentNumber);
        tvIsRequestAnswered = findViewById(R.id.tvIsRequestAnswered);
        tvStudentEmail = findViewById(R.id.tvStudentEmail);
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

                tvStudentNumber.setText(String.format("Student Number: " + "%s", studentNumber));
                tvFullName.setText(String.format("Student Fullname: " + "%s %s", name, surname));
                etEmailAddress.setText(email);
                etCampus.setText(campus);
                etCourse.setText(course);

                readBooking();

                readRequest();

            }
        });


        btnUpdateDetails.setOnClickListener(v -> {
            updateDetails();
        });

        btnDeleteBooking.setOnClickListener(v -> {
            //deleteBookingData();
        });

        btnEditBooking.setOnClickListener(v -> {

            Intent intent = new Intent(StudentProfile.this, CreateBooking.class);
            startActivityForResult(intent, 1);
        });

        btnEditRequest.setOnClickListener(v -> {
            Intent intent = new Intent(StudentProfile.this, CreateRequest.class);
            startActivityForResult(intent, 2);
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
                }).addOnFailureListener(e -> Utilities.show(StudentProfile.this, "Error: " + e.getMessage()));

                Utilities.show(StudentProfile.this, "Profile updated!");
            }
        }).addOnFailureListener(e -> Utilities.show(StudentProfile.this, "Error: " + e.getMessage()));

    }

    public void readBooking()
    {

        //Reads the data entered when the user registered just to check if we can read back the data
        bookingDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Bookings");
        bookingDatabaseRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Booking details = snapshot1.getValue(Booking.class);
                    assert details != null;

                    bookingDate = details.getDate();
                    bType = details.getReason();
                    date = details.getDate();
                    bookingStudentNumber = details.getStudNum();
                    isAttendedTo = details.isAttendedTo();
                    studEmail = details.getStudEmail();

                    if (bookingStudentNumber.equals(studentNumber))
                    {
                        tvNameAndSurname.setText(String.format("%s %s", name, surname));
                        tvStudentNum.setText(bookingStudentNumber);
                        tvBookingDate.setText(bookingDate);
                        tvBookingType.setText(bType);
                        tvIsAttendedTo.setText(String.format("Is Attended To: %s", isAttendedTo.toString()));
                        tvStudentEmail.setText(studEmail);
                    }
                    else
                    {
                        tvNameAndSurname.setText("********");
                        tvStudentNum.setText("********");
                        tvBookingDate.setText("********");
                        tvBookingType.setText("********");
                        tvIsAttendedTo.setText("********");
                        tvStudentEmail.setText("********");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public  void deleteBookingData()
    {
        String toDelete = booking.getStudNum();

        query1 = bookingDatabaseRef.orderByChild("studNum").equalTo(toDelete);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    if(studentNumber.equals(rStudentNumber))
                    {
                        ds.getRef().removeValue();
                    }
                    else
                    {
                        Utilities.show(getApplicationContext(), "Data can't be removed");
                    }

                }
                Toast.makeText(StudentProfile.this, query.getqId()+" Removed!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentProfile.this, "Error! " + error, Toast.LENGTH_SHORT).show();
            }
        });

        queries.remove(query);
    }

    public void deleteBooking()
    {
        if(tvNameAndSurname.getText().toString().isEmpty()
                || tvStudentNum.getText().toString().isEmpty()
                || tvBookingDate.getText().toString().isEmpty()
                || tvBookingType.getText().toString().isEmpty()
                || tvDateCreated.getText().toString().isEmpty())
        {
            Utilities.show(StudentProfile.this, "No data to remove!");
        }
        else
        {
            Booking booking = new Booking();

            bookingDatabaseRef.child("Bookings").setValue(null).addOnSuccessListener(aVoid ->
                    Utilities.show(StudentProfile.this, "Booking removed!")
        ).addOnFailureListener(e ->
        {
            Utilities.show(StudentProfile.this, "Error: " + e.getMessage());
        });
//            details.setBookingDay(null);
//            details.setbType(null);
//            details.setDate(null);
//            details.setStudentNumber(null);

            tvNameAndSurname.setText("********");
            tvStudentNum.setText("********");
            tvBookingDate.setText("********");
            tvBookingType.setText("********");
            tvDateCreated.setText("********");
        }
    }

    public void editBooking()
    {

    }


    public void readRequest()
    {

        //Reads the data entered when the user registered just to check if we can read back the data
        DocumentReference documentReference = firestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                assert value != null;
                /*-----------------For the profile details tab----------------*/
                studentNumber = value.getString("studentNumber");

                //Reads the data entered when the user registered just to check if we can read back the data
                bookingDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Requests");
                bookingDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren())
                        {

                            Request details = snapshot1.getValue(Request.class);
                            assert details != null;

                            studentNumber = value.getString("studentNumber");

                            rStudentNumber = details.getStudNum();
                            requestType = details.getRequestType();
                            requestDate = details.getDate();
                            requestIsAnswered = details.isAnswered();

                            if(rStudentNumber.equals(studentNumber))
                            {
                                tvRequestStudentNumber.setText(rStudentNumber);
                                tvRequestDate.setText(requestDate);
                                tvRequestType.setText(requestType);
                                tvIsRequestAnswered.setText(requestIsAnswered.toString());
                            }
                            else
                            {
                                tvRequestDate.setText("********");
                                tvRequestType.setText("********");
                                tvRequestType.setText("********");
                                tvIsRequestAnswered.setText("********");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    public void editRequest()
    {

    }

    private void readData(FireBaseCallBack fireBaseCallBack)
    {
        queryReff = Utilities.getDatabaseRefence().child("Queries");

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

}