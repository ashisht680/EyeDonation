package com.javinindia.ansheyedonation.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javinindia.ansheyedonation.R;
import com.javinindia.ansheyedonation.constant.Constants;
import com.javinindia.ansheyedonation.font.FontAsapRegularSingleTonClass;
import com.javinindia.ansheyedonation.preference.SharedPreferencesManager;
import com.javinindia.ansheyedonation.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 23-03-2017.
 */

public class BloodDonateFragment extends BaseFragment implements View.OnClickListener{
    private RequestQueue requestQueue;
    private String day = "";
    private String month = "";
    private String year = "";
    private String hour = "";
    private String min = "";
    private String sec = "";

    AppCompatEditText etName, etEmail, etContact, etAddress;
    AppCompatTextView txtDob, txtGender,txtSubmit;

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
        setToolbarTitle("Blood Donate");
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

        txtDob = (AppCompatTextView) view.findViewById(R.id.txtDob);
        txtDob.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtGender = (AppCompatTextView) view.findViewById(R.id.txtGender);
        txtGender.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtSubmit = (AppCompatTextView) view.findViewById(R.id.txtSubmit);
        txtSubmit.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtDob.setOnClickListener(this);
        txtGender.setOnClickListener(this);
        txtSubmit.setOnClickListener(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.blood_donate_registration_layout;
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
            case R.id.txtDob:
                methodOpenDatePicker();
                break;
            case R.id.txtGender:
                final String genderArray[] = {"MALE", "FEMALE"};
                popUp(genderArray, "gender");
                break;
            case R.id.txtSubmit:
                methodSubmit();
                break;
        }
    }

    //--------------------------popup for gender start-----------------------//
    private void popUp(final String[] popArray, final String type) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setTitle("Select " + type);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setItems(popArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (type.equals("gender")) {
                    txtGender.setText(popArray[item]);
                }
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }
    //------------------------------gender end--------------------------------//

    //----------------------- dob start -------------------------------//
    private void methodOpenDatePicker() {
        int mYear, mMonth, mDay, mHour, mMinute;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        txtDob.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                        day = Integer.toString(dayOfMonth);
                        month = Integer.toString(monthOfYear + 1);
                        BloodDonateFragment.this.year = Integer.toString(year);
                    }
                }, mYear, mMonth, mDay);
        long now = System.currentTimeMillis() - 1000;
        // datePickerDialog.getDatePicker().setMinDate(now);
        datePickerDialog.show();
    }
    //----------------------- dob end-------------------------------//

    private void methodSubmit() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String mobile = etContact.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String donateDate = txtDob.getText().toString().trim();
        String gender = txtGender.getText().toString().trim();
        if (validation(name,email, mobile, address,donateDate, gender)) {
           methodRegidter(name,email, mobile, address,donateDate, gender);
        }
    }

    private void methodRegidter(final String name, final String email, final String mobile, final String address, final String donateDate, final String gender) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BLOOD_DONATE_URL,
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

    private boolean validation(String name, String email, String mobile, String address, String dob, String gender) {
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
        } else if (dob.equals("Donate Date")) {
            Toast.makeText(activity, "You have not select Donate Date", Toast.LENGTH_LONG).show();
            return false;
        } else if (gender.equals("Gender")) {
            Toast.makeText(activity, "You have not select Gender", Toast.LENGTH_LONG).show();
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
