package com.javinindia.ansheyedonate.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javinindia.ansheyedonate.R;
import com.javinindia.ansheyedonate.apiparsing.blooddonatelistparsing.BloodDonateListResponse;
import com.javinindia.ansheyedonate.apiparsing.hospitalparsing.HospitalResponseParsing;
import com.javinindia.ansheyedonate.constant.Constants;
import com.javinindia.ansheyedonate.font.FontAsapRegularSingleTonClass;
import com.javinindia.ansheyedonate.preference.SharedPreferencesManager;
import com.javinindia.ansheyedonate.recyclerview.AllHospitalAdapter;
import com.javinindia.ansheyedonate.recyclerview.BloodDonateAdaptar;
import com.javinindia.ansheyedonate.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 23-03-2017.
 */

public class BloodDonateFragment extends BaseFragment implements BloodDonateAdaptar.MyClickListener {
    private RequestQueue requestQueue;

    RecyclerView recyclerviewList;
    ProgressBar progressBar;

    private BloodDonateAdaptar adapter;
    ArrayList arrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (menu != null)
            menu.clear();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setToolbarTitle("Blood Donation");
        initializeView(view);
        sendRequestOnReplyFeed();
        return view;
    }

    private void initializeView(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        recyclerviewList = (RecyclerView) view.findViewById(R.id.recyclerviewList);
        adapter = new BloodDonateAdaptar(activity);
        LinearLayoutManager layoutMangerDestination
                = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerviewList.setLayoutManager(layoutMangerDestination);
        recyclerviewList.setAdapter(adapter);
        adapter.setMyClickListener(BloodDonateFragment.this);

    }

    private void sendRequestOnReplyFeed() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BLOOD_DONATE_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        BloodDonateListResponse responseparsing = new BloodDonateListResponse();
                        responseparsing.responseParseMethod(response);
                        int status = responseparsing.getStatus();
                        if (status == 1) {
                            if (responseparsing.getDonateListDetails().size() > 0) {
                                arrayList = responseparsing.getDonateListDetails();
                                if (arrayList.size() > 0) {
                                    // txtDataNotFound.setVisibility(View.GONE);
                                    if (adapter.getData() != null && adapter.getData().size() > 0) {
                                        adapter.getData().addAll(arrayList);
                                        adapter.notifyDataSetChanged();
                                    } else {

                                        adapter.setData(arrayList);
                                        adapter.notifyDataSetChanged();
                                    }
                                } else {
                                    //txtDataNotFound.setVisibility(View.VISIBLE);
                                }
                            } else {
                                // txtDataNotFound.setVisibility(View.VISIBLE);
                            }
                        } else {
                            // txtDataNotFound.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyErrorHandle(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", SharedPreferencesManager.getUserID(activity));
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.blood_donate_list_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }


    @Override
    public void onOfferClick(int position, final String name, final String email, final String mobile, final String address, final String donateDate, final String gender) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BLOOD_DONATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("edit", response);
                        int status = 0;
                        String msg = null;
                        loading.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            if (jsonObject.has("status"))
                                status = jsonObject.optInt("status");
                            if (jsonObject.has("msg"))
                                msg = jsonObject.optString("msg");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (status == 1) {
                            Toast.makeText(activity, "Submitted successfully", Toast.LENGTH_SHORT).show();
                            activity.onBackPressed();
                        } else {
                            if (!TextUtils.isEmpty(msg)) {
                                showDialogMethod(msg);
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        volleyErrorHandle(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", SharedPreferencesManager.getUserID(activity));
                params.put("recipientName", name);
                params.put("mobile", mobile);
                params.put("email", email);
                params.put("dob", donateDate);
                params.put("gender", gender);
                params.put("address", address);
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }
}
