package com.javinindia.ansheyedonation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.javinindia.ansheyedonation.R;
import com.javinindia.ansheyedonation.font.FontAsapBoldSingleTonClass;
import com.javinindia.ansheyedonation.font.FontAsapRegularSingleTonClass;
import com.javinindia.ansheyedonation.picasso.CircleTransform;
import com.squareup.picasso.Picasso;


public class SplashActivity extends BaseActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(getLayoutResourceId());
        TextView txt_splash = (TextView) findViewById(R.id.txt_splash);
        txt_splash.setTypeface(FontAsapRegularSingleTonClass.getInstance(getApplicationContext()).getTypeFace());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashIntent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(splashIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);


    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_splash;
    }

}
