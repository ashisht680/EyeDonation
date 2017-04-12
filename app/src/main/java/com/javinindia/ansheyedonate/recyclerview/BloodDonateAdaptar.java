package com.javinindia.ansheyedonate.recyclerview;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.javinindia.ansheyedonate.R;
import com.javinindia.ansheyedonate.apiparsing.blooddonatelistparsing.DonateListDetail;
import com.javinindia.ansheyedonate.font.FontAsapBoldSingleTonClass;
import com.javinindia.ansheyedonate.font.FontAsapRegularSingleTonClass;
import com.javinindia.ansheyedonate.fragments.BloodDonateFragment;
import com.javinindia.ansheyedonate.preference.SharedPreferencesManager;
import com.javinindia.ansheyedonate.utility.Utility;
import com.javinindia.ansheyedonate.volleycustomrequest.VolleySingleTon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class BloodDonateAdaptar extends RecyclerView.Adapter<BloodDonateAdaptar.ViewHolder> {

    private String day = "";
    private String month = "";
    private String year = "";
    private String hour = "";
    private String min = "";
    private String sec = "";

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private List<Object> list;
    private Context context;
    MyClickListener myClickListener;


    public BloodDonateAdaptar(Context context) {
        this.context = context;
    }

    public void setData(List<Object> list) {
        this.list = list;
    }

    public List<Object> getData() {
        return list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        int holderId;
        //***********************Recent list
        AppCompatTextView txtAddress, txtDate;

        //********************** about header
        AppCompatEditText etName, etEmail, etContact, etAddress;
        AppCompatTextView txtDob, txtGender, txtSubmit, txtDonateDetail;

        public ViewHolder(View itemLayoutView, int ViewType) {
            super(itemLayoutView);

            if (ViewType == TYPE_ITEM) {
                txtAddress = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtAddress);
                txtAddress.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
                txtDate = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtDate);
                txtDate.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());

                holderId = 1;
            } else {
                etName = (AppCompatEditText) itemLayoutView.findViewById(R.id.etName);
                etName.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
                etEmail = (AppCompatEditText) itemLayoutView.findViewById(R.id.etEmail);
                etEmail.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
                etContact = (AppCompatEditText) itemLayoutView.findViewById(R.id.etContact);
                etContact.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
                etAddress = (AppCompatEditText) itemLayoutView.findViewById(R.id.etAddress);
                etAddress.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());

                txtDob = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtDob);
                txtDob.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
                txtGender = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtGender);
                txtGender.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
                txtSubmit = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtSubmit);
                txtSubmit.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
                txtDonateDetail = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtDonateDetail);
                txtDonateDetail.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
                holderId = 0;
            }
        }
    }


    @Override
    public BloodDonateAdaptar.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.donate_item_layout, parent, false);
            ViewHolder vhItem = new ViewHolder(v, viewType);
            return vhItem;
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_donate_registration_layout, parent, false);
            ViewHolder vhHeader = new ViewHolder(v, viewType);
            return vhHeader;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final BloodDonateAdaptar.ViewHolder viewHolder, final int position) {
        VolleySingleTon volleySingleTon = VolleySingleTon.getInstance(context);
        if (viewHolder.holderId == 1) {


            final DonateListDetail requestDetail = (DonateListDetail) list.get(position - 1);
            if (!TextUtils.isEmpty(requestDetail.getAddress().trim())) {
                String brandName = requestDetail.getAddress().trim();
                viewHolder.txtAddress.setText(Utility.fromHtml("<b>" + "Address :" + "</b>" + "\t" + brandName));
            }
            if (!TextUtils.isEmpty(requestDetail.getDob().trim())) {
                String brandName = requestDetail.getDob().trim();
                viewHolder.txtDate.setText(Utility.fromHtml("<b>" + "Date :" + "</b>" + "\t" + brandName));
            }


        } else {

            viewHolder.txtDob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int mYear, mMonth, mDay, mHour, mMinute;
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    viewHolder.txtDob.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);

                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            });

            viewHolder.txtGender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String genderArray[] = {"MALE", "FEMALE"};
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                    builder.setTitle("Select Gender");
                    builder.setNegativeButton(android.R.string.cancel, null);
                    builder.setItems(genderArray, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {

                            viewHolder.txtGender.setText(genderArray[item]);

                            dialog.dismiss();
                        }
                    });
                    builder.create();
                    builder.show();
                }
            });

            viewHolder.txtSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = viewHolder.etName.getText().toString().trim();
                    String email = viewHolder.etEmail.getText().toString().trim();
                    String mobile = viewHolder.etContact.getText().toString().trim();
                    String address = viewHolder.etAddress.getText().toString().trim();
                    String donateDate = viewHolder.txtDob.getText().toString().trim();
                    String gender = viewHolder.txtGender.getText().toString().trim();
                    if (validation(name, email, mobile, address, donateDate, gender)) {
                        myClickListener.onOfferClick(position, name, email, mobile, address, donateDate, gender);
                    }

                }
            });
            if (!TextUtils.isEmpty(SharedPreferencesManager.getUsername(context))) {
                viewHolder.etName.setText(SharedPreferencesManager.getUsername(context));
            }
            if (!TextUtils.isEmpty(SharedPreferencesManager.getEmail(context))) {
                viewHolder.etEmail.setText(SharedPreferencesManager.getEmail(context));
            }
            if (!TextUtils.isEmpty(SharedPreferencesManager.getMobile(context))) {
                viewHolder.etContact.setText(SharedPreferencesManager.getMobile(context));
            }


        }
    }

    private boolean validation(String name, String email, String mobile, String address, String dob, String gender) {
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(context, "You have not entered name", Toast.LENGTH_LONG).show();
            return false;
        } else if (!Utility.isEmailValid(email)) {
            Toast.makeText(context, "Email id entered is invalid", Toast.LENGTH_LONG).show();
            return false;
        } else if (mobile.length() != 10) {
            Toast.makeText(context, "Mobile number entered is invalid", Toast.LENGTH_LONG).show();
            return false;
        } else if (dob.equals("Donate Date")) {
            Toast.makeText(context, "You have not select Donate Date", Toast.LENGTH_LONG).show();
            return false;
        } else if (gender.equals("Gender")) {
            Toast.makeText(context, "You have not select Gender", Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(address)) {
            Toast.makeText(context, "You have not entered Address", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public int getItemCount() {
        return list != null ? list.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public interface MyClickListener {
        void onOfferClick(int position, String name, String email, String mobile, String address, String donateDate, String gender);

    }

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

}
