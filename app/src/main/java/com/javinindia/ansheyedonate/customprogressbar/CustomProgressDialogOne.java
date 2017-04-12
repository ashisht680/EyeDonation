package com.javinindia.ansheyedonate.customprogressbar;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.javinindia.ansheyedonate.R;


public class CustomProgressDialogOne  {

    public void showCustomDialog(Context context) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_progress_dialog_one, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);
        RelativeLayout mainLayout = (RelativeLayout) dialogView.findViewById(R.id.mainLayout);
        mainLayout.setBackgroundResource(R.drawable.my_progress_one);
        AnimationDrawable frameAnimation = (AnimationDrawable)mainLayout.getBackground();
        frameAnimation.start();
        AlertDialog b = dialogBuilder.create();
        b.show();
    }


}
