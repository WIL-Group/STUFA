package com.example.stufa.activities;

import android.os.Bundle;
import android.widget.ProgressBar;

import com.example.stufa.R;
import com.example.stufa.app_utilities.AnnouncementAdapter;
import com.example.stufa.app_utilities.FirebaseCRUDHelper;
import com.example.stufa.app_utilities.QueryAdapter;
import com.example.stufa.app_utilities.Utilities;
import com.example.stufa.data_models.Announcement;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AnnouncementBrowsing extends AppCompatActivity implements AnnouncementAdapter.ItemClickListener{

    AnnouncementAdapter adapter;
    ArrayList<Announcement> announcements;
    Announcement announcement;
    private RecyclerView rv;
    public ProgressBar mProgressBar;
    private FirebaseCRUDHelper crudHelper=new FirebaseCRUDHelper();
    private LinearLayoutManager layoutManager;
    String id;
    Boolean viewed = false;
    DatabaseReference announcementRef;
    Query query;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcement_browsing);

        announcements = new ArrayList<>();
        rv = findViewById(R.id.aList);
        rv.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(AnnouncementBrowsing.this);
        rv.setLayoutManager(layoutManager);

        announcementRef = FirebaseDatabase.getInstance().getReference().child("Announcements");


        readData(List -> {
            announcements = List;
            adapter = new AnnouncementAdapter(AnnouncementBrowsing.this,announcements);
            rv.setAdapter(adapter);
        });
        /*date = new SimpleDateFormat("dd MM, yyyy", Locale.getDefault()).format(new Date());

        announcement = new Announcement(id,"Ditaba","Corona closes schools",date,viewed);

        announcementRef.push().setValue(announcement);
        for(int i = 0; i < announcements.size(); i++)
        {
            id = i + announcement.getMessage().charAt(2)+ "";
            announcement = announcements.get(i);
            announcement.setaId(id);
        }*/
    }

    @Override
    public void onItemClick(int pos) {
        if(pos >= 0)
        {
            announcement = announcements.get(pos);
            announcement.setViewed(true);
            update();
        }
        else
        {
            Utilities.show(AnnouncementBrowsing.this, "Pos is" + pos);
        }

    }

    private void update()
    {
        String toUpdate = announcement.getaId();

        query = announcementRef.orderByChild("aId").equalTo(toUpdate);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    ds.getRef().setValue(announcement);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readData(FireBaseCallBack fireBaseCallBack)
    {


        announcementRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Announcement> List = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    announcement = ds.getValue(Announcement.class);
                    List.add(announcement);
                }

                fireBaseCallBack.onCallBack(List);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private interface FireBaseCallBack
    {
        void onCallBack(ArrayList<Announcement> List);
    }
}