package com.example.ngofinder.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ngofinder.Model.EventModel;
import com.example.ngofinder.R;

import java.util.List;

public class EventCal extends RecyclerView.Adapter<EventCal.EventViewHolder> {

    private List<EventModel> eventsList;

    public EventCal(List<EventModel> eventsList) {
        this.eventsList = eventsList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        EventModel event = eventsList.get(position);
        holder.eventName.setText(event.getEventname());
        holder.eventLocation.setText(event.getLocation());
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        public TextView eventName;
        public TextView eventLocation;

        public EventViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_name);
            eventLocation = itemView.findViewById(R.id.event_location);
        }
    }
}

