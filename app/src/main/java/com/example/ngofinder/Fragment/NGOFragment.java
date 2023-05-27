package com.example.ngofinder.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.ngofinder.Adapter.EventAdapter;

import com.example.ngofinder.MessageActivity;
import com.example.ngofinder.Model.EventModel;

import com.example.ngofinder.Model.NGOModel;


import com.example.ngofinder.R;

import com.example.ngofinder.event_info;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;

public class NGOFragment extends Fragment {

    FirebaseDatabase database;
    String NGOID,EVENTID,event_id;
    private RecyclerView recyclerView;
    private EventAdapter adapter;

    public NGOFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentngo, container, false);
        recyclerView = view.findViewById(R.id.eventrv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);


        Bundle bundle = this.getArguments();
        NGOID = bundle.getString("ngo_id");

        database = FirebaseDatabase.getInstance();

        ImageView ngoimage = view.findViewById(R.id.ngoimage);
        TextView ngoname = view.findViewById(R.id.NGOname);
        TextView ngodescription = view.findViewById(R.id.NGOdescription);
        ImageButton b1 = view.findViewById(R.id.chattt);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                intent.putExtra("ngo_id", NGOID);
                view.getContext().startActivity(intent);
            }
        });

        database.getReference().child("NGOS").child(NGOID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                NGOModel ngo = snapshot.getValue(NGOModel.class);
                Picasso.get().load(ngo.getLink()).placeholder(R.drawable.placeholder).into(ngoimage);
                ngoname.setText(ngo.getName());
                ngodescription.setText(ngo.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<EventModel> options = new FirebaseRecyclerOptions.Builder<EventModel>()
                .setQuery(database.getReference().child("NGOS").child(NGOID).child("Events"), EventModel.class)
                .build();

        FirebaseRecyclerAdapter<EventModel, EventViewHolder> adapter =
                new FirebaseRecyclerAdapter<EventModel, EventViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull EventModel model) {
                        // Bind the data to the views in the RecyclerView's item
                        holder.eventName.setText(model.getEventname());
                        holder.eventLocation.setText(model.getLocation());


                        // Set the onClick listener to the RecyclerView's item
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                event_id = getRef(position).getKey();
                                Intent intent = new Intent(getActivity(), event_info.class);
                                intent.putExtra("ngo_id", NGOID);
                                intent.putExtra("event_id", event_id);
                                view.getContext().startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        // Inflate the layout for the RecyclerView's item
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_rv, parent, false);
                        return new EventViewHolder(view);
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView eventName,eventLocation;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventLocation = itemView.findViewById(R.id.eventlocation);
            eventName = itemView.findViewById(R.id.eventname);
        }
    }

}

