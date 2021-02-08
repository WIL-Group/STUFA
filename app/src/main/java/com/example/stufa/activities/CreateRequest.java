package com.example.stufa.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.stufa.R;
import com.example.stufa.app_utilities.Utilities;
import com.example.stufa.data_models.Booking;
import com.example.stufa.data_models.Request;
import com.example.stufa.data_models.Student;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class CreateRequest extends AppCompatActivity {

    Button btnViewFinancialStatement, btnRequestFinancialClearance, btnDownloadStatement, btnCancel;
    private ArrayList<Request> requests;
    Request request;
    int i = 0;
    boolean rIsAnswered = false, cleared = false;
    String userID, id, staffEmail = "", requestType, rDate, rStudentNumber,
            studentEmail, studentId, studentNumber, studEmail;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
    Date date = new Date();

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    DatabaseReference requestReff, userDatabaseRef;
    StorageReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_request);

        btnViewFinancialStatement = findViewById(R.id.btnViewFinancialStatement);
        btnRequestFinancialClearance = findViewById(R.id.btnRequestFinancialClearance);
        btnCancel = findViewById(R.id.btnCancel);
        btnDownloadStatement = findViewById(R.id.btnDownloadStatement);

        requestReff = Utilities.getDatabaseRefence().child("Requests");

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        userID = firebaseAuth.getCurrentUser().getUid();

        rDate = dateFormat.format(date);

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


        btnViewFinancialStatement.setOnClickListener(v -> {

            requestFinancialStatement();
        });

        btnDownloadStatement.setOnClickListener(v ->
                downloadStatement());

        btnRequestFinancialClearance.setOnClickListener(v -> {

            requestFinancialClearance();
        });

        btnCancel.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), StudentHomePage.class));
            CreateRequest.this.finish();
        });

    }

    public void requestFinancialStatement()
    {
        requestType = "Financial Statement";
        rStudentNumber = studentNumber;
        studEmail = studentEmail;

        request = new Request(rIsAnswered, rDate, cleared, requestType, rStudentNumber, staffEmail, studEmail);


        requestReff.push().setValue(request);

        CreateRequest.this.finish();
        Utilities.show(CreateRequest.this, getString(R.string.fs_sent));
    }

    public void requestFinancialClearance()
    {
        requestType = "Financial Clearance";
        rStudentNumber = studentNumber;
        studEmail = studentEmail;

        request = new Request(/*id,*/ rIsAnswered, rDate, cleared, requestType, rStudentNumber, staffEmail, studEmail);


        requestReff.push().setValue(request);

        CreateRequest.this.finish();
        Utilities.show(CreateRequest.this, getString(R.string.fc_sent));
    }

    public void downloadStatement()
    {
        storageReference = firebaseStorage.getInstance().getReference();
        //reff = storageReference.child("Financials").child("finState.pdf");
        reff = storageReference.child("Financials").child("statement.pdf");

        reff.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                String url = uri.toString();
                //downloadFiles(CreateRequest.this, "finState", ".pdf", DIRECTORY_DOWNLOADS, url);
                downloadFiles(CreateRequest.this, "statement", ".pdf", DIRECTORY_DOWNLOADS, url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void downloadFiles(Context context, String fileName, String fileExtension, String destinationDirectory, String url)
    {

        DownloadManager downloadManager = (DownloadManager) context
                .getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        downloadManager.enqueue(request);
    }

}