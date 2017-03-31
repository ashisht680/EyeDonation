package com.javinindia.ansheyedonation.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javinindia.ansheyedonation.R;
import com.javinindia.ansheyedonation.activity.LoginActivity;
import com.javinindia.ansheyedonation.apiparsing.stateparsing.CountryMasterApiParsing;
import com.javinindia.ansheyedonation.constant.Constants;
import com.javinindia.ansheyedonation.font.FontAsapRegularSingleTonClass;
import com.javinindia.ansheyedonation.picasso.CircleTransform;
import com.javinindia.ansheyedonation.preference.SharedPreferencesManager;
import com.javinindia.ansheyedonation.utility.CheckConnection;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 09-09-2016.
 */
public class NavigationAboutFragment extends BaseFragment implements View.OnClickListener, CheckConnectionFragment.OnCallBackInternetListener, EditProfileFragment.OnCallBackEditProfileListener {
    private RequestQueue requestQueue;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    ProgressBar progressBar;

    public ArrayList<String> stateList = new ArrayList<>();
    String stateArray[] = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initToolbar(view);
        setupDrawerLayout(view);
        initialize(view);
        return view;
    }

    private void initToolbar(View view) {
        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbarNav);
        activity.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        final ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(null);

    }

    private void setupDrawerLayout(View view) {
        drawerLayout = (DrawerLayout) view.findViewById(R.id.DrawerLayout);
        navigationView = (NavigationView) view.findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                displayView(menuItem.getTitle());
                drawerLayout.closeDrawers();
                return true;
            }
        });

        final ImageView avatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.avatar);
        final TextView email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtOwnerName);
        final TextView txtOwnerName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtShopName);
        if (!TextUtils.isEmpty(SharedPreferencesManager.getProfileImage(activity))) {
            Picasso.with(activity).load(SharedPreferencesManager.getProfileImage(activity)).transform(new CircleTransform()).into(avatar);
        } else {
            Picasso.with(activity).load(R.drawable.default_avatar).transform(new CircleTransform()).into(avatar);
        }
        if (!TextUtils.isEmpty(SharedPreferencesManager.getUsername(activity))) {
            txtOwnerName.setText(SharedPreferencesManager.getUsername(activity));
        } else {
            txtOwnerName.setText("Login");
        }
        if (!TextUtils.isEmpty(SharedPreferencesManager.getEmail(activity))) {
            email.setText(SharedPreferencesManager.getEmail(activity));
        }
    }

    private void displayView(CharSequence title) {
        if (CheckConnection.haveNetworkConnection(activity)) {
            if (title.equals("Edit profile")) {
                drawerLayout.closeDrawers();
                EditProfileFragment fragment = new EditProfileFragment();
                fragment.setMyCallBackOfferListener(this);
                callFragmentMethod(fragment, this.getClass().getSimpleName(), R.id.container);
            } else if (title.equals("Donate your eye")) {
                drawerLayout.closeDrawers();
                DonateEyeFragment fragment = new DonateEyeFragment();
                callFragmentMethod(fragment, this.getClass().getSimpleName(), R.id.container);
            } else if (title.equals("Eye care")) {
                drawerLayout.closeDrawers();
                EyeCareFragment eyeCareFragment = new EyeCareFragment();
                callFragmentMethod(eyeCareFragment, this.getClass().getSimpleName(), R.id.container);
            } else if (title.equals("Eye Hospitals")) {
                drawerLayout.closeDrawers();
                HospitalListFragment hospitalListFragment = new HospitalListFragment();
                callFragmentMethod(hospitalListFragment, this.getClass().getSimpleName(), R.id.container);
                //  methodState();
            } else if (title.equals("FAQs")) {
                drawerLayout.closeDrawers();
                FAQsFragment faQsFragment = new FAQsFragment();
                callFragmentMethod(faQsFragment, this.getClass().getSimpleName(), R.id.container);
            } else if (title.equals("Events")) {
                drawerLayout.closeDrawers();
                EventListFragment faQsFragment = new EventListFragment();
                callFragmentMethod(faQsFragment, this.getClass().getSimpleName(), R.id.container);
            } else if (title.equals("Gallery")) {
                drawerLayout.closeDrawers();
                GalleryFragment galleryFragment = new GalleryFragment();
                callFragmentMethod(galleryFragment, this.getClass().getSimpleName(), R.id.container);
            } else if (title.equals("About App")) {
                drawerLayout.closeDrawers();
                AboutAppFragments fragment = new AboutAppFragments();
                callFragmentMethod(fragment, this.getClass().getSimpleName(), R.id.container);
            } else if (title.equals("Invite")) {
                drawerLayout.closeDrawers();
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Ansh");
                    String sAux = "\nLet me recommend you this application\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.javinindia.ansheyedonation\n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "Choose one"));
                } catch (Exception e) {

                }

            } else if (title.equals("Rate us")) {
                drawerLayout.closeDrawers();
                final String appPackageName = activity.getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        } else {
            methodCallCheckInternet();
        }
    }

    public void dialogBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle("Logout");
        alertDialogBuilder.setMessage("Thanks for visiting Ansh Be back soon!");
        alertDialogBuilder.setPositiveButton("Ok!",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        sendDataOnLogOutApi();
                    }
                });

        alertDialogBuilder.setNegativeButton("Take me back",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void sendDataOnLogOutApi() {
        final ProgressDialog loading = ProgressDialog.show(activity, "Logging out...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.LOGOUT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        responseImplement(response);
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
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void responseImplement(String response) {
        JSONObject jsonObject = null;
        String msg = null;
        int status = 0;
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
            SharedPreferencesManager.setUserID(activity, null);
            SharedPreferencesManager.setUsername(activity, null);
            SharedPreferencesManager.setPassword(activity, null);
            SharedPreferencesManager.setEmail(activity, null);
            SharedPreferencesManager.setProfileImage(activity, null);
            SharedPreferencesManager.setDeviceToken(activity, null);
            SharedPreferencesManager.setMobile(activity, null);
            Intent refresh = new Intent(activity, LoginActivity.class);
            startActivity(refresh);//Start the same Activity
            activity.finish();
        } else {
            if (!TextUtils.isEmpty(msg)) {
                showDialogMethod("Try again");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(Gravity.LEFT);
                return true;
            case R.id.action_changePass:
                ChangePasswordFragment fragment = new ChangePasswordFragment();
                callFragmentMethod(fragment, this.getClass().getSimpleName(), R.id.container);
                drawerLayout.closeDrawers();
                return true;
            case R.id.action_feedback:
                FeedbackFragment fragment1 = new FeedbackFragment();
                callFragmentMethod(fragment1, this.getClass().getSimpleName(), R.id.container);
                drawerLayout.closeDrawers();
                return true;
            case R.id.action_logout:
                dialogBox();
                drawerLayout.closeDrawers();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        activity.getMenuInflater().inflate(R.menu.navigation_menu, menu);
    }


    private void methodSetNavData() {
        final ImageView avatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.avatar);
        final TextView email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtOwnerName);
        final TextView txtOwnerName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtShopName);
        if (!TextUtils.isEmpty(SharedPreferencesManager.getProfileImage(activity))) {
            Picasso.with(activity).load(SharedPreferencesManager.getProfileImage(activity)).transform(new CircleTransform()).into(avatar);
        } else {
            Picasso.with(activity).load(R.drawable.default_avatar).transform(new CircleTransform()).into(avatar);
        }
        if (!TextUtils.isEmpty(SharedPreferencesManager.getUsername(activity))) {
            txtOwnerName.setText(SharedPreferencesManager.getUsername(activity));
        }
        if (!TextUtils.isEmpty(SharedPreferencesManager.getEmail(activity))) {
            email.setText(SharedPreferencesManager.getEmail(activity));
        }
    }


    private void initialize(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progress);

        AppCompatButton btnRecipientRegister = (AppCompatButton) view.findViewById(R.id.btnRecipientRegister);
        btnRecipientRegister.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());

        RelativeLayout rlDonateEye = (RelativeLayout) view.findViewById(R.id.rlDonateEye);
        RelativeLayout rlEyeCare = (RelativeLayout) view.findViewById(R.id.rlEyeCare);
        RelativeLayout rlEyeHospitals = (RelativeLayout) view.findViewById(R.id.rlEyeHospitals);
        RelativeLayout rlFAQs = (RelativeLayout) view.findViewById(R.id.rlFAQs);
        RelativeLayout rlEvents = (RelativeLayout) view.findViewById(R.id.rlEvents);
        RelativeLayout rlGallery = (RelativeLayout) view.findViewById(R.id.rlGallery);

        rlDonateEye.setOnClickListener(this);
        rlEyeCare.setOnClickListener(this);
        rlEyeHospitals.setOnClickListener(this);
        rlFAQs.setOnClickListener(this);
        rlEvents.setOnClickListener(this);
        rlGallery.setOnClickListener(this);
        btnRecipientRegister.setOnClickListener(this);
    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.about_layout;
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
        if (CheckConnection.haveNetworkConnection(activity)) {
            switch (v.getId()) {
                case R.id.btnRecipientRegister:
                    BloodDonateFragment recipientRegistrationFragment = new BloodDonateFragment();
                    callFragmentMethod(recipientRegistrationFragment, this.getClass().getSimpleName(), R.id.container);
                    break;
                case R.id.rlDonateEye:
                    DonateEyeFragment donateEyeFragment = new DonateEyeFragment();
                    callFragmentMethod(donateEyeFragment, this.getClass().getSimpleName(), R.id.container);
                    break;
                case R.id.rlEyeHospitals:
                    //methodState();
                    HospitalListFragment hospitalListFragment = new HospitalListFragment();
                    callFragmentMethod(hospitalListFragment, this.getClass().getSimpleName(), R.id.container);
                    break;
                case R.id.rlFAQs:
                    FAQsFragment faQsFragment = new FAQsFragment();
                    callFragmentMethod(faQsFragment, this.getClass().getSimpleName(), R.id.container);
                    break;
                case R.id.rlEvents:
                    EventListFragment eventListFragment = new EventListFragment();
                    callFragmentMethod(eventListFragment, this.getClass().getSimpleName(), R.id.container);
                    break;
                case R.id.rlGallery:
                    GalleryFragment galleryFragment = new GalleryFragment();
                    callFragmentMethod(galleryFragment, this.getClass().getSimpleName(), R.id.container);
                    break;
                case R.id.rlEyeCare:
                    EyeCareFragment eyeCareFragment = new EyeCareFragment();
                    callFragmentMethod(eyeCareFragment, this.getClass().getSimpleName(), R.id.container);
                    break;
            }
        } else {
            methodCallCheckInternet();
        }

    }

    public void methodCallCheckInternet() {
        CheckConnectionFragment fragment = new CheckConnectionFragment();
        fragment.setMyCallBackInternetListener(this);
        callFragmentMethod(fragment, this.getClass().getSimpleName(), R.id.container);
    }


    @Override
    public void OnCallBackInternet() {
        Intent refresh = new Intent(activity, LoginActivity.class);
        startActivity(refresh);//Start the same Activity
        activity.finish();
    }


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

    private void popUp(final String[] popArray, final String type) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setTitle("Select " + type);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setItems(popArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (type.equals("state")) {
                    Toast.makeText(activity, popArray[item], Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    @Override
    public void OnCallBackEditProfile() {
        methodSetNavData();
    }
}
