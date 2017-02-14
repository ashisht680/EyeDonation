package com.javinindia.commonproject.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javinindia.commonproject.R;
import com.javinindia.commonproject.activity.LoginActivity;
import com.javinindia.commonproject.apiparsing.loginsignupparsing.SellerLoginSignUpResponse;
import com.javinindia.commonproject.constant.Constants;
import com.javinindia.commonproject.font.FontAsapRegularSingleTonClass;
import com.javinindia.commonproject.preference.SharedPreferencesManager;
import com.javinindia.commonproject.utility.Utility;

import java.util.HashMap;
import java.util.Map;


public class LoginFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private RequestQueue requestQueue;
    private EditText etUsername;
    private EditText etPassword;
    private BaseFragment fragment;
    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
   // private CheckBox checkShowPassword;

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


    private void initialize(View view) {
        AppCompatButton buttonLogin = (AppCompatButton) view.findViewById(R.id.btn_login);
        buttonLogin.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        TextView txtForgotPass = (TextView) view.findViewById(R.id.forgot_password);
        txtForgotPass.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etUsername = (EditText) view.findViewById(R.id.et_username);
        etUsername.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etPassword = (EditText) view.findViewById(R.id.et_password);
        etPassword.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        TextView txtRegistration = (TextView) view.findViewById(R.id.txtRegistration);
        txtRegistration.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        buttonLogin.setOnClickListener(this);
        txtForgotPass.setOnClickListener(this);
        txtRegistration.setOnClickListener(this);
        /*checkShowPassword = (CheckBox) view.findViewById(R.id.checkShowPassword);
        checkShowPassword.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        checkShowPassword.setOnClickListener(this);*/

        AppCompatCheckBox cbShowPwd = (AppCompatCheckBox) view.findViewById(R.id.cbShowPwd);
        cbShowPwd.setOnCheckedChangeListener(this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.login_layout;
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
        BaseFragment baseFragment;
        switch (v.getId()) {
            case R.id.btn_login:
                Utility.hideKeyboard(activity);
                loginMethod();
                break;
            case R.id.forgot_password:
                baseFragment = new ForgotPasswordFragment();
                callFragmentMethod(baseFragment, this.getClass().getSimpleName(), R.id.container);
                break;
            case R.id.txtRegistration:
                baseFragment = new SignUpFragment();
                callFragmentMethod(baseFragment, this.getClass().getSimpleName(), R.id.container);
                break;
           /* case R.id.checkShowPassword:
                if (checkShowPassword.isChecked()) {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;*/
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void loginMethod() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        SharedPreferencesManager.setPassword(activity, password);
        BaseFragment baseFragment = new NavigationAboutFragment();
        callFragmentMethod(baseFragment, this.getClass().getSimpleName(), R.id.container);
        /*if (validation(username, password)) {
            //sendDataOnLoginApi(username, password);
            BaseFragment baseFragment = new NavigationAboutFragment();
            callFragmentMethod(baseFragment, this.getClass().getSimpleName(), R.id.container);
        }*/

    }

    private void sendDataOnLoginApi(final String username, final String password) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SELLER_LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        int status = 0;
                        String sID = null, msg = null, sPic = null;
                        String sName, oName, sEmail, sMobileNum, sLandline, sState, sCity, sAddress, mName, mAddress, mLat, mLong, banner;
                        SellerLoginSignUpResponse loginSignupResponseParsing = new SellerLoginSignUpResponse();
                        loginSignupResponseParsing.responseParseMethod(response);

                        status = loginSignupResponseParsing.getStatus();
                        msg = loginSignupResponseParsing.getMsg();

                        if (status == 1) {
                            sID = loginSignupResponseParsing.getSellerDetailses().get(0).getId();
                            sPic = loginSignupResponseParsing.getSellerDetailses().get(0).getProfilepic().trim();
                            banner = loginSignupResponseParsing.getSellerDetailses().get(0).getBanner().trim();
                            sName = loginSignupResponseParsing.getSellerDetailses().get(0).getSellerName();
                            sEmail = loginSignupResponseParsing.getSellerDetailses().get(0).getEmail();
                            sMobileNum = loginSignupResponseParsing.getSellerDetailses().get(0).getMobile();
                            sLandline = loginSignupResponseParsing.getSellerDetailses().get(0).getLandline();
                            sState = loginSignupResponseParsing.getSellerDetailses().get(0).getState();
                            sCity = loginSignupResponseParsing.getSellerDetailses().get(0).getCity();
                            sAddress = loginSignupResponseParsing.getSellerDetailses().get(0).getAddress();
                            mAddress = loginSignupResponseParsing.getSellerDetailses().get(0).getAddress();
                            mLat = loginSignupResponseParsing.getSellerDetailses().get(0).getLat();
                            mLong = loginSignupResponseParsing.getSellerDetailses().get(0).getLongitude();
                            saveDataOnPreference(sEmail, sName, mLat, mLong, sID, banner);
                            Intent refresh = new Intent(activity, LoginActivity.class);
                            startActivity(refresh);//Start the same Activity
                            activity.finish();
                        } else {
                            if (!TextUtils.isEmpty(msg)) {
                                showDialogMethod("Invalid username/password.");
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
                params.put("email", username);
                params.put("password", password);
                if (!TextUtils.isEmpty(SharedPreferencesManager.getDeviceToken(activity))) {
                    params.put("deviceToken", SharedPreferencesManager.getDeviceToken(activity));
                } else {
                    params.put("deviceToken", "deviceToken");
                }
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void saveDataOnPreference(String sEmail, String sName, String mLat, String mLong, String sID, String profilepic) {
        SharedPreferencesManager.setUserID(activity, sID);
        SharedPreferencesManager.setEmail(activity, sEmail);
        SharedPreferencesManager.setUsername(activity, sName);
        SharedPreferencesManager.setLatitude(activity, mLat);
        SharedPreferencesManager.setLongitude(activity, mLong);
        SharedPreferencesManager.setProfileImage(activity, profilepic);
    }

    private boolean validation(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Mobile/Email field is empty.");
            etUsername.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(password)) {
            etPassword.setError("Please enter Password.");
            etPassword.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (!b) {
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            compoundButton.setButtonDrawable(R.drawable.ic_visibility_white);
        } else {
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            compoundButton.setButtonDrawable(R.drawable.ic_visibility_off_white);
        }
    }
}

