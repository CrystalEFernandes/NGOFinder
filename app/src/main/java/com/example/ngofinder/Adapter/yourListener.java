package com.example.ngofinder.Adapter;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ngofinder.Model.EventModel;
import com.example.ngofinder.Model.RegisteredUserModel;
import com.example.ngofinder.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wenchao.cardstack.CardStack;

public class yourListener implements CardStack.CardEventListener {
        private String ngoId;
        private String eventId;
        String name, status;
        FirebaseDatabase database;
        FirebaseAuth auth;
        private Context context;
        private MyCardStackAdapter mAdapter;

        public yourListener(String ngoId, String eventId,Context context,MyCardStackAdapter adapter) {
                this.ngoId = ngoId;
                this.eventId = eventId;
                this.context = context;
                this.mAdapter = adapter;
        }
        @Override
        public boolean swipeEnd(int direction, float distance) {
                if(direction==1 || direction==3)
                {
                        registerforEvent(ngoId, eventId);
                }else {
                        Toast.makeText(context, "Ah! Next Time Surely?!", Toast.LENGTH_SHORT).show();
                }

                return (distance > 300) ? true : false;
        }

        @Override
        public boolean swipeStart(int direction, float distance) {
                return true;
        }

        @Override
        public boolean swipeContinue(int direction, float distanceX, float distanceY) {

                return true;
        }

        @Override
        public void discarded(int id, int direction) {
                int currentPosition = mAdapter.getCount() - 1; // Get the current card position
                mAdapter.mSwipedPositions.add(currentPosition); // Add the position to the set of swiped positions
                mAdapter.mEventList.remove(currentPosition); // Remove the swiped card from the list
                mAdapter.notifyDataSetChanged(); // Notify the adapter that the data set has changed
        }

        @Override
        public void topCardTapped() {
                // This callback invoked when a top card is tapped by user.
        }

        private void registerforEvent(String ngoId, String eventId) {
                // Get database reference
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference eventRef = database.getReference().child("NGOS").child(ngoId).child("Events").child(eventId);

                eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                EventModel event = snapshot.getValue(EventModel.class);
                                long count = snapshot.child("Registered Users").getChildrenCount();
                                int limit = event.getLimit();

                                if (count < limit) {
                                        // Get user details
                                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        database.getReference().child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        User user = snapshot.getValue(User.class);
                                                        name = user.getName();
                                                        status = "Approved";

                                                        // Create registered user object and add to database
                                                        RegisteredUserModel reg = new RegisteredUserModel(name, status);
                                                        reg.setTimeStamp(System.currentTimeMillis());
                                                        database.getReference().child("NGOS").child(ngoId).child("Events").child(eventId).child("Registered Users").child(userId).setValue(reg);

                                                        // Update user's registered events
                                                        database.getReference().child("Users").child(userId).child("EventsRegistered").child(ngoId).child(eventId).setValue(event);

                                                        Toast.makeText(context, "You have successfully registered", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                        // Handle any errors
                                                }
                                        });
                                } else {
                                        Toast.makeText(context, "Limit reached! Unsuccessful", Toast.LENGTH_SHORT).show();
                                }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                                // Handle any errors
                        }
                });
        }


}
