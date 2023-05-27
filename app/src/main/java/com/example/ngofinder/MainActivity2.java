package com.example.ngofinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;

import android.widget.CalendarView;
import android.widget.Toast;

import com.example.ngofinder.Adapter.EventCal;
import com.example.ngofinder.Model.EventModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        CalendarView calendarView = findViewById(R.id.calendarView);


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();


// Get a reference to the current user's events in the database
                // Get a reference to the current user's events in the database
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                String userId = currentUser.getUid();
                DatabaseReference userEventsRef = rootRef.child("Users").child(userId).child("EventsRegistered");

// Query the database to retrieve events for all IDs and EVs
                userEventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<EventModel> eventsList = new ArrayList<>();
                        for (DataSnapshot idSnapshot : dataSnapshot.getChildren()) {
                            for (DataSnapshot evSnapshot : idSnapshot.getChildren()) {
                                EventModel event = evSnapshot.getValue(EventModel.class);
                                eventsList.add(event);
                            }
                        }
                        RecyclerView recyclerView = findViewById(R.id.events_recycler_view);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity2.this));
                        recyclerView.setAdapter(new EventCal(eventsList));



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle the error
                    }
                });

            }
        });
    }
}