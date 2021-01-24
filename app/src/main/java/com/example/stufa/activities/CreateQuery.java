package com.example.stufa.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stufa.R;
import com.example.stufa.app_utilities.QueryAdapter;
import com.example.stufa.app_utilities.Utilities;
import com.example.stufa.data_models.Query;
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

public class CreateQuery extends AppCompatActivity implements QueryAdapter.ItemClicked {

    /*---------------------Variables----------------------------------*/
    CheckBox cbBookAllowance, cbMealAllowance, cbGeneralQuery, cbAccommodationOrTransportAllowance;
    EditText etQueryMessage;
    Button btnSave, btnDelete, btnSubmit;

    RecyclerView recyclerView;
    private QueryAdapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<Query> queries;
    Query query;
    boolean isAnswered = false;
    String qType, qMessage, qId, qDate, studentNum, response, staffEmail, studentNumber, userID;
    DatabaseReference queryReff,submittedQueryReff;
    com.google.firebase.database.Query query1;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    Date date = new Date();

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_query);

        /*---------------------Hooks----------------------------------*/
        cbBookAllowance = findViewById(R.id.cbBookAllowance);
        cbMealAllowance = findViewById(R.id.cbMealAllowance);
        cbAccommodationOrTransportAllowance = findViewById(R.id.cbAccommodationOrTransportAllowance);
//        cbGeneralQuery = findViewById(R.id.cbGeneralQuery);
        etQueryMessage = findViewById(R.id.etQueryMessage);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        btnSubmit = findViewById(R.id.btnSubmit);
        queries = new ArrayList<>();

        recyclerView = findViewById(R.id.rvList);
        recyclerView.setHasFixedSize(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        userID = firebaseAuth.getCurrentUser().getUid();

        layoutManager = new LinearLayoutManager(CreateQuery.this);
        recyclerView.setLayoutManager(layoutManager);

        qDate = dateFormat.format(date);

        //Reads the data entered when the user registered just to check if we can read back the data
        DocumentReference documentReference = firestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                assert value != null;
                studentNumber = value.getString("studentNumber");
                //studEmail = value.getString("email");

            }
        });

        //sets the query list to my adapter
        readData(list -> {
            myAdapter = new QueryAdapter(list,CreateQuery.this);
            recyclerView.setAdapter(myAdapter);
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
        if(cbBookAllowance.isChecked())
        {
            qType = cbBookAllowance.getText().toString().trim();

            if(etQueryMessage.getText().toString().isEmpty())
            {
                Toast.makeText(CreateQuery.this, "Please enter query message", Toast.LENGTH_SHORT).show();
            }
            else
            {
                studentNum = studentNumber;
                qMessage = etQueryMessage.getText().toString().trim();
                qId = queries.size() + qMessage.charAt(2) + "";
                query = new Query(qId, isAnswered, staffEmail, qMessage, qType, qDate, studentNum, response);

                queryReff.push().setValue(query);
                Toast.makeText(CreateQuery.this, "Query successfully added", Toast.LENGTH_SHORT).show();
            }
        }
        else if(cbMealAllowance.isChecked())
        {
            qType = cbMealAllowance.getText().toString().trim();

            if(etQueryMessage.getText().toString().isEmpty())
            {
                Toast.makeText(CreateQuery.this, "Please enter query message", Toast.LENGTH_SHORT).show();
            }
            else
            {
                studentNum = studentNumber;
                qMessage = etQueryMessage.getText().toString().trim();
                qId = queries.size() + qMessage.charAt(2) + "";
                query = new Query(qId, isAnswered, staffEmail, qMessage, qType, qDate, studentNum, response);

                queryReff.push().setValue(query);
                Toast.makeText(CreateQuery.this, "Query successfully added", Toast.LENGTH_SHORT).show();
            }
        }
        else if(cbAccommodationOrTransportAllowance.isChecked())
        {
            qType = cbAccommodationOrTransportAllowance.getText().toString().trim();

            if(etQueryMessage.getText().toString().isEmpty())
            {
                Toast.makeText(CreateQuery.this, "Please enter query message", Toast.LENGTH_SHORT).show();
            }
            else
            {
                studentNum = studentNumber;
                qMessage = etQueryMessage.getText().toString().trim();
                qId = queries.size() + qMessage.charAt(2) + "";
                query = new Query(qId, isAnswered, staffEmail, qMessage, qType, qDate, studentNum, response);

                queryReff.push().setValue(query);
                Toast.makeText(CreateQuery.this, "Query successfully added", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(CreateQuery.this,"Please select one of the boxes",Toast.LENGTH_SHORT).show();
        }

    }

    public  void deleteData()
    {
        String toDelete = query.getqId();

        query1 = queryReff.orderByChild("qId").equalTo(toDelete);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    ds.getRef().removeValue();
                }
                //Toast.makeText(CreateQuery.this, query.getqId()+" Removed!",Toast.LENGTH_SHORT).show();
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