package com.example.ngofinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;

import android.view.View;


import com.example.ngofinder.Fragment.HomeFragment;
import com.example.ngofinder.Fragment.NGOFragment;

import com.example.ngofinder.Fragment.NgoReview;
import com.example.ngofinder.databinding.ActivityNgoInfoBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.iammert.library.readablebottombar.ReadableBottomBar;

public class Ngoinfo extends AppCompatActivity {
    ActivityNgoInfoBinding binding;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    String NGOID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNgoInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        NGOID = getIntent().getExtras().get("ngo_id").toString();

        Ngoinfo.this.setTitle(" ");

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


        Bundle bundle=new Bundle();
        bundle.putString("ngo_id",NGOID);
        NGOFragment fraginfo=new NGOFragment();
        fraginfo.setArguments(bundle);
        transaction.replace(R.id.content1, fraginfo);
        NgoReview fraginfo1=new NgoReview();
        fraginfo1.setArguments(bundle);
        transaction.commit();

        binding.readableBar.setOnItemSelectListener(new ReadableBottomBar.ItemSelectListener(){
            @Override
            public void onItemSelected(int i) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (i) {
                    case 0:
                        transaction.replace(R.id.content1, fraginfo);
                        binding.toolbar.setVisibility(View.GONE);
                        break;
                    case 1:
                        binding.toolbar.setVisibility(View.GONE);
                        transaction.replace(R.id.content1, fraginfo1);
                        break;

                }
                transaction.commit();
            }

        });
    }
}