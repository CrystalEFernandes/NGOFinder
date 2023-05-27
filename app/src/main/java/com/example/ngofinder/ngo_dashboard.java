package com.example.ngofinder;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ngofinder.Model.NGOModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ngo_dashboard extends AppCompatActivity
{
    private RecyclerView FindNGO;
    private DatabaseReference NGORef;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_dashboard);


        NGORef = FirebaseDatabase.getInstance().getReference().child("NGOS");


        FindNGO = (RecyclerView) findViewById(R.id.recview);
        FindNGO.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }


    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<NGOModel> options =
                new FirebaseRecyclerOptions.Builder<NGOModel>()
                        .setQuery(NGORef, NGOModel.class)
                        .build();

        FirebaseRecyclerAdapter<NGOModel, FindFriendViewHolder> adapter =
                new FirebaseRecyclerAdapter<NGOModel, FindFriendViewHolder>(options) {
                    @Override
                    public void onBindViewHolder(@NonNull FindFriendViewHolder holder, int position, @NonNull NGOModel model)
                    {
                        holder.name.setText(model.getName());
                        holder.type.setText(model.getType());
                        Glide.with(holder.img.getContext()).load(model.getLink()).into(holder.img);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                String ngo_id = getRef(position).getKey();
                                Intent detailIntent = new Intent(ngo_dashboard.this, Ngoinfo.class);
                                detailIntent.putExtra("ngo_id", ngo_id);
                                view.getContext().startActivity(detailIntent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                    {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ngorv, viewGroup, false);
                        FindFriendViewHolder viewHolder = new FindFriendViewHolder(view);
                        return viewHolder;
                    }
                };

        FindNGO.setAdapter(adapter);

        adapter.startListening();
    }



    public static class FindFriendViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,type;
        ImageView img;


        public FindFriendViewHolder(@NonNull View itemView)
        {
            super(itemView);

            name = itemView.findViewById(R.id.ngoname);
            img = itemView.findViewById(R.id.ngoimg);
            type=itemView.findViewById(R.id.ngotype);
        }
    }
}