package com.javinindia.ansheyedonate.apiparsing.hospitalparsing;

import android.util.Log;

import com.javinindia.ansheyedonate.apiparsing.base.ApiBaseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish on 30-03-2017.
 */

public class HospitalResponseParsing extends ApiBaseData {

    private ArrayList<HospitalDetail> detailsArrayList;

    public ArrayList<HospitalDetail> getDetailsArrayList() {
        return detailsArrayList;
    }

    public void setDetailsArrayList(ArrayList<HospitalDetail> detailsArrayList) {
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

    private ArrayList<HospitalDetail> getDetailMethod(JSONArray details) {
        ArrayList<HospitalDetail> hosDetail = new ArrayList<>();
        for (int i = 0; i < details.length(); i++) {
            HospitalDetail info = new HospitalDetail();
            JSONObject jsonObject = details.optJSONObject(i);

            if (jsonObject.has("hospitalId"))
                info.setHospitalId(jsonObject.optString("hospitalId"));
            if (jsonObject.has("hospitalName"))
                info.setHospitalName(jsonObject.optString("hospitalName"));
            if (jsonObject.has("contactNumber1"))
                info.setContactNumber1(jsonObject.optString("contactNumber1"));
            if (jsonObject.has("contactNumber2"))
                info.setContactNumber2(jsonObject.optString("contactNumber2"));
            if (jsonObject.has("hospitalAddress"))
                info.setHospitalAddress(jsonObject.optString("hospitalAddress"));
            if (jsonObject.has("state"))
                info.setState(jsonObject.optString("state"));
            if (jsonObject.has("status"))
                info.setStatus(jsonObject.optString("status"));
            if (jsonObject.has("insertDate"))
                info.setInsertDate(jsonObject.optString("insertDate"));
            if (jsonObject.has("updateDate"))
                info.setUpdateDate(jsonObject.optString("updateDate"));

            hosDetail.add(info);
            Log.d("Response", hosDetail.toString());
        }
        return hosDetail;
    }
}
