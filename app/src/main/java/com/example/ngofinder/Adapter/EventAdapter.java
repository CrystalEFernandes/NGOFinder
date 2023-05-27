package com.example.ngofinder.Adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.ngofinder.CommentActivity;
import com.example.ngofinder.Model.EventModel;
import com.example.ngofinder.Model.Notification;
import com.example.ngofinder.Model.PostModel;
import com.example.ngofinder.R;
import com.example.ngofinder.User;
import com.example.ngofinder.databinding.DashboardRvSampleBinding;
import com.example.ngofinder.databinding.EventRvBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class EventAdapter extends FirebaseRecyclerAdapter<EventModel, EventViewHolder> {
    public EventAdapter(@NonNull FirebaseRecyclerOptions<EventModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull EventModel model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_rv, parent, false);
        return new EventViewHolder(view);
    }
}

