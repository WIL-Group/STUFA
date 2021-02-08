package com.example.stufa.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stufa.R;
import com.example.stufa.app_utilities.QueryAdapter;
import com.example.stufa.app_utilities.Utilities;
import com.example.stufa.data_models.Booking;
import com.example.stufa.data_models.Query;
import com.example.stufa.data_models.Request;
import com.example.stufa.data_models.Student;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CreateQuery extends AppCompatActivity implements QueryAdapter.ItemClicked {

    /*---------------------Variables----------------------------------*/
    RadioGroup rgQueryType;
    RadioButton rbMealAllowance, rbAccommodationOrTransportAllowance, rbBookAllowance;
    EditText etDialogQueryMessage;
    TextView tvQueryMessage, tvTypeMessage;
    Button btnSave, btnDelete, btnSubmit, btnSaveMessage;
    Dialog queryMessageDialog;

    RecyclerView recyclerView;
    private QueryAdapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<Query> queries;
    Query query;
    boolean isAnswered = false;
    String qType, qMessage, qId, id, qDate, studentNum, response = "", staffEmail = "", studEmail,
            studentNumber, studentEmail, studentId,  userID;
    DatabaseReference queryReff, submittedQueryReff, userDatabaseRef;
    com.google.firebase.database.Query query1;
    Student student;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
    Date date = new Date();

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_query);

        /*---------------------Hooks----------------------------------*/
        rgQueryType = findViewById(R.id.rgQueryType);
        rbMealAllowance = findViewById(R.id.rbMealAllowance);
        rbAccommodationOrTransportAllowance = findViewById(R.id.rbAccommodationOrTransportAllowance);
        rbBookAllowance = findViewById(R.id.rbBookAllowance);
        tvQueryMessage = findViewById(R.id.tvQueryMessage);
        tvTypeMessage = findViewById(R.id.tvTypeMessage);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        btnSubmit = findViewById(R.id.btnSubmit);
        queries = new ArrayList<>();

        recyclerView = findViewById(R.id.rvList);
        recyclerView.setHasFixedSize(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        queryReff = Utilities.getDatabaseRefence().child("Queries");


        userID = firebaseAuth.getCurrentUser().getUid();


        layoutManager = new LinearLayoutManager(CreateQuery.this);
        recyclerView.setLayoutManager(layoutManager);

        qDate = dateFormat.format(date);

        /*------------------Query Message dialog--------------------*/
        queryMessageDialog = new Dialog(CreateQuery.this);
        queryMessageDialog.setContentView(R.layout.custom_describe_query_background);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Objects.requireNonNull(queryMessageDialog.getWindow()).setBackgroundDrawable(getDrawable(R.drawable.tips_background));
        }
        queryMessageDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        queryMessageDialog.setCancelable(true);//when you click the outside of the dialog it will disappear
        queryMessageDialog.getWindow().getAttributes().windowAnimations = R.style.tips_animation;

        btnSaveMessage = queryMessageDialog.findViewById(R.id.btnSaveMessage);
        etDialogQueryMessage = queryMessageDialog.findViewById(R.id.etDialogQueryMessage);

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

        queryReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Query details = snapshot1.getValue(Query.class);
                    assert details != null;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

        //sets the query list to my adapter
        readData(list -> {
            myAdapter = new QueryAdapter(list,CreateQuery.this);
            recyclerView.setAdapter(myAdapter);
        });

        tvTypeMessage.setOnClickListener(v -> queryMessageDialog.show());
        tvQueryMessage.setOnClickListener(v -> queryMessageDialog.show());

        btnSaveMessage.setOnClickListener(v -> {

            if(etDialogQueryMessage.getText().toString().isEmpty())
            {
                Utilities.show(getApplicationContext(), "Please enter message!");
            }
            else
            {
                String message = etDialogQueryMessage.getText().toString().trim();
                tvQueryMessage.setText(message);
                queryMessageDialog.dismiss();
            }

        });

        btnSave.setOnClickListener(v -> {
            //method that inserts data into the recyclerview list when button save is clicked
            
            insertData();
        });

        btnDelete.setOnClickListener(v -> {
            //deletes the query clicked on the recyclerview list
            
            if(query == null)
            {
                Toast.makeText(CreateQuery.this,"Please select a query to delete", Toast.LENGTH_SHORT).show();
            }
            else
            {
                deleteData();
            }
        });

        btnSubmit.setOnClickListener(v -> {

            //submits the entire query to the firebase realtime database
            if(query != null)
            {
                submittedQueryReff = FirebaseDatabase.getInstance().getReference().child("Queries");
                submittedQueryReff.push().setValue(query);
                deleteData();
                Toast.makeText(CreateQuery.this, "Query " + query.getqId() + " successfully submitted!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(CreateQuery.this, "Please select query from the list!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onItemClicked(int index) {
        query = queries.get(index);
    }

    public void insertData()
    {
        if(rbBookAllowance.isChecked())
        {
            qType = rbBookAllowance.getText().toString().trim();

            if(tvQueryMessage.getText().toString().isEmpty())
            {
                Toast.makeText(CreateQuery.this, "Please enter query message", Toast.LENGTH_LONG).show();
            }
            else
            {
                studentNum = studentNumber;
                studEmail = studentEmail;
                qId = studentId;
                qMessage = tvQueryMessage.getText().toString().trim();
                id = queries.size() + qMessage.charAt(2) + "";

                query = new Query(qId, id, isAnswered, staffEmail, qMessage, qType, qDate, studentNum, response, studEmail);


                queryReff.push().setValue(query);
                Toast.makeText(CreateQuery.this, "Query successfully added", Toast.LENGTH_SHORT).show();
            }
        }
        else if(rbMealAllowance.isChecked())
        {
            qType = rbMealAllowance.getText().toString().trim();

            if(tvQueryMessage.getText().toString().isEmpty())
            {
                Toast.makeText(CreateQuery.this, "Please enter query message", Toast.LENGTH_LONG).show();
            }
            else
            {
                studentNum = studentNumber;
                studEmail = studentEmail;
                qId = studentId;
                qMessage = tvQueryMessage.getText().toString().trim();
                id = queries.size() + qMessage.charAt(2) + "";

                query = new Query(qId, id, isAnswered, staffEmail, qMessage, qType, qDate, studentNum, response, studEmail);

                queryReff.push().setValue(query);
                Toast.makeText(CreateQuery.this, "Query successfully added", Toast.LENGTH_SHORT).show();
            }
        }
        else if(rbAccommodationOrTransportAllowance.isChecked())
        {
            qType = rbAccommodationOrTransportAllowance.getText().toString().trim();

            if(tvQueryMessage.getText().toString().isEmpty())
            {
                Toast.makeText(CreateQuery.this, "Please enter query message", Toast.LENGTH_LONG).show();
            }
            else
            {
                studentNum = studentNumber;
                studEmail = studentEmail;
                qId = query.getqId();
                qMessage = tvQueryMessage.getText().toString().trim();
                id = queries.size() + qMessage.charAt(2) + "";

                query = new Query(qId, id, isAnswered, staffEmail, qMessage, qType, qDate, studentNum, response, studEmail);

                queryReff.push().setValue(query);
                Toast.makeText(CreateQuery.this, "Query successfully added", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(CreateQuery.this,"Please select one of the boxes",Toast.LENGTH_LONG).show();
        }

    }

    public  void deleteData()
    {
        String toDelete = query.getId();

        tvQueryMessage.setText("");
        query1 = queryReff.orderByChild("id").equalTo(toDelete);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    ds.getRef().removeValue();
                }
                //Toast.makeText(CreateQuery.this, query.getId()+" Query submitted!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CreateQuery.this, "Error! " + error, Toast.LENGTH_SHORT).show();
            }
        });

        queries.remove(query);
    }

    private void readData(FireBaseCallBack fireBaseCallBack)
    {
        queryReff = Utilities.getDatabaseRefence().child("saved_queries");

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
                Toast.makeText(CreateQuery.this, "Error!" + error, Toast.LENGTH_SHORT).show();

            }
        });
    }

    interface FireBaseCallBack
    {
        void onCallBack(ArrayList<Query> list);
    }
    
}