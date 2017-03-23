package com.javinindia.ansheyedonation.apiparsing.loginsignupparsing;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish on 08-02-2017.
 */

public class SellerLoginSignUpResponse {
    private int status;
    private String msg;
    private String pic;
    private ArrayList<SellerDetails> sellerDetailses;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public ArrayList<SellerDetails> getSellerDetailses() {
        return sellerDetailses;
    }

    public void setSellerDetailses(ArrayList<SellerDetails> sellerDetailses) {
        this.sellerDetailses = sellerDetailses;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            setMsg(jsonObject.optString("msg"));
            setStatus(jsonObject.optInt("status"));
            setPic(jsonObject.optString("pic"));
            if (jsonObject.optInt("status")==1 && jsonObject.has("details") && jsonObject.optJSONArray("details")!=null)
                setSellerDetailses(getSellerDetailMethod(jsonObject.optJSONArray("details")));

            Log.d("Response", this.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<SellerDetails> getSellerDetailMethod(JSONArray details) {
        ArrayList<SellerDetails> seller = new ArrayList<>();
        for (int i = 0; i < details.length(); i++) {
            SellerDetails info = new SellerDetails();
            JSONObject jsonObject = details.optJSONObject(i);

            if (jsonObject.has("id"))
                info.setId(jsonObject.optString("id"));
            if (jsonObject.has("sellerName"))
                info.setSellerName(jsonObject.optString("sellerName"));
            if (jsonObject.has("email"))
                info.setEmail(jsonObject.optString("email"));
            if (jsonObject.has("mobile"))
                info.setMobile(jsonObject.optString("mobile"));
            if (jsonObject.has("landline"))
                info.setLandline(jsonObject.optString("landline"));
            if (jsonObject.has("password"))
                info.setPassword(jsonObject.optString("password"));
            if (jsonObject.has("comp_name"))
                info.setComp_name(jsonObject.optString("comp_name"));
            if (jsonObject.has("state"))
                info.setState(jsonObject.optString("state"));
            if (jsonObject.has("city"))
                info.setCity(jsonObject.optString("city"));
            if (jsonObject.has("profilepic"))
                info.setProfilepic(jsonObject.optString("profilepic"));
            if (jsonObject.has("banner"))
                info.setBanner(jsonObject.optString("banner"));
            if (jsonObject.has("address"))
                info.setAddress(jsonObject.optString("address"));
            if (jsonObject.has("description"))
                info.setDescription(jsonObject.optString("description"));
            if (jsonObject.has("insertDate"))
                info.setInsertDate(jsonObject.optString("insertDate"));
            if (jsonObject.has("updateDate"))
                info.setUpdateDate(jsonObject.optString("updateDate"));
            if (jsonObject.has("status"))
                info.setStatus(jsonObject.optString("status"));
            if (jsonObject.has("pinCode"))
                info.setPinCode(jsonObject.optString("pinCode"));
            if (jsonObject.has("rating"))
                info.setRating(jsonObject.optString("rating"));
            if (jsonObject.has("lat"))
                info.setLat(jsonObject.optString("lat"));
            if (jsonObject.has("long"))
                info.setLongitude(jsonObject.optString("long"));
            if (jsonObject.has("device_token"))
                info.setDevice_token(jsonObject.optString("device_token"));

            seller.add(info);
            Log.d("Response", seller.toString());
        }
        return seller;
    }
}
