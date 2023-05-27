package com.example.ngofinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class myappliedEvents extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> eventList;
    FirebaseAuth auth;
    FirebaseUser user;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myapplied_events);

        listView = findViewById(R.id.list_view_events);
        eventList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        uid = user.getUid();

        DatabaseReference ngosRef = FirebaseDatabase.getInstance().getReference().child("NGOS");

        Query userEventsQuery = ngosRef.orderByKey();
        userEventsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {

                for (DataSnapshot eventSnapshot : dataSnapshot.child("Events").getChildren()) {
                    if (eventSnapshot.hasChild("Registered Users")) { // check if the node exists
                        for (DataSnapshot userSnapshot : eventSnapshot.child("Registered Users").getChildren()) {
                            if (userSnapshot.getKey().equals(uid)) {
                                String eventName = eventSnapshot.child("eventname").getValue(String.class);
                                Log.d("EVENT", "Event name: " + eventName);
                                String eventStatus = userSnapshot.child("status").getValue(String.class);
                                String eventString = "Event Name: " + eventName + " Status: " + eventStatus;
                                eventList.add(eventString);
                            }
                        }
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(myappliedEvents.this, android.R.layout.simple_list_item_1, eventList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Handle any changes to the data
            }


            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // Handle any removed data
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Handle any moved data
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that may occur
            }

        });
    }
}
