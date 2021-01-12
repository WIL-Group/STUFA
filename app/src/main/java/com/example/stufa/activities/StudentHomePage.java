package com.example.stufa.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.stufa.R;
import com.example.stufa.app_utilities.HomeAdapter;
import com.example.stufa.app_utilities.Utilities;
import com.example.stufa.data_models.Announcement;
import com.example.stufa.data_models.Booking;
import com.google.android.material.navigation.NavigationView;
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

import java.util.ArrayList;

public class StudentHomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /*---------------DrawerLayout, NavigationView, Toolbar 0Variables----------------*/
    DrawerLayout drawer_layout;
    NavigationView nav_view;
    Toolbar my_toolbar;

    Button btnAllowanceRelatedQuery, btnBooking, btnGeneralQuery,
            btnFinancialStatement, btnFinancialClearance, btnFillForm;
    ImageView ivCreateQuery, ivCreateRequest, ivCreateBooking, ivFillForm;
    TextView tvGreeting, tvFullName, tvBookingPercentage;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String userID;
    FragmentManager fragmentManager;
    RecyclerView rView;
    int totalNumberOfBookings;
    DatabaseReference databaseReference;
    ArrayList<Announcement> announcements;
    ArrayList<Booking> bookings;
    Announcement announcement;
    HomeAdapter adapter;
    Booking booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home_page);

        /*------------------Hooks--------------------------------------*/
        drawer_layout = findViewById(R.id.drawer_layout);
        nav_view = findViewById(R.id.nav_view);
        my_toolbar = findViewById(R.id.my_toolbar);

        tvGreeting = findViewById(R.id.tvGreeting);
        tvFullName = findViewById(R.id.tvFullName);
        ivCreateBooking = findViewById(R.id.ivCreateBooking);
        ivCreateQuery = findViewById(R.id.ivCreateQuery);
        ivCreateRequest = findViewById(R.id.ivCeateRequest);
        ivFillForm = findViewById(R.id.ivFillForm);
        tvBookingPercentage = findViewById(R.id.tvBookingPercentage);
//        tvStudentNumber = findViewById(R.id.tvStudentNumber);
//        tvEmail = findViewById(R.id.tvEmail);
//        tvCourse = findViewById(R.id.tvCourse);

        /*--------------------Fragment and Recycler list view----------------------*/
        fragmentManager = getSupportFragmentManager();

        rView = findViewById(R.id.rvAnnounce);


        /*--------------------Tool Bar----------------------*/
        setSupportActionBar(my_toolbar);
        my_toolbar.setTitle(getString(R.string.empty));

        /*--------------------Navigation Drawer Menu----------------------*/
        nav_view.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout, my_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();

        nav_view.setNavigationItemSelectedListener(this);

        /*--------------------Firebase Instance retrievals----------------------*/
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        userID = firebaseAuth.getCurrentUser().getUid();

        /*---------------Reads the data entered when the user registered just to check if we can read back the data----------------*/
        DocumentReference documentReference = firestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                assert value != null;
                tvFullName.setText(String.format("%s%s%s", value.getString("name"), " ", value.getString("surname")));

//                tvEmail.setText(String.format("%s%s",  "Email Address: ", value.getString("email")));
//
//                tvCourse.setText(String.format("%s%s",  "Campus: ", value.getString("campus")));
//
//                tvStudentNumber.setText(String.format("%s%s",  "Student Number: ", value.getString("studentNumber")));
            }
        });
        rView.setHasFixedSize(true);
        announcements = new ArrayList<>();
        bookings = new ArrayList<>();

        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rView.setLayoutManager(layoutManager);

        //use this for calculating the number/percentage of bookings
        readData(list -> totalNumberOfBookings = list.size());
        int number = (totalNumberOfBookings/10) * 100;
        tvBookingPercentage.setText(new StringBuilder().append("Bookings are at ").append
                (number).append("%").append
                (" full capacity(for").append
                (" accurate percentage, click the refresh option in the navigation panel on you right)").toString());

        readAnnouncements(list -> {
            announcements = list;
            adapter = new HomeAdapter(StudentHomePage.this, announcements);
            rView.setAdapter(adapter);
        });

        rView.setOnClickListener(v -> {
            Intent intent = new Intent(StudentHomePage.this, AnnouncementBrowsing.class);
            startActivity(intent);
        });

//        for(int i = 0; i < Utilities.DataCache.size(); i++)
//        {
//            announcement = Utilities.DataCache.get(i);
//            announcements.add(announcement);
//        }

//        adapter = new AnnouncementAdapter(StudentHomePage.this,announcements);
//        rView.setAdapter(adapter);

        readData(list -> totalNumberOfBookings = list.size());

        ivCreateQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentHomePage.this, CreateQuery.class);
                startActivity(intent);
            }
        });

        ivCreateRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentHomePage.this, CreateRequest.class);
                startActivity(intent);
            }
        });

        ivCreateBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentHomePage.this, CreateBooking.class);
                startActivity(intent);
            }
        });

        ivFillForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentHomePage.this, FillForm.class);
                startActivity(intent);
            }
        });


    }


    //creates a menu with icon of 'lock' for logging out
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //onClick options item selected performing a firebase logout functionality for the user
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout)//Logs out the use and sends them to the Login Activity
        {

            Toast.makeText(this, "Logging user out...", Toast.LENGTH_LONG).show();

            FirebaseAuth.getInstance().signOut();//used for logging out the user
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    //for when the navigation panel is open, when pressing the back button the app does'nt close
    @Override
    public void onBackPressed() {

        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    //used for when the user clicks on the option to take them to the other activity
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_refresh_bookings:

                Toast.makeText(this, "Refreshing bookings percentage...", Toast.LENGTH_SHORT).show();

                break;

            case R.id.nav_announcements:

                startActivity(new Intent(getApplicationContext(), AnnouncementBrowsing.class));
                break;

            case R.id.nav_create_query:

                startActivity(new Intent(getApplicationContext(), CreateQuery.class));
                break;

            case R.id.nav_create_request:

                startActivity(new Intent(getApplicationContext(), CreateRequest.class));
                break;

            case R.id.nav_create_booking:

                startActivity(new Intent(getApplicationContext(), CreateBooking.class));
                break;

            case R.id.nav_login:

                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
                break;

            case R.id.nav_profile:

                startActivity(new Intent(getApplicationContext(), StudentProfile.class));
                break;

            case R.id.nav_logout:

                Toast.makeText(this, "Logging user out...", Toast.LENGTH_SHORT).show();

                FirebaseAuth.getInstance().signOut();//used for logging out the user
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
                break;
        }

        return true;

    }

//    @Override
//    public void onItemClick(int pos) {
//
//        Intent intent = new Intent(StudentHomePage.this, AnnouncementBrowsing.class);
//        startActivity(intent);
//
//    }

    private void readData(FireBaseCallBack fireBaseCallBack) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("bookings");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookings.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    booking = ds.getValue(Booking.class);
                    bookings.add(booking);
                }
                fireBaseCallBack.onCallBack(bookings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utilities.show(StudentHomePage.this, "ERROR! " + error);
            }
        });
    }

    private void readAnnouncements(FireCallBack fireCallBack) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Announcements");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Announcement> List = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    announcement = ds.getValue(Announcement.class);
                    announcement.setMessage("");
                    List.add(announcement);
                }

                fireCallBack.onFireCallback(List);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private interface FireBaseCallBack {
        void onCallBack(ArrayList<Booking> list);
    }

    private interface FireCallBack {
        void onFireCallback(ArrayList<Announcement> list);
    }

}