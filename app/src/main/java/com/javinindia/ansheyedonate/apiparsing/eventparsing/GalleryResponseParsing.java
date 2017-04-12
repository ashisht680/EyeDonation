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

public class GalleryResponseParsing extends ApiBaseData {
    private ArrayList<Event_details_images> detailsArrayList;

    public ArrayList<Event_details_images> getDetailsArrayList() {
        return detailsArrayList;
    }

    public void setDetailsArrayList(ArrayList<Event_details_images> detailsArrayList) {
        this.detailsArrayList = detailsArrayList;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            setMsg(jsonObject.optString("msg"));
            setStatus(jsonObject.optInt("status"));
            if (jsonObject.optInt("status") == 1 && jsonObject.has("details") && jsonObject.optJSONArray("details") != null)
                setDetailsArrayList(getDetailMethod(jsonObject.optJSONArray("details")));

            Log.d("Response", this.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Event_details_images> getDetailMethod(JSONArray details) {
        ArrayList<Event_details_images> logDetail = new ArrayList<>();
        for (int i = 0; i < details.length(); i++) {
            Event_details_images postImage = new Event_details_images();
            JSONObject jsonObject = details.optJSONObject(i);

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

            logDetail.add(postImage);
            Log.d("Response", logDetail.toString());
        }
        return logDetail;
    }
}
