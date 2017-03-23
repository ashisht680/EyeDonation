package com.javinindia.ansheyedonation.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import com.javinindia.ansheyedonation.R;
import com.javinindia.ansheyedonation.font.FontAsapRegularSingleTonClass;
import com.javinindia.ansheyedonation.utility.Utility;

import java.io.File;
import java.util.Calendar;

/**
 * Created by Ashish on 23-03-2017.
 */

public class RecipientRegistrationFragment extends BaseFragment implements View.OnClickListener{

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (menu != null) {
            menu.findItem(R.id.action_changePass).setVisible(false);
            menu.findItem(R.id.action_feedback).setVisible(false);
        }
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
        setToolbarTitle("Recipient Registration");
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
        return R.layout.recipient_registration_layout;
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
                        txtDob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        day = Integer.toString(dayOfMonth);
                        month = Integer.toString(monthOfYear + 1);
                        RecipientRegistrationFragment.this.year = Integer.toString(year);
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
        String dob = txtDob.getText().toString().trim();
        String gender = txtGender.getText().toString().trim();
        if (validation(name,email, mobile, address,dob, gender)) {
            Toast.makeText(activity, "success", Toast.LENGTH_LONG).show();
        }
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
        } else if (dob.equals("DOB")) {
            Toast.makeText(activity, "You have not select Date of birth", Toast.LENGTH_LONG).show();
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
