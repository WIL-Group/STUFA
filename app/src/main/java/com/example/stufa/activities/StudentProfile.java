package com.example.stufa.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.stufa.data_models.Student;
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

    TextView tvStudentNumber, tvStudentNum, tvFullName, tvNameAndSurname, tvBookingID, tvBookingNum, tvBookingDate, tvBookingType, tvDateCreated,
            tvIsAttendedTo, tvRequestDate, tvRequestType, tvRequestStudentNumber, tvIsRequestAnswered, tvStudentEmail;
    EditText etEmailAddress, etCourse, etCampus, etFundingType, etBursar;
    Button btnUpdateDetails, btnDeleteBooking, btnEditBooking, btnEditRequest, btnDeleteBookingDialog,
            btnCancelDeleteDialog;
    Dialog deleteBookingDialog;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FirebaseUser user;
    DatabaseReference userRer, userDatabaseRef;

    Form form;
    /*----------strings I use to retrieve Student(user) details from realtime database------------*/
    String studentNumber, name,surname, email, course , campus, bursar, fundingType;

    /*----------strings I use to read from all table details in my realtime database------------*/
    String userID, id, bType, date, bookingDate, studEmail,
            bookingStudentNumber, rStudentNumber, requestDate, requestType;

    Boolean requestIsAnswered, isAttendedTo;
    int i = 0, bookingNum;
    RecyclerView recyclerView;
    private QueryAdapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<Query> queries;
    Query query;
    Booking booking;
    DatabaseReference queryReff, submittedQueryReff, databaseReference, bookingDatabaseRef;
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
        //tvBookingID = findViewById(R.id.tvBookingID);
        tvBookingDate = findViewById(R.id.tvBookingDate);
        tvBookingType = findViewById(R.id.tvBookingType);
        tvBookingNum = findViewById(R.id.tvBookingNum);
        //tvDateCreated = findViewById(R.id.tvDateCreated);
        tvIsAttendedTo = findViewById(R.id.tvIsAttendedTo);
        tvRequestDate = findViewById(R.id.tvRequestDate);
        tvRequestType = findViewById(R.id.tvRequestType);
        tvRequestStudentNumber = findViewById(R.id.tvRequestStudentNumber);
        tvIsRequestAnswered = findViewById(R.id.tvIsRequestAnswered);
        tvStudentEmail = findViewById(R.id.tvStudentEmail);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        etCourse = findViewById(R.id.etCourse);
        etCampus = findViewById(R.id.etCampus);
        etFundingType = findViewById(R.id.etFundingType);
        etBursar = findViewById(R.id.etBursar);
        btnUpdateDetails = findViewById(R.id.btnUpdateDetails);
        btnDeleteBooking = findViewById(R.id.btnDeleteBooking);
        btnEditBooking = findViewById(R.id.btnEditBooking);
        //btnEditRequest = findViewById(R.id.btnEditRequest);
        btnDeleteBookingDialog = findViewById(R.id.btnDeleteBookingDialog);
        btnCancelDeleteDialog = findViewById(R.id.btnCancelDeleteDialog);


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

        btnEditBooking.setVisibility(View.GONE);

        /*------------------Delete Booking dialog--------------------*/
        deleteBookingDialog = new Dialog(StudentProfile.this);
        deleteBookingDialog.setContentView(R.layout.custom_delete_booking_background);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Objects.requireNonNull(deleteBookingDialog.getWindow()).setBackgroundDrawable(getDrawable(R.drawable.tips_background));
        }
        deleteBookingDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        deleteBookingDialog.setCancelable(true);//when you click the outside of the dialog it will disappear
        deleteBookingDialog.getWindow().getAttributes().windowAnimations = R.style.tips_animation;

        btnDeleteBookingDialog = deleteBookingDialog.findViewById(R.id.btnDeleteBookingDialog);
        btnCancelDeleteDialog = deleteBookingDialog.findViewById(R.id.btnCancelDeleteDialog);

//        readData(new FireBaseCallBack() {
//            @Override
//            public void onCallBack(ArrayList<Query> list) {
//                myAdapter = new QueryAdapter(list, StudentProfile.this);
//                recyclerView.setAdapter(myAdapter);
//            }
//        });

        /*---------------Reads the data entered when the user registered----------------*/
        userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Students");
        userDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Student student = snapshot1.getValue(Student.class);
                    assert student != null;

                    studentNumber = student.getStudentNumber();
                    email = student.getEmail();
                    id = student.getId();
                    name = student.getName();
                    surname = student.getSurname();
                    bursar = student.getBursar();
                    fundingType = student.getFundingType();
                    campus = student.getCampus();

                    tvStudentNumber.setText(String.format("Student Number: " + "%s", studentNumber));
                    tvFullName.setText(String.format("Student Fullname: " + "%s %s", name, surname));
                    etEmailAddress.setText(email);
                    etFundingType.setText(fundingType);
                    etBursar.setText(bursar);
                    etCampus.setText(campus);

                    readBooking();

                    readRequest();

                    readData(new FireBaseCallBack() {
                        @Override
                        public void onCallBack(ArrayList<Query> list) {
                            myAdapter = new QueryAdapter(list, StudentProfile.this);
                            recyclerView.setAdapter(myAdapter);
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnUpdateDetails.setOnClickListener(v -> {
            updateDetails();
        });

        btnDeleteBooking.setOnClickListener(v -> {

            deleteBookingDialog.show();
        });

        btnEditBooking.setOnClickListener(v -> {

            editBooking();
        });

        btnDeleteBookingDialog.setOnClickListener(v -> {
            deleteBooking();
            deleteBookingDialog.dismiss();
        });

        btnCancelDeleteDialog.setOnClickListener(v -> deleteBookingDialog.dismiss());

//        btnEditRequest.setOnClickListener(v -> {
//            Intent intent = new Intent(StudentProfile.this, CreateRequest.class);
//            startActivityForResult(intent, 2);
//        });

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
                    id = details.getId();
                    bookingNum = details.getBookingNum();

                    if (bookingStudentNumber.equals(studentNumber))
                    {
                        tvNameAndSurname.setText(String.format("%s %s", name, surname));
                        tvStudentNum.setText(bookingStudentNumber);
                        tvBookingDate.setText(bookingDate);
                        tvBookingType.setText(bType);
                        tvIsAttendedTo.setText(String.format("Is Attended To: %s", isAttendedTo.toString()));
                        tvStudentEmail.setText(studEmail);
                        //tvBookingID.setText(id);
                        tvBookingNum.setText(String.format("Your Ticket Number: %s", bookingNum));
                    }
                    else
                    {
                        tvNameAndSurname.setText("******");
                        tvStudentNum.setText("******");
                        tvBookingDate.setText("******");
                        tvBookingType.setText("******");
                        tvIsAttendedTo.setText("******");
                        tvStudentEmail.setText("******");
                        tvBookingNum.setText("******");
                        //tvBookingID.setText("********");

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void deleteBooking()
    {
        //if()
        bookingDatabaseRef = FirebaseDatabase.getInstance().getReference("Bookings");
        bookingDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                bookingDatabaseRef.removeValue();

                tvNameAndSurname.setText("******");
                tvStudentNum.setText("******");
                tvBookingDate.setText("******");
                tvBookingType.setText("******");
                tvIsAttendedTo.setText("******");
                tvStudentEmail.setText("******");
                tvBookingNum.setText("******");

                Utilities.show(getApplicationContext(),"Booking successfully deleted");
                //readBooking();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utilities.show(getApplicationContext(),"Error: " + error.getMessage());
            }
        });


    }

    public void editBooking()
    {
        //boolean result = booking.isAttendedTo();
//        if(!booking.isAttendedTo())
//        {
            Intent intent = new Intent(StudentProfile.this, EditBooking.class);
            intent.putExtra(bookingDate, "bookingDate");
            intent.putExtra(bookingStudentNumber, "bookingStudentNumber");
            intent.putExtra(bType, "bType");
            intent.putExtra(studEmail, "studEmail");
            startActivity(intent);
//        }
//        else
//        {
//            Utilities.show(getApplicationContext(), "Sorry...Booking is already attended to!");
//        }

    }


    public void readRequest()
    {
        /*---------------Reads the data entered when the user registered----------------*/
//        userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Students");
//        userDatabaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                    Student student = snapshot1.getValue(Student.class);
//                    assert student != null;
//
//                    //gets the student number of the current user logged in
//                    studentNumber = student.getStudentNumber();

                    //Reads the data entered when the user registered just to check if we can read back the data
                    bookingDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Requests");
                    bookingDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                Request details = snapshot1.getValue(Request.class);
                                assert details != null;

                                rStudentNumber = details.getStudNum();
                                requestType = details.getRequestType();
                                requestDate = details.getDate();
                                requestIsAnswered = details.isAnswered();

                                //test if the student number of the current logged in user is the
                                // same as the student number of the the booking the user has sent
                                if (rStudentNumber.equals(studentNumber))
                                {
                                    tvRequestStudentNumber.setText(String.format("Student Number: %s", rStudentNumber));
                                    tvRequestDate.setText(String.format("Request Date: %s", requestDate));
                                    tvRequestType.setText(String.format("Request Type: %s", requestType));
                                    tvIsRequestAnswered.setText(String.format("Request Answered: %s", requestIsAnswered.toString()));
                                }
                                else
                                {
                                    tvRequestStudentNumber.setText("********");
                                    tvRequestDate.setText("********");
                                    tvRequestType.setText("********");
                                    tvIsRequestAnswered.setText("********");
                                }


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

//                }
//            }

//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


    }

    private void readData(FireBaseCallBack fireBaseCallBack)
    {
        queryReff = Utilities.getDatabaseRefence().child("Queries");

        queryReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                queries.clear();

                for (DataSnapshot ds : snapshot.getChildren())
                {
                    Request details = ds.getValue(Request.class);
                    assert details != null;

                    String qStudentNumber = details.getStudNum();

                    if (qStudentNumber.equals(studentNumber)) {
                        query = ds.getValue(Query.class);
                        queries.add(query);
                    }

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
    public void onItemClicked(int index)
    {

    }

    interface FireBaseCallBack
    {
        void onCallBack(ArrayList<Query> list);
    }

}