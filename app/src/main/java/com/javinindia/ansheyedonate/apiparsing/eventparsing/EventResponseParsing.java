package com.javinindia.ansheyedonate.apiparsing.eventparsing;

import android.util.Log;

import com.javinindia.ansheyedonate.apiparsing.base.ApiBaseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish on 30-03-2017.
 */

public class EventResponseParsing extends ApiBaseData {

    ArrayList<DetailEventItem> detailEventItems;

    public ArrayList<DetailEventItem> getDetailEventItems() {
        return detailEventItems;
    }

    public void setDetailEventItems(ArrayList<DetailEventItem> detailEventItems) {
        this.detailEventItems = detailEventItems;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            setMsg(jsonObject.optString("msg"));
            setStatus(jsonObject.optInt("status"));
            if (jsonObject.optInt("status") == 1 && jsonObject.has("details"))
                setDetailEventItems(getDetailInMethod(jsonObject.optJSONArray("details")));

            Log.d("Response", this.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<DetailEventItem> getDetailInMethod(JSONArray details) {
        ArrayList<DetailEventItem> detailsArrayList = new ArrayList<>();
        for (int i = 0; i < details.length(); i++) {
            DetailEventItem wishDetails = new DetailEventItem();
            JSONObject jsonObject = details.optJSONObject(i);

            if (jsonObject.has("eventdetails"))
                wishDetails.setEventdetails(getEventDetailsMethod(jsonObject.optJSONObject("eventdetails")));
            if (jsonObject.has("event_details_images"))
                wishDetails.setEvent_details_images(getEventImageMethod(jsonObject.optJSONArray("event_details_images")));

            detailsArrayList.add(wishDetails);
            Log.d("Response 2", detailsArrayList.toString());
        }
        return detailsArrayList;
    }

    private ArrayList<Event_details_images> getEventImageMethod(JSONArray jsonArray) {
        ArrayList<Event_details_images> postImageArrayList = new ArrayList<>();
        Event_details_images postImage;
        for (int i = 0; i < jsonArray.length(); i++) {
            postImage = new Event_details_images();
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject.has("id"))
                postImage.setId(jsonObject.optString("id"));
            if (jsonObject.has("event_id"))
                postImage.setEvent_id(jsonObject.optString("event_id"));
            if (jsonObject.has("type"))
                postImage.setType(jsonObject.optString("type"));
            if (jsonObject.has("image_url"))
                postImage.setImage_url(jsonObject.optString("image_url"));
            if (jsonObject.has("insertDate"))
                postImage.setInsertDate(jsonObject.optString("insertDate"));
            if (jsonObject.has("updateDate"))
                postImage.setUpdateDate(jsonObject.optString("updateDate"));

            postImageArrayList.add(postImage);
        }
        return postImageArrayList;
    }

    private Eventdetails getEventDetailsMethod(JSONObject jsonObject) {
        Eventdetails eventDetails = new Eventdetails();
        if (jsonObject.has("event_id"))
            eventDetails.setEvent_id(jsonObject.optString("event_id"));
        if (jsonObject.has("event_name"))
            eventDetails.setEvent_name(jsonObject.optString("event_name"));
        if (jsonObject.has("event_date"))
            eventDetails.setEvent_date(jsonObject.optString("event_date"));
        if (jsonObject.has("event_time"))
            eventDetails.setEvent_time(jsonObject.optString("event_time"));
        if (jsonObject.has("description"))
            eventDetails.setDescription(jsonObject.optString("description"));
        if (jsonObject.has("insertDate"))
            eventDetails.setInsertDate(jsonObject.optString("insertDate"));
        if (jsonObject.has("updateDate"))
            eventDetails.setUpdateDate(jsonObject.optString("updateDate"));
        if (jsonObject.has("status"))
            eventDetails.setStatus(jsonObject.optString("status"));
        if (jsonObject.has("address"))
            eventDetails.setAddress(jsonObject.optString("address"));

        return eventDetails;

    }
}
