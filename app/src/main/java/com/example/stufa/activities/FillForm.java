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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.stufa.R;
import com.example.stufa.app_utilities.Utilities;
import com.example.stufa.data_models.Form;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FillForm extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    /*--------------------------------------Variables----------------------------------------*/
    EditText etStudName, etStudSurname, etIDNumber, etStudNumber, etAppRefNumber, etInstNumber,
            etCourseOrProgramme, etYearOfStudy, etLatestYearOfFunding, etCurrentFundingStatus,
            etDateOfAppeal;
    RadioGroup rgReasonForAppeal, rgAppealCategory;
    RadioButton rbNewAppeal, rbReturningStudent, rbFailure, rbChangeInFinance, rbLossOfBursary,
            rbIncorrectAcademicResults, rbGAPYear;
    CheckBox cbFullNameDeclaration;
    Button btnSubmitForm;

    View progressBarLayout, constraint_content_layout;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    DatabaseReference formRer, userDatabaseRef;
    int i = 0;

    String id, name, surname, email, studentNumber, fundingType, bursar, campus;

    Form form;
    String userID, idNumber, applicationRefNumber, instNumber, currentDateString, currentYearString,
            courseOrProgramme, yearOfStudy, lastYearOfFunding, currentFundingStatus, dateOfAppeal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fill_form);

        /*------------------------------------Hooks-----------------------------------------*/
        etStudName = findViewById(R.id.etStudName);
        etStudSurname = findViewById(R.id.etStudSurname);
        etIDNumber = findViewById(R.id.etIDNumber);
        etStudNumber = findViewById(R.id.etStudNumber);
        etAppRefNumber = findViewById(R.id.etAppRefNumber);
        etInstNumber = findViewById(R.id.etInstNumber);
        etCourseOrProgramme = findViewById(R.id.etCourseOrProgramme);
        etYearOfStudy = findViewById(R.id.etYearOfStudy);
        etLatestYearOfFunding = findViewById(R.id.etLatestYearOfFunding);
        etCurrentFundingStatus = findViewById(R.id.etCurrentFundingStatus);
        etDateOfAppeal = findViewById(R.id.etDateOfAppeal);

        rgAppealCategory = findViewById(R.id.rgAppealCategory);
        rgReasonForAppeal = findViewById(R.id.rgReasonForAppeal);
        rbNewAppeal = findViewById(R.id.rbNewAppeal);
        rbReturningStudent = findViewById(R.id.rbReturningStudent);
        rbFailure = findViewById(R.id.rbFailure);
        rbChangeInFinance = findViewById(R.id.rbChangeInFinance);
        rbLossOfBursary = findViewById(R.id.rbLossOfBursary);
        rbIncorrectAcademicResults = findViewById(R.id.rbIncorrectAcademicResults);
        rbGAPYear = findViewById(R.id.rbGAPYear);
        cbFullNameDeclaration = findViewById(R.id.cbFullNameDeclaration);

        btnSubmitForm = findViewById(R.id.btnSubmitForm);

        progressBarLayout = findViewById(R.id.progressBarLayout);
        constraint_content_layout = findViewById(R.id.constraint_content_layout);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        form = new Form();

        userID = firebaseAuth.getCurrentUser().getUid();
        formRer = FirebaseDatabase.getInstance().getReference().child("Forms");

        /*------------------sets the date format---------------------*/
        String DateFormat = "yyy/MM/dd";
        String YearFormat = "yyyy";
        currentDateString = new SimpleDateFormat(DateFormat, Locale.getDefault()).format(new Date());
        currentYearString = new SimpleDateFormat(YearFormat, Locale.getDefault()).format(new Date());


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
                    fundingType = student.getFundingType();
                    bursar = student.getBursar();
                    campus = student.getCampus();

                    etStudNumber.setText(studentNumber);
                    etStudName.setText(name);
                    etStudSurname.setText(surname);
                    etIDNumber.setText(id);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        etYearOfStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");

                etYearOfStudy.setText(currentYearString);
            }
        });

        etDateOfAppeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");

                etDateOfAppeal.setText(currentDateString);
            }
        });

        btnSubmitForm.setOnClickListener(v -> {

            //submits the form to the firebase database and sends it to the staff side to view
                createForm();
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
        currentYearString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
    }


    public void createForm() {


        if(etIDNumber.getText().toString().isEmpty()
                || etStudName.getText().toString().isEmpty()
                || etStudSurname.getText().toString().isEmpty()
                || etInstNumber.getText().toString().isEmpty()
                || etCourseOrProgramme.getText().toString().isEmpty()
                || etCurrentFundingStatus.getText().toString().isEmpty()
                || etDateOfAppeal.getText().toString().isEmpty()
                || !cbFullNameDeclaration.isChecked())
        {
            Utilities.show(FillForm.this, "Please enter your Personal Details and tick relevant options provided before submitting!");
        }
        else
        {
            /*------------------retrieving my EditText data-------------------*/
            studentNumber = etStudNumber.getText().toString().trim();
            name = etStudName.getText().toString().trim();
            surname = etStudSurname.getText().toString().trim();
            idNumber = etIDNumber.getText().toString().trim();
            applicationRefNumber = etAppRefNumber.getText().toString().trim();
            instNumber = etInstNumber.getText().toString().trim();
            courseOrProgramme = etCourseOrProgramme.getText().toString().trim();
            yearOfStudy = etYearOfStudy.getText().toString().trim();
            lastYearOfFunding = etLatestYearOfFunding.getText().toString().trim();
            currentFundingStatus = etCurrentFundingStatus.getText().toString().trim();
            dateOfAppeal = etDateOfAppeal.getText().toString().trim();


            /*----------------set my entered data to my Forms firebase database-----------------*/
            form.setStudentNumber(studentNumber);
            form.setName(name);
            form.setSurname(surname);
            form.setIdNumber(idNumber);
            form.setAppRef(applicationRefNumber);
            form.setInstName(instNumber);
            form.setCourse(courseOrProgramme);
            form.setYearOfStudy(yearOfStudy);
            form.setLastYearOfStudy(lastYearOfFunding);
            form.setFundingStatus(currentFundingStatus);
            form.setDateOfAppeal(dateOfAppeal);
            form.setId(id);

            if(rbNewAppeal.isChecked())
            {
                form.setNewApplicant(true);
            }
            else if (rbReturningStudent.isChecked())
            {
                form.setReturningStudent(true);
            }
            else
            {
                Utilities.show(FillForm.this, "Please tick relevant box of Appeal Category!");
            }

            if(rbFailure.isChecked())
            {
                form.setFailureToMeet(true);
            }
            else if(rbChangeInFinance.isChecked())
            {
                form.setChangeInFinancialCircumstances(true);
            }
            else if (rbLossOfBursary.isChecked())
            {
                form.setLossOfBursarySponsor(true);
            }
            else if(rbIncorrectAcademicResults.isChecked())
            {
                form.setIncorrectAcademicResults(true);
            }
            else if(rbGAPYear.isChecked())
            {
                form.setGapYear(true);
            }
            if (cbFullNameDeclaration.isChecked())
            {
                form.setFullNameDeclaration(true);
            }

            formRer.push().setValue(form);
            clearData();
            FillForm.this.finish();
            Utilities.show(FillForm.this, "Form submitted!");
        }

   }

    public void clearData() {
        etYearOfStudy.setText(null);
        etAppRefNumber.setText(null);
        etCourseOrProgramme.setText(null);
        etCurrentFundingStatus.setText(null);
        etDateOfAppeal.setText(null);
        etIDNumber.setText(null);
        etInstNumber.setText(null);
        etLatestYearOfFunding.setText(null);
    }

}