package com.example.stufa.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.stufa.R;
import com.example.stufa.app_utilities.Utilities;
import com.example.stufa.data_models.Form;
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

public class FillForm extends AppCompatActivity {

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
    DatabaseReference formRer;
    int i = 0;

    Form form;
    String userID, name, surname, idNumber, studentNumber, applicationRefNumber, instNumber,
            courseOrProgramme, yearOfStudy, lastYearOfFunding, currentFundingStatus, dateOfAppeal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        //Reads the data entered when the user registered just to check if we can read back the data
        DocumentReference documentReference = firestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                assert value != null;
                studentNumber = value.getString("studentNumber");
                name = value.getString("name");
                surname = value.getString("surname");

                etStudNumber.setText(studentNumber);
                etStudName.setText(name);
                etStudSurname.setText(surname);
            }
        });

        formRer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

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

        btnSubmitForm.setOnClickListener(v -> {

            //submits the form to the firebase database and sends it to the staff side to view
            if (!etStudNumber.getText().toString().trim().equals(studentNumber))
            {
                Utilities.show(FillForm.this, "Please enter your correct student number!");
            }
            else
            {
                createForm();
            }

        });

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
            formRer.child(String.valueOf(i + 1)).setValue(form);
            form.setName(name);
            formRer.child(String.valueOf(i + 1)).setValue(form);
            form.setSurname(surname);
            formRer.child(String.valueOf(i + 1)).setValue(form);
            form.setIDNumber(idNumber);
            formRer.child(String.valueOf(i + 1)).setValue(form);
            form.setAppRef(applicationRefNumber);
            form.setInstName(instNumber);
            formRer.child(String.valueOf(i + 1)).setValue(form);
            form.setCourse(courseOrProgramme);
            formRer.child(String.valueOf(i + 1)).setValue(form);
            form.setYearOfStudy(yearOfStudy);
            formRer.child(String.valueOf(i + 1)).setValue(form);
            form.setLastYearOfStudy(lastYearOfFunding);
            formRer.child(String.valueOf(i + 1)).setValue(form);
            form.setFundingStatus(currentFundingStatus);
            formRer.child(String.valueOf(i + 1)).setValue(form);
            form.setDateOfAppeal(dateOfAppeal);
            formRer.child(String.valueOf(i + 1)).setValue(form);

            if(rbNewAppeal.isChecked())
            {
                form.setNewAppeal(true);
                formRer.child(String.valueOf(i + 1)).setValue(form);
            }
            else if (rbReturningStudent.isChecked())
            {
                form.setReturningStudent(true);
                formRer.child(String.valueOf(i + 1)).setValue(form);
            }
            else
            {
                Utilities.show(FillForm.this, "Please tick relevant box of Appeal Category!");
            }

            if(rbFailure.isChecked())
            {
                form.setFailureToMeetAcademicPerformanceRequirements(true);
                formRer.child(String.valueOf(i + 1)).setValue(form);
            }
            else if(rbChangeInFinance.isChecked())
            {
                form.setChangeInFinancialCircumstances(true);
                formRer.child(String.valueOf(i + 1)).setValue(form);
            }
            else if (rbLossOfBursary.isChecked())
            {
                form.setLossOfBursarySponsor(true);
                formRer.child(String.valueOf(i + 1)).setValue(form);
            }
            else if(rbIncorrectAcademicResults.isChecked())
            {
                form.setIncorrectAcademicResultsSubmittedResultingInNonrenewalOfFunding(true);
                formRer.child(String.valueOf(i + 1)).setValue(form);
            }
            else if(rbGAPYear.isChecked())
            {
                form.setiCompletedAGapYearDueToAcademicPerformance(true);
                formRer.child(String.valueOf(i + 1)).setValue(form);
            }
            if (cbFullNameDeclaration.isChecked())
            {
                form.setFullNameDeclaration(true);
                formRer.child(String.valueOf(i + 1)).setValue(form);
            }

            clearData();
            FillForm.this.finish();
            //startActivity(new Intent(getApplicationContext(), StudentHomePage.class));
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