package com.javinindia.ansheyedonate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.javinindia.ansheyedonate.R;
import com.javinindia.ansheyedonate.fragments.BaseFragment;
import com.javinindia.ansheyedonate.fragments.CheckConnectionFragment;
import com.javinindia.ansheyedonate.fragments.LoginFragment;
import com.javinindia.ansheyedonate.fragments.NavigationAboutFragment;
import com.javinindia.ansheyedonate.preference.SharedPreferencesManager;
import com.javinindia.ansheyedonate.utility.CheckConnection;


/**
 * Created by Ashish on 26-09-2016.
 */
public class LoginActivity extends BaseActivity implements CheckConnectionFragment.OnCallBackInternetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        if (CheckConnection.haveNetworkConnection(this)) {
            String username = SharedPreferencesManager.getUsername(getApplicationContext());
            if (TextUtils.isEmpty(username)) {
                BaseFragment baseFragment = new LoginFragment();
                FragmentManager fm = this.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.setCustomAnimations(0, 0, 0, 0);
                fragmentTransaction.add(R.id.container, baseFragment);
                fragmentTransaction.commit();
            } else {
                BaseFragment baseFragment = new NavigationAboutFragment();
                FragmentManager fm = this.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.setCustomAnimations(0, 0, 0, 0);
                fragmentTransaction.add(R.id.container, baseFragment);
                fragmentTransaction.commit();
            }
        } else {
            CheckConnectionFragment baseFragment = new CheckConnectionFragment();
            baseFragment.setMyCallBackInternetListener(this);
            FragmentManager fm = this.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.setCustomAnimations(0, 0, 0, 0);
            fragmentTransaction.add(R.id.container, baseFragment);
            fragmentTransaction.commit();
        }


    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_login;
    }

    @Override
    public void OnCallBackInternet() {
        Intent refresh = new Intent(this, LoginActivity.class);
        startActivity(refresh);//Start the same Activity
        finish();
    }
}
