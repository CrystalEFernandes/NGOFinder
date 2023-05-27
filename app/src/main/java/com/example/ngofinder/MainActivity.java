package com.example.ngofinder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    Animation topAnim,bottomAnim;
    ImageView imagelogo,image;
    TextView slogan;
    private static int SPLASH_SCREEN=4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        imagelogo=findViewById(R.id.imageView6);
        image=findViewById(R.id.imageView13);
        slogan=findViewById(R.id.textView3);

        imagelogo.setAnimation(topAnim);
        slogan.setAnimation(bottomAnim);
        image.setAnimation(topAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(MainActivity.this,LoginPage.class);
                Pair[] pairs=new Pair[2];
                pairs[0]=new Pair<View,String>(imagelogo,"logo_image");
                pairs[1]=new Pair<View,String>(slogan,"logo_text");
                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                startActivity(intent,options.toBundle());
                finish();
            }
        },SPLASH_SCREEN);
    }
}