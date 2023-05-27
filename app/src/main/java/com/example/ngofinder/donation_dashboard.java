package com.example.ngofinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class donation_dashboard extends AppCompatActivity {

    CardView reg_char,care_pack,stats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_dashboard);
        reg_char=findViewById(R.id.cardView);
        care_pack=findViewById(R.id.cardView2);
        stats=findViewById(R.id.cardView3);

        reg_char.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(donation_dashboard.this, Charities_list.class);
                startActivity(intent);
            }
        });

        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(donation_dashboard.this, statistics.class);
                startActivity(intent);
            }
        });

    }
}