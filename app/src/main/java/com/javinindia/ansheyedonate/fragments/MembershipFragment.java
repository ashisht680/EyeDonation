package com.javinindia.ansheyedonate.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javinindia.ansheyedonate.R;
import com.javinindia.ansheyedonate.constant.Constants;
import com.javinindia.ansheyedonate.font.FontAsapRegularSingleTonClass;
import com.javinindia.ansheyedonate.preference.SharedPreferencesManager;
import com.javinindia.ansheyedonate.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 12-04-2017.
 */

public class MembershipFragment extends BaseFragment implements View.OnClickListener{
    private RequestQueue requestQueue;
    AppCompatEditText etName, etEmail, etContact, etAddress;
    AppCompatTextView txtSubmit,txtMembershipTag;

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
        setToolbarTitle("Membership");
        initializeView(view);
        return view;
    }

    private void initializeView(View view) {
        etName = (AppCompatEditText) view.findViewById(R.id.etName);
        etName.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etEmail = (AppCompatEditText) view.findViewById(R.id.etEmail);
        etEmail.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etContact = (AppCompatEditText) view.findViewById(R.id.etContact);
        etContact.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etAddress = (AppCompatEditText) view.findViewById(R.id.etAddress);
        etAddress.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtMembershipTag = (AppCompatTextView) view.findViewById(R.id.txtMembershipTag);
        txtMembershipTag.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtSubmit = (AppCompatTextView) view.findViewById(R.id.txtSubmit);
        txtSubmit.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtSubmit.setOnClickListener(this);

        if (!TextUtils.isEmpty(SharedPreferencesManager.getUsername(activity))){
            etName.setText(SharedPreferencesManager.getUsername(activity));
        }
        if (!TextUtils.isEmpty(SharedPreferencesManager.getEmail(activity))){
            etEmail.setText(SharedPreferencesManager.getEmail(activity));
        }
        if (!TextUtils.isEmpty(SharedPreferencesManager.getMobile(activity))){
            etContact.setText(SharedPreferencesManager.getMobile(activity));
        }
        if (!TextUtils.isEmpty(SharedPreferencesManager.getAddress(activity))){
            etAddress.setText(SharedPreferencesManager.getAddress(activity));
        }
    }
    @Override
    protected int getFragmentLayout() {
        return R.layout.membership_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.txtSubmit:
                methodSubmit();
                break;
        }
    }

    private void methodSubmit() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String mobile = etContact.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        if (validation(name,email, mobile, address)) {
            methodRegidter(name,email, mobile, address);
        }
    }

    private void methodRegidter(final String name, final String email, final String mobile, final String address) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.MEMBERSHIP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("edit", response);
                        int status=0;
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
                //uid=7&Name=kamal&mobile=9599348895&email=brijesh@yahoo.com&address=hellohowareyou
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", SharedPreferencesManager.getUserID(activity));
                params.put("Name", name);
                params.put("mobile", mobile);
                params.put("email", email);
                params.put("address", address);
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private boolean validation(String name, String email, String mobile, String address) {
        if (TextUtils.isEmpty(name)) {
            etName.setError("You have not entered name");
            etName.requestFocus();
            return false;
        } else if (!Utility.isEmailValid(email)) {
            etEmail.setError("Email id entered is invalid");
            etEmail.requestFocus();
            return false;
        } else if (mobile.length() != 10) {
            etContact.setError("Mobile number entered is invalid");
            etContact.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(address)) {
            etAddress.setError("You have not entered Address");
            etAddress.requestFocus();
            return false;
        }  else {
            return true;
        }
    }
}
