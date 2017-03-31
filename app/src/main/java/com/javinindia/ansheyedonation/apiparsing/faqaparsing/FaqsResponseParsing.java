package com.javinindia.ansheyedonation.apiparsing.faqaparsing;

import android.util.Log;

import com.javinindia.ansheyedonation.apiparsing.base.ApiBaseData;
import com.javinindia.ansheyedonation.apiparsing.hospitalparsing.HospitalDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish on 30-03-2017.
 */

public class FaqsResponseParsing extends ApiBaseData {

    private ArrayList<FaqDetail> detailsArrayList;

    public ArrayList<FaqDetail> getDetailsArrayList() {
        return detailsArrayList;
    }

    public void setDetailsArrayList(ArrayList<FaqDetail> detailsArrayList) {
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

    private ArrayList<FaqDetail> getDetailMethod(JSONArray details) {
        ArrayList<FaqDetail> faqDetail = new ArrayList<>();
        for (int i = 0; i < details.length(); i++) {
            FaqDetail info = new FaqDetail();
            JSONObject jsonObject = details.optJSONObject(i);

            if (jsonObject.has("id"))
                info.setId(jsonObject.optString("id"));
            if (jsonObject.has("question"))
                info.setQuestion(jsonObject.optString("question"));
            if (jsonObject.has("answer"))
                info.setAnswer(jsonObject.optString("answer"));
            if (jsonObject.has("date"))
                info.setDate(jsonObject.optString("date"));

            faqDetail.add(info);
            Log.d("Response", faqDetail.toString());
        }
        return faqDetail;
    }
}
