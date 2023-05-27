package com.example.ngofinder.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.ngofinder.Model.EventModel;
import com.example.ngofinder.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyCardStackAdapter extends ArrayAdapter<EventModel> {
    public List<EventModel> mEventList;
    public Set<Integer> mSwipedPositions;

    public MyCardStackAdapter(@NonNull Context context, int resource, @NonNull List<EventModel> events) {
        super(context, resource, events);
        mEventList = events;
        mSwipedPositions = new HashSet<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.card_layout, parent, false);
        }

        TextView eventNameTextView = view.findViewById(R.id.eventn);
        TextView dateTextView = view.findViewById(R.id.eventd);
        TextView locationTextView = view.findViewById(R.id.eventl);
        TextView limitTextView = view.findViewById(R.id.eventlim);

        EventModel event = mEventList.get(position);
        eventNameTextView.setText(event.getEventname());
        dateTextView.setText(event.getDate());
        locationTextView.setText(event.getLocation());
        limitTextView.setText(String.valueOf(event.getLimit()));

        return view;
    }

}
