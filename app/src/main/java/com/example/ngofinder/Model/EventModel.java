package com.example.ngofinder.Model;

public class EventModel {
    public EventModel() {
    }

    String image;
    String eventname;
    String date;
    String time;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    String description;

    public String getEVENTID() {
        return EVENTID;
    }

    public void setEVENTID(String EVENTID) {
        this.EVENTID = EVENTID;
    }

    public String getNGOID() {
        return NGOID;
    }

    public void setNGOID(String NGOID) {
        this.NGOID = NGOID;
    }

    String EVENTID;
    String NGOID;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    int limit;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    String location;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }



    public EventModel(String image) {
        this.image = image;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
