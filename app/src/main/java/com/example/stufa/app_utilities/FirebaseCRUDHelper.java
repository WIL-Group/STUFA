package com.example.stufa.app_utilities;

import android.util.Log;

import com.example.stufa.data_models.Announcement;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.stufa.app_utilities.Utilities.DataCache;

public class FirebaseCRUDHelper
{
    public void select(final AppCompatActivity a, DatabaseReference db,
                       //final ProgressBar pb,
                       final RecyclerView rv, AnnouncementAdapter adapter)
    {
        //Utilities.showProgressBar(pb);

        db.child("Announcements");

        db.child("Announcements").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataCache.clear();
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //Now get Announcement Objects and populate our arraylist.
                        Announcement announcement = ds.getValue(Announcement.class);
                        announcement.setKey(ds.getKey());
                        DataCache.add(announcement);
                    }
                    adapter.notifyDataSetChanged();
                    new Handler() {
                        public void post(Runnable runnable) {
                        }

                        @Override
                        public void publish(LogRecord logRecord) {

                        }

                        @Override
                        public void flush() {

                        }

                        @Override
                        public void close() throws SecurityException {

                        }
                    }.post(new Runnable() {
                        @Override
                        public void run() {
                            //Utilities.hideProgressBar(pb);
                            rv.smoothScrollToPosition(DataCache.size());
                        }
                    });
                }
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //Now get announcement Objects and populate our arraylist.
                    Announcement announcement = ds.getValue(Announcement.class);
                    announcement.setKey(ds.getKey());
                    DataCache.add(announcement);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FIREBASE CRUD", error.getMessage());
                //Utilities.hideProgressBar(pb);
                Utilities.show(a, "CANCELLED" + error.getMessage());
            }
        });
    }

}
