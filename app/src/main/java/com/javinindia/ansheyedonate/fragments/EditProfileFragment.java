package com.javinindia.ansheyedonate.fragments;

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
import android.graphics.BitmapFactory;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javinindia.ansheyedonate.R;
import com.javinindia.ansheyedonate.apiparsing.loginsignupparsing.LoginSignupResponseParsing;
import com.javinindia.ansheyedonate.constant.Constants;
import com.javinindia.ansheyedonate.font.FontAsapRegularSingleTonClass;
import com.javinindia.ansheyedonate.preference.SharedPreferencesManager;
import com.javinindia.ansheyedonate.utility.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.javinindia.ansheyedonate.utility.Utility.getOutputMediaFile;

/**
 * Created by Ashish on 12-10-2016.
 */
public class EditProfileFragment extends BaseFragment implements View.OnClickListener {
    private RequestQueue requestQueue;

    ImageView imgProfilePic, imgProfilePicNotFound;
    AppCompatEditText etName, etEmailAddress, etMobile, etAddress,etDob,etGender;
    RelativeLayout rlUpadteImg;
    AppCompatTextView txtUpdate, txtEmailHd, txtMobileHd, txtAddressHd,txtDobHd,txtGenderHd;

    private String day = "";
    private String month = "";
    private String year = "";
    private String hour = "";
    private String min = "";
    private String sec = "";

    private Uri mImageCaptureUri;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    Bitmap photo = null;
    String sPic;
    int size = 0;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;

    private File outPutFile = null;


    private OnCallBackEditProfileListener onCallBackEditProfile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public interface OnCallBackEditProfileListener {
        void OnCallBackEditProfile();
    }

    public void setMyCallBackOfferListener(OnCallBackEditProfileListener callback) {
        this.onCallBackEditProfile = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        initToolbar(view);
        initialize(view);

        return view;
    }


    private void initToolbar(View view) {
        setToolbarTitle("Edit Profile");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sendDataOnRegistrationApi();
        outPutFile = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (menu != null) {
            menu.findItem(R.id.action_changePass).setVisible(false);
            menu.findItem(R.id.action_feedback).setVisible(false);
        }
    }

    private void initialize(View view) {

        imgProfilePicNotFound = (ImageView) view.findViewById(R.id.imgProfilePicNotFound);
        imgProfilePic = (ImageView) view.findViewById(R.id.imgProfilePic);
        rlUpadteImg = (RelativeLayout) view.findViewById(R.id.rlUpadteImg);
        etName = (AppCompatEditText) view.findViewById(R.id.etName);
        etName.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtEmailHd = (AppCompatTextView) view.findViewById(R.id.txtEmailHd);
        txtEmailHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etEmailAddress = (AppCompatEditText) view.findViewById(R.id.etEmailAddress);
        etEmailAddress.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtMobileHd = (AppCompatTextView) view.findViewById(R.id.txtMobileHd);
        txtMobileHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etMobile = (AppCompatEditText) view.findViewById(R.id.etMobile);
        etMobile.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtDobHd = (AppCompatTextView) view.findViewById(R.id.txtDobHd);
        txtDobHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etDob = (AppCompatEditText) view.findViewById(R.id.etDob);
        etDob.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtGenderHd = (AppCompatTextView) view.findViewById(R.id.txtGenderHd);
        txtGenderHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etGender = (AppCompatEditText) view.findViewById(R.id.etGender);
        etGender.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtAddressHd = (AppCompatTextView) view.findViewById(R.id.txtAddressHd);
        txtAddressHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etAddress = (AppCompatEditText) view.findViewById(R.id.etAddress);
        etAddress.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtUpdate = (AppCompatTextView) view.findViewById(R.id.txtUpdate);
        txtUpdate.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtUpdate.setOnClickListener(this);
        rlUpadteImg.setOnClickListener(this);
        etGender.setOnClickListener(this);
        etDob.setOnClickListener(this);

    }

    private void sendDataOnRegistrationApi() {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int status;
                        String msg = null, id, name, email, mobile, address, dob,gender;
                        loading.dismiss();
                        LoginSignupResponseParsing loginSignupResponseParsing = new LoginSignupResponseParsing();
                        loginSignupResponseParsing.responseParseMethod(response);

                        status = loginSignupResponseParsing.getStatus();
                        msg = loginSignupResponseParsing.getMsg();

                        if (status == 1) {
                            id = loginSignupResponseParsing.getDetailsArrayList().get(0).getProfileId().trim();
                            sPic = loginSignupResponseParsing.getPic().trim();
                            name = loginSignupResponseParsing.getDetailsArrayList().get(0).getProfileName().trim();
                            email = loginSignupResponseParsing.getDetailsArrayList().get(0).getEmail().trim();
                            mobile = loginSignupResponseParsing.getDetailsArrayList().get(0).getPhone().trim();
                            address = loginSignupResponseParsing.getDetailsArrayList().get(0).getAddress().trim();
                            dob = loginSignupResponseParsing.getDetailsArrayList().get(0).getDob().trim();
                            gender = loginSignupResponseParsing.getDetailsArrayList().get(0).getGender().trim();

                            if (!TextUtils.isEmpty(name)) {
                                etName.setText(Utility.fromHtml(name));
                            }
                            if (!TextUtils.isEmpty(email)) {
                                etEmailAddress.setText(Utility.fromHtml(email));
                            }
                            if (!TextUtils.isEmpty(mobile)) {
                                etMobile.setText(Utility.fromHtml(mobile));
                            }
                            if (!TextUtils.isEmpty(address)) {
                                etAddress.setText(Utility.fromHtml(address));
                            }
                            if (!TextUtils.isEmpty(dob)) {
                                etDob.setText(Utility.fromHtml(dob));
                            }
                            if (!TextUtils.isEmpty(gender)) {
                                etGender.setText(Utility.fromHtml(gender));
                            }


                            if (!TextUtils.isEmpty(sPic))
                                Utility.imageLoadGlideLibraryPic(activity, imgProfilePicNotFound, imgProfilePic, sPic);

                            saveDataOnPreference(email, name, id, sPic,mobile,address);
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
                params.put("userid", SharedPreferencesManager.getUserID(activity));
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
        return R.layout.edit_profile_layout;
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
        int i;
        switch (v.getId()) {

            case R.id.txtUpdate:
                methodUpdateView();
                break;
            case R.id.rlUpadteImg:
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);
                } else {
                    methodAddImages();
                }
                break;
            case R.id.etDob:
                methodOpenDatePicker();
                break;
            case R.id.etGender:
                final String genderArray[] = {"MALE", "FEMALE"};
                popUp(genderArray, "gender");
                break;
        }
    }


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
                                Environment.DIRECTORY_PICTURES), "CameraAnsh");
                        File newFile = new File(imagePath, "temp.jpg");
                        outPutFile = newFile;
                        mImageCaptureUri = FileProvider.getUriForFile(activity, "com.javinindia.ansheyedonate.fileprovider", newFile);
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
                        etDob.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                        day = Integer.toString(dayOfMonth);
                        month = Integer.toString(monthOfYear + 1);
                        EditProfileFragment.this.year = Integer.toString(year);
                    }
                }, mYear, mMonth, mDay);
        long now = System.currentTimeMillis() - 1000;
        // datePickerDialog.getDatePicker().setMinDate(now);
        datePickerDialog.show();
    }

    private void popUp(final String[] popArray, final String type) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setTitle("Select " + type);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setItems(popArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (type.equals("gender")) {
                    etGender.setText(popArray[item]);
                }
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    private void methodUpdateView() {
        String name = etName.getText().toString().trim();
        String email = etEmailAddress.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        String gender = etGender.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(activity, "Please write  name", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(activity, "Please write email", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(activity, "Please write Mobile number", Toast.LENGTH_LONG).show();
        } else {
            methodSubbmit(name,email, mobile, dob,gender,address);
        }
    }

    private void methodSubbmit(final String name, final String email, final String mobile, final String dob, final String gender, final String address) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UPDATE_PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("edit", response);
                        int status;
                        String msg = null, id, name, email, mobile, address, dob,gender;
                        loading.dismiss();
                        LoginSignupResponseParsing loginSignupResponseParsing = new LoginSignupResponseParsing();
                        loginSignupResponseParsing.responseParseMethod(response);

                        status = loginSignupResponseParsing.getStatus();
                        msg = loginSignupResponseParsing.getMsg();
                        sPic = loginSignupResponseParsing.getPic().trim();

                        if (status == 1) {
                            id = loginSignupResponseParsing.getDetailsArrayList().get(0).getProfileId().trim();
                            sPic = loginSignupResponseParsing.getPic().trim();
                            name = loginSignupResponseParsing.getDetailsArrayList().get(0).getProfileName().trim();
                            email = loginSignupResponseParsing.getDetailsArrayList().get(0).getEmail().trim();
                            mobile = loginSignupResponseParsing.getDetailsArrayList().get(0).getPhone().trim();
                            address = loginSignupResponseParsing.getDetailsArrayList().get(0).getAddress().trim();
                            dob = loginSignupResponseParsing.getDetailsArrayList().get(0).getDob().trim();
                            gender = loginSignupResponseParsing.getDetailsArrayList().get(0).getGender().trim();

                            saveDataOnPreference(email, name, id, sPic,mobile,address);
                            onCallBackEditProfile.OnCallBackEditProfile();
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
                //userid=2&name=james&email=bbbb@y.com&mobile=8989898&device_token=fdfdf&gender=male&dob=2017/12/12&address=javinindia&profile_pic=abc.jpg&action=new
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", SharedPreferencesManager.getUserID(activity));
                params.put("name", name);
                params.put("mobile", mobile);
                params.put("email", email);
                params.put("address", address);
                params.put("gender", gender);
                params.put("dob", dob);
                if (photo != null) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    byte[] data = bos.toByteArray();
                    String encodedImage = Base64.encodeToString(data, Base64.DEFAULT);
                    params.put("profile_pic", encodedImage + "image/jpeg");
                    params.put("action", "new");
                } else {
                    params.put("profile_pic", sPic);
                    params.put("action", "old");
                }
                if (!TextUtils.isEmpty(SharedPreferencesManager.getDeviceToken(activity))) {
                    params.put("device_token", SharedPreferencesManager.getDeviceToken(activity));
                } else {
                    params.put("device_token", "deviceToken");
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
                    imgProfilePicNotFound.setImageBitmap(photo);
                    imgProfilePic.setImageBitmap(photo);
                } else {
                    Toast.makeText(activity, "Error while save image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static final Bitmap decodeFile(String filePath) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);
        final int REQUIRED_SIZE = 1024;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
        return bitmap;
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

}