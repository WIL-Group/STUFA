package com.example.stufa.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stufa.R;
import com.example.stufa.app_utilities.Utilities;
import com.example.stufa.data_models.Booking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateBooking extends AppCompatActivity {

    CheckBox cbQueryRelatedBooking, cbGeneralBooking, cbRequestRelatedBooking;
    Button btnCreate, btnDelete, btnSubmit;
    TextView tvStudentNumer, tvBookingType, tvDateCreated;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    DatabaseReference bookingRef;

    Booking booking;
    String userID, bType, studentNumber, date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_booking);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.booking));
        actionBar.setIcon(R.drawable.booking);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        cbQueryRelatedBooking = findViewById(R.id.cbQueryRelatedBooking);
        cbGeneralBooking = findViewById(R.id.cbGeneralBooking);
        cbRequestRelatedBooking = findViewById(R.id.cbRequestRelatedBooking);
        btnCreate = findViewById(R.id.btnCreate);
        btnDelete = findViewById(R.id.btnDelete);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvStudentNumer = findViewById(R.id.tvNameAndSurname);
        tvBookingType = findViewById(R.id.tvBookingType);
        tvDateCreated = findViewById(R.id.tvDateCreated);

        tvBookingType.setVisibility(View.GONE);
        tvStudentNumer.setVisibility(View.GONE);
        tvDateCreated.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        date = new SimpleDateFormat("dd MM, yyyy", Locale.getDefault()).format(new Date());

        userID = firebaseAuth.getCurrentUser().getUid();

        //Reads the data entered when the user registered just to check if we can read back the data
        DocumentReference documentReference = firestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                assert value != null;
                studentNumber = value.getString("studentNumber");
            }
        });


        btnCreate.setOnClickListener(v ->
                createBooking());

        btnDelete.setOnClickListener(v -> {
            deleteBooking();
        });

        btnSubmit.setOnClickListener(v -> {
            if(!tvStudentNumer.getText().toString().trim().equals(studentNumber))
            {
                Utilities.show(CreateBooking.this, "Please select a query type to submit!");
            }
            else
            {
                booking = new Booking(studentNumber,bType,date);

                bookingRef = FirebaseDatabase.getInstance().getReference().child("bookings");
                bookingRef.push().setValue(booking);

                Utilities.show(CreateBooking.this, "Booking submitted!");

                deleteBooking();
            }
        });

    }
    public void createBooking()
    {
        if(cbQueryRelatedBooking.isChecked())
        {
            bType = cbQueryRelatedBooking.getText().toString().trim();
            tvBookingType.setText(bType);
            tvStudentNumer.setText(studentNumber);
            tvDateCreated.setText(date);

            tvBookingType.setVisibility(View.VISIBLE);
            tvStudentNumer.setVisibility(View.VISIBLE);
            tvDateCreated.setVisibility(View.VISIBLE);
        }
        else if(cbGeneralBooking.isChecked())
        {
            bType = cbGeneralBooking.getText().toString().trim();
            tvBookingType.setText(bType);
            tvStudentNumer.setText(studentNumber);
            tvDateCreated.setText(date);

            tvBookingType.setVisibility(View.VISIBLE);
            tvStudentNumer.setVisibility(View.VISIBLE);
            tvDateCreated.setVisibility(View.VISIBLE);
        }
        else if(cbRequestRelatedBooking.isChecked())
        {
            bType = cbRequestRelatedBooking.getText().toString().trim();
            tvBookingType.setText(bType);
            tvStudentNumer.setText(studentNumber);
            tvDateCreated.setText(date);

            tvBookingType.setVisibility(View.VISIBLE);
            tvStudentNumer.setVisibility(View.VISIBLE);
            tvDateCreated.setVisibility(View.VISIBLE);
        }
        else
        {
            Utilities.show(CreateBooking.this, "please select query type by checking one box!");
        }
    }
    public void deleteBooking()
    {
        tvBookingType.setText("");
        tvStudentNumer.setText("");
        tvDateCreated.setText("");

        tvBookingType.setVisibility(View.GONE);
        tvStudentNumer.setVisibility(View.GONE);
        tvDateCreated.setVisibility(View.GONE);
    }
}