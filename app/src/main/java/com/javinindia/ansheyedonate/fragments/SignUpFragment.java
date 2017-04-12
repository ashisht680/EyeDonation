package com.javinindia.ansheyedonate.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javinindia.ansheyedonate.R;
import com.javinindia.ansheyedonate.activity.LoginActivity;
import com.javinindia.ansheyedonate.apiparsing.loginsignupparsing.LoginSignupResponseParsing;
import com.javinindia.ansheyedonate.constant.Constants;
import com.javinindia.ansheyedonate.font.FontAsapRegularSingleTonClass;
import com.javinindia.ansheyedonate.preference.SharedPreferencesManager;
import com.javinindia.ansheyedonate.utility.Utility;

import java.util.HashMap;
import java.util.Map;


public class SignUpFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private AppCompatEditText et_Name,et_email, et_MobileNum, et_password, et_ConfirmPassword;
    private RequestQueue requestQueue;
    RadioButton radioButton;
    TextView txtTermCondition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        initialize(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    private void initialize(View view) {
        ImageView imgBack = (ImageView) view.findViewById(R.id.imgBack);
        AppCompatButton btnRegister = (AppCompatButton) view.findViewById(R.id.btnRegister);
        btnRegister.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        AppCompatCheckBox cbShowPwd = (AppCompatCheckBox) view.findViewById(R.id.cbShowPwd);
        AppCompatCheckBox cbShowConPwd = (AppCompatCheckBox) view.findViewById(R.id.cbShowConPwd);
        et_Name = (AppCompatEditText) view.findViewById(R.id.et_Name);
        et_Name.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_email = (AppCompatEditText) view.findViewById(R.id.et_email);
        et_email.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_MobileNum = (AppCompatEditText) view.findViewById(R.id.et_MobileNum);
        et_MobileNum.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_password = (AppCompatEditText) view.findViewById(R.id.et_password);
        et_password.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_ConfirmPassword = (AppCompatEditText) view.findViewById(R.id.et_ConfirmPassword);
        et_ConfirmPassword.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        radioButton = (RadioButton) view.findViewById(R.id.radioButton);
        txtTermCondition = (TextView) view.findViewById(R.id.txtTermCondition);
        txtTermCondition.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtTermCondition.setText(Utility.fromHtml("<font color=#ffffff>" + "I accept the" + "</font>" + "\t" + "<font color=#0d7bbf>" + "terms and conditions." + "</font>"));


        cbShowConPwd.setOnCheckedChangeListener(this);
        cbShowPwd.setOnCheckedChangeListener(this);
        btnRegister.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        txtTermCondition.setOnClickListener(this);
    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.sign_up_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnRegister:
                Utility.hideKeyboard(activity);
                registrationMethod();
                break;
            case R.id.imgBack:
                activity.onBackPressed();
                break;
            case R.id.txtTermCondition:
                BaseFragment termFragment = new TermAndConditionFragment();
                callFragmentMethod(termFragment, this.getClass().getSimpleName(), R.id.container);
                break;
        }
    }

    private void registrationMethod() {
        String name = et_Name.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String mobileNum = et_MobileNum.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String confirmPassword = et_ConfirmPassword.getText().toString().trim();

        if (registerValidation(name, email, mobileNum,  password, confirmPassword)) {
            sendDataOnRegistrationApi(name, email, mobileNum,  password, confirmPassword);
        }

    }

    private void sendDataOnRegistrationApi(final String name, final String email, final String mobileNum, final String password, String confirmPassword) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SIGN_UP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int status ;
                        String  id = null, msg = null, pic = null,address=null;
                        String name, email, phoneNum;
                        loading.dismiss();
                        LoginSignupResponseParsing loginSignupResponseParsing = new LoginSignupResponseParsing();
                        loginSignupResponseParsing.responseParseMethod(response);

                        status = loginSignupResponseParsing.getStatus();
                        msg = loginSignupResponseParsing.getMsg();

                        if (status==1) {
                            id = loginSignupResponseParsing.getDetailsArrayList().get(0).getProfileId().trim();
                            pic = loginSignupResponseParsing.getPic().trim();
                            name = loginSignupResponseParsing.getDetailsArrayList().get(0).getProfileName().trim();
                            email = loginSignupResponseParsing.getDetailsArrayList().get(0).getEmail().trim();
                            phoneNum = loginSignupResponseParsing.getDetailsArrayList().get(0).getPhone().trim();
                            address = loginSignupResponseParsing.getDetailsArrayList().get(0).getAddress().trim();
                            saveDataOnPreference(email, name, id, pic, phoneNum,address);
                            Intent refresh = new Intent(activity, LoginActivity.class);
                            startActivity(refresh);//Start the same Activity
                            activity.finish();
                        } else {
                            if (!TextUtils.isEmpty(msg)) {
                                showDialogMethod("Sorry, this email account already exists. Please enter a different email id.");
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
                //  email=bdd@yahoo.com&mobile=95959959&name=kamal&password=123456&device_token=fdfdf
                Map<String, String> params = new HashMap<String, String>();
                params.put("name",name);
                params.put("email", email);
                params.put("password", password);
                params.put("mobile", mobileNum);
                if (!TextUtils.isEmpty(SharedPreferencesManager.getDeviceToken(activity))){
                    params.put("device_token",SharedPreferencesManager.getDeviceToken(activity));
                }else {
                    params.put("device_token","deviceToken");
                }
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void saveDataOnPreference(String email, String name, String id, String pic, String phoneNum,String address) {
        SharedPreferencesManager.setUserID(activity, id);
        SharedPreferencesManager.setEmail(activity, email);
        SharedPreferencesManager.setUsername(activity, name);
        SharedPreferencesManager.setProfileImage(activity, pic);
        SharedPreferencesManager.setMobile(activity, phoneNum);
        SharedPreferencesManager.setAddress(activity,address);
    }


    private boolean registerValidation(String storeName, String email, String mobileNum, String password, String confirmPassword) {
        if (TextUtils.isEmpty(storeName)) {
            et_Name.setError("You have not entered name.");
            et_Name.requestFocus();
            return false;
        } else if (!Utility.isEmailValid(email)) {
            et_email.setError("Email id entered is invalid");
            et_email.requestFocus();
            return false;
        } else if (mobileNum.length() != 10) {
            et_MobileNum.setError("Mobile number entered is invalid");
            et_MobileNum.requestFocus();
            return false;
        } else if (password.length() < 6) {
            et_password.setError("Password should be more than 6 characters");
            et_password.requestFocus();
            return false;
        } else if (!confirmPassword.equals(password)) {
            et_ConfirmPassword.setError("Password does not match");
            et_ConfirmPassword.requestFocus();
            return false;
        }else if (!radioButton.isChecked()){
            Toast.makeText(activity,"You have not accepted the terms and conditions.",Toast.LENGTH_LONG).show();
            return  false;
        } else {
            return true;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(this.getClass().getSimpleName());
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.cbShowPwd:
                if (!b) {
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    compoundButton.setButtonDrawable(R.drawable.ic_visibility_white);
                } else {
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    compoundButton.setButtonDrawable(R.drawable.ic_visibility_off_white);
                }
                break;
            case R.id.cbShowConPwd:
                if (!b) {
                    et_ConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    compoundButton.setButtonDrawable(R.drawable.ic_visibility_white);
                } else {
                    et_ConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    compoundButton.setButtonDrawable(R.drawable.ic_visibility_off_white);
                }
                break;
        }
    }
}
