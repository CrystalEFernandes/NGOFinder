package com.example.ngofinder.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ngofinder.Adapter.ReviewAdapter;

import com.example.ngofinder.Model.NGOModel;

import com.example.ngofinder.Model.ReviewModel;
import com.example.ngofinder.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NgoReview extends Fragment {
    FirebaseDatabase database;
    String NGOID;
    private RecyclerView recyclerView;
    ArrayList<ReviewModel> list = new ArrayList<>();
    RatingBar ratingBar,ratingFinal;

    public NgoReview() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState){
        View view =  inflater.inflate(R.layout.ngoreview, container, false);

        recyclerView = view.findViewById(R.id.reviewrv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);


        Bundle bundle=this.getArguments();
        NGOID=bundle.getString("ngo_id");

        database = FirebaseDatabase.getInstance();

        ImageView ngoimage = view.findViewById(R.id.NGOIMAGE);

        Button commentPostBtn = view.findViewById(R.id.commentPostBtn);
        TextView ngoname = view.findViewById(R.id.NGONAME);
        TextView rate = view.findViewById(R.id.rate);

        EditText commentET=view.findViewById(R.id.commentET);
        ratingBar = view.findViewById(R.id.rate_star);
        ratingFinal = view.findViewById(R.id.rate_final);




        commentPostBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                float star= ratingBar.getRating();

                ReviewModel review = new ReviewModel();
                review.setReviewbody(commentET.getText().toString());
                review.setReviewedAt(new Date().getTime());
                review.setReviewedBy(FirebaseAuth.getInstance().getUid());
                review.setStars(Float.toString(star));

                database.getReference()
                        .child("NGOS")
                        .child(NGOID)
                        .child("Reviews")
                        .push()
                        .setValue(review);


            }

        });


        database.getReference().child("NGOS").child(NGOID).child("Reviews").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    float totalStars = 0;
                    int numReviews = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.child("stars").getValue() != null) {
                            float stars = Float.parseFloat(snapshot.child("stars").getValue().toString());
                            totalStars += stars;
                            numReviews++;
                        }
                    }
                    if (numReviews > 0) {
                        float averageStars = (float) totalStars / numReviews;
                        rate.setText(Float.toString(averageStars));
                        ratingFinal.setRating(averageStars);
                        // Use the averageStars value to display in the pie chart or anywhere else in your app.
                    } else {
                        // Handle the case where there are no reviews for this node.
                    }
                } else {
                    // Handle the case where there is no data to display.
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors here
            }
        });




        database.getReference().child("NGOS").child(NGOID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                NGOModel ngo = snapshot.getValue(NGOModel.class);
                Picasso.get().load(ngo.getLink()).placeholder(R.drawable.placeholder).into(ngoimage);
                ngoname.setText(ngo.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        RecyclerView reviewrv=view.findViewById(R.id.reviewrv);
        ReviewAdapter adapter = new ReviewAdapter(getContext(), list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        reviewrv.setLayoutManager(layoutManager);
        reviewrv.setAdapter(adapter);

        database.getReference()
                .child("NGOS").child(NGOID)
                .child("Reviews").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            ReviewModel review = dataSnapshot.getValue(ReviewModel.class);
                            list.add(review);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return view;
    }

}
