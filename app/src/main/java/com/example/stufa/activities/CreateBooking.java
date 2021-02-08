package com.example.stufa.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.stufa.R;
import com.example.stufa.app_utilities.Utilities;
import com.example.stufa.data_models.Booking;
import com.example.stufa.data_models.Student;
import com.example.stufa.fragments.DatePickerFragment;
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
import com.google.type.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateBooking extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    /*------------------------Variables---------------------------*/
    RadioGroup rgBookingType;
    RadioButton rbQueryRelatedBooking, rbGeneralBooking, rbRequestRelatedBooking;
    Button btnCreate, btnDelete, btnSubmit;
    ImageView ivCalendar;
    TextView tvBookingID, tvBookingDay, tvStudentNumber, tvNameAndSurname, tvBookingType, tvDateCreated, tvStudentEmail;

    androidx.appcompat.widget.Toolbar my_toolbar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    DatabaseReference bookingRef, userDatabaseRef;

    Booking booking;
    String userID, studentNumber, studentEmail,
    studentId, bStudentNumber, reason, currentDateString, id, studEmail;
    boolean attendedTo = false;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_booking);

        /*------------------Hooks--------------------------------------*/
        rgBookingType = findViewById(R.id.rgBookingType);
        rbQueryRelatedBooking = findViewById(R.id.rbQueryRelatedBooking);
        rbGeneralBooking = findViewById(R.id.rbGeneralBooking);
        rbRequestRelatedBooking = findViewById(R.id.rbRequestRelatedBooking);
        btnCreate = findViewById(R.id.btnCreate);
        btnDelete = findViewById(R.id.btnDelete);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvBookingID = findViewById(R.id.tvBookingID);
        tvBookingDay = findViewById(R.id.tvBookingDay);
        tvStudentNumber = findViewById(R.id.tvStudentNumber);
        tvNameAndSurname = findViewById(R.id.tvNameAndSurname);
        tvBookingType = findViewById(R.id.tvBookingType);
        tvStudentEmail = findViewById(R.id.tvStudentEmail);
        ivCalendar = findViewById(R.id.ivCalendar);
        
        my_toolbar = findViewById(R.id.my_toolbar);


        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        bookingRef = FirebaseDatabase.getInstance().getReference().child("Bookings");

        /*------------------sets the date format---------------------*/
        String DateFormat = "yyyy/MM/dd HH:mm:ss";
        currentDateString = new SimpleDateFormat(DateFormat, Locale.getDefault()).format(new Date());

        userID = firebaseAuth.getCurrentUser().getUid();

        /*--------------------Tool Bar----------------------*/
        setSupportActionBar(my_toolbar);

        //editBooking();


        tvBookingDay.setVisibility(View.VISIBLE);
        tvBookingType.setVisibility(View.VISIBLE);
        tvStudentNumber.setVisibility(View.VISIBLE);
        tvStudentEmail.setVisibility(View.VISIBLE);


        /*---------------Reads the data entered when the user registered----------------*/
        userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Students");
        userDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Student student = snapshot1.getValue(Student.class);
                    assert student != null;

                    studentNumber = student.getStudentNumber();
                    studentEmail = student.getEmail();
                    studentId = student.getId();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bookingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists())
                {
                    //increments the value when data has changes or updated
                    //i = (int)snapshot.getChildrenCount();
                    i = (int)snapshot.getChildrenCount();
                    //id = snapshot.getKey();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

        ivCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        btnCreate.setOnClickListener(v -> {

            //creates a booking and displays its details for the user to see before submitting
            createBooking();

        });

        btnDelete.setOnClickListener(v -> {

            //deletes the booking if the user decides not to make a booking anymore
            deleteBooking();

        });

        btnSubmit.setOnClickListener(v -> {

            //submits the booking to the firebase database and sends it to the staff side to view
            if(tvBookingDay.getText().toString().isEmpty() || tvStudentNumber.getText().toString().isEmpty()
                || tvBookingType.getText().toString().isEmpty() || tvStudentEmail.getText().toString().isEmpty())
            {
                Utilities.show(CreateBooking.this, "Please select select required booking details!");
            }
            else
            {
                int bookingNum;
                bookingNum = i + 1;

                //id = booking.getId();
                booking = new Booking(id, reason, attendedTo, bookingNum, currentDateString, bStudentNumber, studEmail);

                bookingRef = Utilities.getDatabaseRefence().child("Bookings");
                //bookingRef.child(String.valueOf(i + 1)).setValue(booking);
                bookingRef.push().setValue(booking);

                Utilities.show(CreateBooking.this, "Booking submitted!");

                deleteBooking();
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        //Disable all SUNDAYS and SATURDAYS between Min and Max Dates
//        for (Calendar loopdate = c; c.before(c); c.add(Calendar.DATE, 1), loopdate = c) {
//            int dayOfWeek = loopdate.get(Calendar.DAY_OF_WEEK);
//            if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
//                Calendar[] disabledDays =  new Calendar[1];
//                disabledDays[0] = loopdate;
//                datePickerDialog.setDisabledDays(disabledDays);
//            }
//        }

        currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

    }

    public void createBooking()
    {
        if(rbQueryRelatedBooking.isChecked())
        {
            bStudentNumber = studentNumber;
            studEmail = studentEmail;
            reason = rbQueryRelatedBooking.getText().toString().trim();
            //id = booking.getId();
            tvBookingType.setText(reason);
            tvStudentNumber.setText(bStudentNumber);
            tvStudentEmail.setText(studEmail);
            //tvBookingID.setText(id);
            tvBookingDay.setText(currentDateString);

            tvBookingDay.setVisibility(View.VISIBLE);
            tvBookingType.setVisibility(View.VISIBLE);
            tvStudentNumber.setVisibility(View.VISIBLE);
            tvStudentEmail.setVisibility(View.VISIBLE);
            //tvBookingID.setVisibility(View.VISIBLE);
        }
        else if(rbGeneralBooking.isChecked())
        {
            bStudentNumber = studentNumber;
            studEmail = studentEmail;
            reason = rbGeneralBooking.getText().toString().trim();
            //id = booking.getId();
            tvBookingType.setText(reason);
            tvStudentNumber.setText(bStudentNumber);
            tvStudentEmail.setText(studEmail);
            //tvBookingID.setText(id);
            tvBookingDay.setText(currentDateString);

            tvBookingDay.setVisibility(View.VISIBLE);
            tvBookingType.setVisibility(View.VISIBLE);
            tvStudentNumber.setVisibility(View.VISIBLE);
            tvStudentEmail.setVisibility(View.VISIBLE);
            //tvBookingID.setVisibility(View.VISIBLE);
        }
        else if(rbRequestRelatedBooking.isChecked())
        {
            bStudentNumber = studentNumber;
            studEmail = studentEmail;
            reason = rbRequestRelatedBooking.getText().toString().trim();
            //id = booking.getId();
            tvBookingType.setText(reason);
            tvStudentNumber.setText(bStudentNumber);
            tvStudentEmail.setText(studEmail);
            //tvBookingID.setText(id);
            tvBookingDay.setText(currentDateString);

            tvBookingDay.setVisibility(View.VISIBLE);
            tvBookingType.setVisibility(View.VISIBLE);
            tvStudentNumber.setVisibility(View.VISIBLE);
            tvStudentEmail.setVisibility(View.VISIBLE);
            //tvBookingID.setVisibility(View.VISIBLE);
        }
        else
        {
            Utilities.show(CreateBooking.this, "please select query type by checking one box!");
        }
    }
    public void deleteBooking()
    {
        tvBookingDay.setText("");
        tvBookingType.setText("");
        tvStudentNumber.setText("");
        //tvDateCreated.setText("");
        tvStudentEmail.setText("");
        //tvBookingID.setText("");

        tvBookingDay.setVisibility(View.GONE);
        tvBookingType.setVisibility(View.GONE);
        tvStudentNumber.setVisibility(View.GONE);
        //tvDateCreated.setVisibility(View.GONE);
        tvStudentEmail.setVisibility(View.GONE);
        //tvBookingID.setVisibility(View.GONE);
    }

    public void editBooking()
    {
        currentDateString = getIntent().getStringExtra("bookingDate");
        bStudentNumber = getIntent().getStringExtra("bookingStudentNumber");
        reason = getIntent().getStringExtra("bType");
        studEmail = getIntent().getStringExtra("studEmail");

        Booking booking = new Booking();
        //booking.setAttendedTo(false);
        //booking.setBookingNum(false);
        booking.setDate(currentDateString);
        booking.setReason(reason);
        //booking.setBookingNum(i);
        //booking.setId(id);
        //booking.setStudEmail(id);

    }

}