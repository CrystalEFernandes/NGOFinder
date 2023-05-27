package com.example.ngofinder;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ngofinder.Model.NGOModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {

    FirebaseDatabase database;
    EditText message;
    Button sendbtn;
    String messagestr, phonestr = "",NGOID;
    private ImageSlider imageSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        NGOID = getIntent().getExtras().get("ngo_id").toString();

        database = FirebaseDatabase.getInstance();
        message = findViewById(R.id.message);
        sendbtn = findViewById(R.id.sendbtn);
        imageSlider=findViewById(R.id.image_slider);

        ArrayList<SlideModel> imageList=new ArrayList<>();
        imageList.add(new SlideModel(R.drawable.joinus_1,null));
        imageList.add(new SlideModel(R.drawable.ngo5,null));
        imageList.add(new SlideModel(R.drawable.ngo3,null));
        imageList.add(new SlideModel(R.drawable.notifs,null));

        imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP);

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getReference()
                        .child("NGOS").child(NGOID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                NGOModel model = snapshot.getValue(NGOModel.class);
                                if (model != null) {
                                    phonestr = model.getnumber();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                messagestr = message.getText().toString();

                if (!messagestr.isEmpty() ) {

                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone="+phonestr+
                                "&text="+messagestr));
                        startActivity(i);
                        message.setText("");

                } else {

                    Toast.makeText(MessageActivity.this, "Please fill in the message it can't be empty", Toast.LENGTH_LONG).show();
                }

            }
        });


    }
}