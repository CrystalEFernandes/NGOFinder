package com.example.ngofinder;

import com.example.ngofinder.Adapter.MyCardStackAdapter;
import com.example.ngofinder.Adapter.yourListener;
import com.example.ngofinder.Model.EventModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wenchao.cardstack.CardStack;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class cards extends AppCompatActivity {
    CardStack mCardStack;
    MyCardStackAdapter mCardAdapter;
    List<String> eventNames = new ArrayList<>();
    List<EventModel> mEvents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        mCardStack = (CardStack)findViewById(R.id.container);
        mCardStack.setContentResource(R.layout.card_layout);
        mCardStack.setStackMargin(20);
        mCardAdapter = new MyCardStackAdapter(getApplicationContext(),0,mEvents);

        // Fetch event names from Firebase and add them to the adapter
        FirebaseDatabase.getInstance().getReference().child("NGOS")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean hasEvents = false;
                        for (DataSnapshot ngoSnapshot : dataSnapshot.getChildren()) {
                            String ngoId = ngoSnapshot.getKey(); // get the NGO ID
                            for (DataSnapshot eventSnapshot : ngoSnapshot.child("Events").getChildren()) {
                                EventModel event = eventSnapshot.getValue(EventModel.class);
                                String eventId = eventSnapshot.getKey(); // get the Event ID
                                event.setEVENTID(eventId); // set the Event ID in the EventModel object
                                event.setNGOID(ngoId); // set the NGO ID in the EventModel object
                                mEvents.add(event);
                                hasEvents = true;
                            }
                        }
                        if (hasEvents) {
                            mCardAdapter.addAll(mEvents);
                            mCardStack.setAdapter(mCardAdapter);
                            // Set yourListener with the ngoId and eventId of the first event in the list
                            if (!mEvents.isEmpty()) {
                                EventModel firstEvent = mEvents.get(0);
                                yourListener yourListener = new yourListener(firstEvent.getNGOID(), firstEvent.getEVENTID(),getApplicationContext(),mCardAdapter);
                                mCardStack.setListener(yourListener);
                            }
                        } else {
                            // Display message to user that there are no events to display
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle error
                    }
                });
    }
}
