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

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.stufa.R;
import com.example.stufa.app_utilities.HomeAdapter;
import com.example.stufa.app_utilities.Utilities;
import com.example.stufa.data_models.Announcement;
import com.example.stufa.data_models.Booking;
import com.example.stufa.data_models.Student;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.Collections;
import java.util.Objects;

public class StudentHomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /*---------------DrawerLayout, NavigationView, Toolbar 0Variables----------------*/
    DrawerLayout drawer_layout;
    NavigationView nav_view;
    Toolbar my_toolbar;
    Button btnYes, btnNo, btnLogout, btnCancelLogout;
    Dialog tipsDialog, logoutDialog;
    View sHomeProgressBarLayout;

    ProgressBar progressBar;
    ImageView ivCreateQuery, ivCreateRequest, ivCreateBooking, ivFillForm;
    TextView tvFullName, tvBookingPercentage;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    String userID, name, surname;
    FragmentManager fragmentManager;
    RecyclerView rView;
    int totalNumberOfBookings, maxBookings = 10;
    DatabaseReference databaseReference, userDatabaseRef;
    ArrayList<Announcement> announcements;
    ArrayList<Booking> bookings;
    Announcement announcement;
    HomeAdapter adapter;
    Booking booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_student_home_page);

        /*------------------Hooks--------------------------------------*/
        drawer_layout = findViewById(R.id.drawer_layout);
        nav_view = findViewById(R.id.nav_view);
        my_toolbar = findViewById(R.id.my_toolbar);

        tvFullName = findViewById(R.id.tvFullName);
        ivCreateBooking = findViewById(R.id.ivCreateBooking);
        ivCreateQuery = findViewById(R.id.ivCreateQuery);
        ivCreateRequest = findViewById(R.id.ivCeateRequest);
        ivFillForm = findViewById(R.id.ivFillForm);
        tvBookingPercentage = findViewById(R.id.tvBookingPercentage);

        sHomeProgressBarLayout = findViewById(R.id.sHomeProgressBarLayout);

        /*------------------Tips dialog--------------------*/
        tipsDialog = new Dialog(StudentHomePage.this);
        tipsDialog.setContentView(R.layout.custom_tips_background);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Objects.requireNonNull(tipsDialog.getWindow()).setBackgroundDrawable(getDrawable(R.drawable.tips_background));
        }
        tipsDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tipsDialog.setCancelable(true);//when you click the outside of the dialog it will disappear
        tipsDialog.getWindow().getAttributes().windowAnimations = R.style.tips_animation;

        /*------------------Logout dialog--------------------*/
        logoutDialog = new Dialog(StudentHomePage.this);
        logoutDialog.setContentView(R.layout.custom_logout_message_background);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Objects.requireNonNull(logoutDialog.getWindow()).setBackgroundDrawable(getDrawable(R.drawable.tips_background));
        }
        logoutDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        logoutDialog.setCancelable(true);//when you click the outside of the dialog it will disappear
        logoutDialog.getWindow().getAttributes().windowAnimations = R.style.tips_animation;

        btnYes = tipsDialog.findViewById(R.id.btnYes);
        btnNo = tipsDialog.findViewById(R.id.btnNo);
        btnLogout = logoutDialog.findViewById(R.id.btnLogout);
        btnCancelLogout = logoutDialog.findViewById(R.id.btnCancelLogout);



        /*--------------------Fragment and Recycler list view----------------------*/
        fragmentManager = getSupportFragmentManager();

        rView = findViewById(R.id.rvAnnounce);


        /*--------------------Tool Bar----------------------*/
        setSupportActionBar(my_toolbar);
        //my_toolbar.setTitle(getString(R.string.empty));
        getSupportActionBar().setTitle("Dashboard");
//        getSupportActionBar().setIcon(R.drawable.menu);

        /*--------------------Navigation Drawer Menu----------------------*/
        nav_view.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout, my_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();

        nav_view.setNavigationItemSelectedListener(this);

        /*--------------------Firebase Instance retrievals----------------------*/
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        userID = firebaseAuth.getCurrentUser().getUid();

        //tests if there is a user is already active and email is verified
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null)
        {
            tipsDialog.show();
        }

        /*---------------Reads the data entered when the user registered----------------*/
        userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Students");
        userDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Student student = snapshot1.getValue(Student.class);
                    assert student != null;

                    name = student.getName();
                    surname = student.getSurname();

                    tvFullName.setText(String.format("%s%s%s", name, " ", surname));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rView.setHasFixedSize(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            rView.setBackgroundDrawable(getDrawable(R.drawable.buttons));
        }
        announcements = new ArrayList<>();
        bookings = new ArrayList<>();

        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rView.setLayoutManager(layoutManager);

        //use this for calculating the number/percentage of bookings
        readData(list -> {
            bookings = list;
            if(bookings.size() == 10.0)
            {
                tvBookingPercentage.setText(new StringBuilder().append("Bookings are at full capacity," +
                        " Please be patient and wait for a spot to be open to book"));
            }
                tvBookingPercentage.setText(new StringBuilder().append("Bookings are at ").append
                        (((bookings.size()/10.0) * 100)).append("%").append
                        (" full capacity").toString());

        });
        //readData(list -> totalNumberOfBookings = list.size());

        readAnnouncements(list -> {
            announcements = list;
            adapter = new HomeAdapter(StudentHomePage.this, announcements);
            rView.setAdapter(adapter);
        });

        rView.setOnClickListener(v -> {
            Intent intent = new Intent(StudentHomePage.this, AnnouncementBrowsing.class);
            startActivity(intent);        });

        readData(list -> totalNumberOfBookings = list.size());

        ivCreateQuery.setOnClickListener(v -> {
            Intent intent = new Intent(StudentHomePage.this, CreateQuery.class);
            startActivity(intent);            });

        ivCreateRequest.setOnClickListener(v -> {
            Intent intent = new Intent(StudentHomePage.this, CreateRequest.class);
            startActivity(intent);            });

        ivCreateBooking.setOnClickListener(v -> {

            if(bookings.size() == maxBookings)
            {
                Utilities.show(getApplicationContext(), "Sorry, Bookings are full at the moment");
            }
            else
            {
                Intent intent = new Intent(StudentHomePage.this, CreateBooking.class);
                startActivity(intent);
            }
        });

        ivFillForm.setOnClickListener(v -> {
            Intent intent = new Intent(StudentHomePage.this, FillForm.class);
            startActivity(intent);            });

        btnYes.setOnClickListener(v -> {

            startActivity(new Intent(getApplicationContext(), Tips.class));
            tipsDialog.dismiss();
        });

        btnNo.setOnClickListener(v -> tipsDialog.dismiss());

        btnLogout.setOnClickListener(v -> {

            Toast.makeText(this, "Logging user out...", Toast.LENGTH_LONG).show();

            FirebaseAuth.getInstance().signOut();//used for logging out the user
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
            logoutDialog.dismiss();
        });

        btnCancelLogout.setOnClickListener(v -> logoutDialog.dismiss());

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
            logoutDialog.show();
        }
        else if (item.getItemId() == R.id.tips)//Displays a dialog box to ask if the user wants tips or not
        {
            tipsDialog.show();
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

                if(bookings.size() == maxBookings)
                {
                    Utilities.show(getApplicationContext(), "Sorry, Bookings are full at the moment");
                }
                else
                {
                    startActivity(new Intent(getApplicationContext(), CreateBooking.class));
                }
                break;

            case R.id.nav_fill_form:

                startActivity(new Intent(getApplicationContext(), FillForm.class));
                break;

//            case R.id.nav_login:
//
//                startActivity(new Intent(getApplicationContext(), Login.class));
//                finish();
//                break;

            case R.id.nav_profile:

                startActivity(new Intent(getApplicationContext(), StudentProfile.class));
                break;

            case R.id.nav_logout:

                logoutDialog.show();
                break;
        }

        return true;

    }


    private void readData(FireBaseCallBack fireBaseCallBack) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Bookings");
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
                    Collections.sort(List, Announcement.sort);
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