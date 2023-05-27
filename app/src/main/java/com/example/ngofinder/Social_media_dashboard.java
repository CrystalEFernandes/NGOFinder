package com.example.ngofinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.ngofinder.Fragment.AddFragment;
import com.example.ngofinder.Fragment.HomeFragment;
import com.example.ngofinder.Fragment.NotificationFragment;
import com.example.ngofinder.Fragment.ProfileFragment;
import com.example.ngofinder.Fragment.SearchFragment;
import com.example.ngofinder.databinding.ActivityMainBinding;
import com.example.ngofinder.databinding.ActivitySocialMediaDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.iammert.library.readablebottombar.ReadableBottomBar;

public class Social_media_dashboard extends AppCompatActivity {
    ActivitySocialMediaDashboardBinding binding;
    FirebaseAuth auth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySocialMediaDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Social_media_dashboard.this.setTitle("My Profile");

        //because home page not visible on first click
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        binding.toolbar.setVisibility(View.GONE);
        transaction.replace(R.id.content, new HomeFragment());
        transaction.commit();

        //change fragments using switch case
        binding.readableBottomBar.setOnItemSelectListener(new ReadableBottomBar.ItemSelectListener(){
            @Override
            public void onItemSelected(int i) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (i) {
                case 0:
                    transaction.replace(R.id.content, new HomeFragment());
                    binding.toolbar.setVisibility(View.GONE);
                    break;
                case 1:
                    binding.toolbar.setVisibility(View.GONE);
                    transaction.replace(R.id.content, new NotificationFragment());
                    break;
                case 2:
                    binding.toolbar.setVisibility(View.GONE);
                    transaction.replace(R.id.content, new AddFragment());
                    break;
                case 3:
                    transaction.replace(R.id.content, new SearchFragment());
                    binding.toolbar.setVisibility(View.GONE);
                    break;
                case 4:
                    transaction.replace(R.id.content, new ProfileFragment());
                    binding.toolbar.setVisibility(View.VISIBLE);
                    break;

            }
                transaction.commit();
            }

        });
    }
}