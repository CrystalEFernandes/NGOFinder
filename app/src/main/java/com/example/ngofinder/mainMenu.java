package com.example.ngofinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class mainMenu extends AppCompatActivity {
    ImageButton socialmedia, allngos, donationportal, swipething, maps, calendar, history;
    private ImageView profileImage;
    private TextView fullName;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        socialmedia = findViewById(R.id.socialmedia);
        allngos = findViewById(R.id.allngos);
        swipething = findViewById(R.id.swipething);
        donationportal = findViewById(R.id.donationportal);
        maps = findViewById(R.id.maps);
        calendar = findViewById(R.id.calendar);
        history = findViewById(R.id.history);
        profileImage = findViewById(R.id.profileImage);
        fullName = findViewById(R.id.fulll);
        vibrator=(Vibrator) getSystemService(VIBRATOR_SERVICE);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    Picasso.get().load(user.getProfile()).placeholder(R.drawable.placeholder).into(profileImage);
                    fullName.setText(user.getName()+"!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });


        socialmedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                Intent intent = new Intent(mainMenu.this, Social_media_dashboard.class);
                startActivity(intent);
            }
        });

        allngos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                Intent intent = new Intent(mainMenu.this, ngo_dashboard.class);
                startActivity(intent);
            }
        });

        donationportal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                Intent intent = new Intent(mainMenu.this, donation_dashboard.class);
                startActivity(intent);
            }
        });

        swipething.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                Intent intent = new Intent(mainMenu.this, cards.class);
                startActivity(intent);
            }
        });

        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                Intent intent = new Intent(mainMenu.this, Recommend_Me.class);
                startActivity(intent);
            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                Intent intent = new Intent(mainMenu.this, MainActivity2.class);
                startActivity(intent);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                Intent intent = new Intent(mainMenu.this, myappliedEvents.class);
                startActivity(intent);
            }
        });
    }


}