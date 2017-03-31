package com.javinindia.ansheyedonation.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javinindia.ansheyedonation.R;
import com.javinindia.ansheyedonation.apiparsing.loginsignupparsing.LoginSignupResponseParsing;
import com.javinindia.ansheyedonation.apiparsing.stateparsing.CityMasterParsing;
import com.javinindia.ansheyedonation.apiparsing.stateparsing.CountryMasterApiParsing;
import com.javinindia.ansheyedonation.constant.Constants;
import com.javinindia.ansheyedonation.font.FontAsapRegularSingleTonClass;
import com.javinindia.ansheyedonation.preference.SharedPreferencesManager;
import com.javinindia.ansheyedonation.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.javinindia.ansheyedonation.utility.Utility.decodeFile;
import static com.javinindia.ansheyedonation.utility.Utility.getOutputMediaFile;

/**
 * Created by Ashish on 22-03-2017.
 */

public class DonateEyeFragment extends BaseFragment implements View.OnClickListener {
    private RequestQueue requestQueue;
    ImageView imgDonor, imgKinInfo;
    AppCompatEditText etFirstName, etLastName, etEmail, etContact, etAddress, etPinCode, etKinName, etKinContact, etKinEmail, etNote;
    AppCompatTextView txtDob, txtGender, txtState, txtCity, txtNextOfKin, txtSubmit;

    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private File outPutFile = null;
    private Uri mImageCaptureUri;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    Bitmap photo = null;

    private String day = "";
    private String month = "";
    private String year = "";
    private String hour = "";
    private String min = "";
    private String sec = "";

    public ArrayList<String> stateList = new ArrayList<>();
    String stateArray[] = null;
    public ArrayList<String> cityList = new ArrayList<>();
    String cityArray[] = null;

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

        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        setToolbarTitle("Donate your Eyes");
        initializeView(view);
        return view;
    }


    private void initializeView(View view) {
        imgDonor = (ImageView) view.findViewById(R.id.imgDonor);
        imgKinInfo = (ImageView) view.findViewById(R.id.imgKinInfo);

        etFirstName = (AppCompatEditText) view.findViewById(R.id.etFirstName);
        etFirstName.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etLastName = (AppCompatEditText) view.findViewById(R.id.etLastName);
        etLastName.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etEmail = (AppCompatEditText) view.findViewById(R.id.etEmail);
        etEmail.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etContact = (AppCompatEditText) view.findViewById(R.id.etContact);
        etContact.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etAddress = (AppCompatEditText) view.findViewById(R.id.etAddress);
        etAddress.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etPinCode = (AppCompatEditText) view.findViewById(R.id.etPinCode);
        etPinCode.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etKinName = (AppCompatEditText) view.findViewById(R.id.etKinName);
        etKinName.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etKinContact = (AppCompatEditText) view.findViewById(R.id.etKinContact);
        etKinContact.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etKinEmail = (AppCompatEditText) view.findViewById(R.id.etKinEmail);
        etKinEmail.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etNote = (AppCompatEditText) view.findViewById(R.id.etNote);
        etNote.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtDob = (AppCompatTextView) view.findViewById(R.id.txtDob);
        txtDob.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtGender = (AppCompatTextView) view.findViewById(R.id.txtGender);
        txtGender.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtState = (AppCompatTextView) view.findViewById(R.id.txtState);
        txtState.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtCity = (AppCompatTextView) view.findViewById(R.id.txtCity);
        txtCity.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtNextOfKin = (AppCompatTextView) view.findViewById(R.id.txtNextOfKin);
        txtNextOfKin.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtSubmit = (AppCompatTextView) view.findViewById(R.id.txtSubmit);
        txtSubmit.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtSubmit.setOnClickListener(this);
        imgDonor.setOnClickListener(this);
        imgKinInfo.setOnClickListener(this);
        txtDob.setOnClickListener(this);
        txtGender.setOnClickListener(this);
        txtState.setOnClickListener(this);
        txtCity.setOnClickListener(this);
        txtNextOfKin.setOnClickListener(this);

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.donate_eye_layout;
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
            case R.id.txtState:
                methodState();
                break;
            case R.id.txtCity:
                if (txtState.getText().toString().equals("State")) {
                    Toast.makeText(activity, "Select State first", Toast.LENGTH_LONG).show();
                } else {
                    methodCity();
                }
                break;
            case R.id.txtNextOfKin:
                final String kinArray[] = {"Family", "Friend", "Acquaintance"};
                popUp(kinArray, "Next of kin");
                break;
            case R.id.txtSubmit:
                methodSubmit();
                break;
            case R.id.imgDonor:
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);
                } else {
                    methodAddImages();
                }
                break;
            case R.id.imgKinInfo:
                showDialogMethodMsg("Information","Next of kin refers to your near and dear one who needs to be informed as per low regarding your Eye Donation.");
                break;
        }
    }

    //---------------------start state city-----------------------------//
    private void methodState() {
        stateList.removeAll(stateList);
        stateArray = null;
        sendRequestOnState();
    }

    private void sendRequestOnState() {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.STATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        CountryMasterApiParsing countryMasterApiParsing = new CountryMasterApiParsing();
                        countryMasterApiParsing.responseParseMethod(response);
                        if (countryMasterApiParsing.getCountryDetails().getStateDetailsArrayList().size() > 0) {
                            for (int i = 0; i < countryMasterApiParsing.getCountryDetails().getStateDetailsArrayList().size(); i++) {
                                stateList.add(countryMasterApiParsing.getCountryDetails().getStateDetailsArrayList().get(i).getState());
                            }
                            if (stateList.size() > 0) {
                                stateArray = new String[stateList.size()];
                                stateList.toArray(stateArray);

                                if (stateList != null && stateList.size() > 0) {
                                    popUp(stateArray, "state");

                                }
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
                params.put("country", "india");
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void methodCity() {
        cityList.removeAll(cityList);
        cityArray = null;
        sendRequestOnCity();
    }

    private void sendRequestOnCity() {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.STATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        CityMasterParsing cityMasterParsing = new CityMasterParsing();
                        cityMasterParsing.responseParseMethod(response);
                        for (int i = 0; i < cityMasterParsing.getCountryDetails().getCityDetails().size(); i++) {
                            cityList.add(cityMasterParsing.getCountryDetails().getCityDetails().get(i).getCity());
                        }
                        if (cityList.size() > 0) {
                            cityArray = new String[cityList.size()];
                            cityList.toArray(cityArray);

                            if (cityList != null && cityList.size() > 0) {
                                popUp(cityArray, "city");
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
                params.put("country", "india");
                params.put("state", txtState.getText().toString());
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }
    //---------------------end state city-------------------------//


    //--------------------------popup for gender start-----------------------//
    private void popUp(final String[] popArray, final String type) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setTitle("Select " + type);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setItems(popArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (type.equals("gender")) {
                    txtGender.setText(popArray[item]);
                } else if (type.equals("Next of kin")) {
                    txtNextOfKin.setText(popArray[item]);
                } else if (type.equals("state")) {
                    txtState.setText(popArray[item]);
                    txtCity.setText("City");
                } else if (type.equals("city")) {
                    txtCity.setText(popArray[item]);
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
                        DonateEyeFragment.this.year = Integer.toString(year);
                    }
                }, mYear, mMonth, mDay);
        long now = System.currentTimeMillis() - 1000;
        // datePickerDialog.getDatePicker().setMinDate(now);
        datePickerDialog.show();
    }
    //----------------------- dob end-------------------------------//

    ///----------------------------------- image start------------------------------//

    private void methodAddImages() {
        final CharSequence[] options = {"Take from camera", "Select from gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take from camera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        File imagePath = new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES), "anshEye");
                        File newFile = new File(imagePath, "temp.jpg");
                        outPutFile = newFile;
                        mImageCaptureUri = FileProvider.getUriForFile(activity, "com.javinindia.ansheyedonation.fileprovider", newFile);
                        activity.grantUriPermission("com.android.camera", mImageCaptureUri,
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    } else {
                        mImageCaptureUri = Uri.fromFile(getOutputMediaFile());
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    startActivityForResult(intent, PICK_FROM_CAMERA);
                } else if (options[item].equals("Select from gallery")) {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, PICK_FROM_FILE);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public class CropOptionAdapter extends ArrayAdapter<CropOption> {
        private ArrayList<CropOption> mOptions;
        private LayoutInflater mInflater;

        public CropOptionAdapter(Context context, ArrayList<CropOption> options) {
            super(context, R.layout.crop_selector, options);

            mOptions = options;

            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup group) {
            if (convertView == null)
                convertView = mInflater.inflate(R.layout.crop_selector, null);

            CropOption item = mOptions.get(position);

            if (item != null) {
                ((ImageView) convertView.findViewById(R.id.iv_icon))
                        .setImageDrawable(item.icon);
                ((TextView) convertView.findViewById(R.id.tv_name))
                        .setText(item.title);

                return convertView;
            }

            return null;
        }
    }

    public class CropOption {
        public CharSequence title;
        public Drawable icon;
        public Intent appIntent;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FROM_FILE && resultCode == Activity.RESULT_OK && null != data) {
            mImageCaptureUri = data.getData();
            //outPutFile=getOutputMediaFile();
            doCrop();
        } else if (requestCode == PICK_FROM_CAMERA && resultCode == Activity.RESULT_OK) {
            doCrop();
        } else if (requestCode == CROP_FROM_CAMERA) {
            try {
                if (outPutFile.exists()) {
                    photo = decodeFile(outPutFile.getAbsolutePath());
                   // imgProfilePicNotFound.setImageBitmap(photo);
                    imgDonor.setImageBitmap(photo);
                } else {
                    Toast.makeText(activity, "Error while save image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void doCrop() {

        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        List<ResolveInfo> list = activity.getPackageManager().queryIntentActivities(
                intent, 0);

        int size = list.size();
        if (size == 0) {
            Toast.makeText(activity, "Can not find image crop app",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            intent.setData(mImageCaptureUri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            intent.putExtra("outputX", 512);
            intent.putExtra("outputY", 512);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));


            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName,
                        res.activityInfo.name));
                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                boolean flag = false;
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();
                    co.title = activity.getPackageManager().getApplicationLabel(
                            res.activityInfo.applicationInfo);
                    if (co.title.equals("Photos")) {
                        flag = true;
                    }
                    co.icon = activity.getPackageManager().getApplicationIcon(
                            res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);
                    co.appIntent
                            .setComponent(new ComponentName(
                                    res.activityInfo.packageName,
                                    res.activityInfo.name));
                    cropOptions.add(co);
                }

                if (flag) {
                    for (int a = 0; a < list.size(); a++) {
                        ResolveInfo res2 = list.get(a);
                        if (activity.getPackageManager().getApplicationLabel(res2.activityInfo.applicationInfo).equals("Photos")) {
                            Intent ash = new Intent(intent);
                            ash.setComponent(new ComponentName(res2.activityInfo.packageName,
                                    res2.activityInfo.name));
                            startActivityForResult(ash, CROP_FROM_CAMERA);
                            break;
                        }

                    }

                } else {
                    CropOptionAdapter adapter = new CropOptionAdapter(
                            activity, cropOptions);
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
                    builder.setTitle("Choose Crop App");
                    builder.setCancelable(false);
                    builder.setAdapter(adapter,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    startActivityForResult(
                                            cropOptions.get(item).appIntent,
                                            CROP_FROM_CAMERA);
                                }
                            });

                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {

                            if (mImageCaptureUri != null) {
                                activity.getContentResolver().delete(mImageCaptureUri, null,
                                        null);
                                mImageCaptureUri = null;
                            }
                        }
                    });

                    android.support.v7.app.AlertDialog alert = builder.create();
                    alert.show();
                }

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    methodAddImages();
                    return;
                } else {
                    Toast.makeText(activity, "You Denied for camera permission so you cant't update image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

 /*   private void methodAddImages() {
        final CharSequence[] options = {"Take from camera", "Select from gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take from camera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        File imagePath = new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES), "ansh");
                        File newFile = new File(imagePath, "temp.jpg");
                        outPutFile = newFile;
                        mImageCaptureUri = FileProvider.getUriForFile(activity, "com.javinindia.ansheyedonation.fileprovider", newFile);
                        activity.grantUriPermission("com.android.camera", mImageCaptureUri,
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    } else {
                        mImageCaptureUri = Uri.fromFile(getOutputMediaFile());
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    startActivityForResult(intent, PICK_FROM_CAMERA);
                } else if (options[item].equals("Select from gallery")) {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, PICK_FROM_FILE);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FROM_FILE && resultCode == Activity.RESULT_OK && null != data) {
            mImageCaptureUri = data.getData();
            doCrop();
        } else if (requestCode == PICK_FROM_CAMERA && resultCode == Activity.RESULT_OK) {
            doCrop();
        } else if (requestCode == CROP_FROM_CAMERA) {
            try {
                if (outPutFile.exists()) {
                    photo = decodeFile(outPutFile.getAbsolutePath());
                    imgDonor.setImageBitmap(photo);
                } else {
                    Toast.makeText(activity, "Error while save image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class CropOptionAdapter extends ArrayAdapter<CropOption> {
        private ArrayList<CropOption> mOptions;
        private LayoutInflater mInflater;

        public CropOptionAdapter(Context context, ArrayList<CropOption> options) {
            super(context, R.layout.crop_selector, options);

            mOptions = options;

            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup group) {
            if (convertView == null)
                convertView = mInflater.inflate(R.layout.crop_selector, null);

            CropOption item = mOptions.get(position);

            if (item != null) {
                ((ImageView) convertView.findViewById(R.id.iv_icon))
                        .setImageDrawable(item.icon);
                ((TextView) convertView.findViewById(R.id.tv_name))
                        .setText(item.title);

                return convertView;
            }

            return null;
        }
    }

    public class CropOption {
        public CharSequence title;
        public Drawable icon;
        public Intent appIntent;
    }

    private void doCrop() {

        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image*//*");
        List<ResolveInfo> list = activity.getPackageManager().queryIntentActivities(
                intent, 0);

        int size = list.size();
        if (size == 0) {
            Toast.makeText(activity, "Can not find image crop app",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            intent.setData(mImageCaptureUri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            intent.putExtra("outputX", 512);
            intent.putExtra("outputY", 512);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));


            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName,
                        res.activityInfo.name));
                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                boolean flag = false;
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();
                    co.title = activity.getPackageManager().getApplicationLabel(
                            res.activityInfo.applicationInfo);
                    if (co.title.equals("Photos")) {
                        flag = true;
                    }
                    co.icon = activity.getPackageManager().getApplicationIcon(
                            res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);
                    co.appIntent
                            .setComponent(new ComponentName(
                                    res.activityInfo.packageName,
                                    res.activityInfo.name));
                    cropOptions.add(co);
                }

                if (flag) {
                    for (int a = 0; a < list.size(); a++) {
                        ResolveInfo res2 = list.get(a);
                        if (activity.getPackageManager().getApplicationLabel(res2.activityInfo.applicationInfo).equals("Photos")) {
                            Intent ash = new Intent(intent);
                            ash.setComponent(new ComponentName(res2.activityInfo.packageName,
                                    res2.activityInfo.name));
                            startActivityForResult(ash, CROP_FROM_CAMERA);
                            break;
                        }

                    }

                } else {
                    CropOptionAdapter adapter = new CropOptionAdapter(
                            activity, cropOptions);
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
                    builder.setTitle("Choose Crop App");
                    builder.setCancelable(false);
                    builder.setAdapter(adapter,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    startActivityForResult(
                                            cropOptions.get(item).appIntent,
                                            CROP_FROM_CAMERA);
                                }
                            });

                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {

                            if (mImageCaptureUri != null) {
                                activity.getContentResolver().delete(mImageCaptureUri, null,
                                        null);
                                mImageCaptureUri = null;
                            }
                        }
                    });

                    android.support.v7.app.AlertDialog alert = builder.create();
                    alert.show();
                }

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    methodAddImages();
                    return;
                } else {
                    Toast.makeText(activity, "You Denied for camera permission so you cant't update image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }*/
    ////------------------------------------image end ---------------------------------//

    private void methodSubmit() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String donorEmail = etEmail.getText().toString().trim();
        String donorContact = etContact.getText().toString().trim();
        String donorAddress = etAddress.getText().toString().trim();
        String donorPinCode = etPinCode.getText().toString().trim();
        String kinName = etKinName.getText().toString().trim();
        String kinContact = etKinContact.getText().toString().trim();
        String kinEmail = etKinEmail.getText().toString().trim();
        String note = etNote.getText().toString().trim();
        String donorDob = txtDob.getText().toString().trim();
        String donorGender = txtGender.getText().toString().trim();
        String state = txtState.getText().toString().trim();
        String city = txtCity.getText().toString().trim();
        String nextOfKinType = txtNextOfKin.getText().toString().trim();
        if (validation(firstName, lastName, donorEmail, donorContact, donorAddress, donorPinCode, kinName, kinContact, kinEmail, note, donorDob, donorGender, state, city, nextOfKinType)) {
            Toast.makeText(activity, "success", Toast.LENGTH_LONG).show();
            submitmethod(firstName, lastName, donorEmail, donorContact, donorAddress, donorPinCode, kinName, kinContact, kinEmail, note, donorDob, donorGender, state, city, nextOfKinType);
        }
    }

    private void submitmethod(final String firstName, final String lastName, final String donorEmail, final String donorContact, final String donorAddress, final String donorPinCode, final String kinName, final String kinContact, final String kinEmail, final String note, final String donorDob, final String donorGender, final String state, final String city, final String nextOfKinType) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.DONATE_EYE_URL,
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
                params.put("firstName", firstName);
                params.put("lastName", lastName);
                params.put("mobile", donorContact);
                params.put("email", donorEmail);
                params.put("dob", donorDob);
                params.put("gender", donorGender);
                params.put("state", state);
                params.put("city", city);
                params.put("address", donorAddress);
                params.put("zipCode", donorPinCode);
                params.put("kinType", nextOfKinType);
                params.put("kinName", kinName);
                params.put("kinMobile", kinContact);
                params.put("kinEmail", kinEmail);
                params.put("note", note);
                if (photo != null) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    byte[] data = bos.toByteArray();
                    String encodedImage = Base64.encodeToString(data, Base64.DEFAULT);
                    params.put("imageUrl", encodedImage + "image/jpeg");
                } else {
                    params.put("imageUrl", "");
                }
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private boolean validation(String firstName, String lastName, String donorEmail, String donorContact, String donorAddress, String donorPinCode, String kinName, String kinContact, String kinEmail, String note, String donorDob, String donorGender, String state, String city, String nextOfKinType) {
        if (TextUtils.isEmpty(firstName)) {
            etFirstName.setError("You have not entered first name");
            etFirstName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(lastName)) {
            etLastName.setError("You have not entered last name");
            etLastName.requestFocus();
            return false;
        } else if (!Utility.isEmailValid(donorEmail)) {
            etEmail.setError("Donor email id entered is invalid");
            etEmail.requestFocus();
            return false;
        } else if (donorContact.length() != 10) {
            etContact.setError("Mobile number entered is invalid");
            etContact.requestFocus();
            return false;
        } else if (donorDob.equals("DOB")) {
            Toast.makeText(activity, "You have not select Date of birth", Toast.LENGTH_LONG).show();
            return false;
        } else if (donorGender.equals("Gender")) {
            Toast.makeText(activity, "You have not select Gender", Toast.LENGTH_LONG).show();
            return false;
        } else if (state.equals("State")) {
            Toast.makeText(activity, "You have not select State", Toast.LENGTH_LONG).show();
            return false;
        } else if (city.equals("City")) {
            Toast.makeText(activity, "You have not select City", Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(donorAddress)) {
            etAddress.setError("You have not entered Address");
            etAddress.requestFocus();
            return false;
        } else if (donorPinCode.length() != 6) {
            etPinCode.setError("Pincode entered is invalid");
            etPinCode.requestFocus();
            return false;
        } else if (nextOfKinType.equals("Next of Kin")) {
            Toast.makeText(activity, "You have not select Next of Kin", Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(kinName)) {
            etKinName.setError("You have not entered kim's name");
            etKinName.requestFocus();
            return false;
        } else if (kinContact.length() != 10) {
            etKinContact.setError("Kin's mobile number entered is invalid");
            etKinContact.requestFocus();
            return false;
        } else if (!Utility.isEmailValid(kinEmail)) {
            etKinEmail.setError("Kin's email id entered is invalid");
            etKinEmail.requestFocus();
            return false;
        } else {
            return true;
        }
    }

}
