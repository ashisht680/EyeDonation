package com.javinindia.ansheyedonate.apiparsing.blooddonatelistparsing;

import android.util.Log;

import com.javinindia.ansheyedonate.apiparsing.base.ApiBaseData;
import com.javinindia.ansheyedonate.apiparsing.hospitalparsing.HospitalDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish on 12-04-2017.
 */

public class BloodDonateListResponse extends ApiBaseData {

    private ArrayList<DonateListDetail> donateListDetails;

    public ArrayList<DonateListDetail> getDonateListDetails() {
        return donateListDetails;
    }

    public void setDonateListDetails(ArrayList<DonateListDetail> donateListDetails) {
        this.donateListDetails = donateListDetails;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            setMsg(jsonObject.optString("msg"));
            setStatus(jsonObject.optInt("status"));
            if (jsonObject.optInt("status") == 1 && jsonObject.has("details") && jsonObject.optJSONArray("details") != null)
                setDonateListDetails(getDetailMethod(jsonObject.optJSONArray("details")));

            Log.d("Response", this.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<DonateListDetail> getDetailMethod(JSONArray details) {
        ArrayList<DonateListDetail> hosDetail = new ArrayList<>();
        for (int i = 0; i < details.length(); i++) {
            DonateListDetail info = new DonateListDetail();
            JSONObject jsonObject = details.optJSONObject(i);

            if (jsonObject.has("recipientId"))
                info.setDonnerId(jsonObject.optString("recipientId"));
            if (jsonObject.has("uid"))
                info.setUid(jsonObject.optString("uid"));
            if (jsonObject.has("recipientName"))
                info.setName(jsonObject.optString("recipientName"));
            if (jsonObject.has("dob"))
                info.setDob(jsonObject.optString("dob"));
            if (jsonObject.has("gender"))
                info.setGender(jsonObject.optString("gender"));
            if (jsonObject.has("email"))
                info.setEmail(jsonObject.optString("email"));
            if (jsonObject.has("contactNumber"))
                info.setContactNumber(jsonObject.optString("contactNumber"));
            if (jsonObject.has("address"))
                info.setAddress(jsonObject.optString("address"));

            hosDetail.add(info);
            Log.d("Response", hosDetail.toString());
        }
        return hosDetail;
    }
}
