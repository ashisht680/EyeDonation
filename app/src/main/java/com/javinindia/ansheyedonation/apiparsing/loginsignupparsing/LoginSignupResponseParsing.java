package com.javinindia.ansheyedonation.apiparsing.loginsignupparsing;


import android.util.Log;

import com.javinindia.ansheyedonation.apiparsing.base.ApiBaseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish on 21-09-2016.
 */
public class LoginSignupResponseParsing extends ApiBaseData {
    private String pic;
    private ArrayList<Details> detailsArrayList;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public ArrayList<Details> getDetailsArrayList() {
        return detailsArrayList;
    }

    public void setDetailsArrayList(ArrayList<Details> detailsArrayList) {
        this.detailsArrayList = detailsArrayList;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            setMsg(jsonObject.optString("msg"));
            setStatus(jsonObject.optInt("status"));
            if (jsonObject.has("pic"))
                setPic(jsonObject.optString("pic"));
            if (jsonObject.optInt("status") == 1 && jsonObject.has("details") && jsonObject.optJSONArray("details") != null)
                setDetailsArrayList(getDetailMethod(jsonObject.optJSONArray("details")));

            Log.d("Response", this.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Details> getDetailMethod(JSONArray details) {
        ArrayList<Details> logDetail = new ArrayList<>();
        for (int i = 0; i < details.length(); i++) {
            Details info = new Details();
            JSONObject jsonObject = details.optJSONObject(i);

            if (jsonObject.has("profileId"))
                info.setProfileId(jsonObject.optString("profileId"));
            if (jsonObject.has("profileName"))
                info.setProfileName(jsonObject.optString("profileName"));
            if (jsonObject.has("email"))
                info.setEmail(jsonObject.optString("email"));
            if (jsonObject.has("password"))
                info.setPassword(jsonObject.optString("password"));
            if (jsonObject.has("phone"))
                info.setPhone(jsonObject.optString("phone"));
            if (jsonObject.has("gender"))
                info.setGender(jsonObject.optString("gender"));
            if (jsonObject.has("dob"))
                info.setDob(jsonObject.optString("dob"));
            if (jsonObject.has("profile_image"))
                info.setProfile_image(jsonObject.optString("profile_image"));
            if (jsonObject.has("status"))
                info.setStatus(jsonObject.optString("status"));
            if (jsonObject.has("insertDate"))
                info.setInsertDate(jsonObject.optString("insertDate"));
            if (jsonObject.has("updateDate"))
                info.setUpdateDate(jsonObject.optString("updateDate"));
            if (jsonObject.has("device_token"))
                info.setDevice_token(jsonObject.optString("device_token"));
            if (jsonObject.has("address"))
                info.setAddress(jsonObject.optString("address"));

            logDetail.add(info);
            Log.d("Response", logDetail.toString());
        }
        return logDetail;
    }




}
