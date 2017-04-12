package com.javinindia.ansheyedonate.apiparsing.eventparsing;

import java.util.ArrayList;

/**
 * Created by Ashish on 30-03-2017.
 */

public class DetailEventItem {

    private Eventdetails eventdetails;
    private ArrayList<Event_details_images> event_details_images;

    public ArrayList<Event_details_images> getEvent_details_images() {
        return event_details_images;
    }

    public void setEvent_details_images(ArrayList<Event_details_images> event_details_images) {
        this.event_details_images = event_details_images;
    }

    public Eventdetails getEventdetails() {
        return eventdetails;
    }

    public void setEventdetails(Eventdetails eventdetails) {
        this.eventdetails = eventdetails;
    }
}
