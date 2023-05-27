package com.example.ngofinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ngofinder.Model.CharityModel;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Charities_list extends AppCompatActivity {
    private RecyclerView FindCharity;
    private DatabaseReference DONRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charities_list);

        DONRef = FirebaseDatabase.getInstance().getReference().child("Donations");
        FindCharity = (RecyclerView) findViewById(R.id.reccc);
        FindCharity.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<CharityModel> options =
                new FirebaseRecyclerOptions.Builder<CharityModel>()
                        .setQuery(DONRef, CharityModel.class)
                        .build();

        FirebaseRecyclerAdapter<CharityModel, Charities_list.FindFriendViewHolder> adapter =
                new FirebaseRecyclerAdapter<CharityModel, Charities_list.FindFriendViewHolder>(options) {
                    @Override
                    public void onBindViewHolder(@NonNull Charities_list.FindFriendViewHolder holder, int position, @NonNull CharityModel model)
                    {
                        holder.name.setText(model.getName());
                        holder.description.setText(model.getDescription());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                String charity_id = getRef(position).getKey();
                                Intent detailIntent = new Intent(Charities_list.this, Donation_page.class);
                                detailIntent.putExtra("charity_id", charity_id);
                                view.getContext().startActivity(detailIntent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public Charities_list.FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                    {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_charity, viewGroup, false);
                        Charities_list.FindFriendViewHolder viewHolder = new Charities_list.FindFriendViewHolder(view);
                        return viewHolder;
                    }
                };

        FindCharity.setAdapter(adapter);

        adapter.startListening();
    }

    public static class FindFriendViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,description;
         public FindFriendViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name = itemView.findViewById(R.id.nameCha);
            description=itemView.findViewById(R.id.desCha);
        }
    }
}