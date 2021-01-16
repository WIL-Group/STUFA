package com.example.stufa.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.stufa.R;
import com.example.stufa.app_utilities.Utilities;
import com.example.stufa.data_models.Query;
import com.example.stufa.data_models.Request;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CreateRequest extends AppCompatActivity {

    Button btnViewFinancialStatement, btnRequestFinancialClearance, btnCancel;
    private ArrayList<Request> requests;
    Request request;
    String rID, rType;
    DatabaseReference requestReff;
    //com.google.firebase.database.Request request1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        btnViewFinancialStatement = findViewById(R.id.btnViewFinancialStatement);
        btnRequestFinancialClearance = findViewById(R.id.btnRequestFinancialClearance);
        btnCancel = findViewById(R.id.btnCancel);
        requestReff = Utilities.getDatabaseRefence().child("requests_table");

    }

    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.btnViewFinancialStatement:
                request = new Request("Financial Statement");
                requestReff.push().setValue(request);
               // Utilities.openActivity(CreateRequest.this, StudentHomePage.class);
                CreateRequest.this.finish();
                Utilities.show(CreateRequest.this, getString(R.string.fs_sent));
                break;
            case R.id.btnRequestFinancialClearance:
                request = new Request("Financial Clearance");
                requestReff.push().setValue(request);
               // Utilities.openActivity(CreateRequest.this, StudentHomePage.class);
                CreateRequest.this.finish();
                Utilities.show(CreateRequest.this, getString(R.string.fc_sent));
                break;
            case R.id.btnCancel:
                Utilities.openActivity(CreateRequest.this, StudentHomePage.class);
                break;
        }
    }

//    private void readData(CreateRequest.FireBaseCallBack fireBaseCallBack)
//    {
//
//
//        requestReff.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                requests.clear();
//
//                for(DataSnapshot ds : snapshot.getChildren())
//                {
//                    request = ds.getValue(Request.class);
//                    requests.add(request);
//                }
//                fireBaseCallBack.onCallBack(requests);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(CreateRequest.this, "Error!" + error, Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }
//    private interface FireBaseCallBack
//    {
//        void onCallBack(ArrayList<Request> list);
//    }
}