package com.example.ngofinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class Introduction extends AppCompatActivity {

    private ImageSlider imageSlider;
    private Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        imageSlider=findViewById(R.id.image_slider);
        vibrator=(Vibrator) getSystemService(VIBRATOR_SERVICE);


        ArrayList<SlideModel> imageList=new ArrayList<>();
        imageList.add(new SlideModel(R.drawable.joinus_1,null));
        imageList.add(new SlideModel(R.drawable.ngo5,null));
        imageList.add(new SlideModel(R.drawable.ngo3,null));
        imageList.add(new SlideModel(R.drawable.notifs,null));


        imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP);

        ImageButton b3 = (ImageButton)findViewById(R.id.getstartedblock);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(100);
                openActivity();
            }
        });
    }

    private void openActivity(){
        Intent intent=new Intent(this,LoginPage.class);
        startActivity(intent);
    }
}
