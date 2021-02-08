package com.example.stufa.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.stufa.R;

public class EditBooking extends AppCompatActivity {

    EditText etBookingDay, etStudentNumber, etBookingType, etStudentEmail;
    TextView tvBookingNum;
    Button btnSubmit;

    String currentDateString, bStudentNumber, reason, studEmail, bookingNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_booking);

        etBookingDay = findViewById(R.id.etBookingDay);
        etStudentNumber = findViewById(R.id.etStudentNumber);
        etBookingType = findViewById(R.id.etBookingType);
        etStudentEmail = findViewById(R.id.etStudentEmail);
        tvBookingNum = findViewById(R.id.tvBookingNum);
        btnSubmit = findViewById(R.id.btnSubmit);

        currentDateString = getIntent().getStringExtra("bookingDate");
        bStudentNumber = getIntent().getStringExtra("bookingStudentNumber");
        reason = getIntent().getStringExtra("bType");
        studEmail = getIntent().getStringExtra("studEmail");
        bookingNum = getIntent().getStringExtra("bookingNum");

        etBookingDay.setText(currentDateString);
        etStudentNumber.setText(bStudentNumber);
        etBookingType.setText(reason);
        etStudentEmail.setText(studEmail);
        tvBookingNum.setText(bookingNum);

    }
}