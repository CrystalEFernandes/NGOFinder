package com.example.ngofinder.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ngofinder.Model.EventModel;
import com.example.ngofinder.R;
import com.squareup.picasso.Picasso;

public class EventViewHolder extends RecyclerView.ViewHolder {
    private TextView nameTextView;
    private ImageView image;


    public EventViewHolder(@NonNull View itemView) {
        super(itemView);
        nameTextView = itemView.findViewById(R.id.eventname);
        image = itemView.findViewById(R.id.eventimage);

    }
    public void bind(EventModel event) {
        nameTextView.setText(event.getEventname());
        Picasso.get().load(event.getImage()).placeholder(R.drawable.placeholder).into(image);

    }
}
