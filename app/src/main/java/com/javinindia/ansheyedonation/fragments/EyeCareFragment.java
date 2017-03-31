package com.javinindia.ansheyedonation.fragments;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.javinindia.ansheyedonation.R;
import com.javinindia.ansheyedonation.font.FontAsapBoldSingleTonClass;
import com.javinindia.ansheyedonation.font.FontAsapRegularSingleTonClass;

/**
 * Created by Ashish on 31-03-2017.
 */

public class EyeCareFragment extends BaseFragment {


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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setToolbarTitle("Eye care");
        AppCompatTextView txtHd1,txtHd2,txtHd3,txtCon1,txtCon2,txtCon3;
        txtHd1 = (AppCompatTextView)view.findViewById(R.id.txtHd1);
        txtHd1.setPaintFlags(txtHd1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtHd1.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());

        txtHd2 = (AppCompatTextView)view.findViewById(R.id.txtHd2);
        txtHd2.setPaintFlags(txtHd2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtHd2.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());

        txtHd3 = (AppCompatTextView)view.findViewById(R.id.txtHd3);
        txtHd3.setPaintFlags(txtHd3.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtHd3.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());

        txtCon1 = (AppCompatTextView)view.findViewById(R.id.txtCon1);
        txtCon1.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtCon2 = (AppCompatTextView)view.findViewById(R.id.txtCon2);
        txtCon2.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtCon3 = (AppCompatTextView)view.findViewById(R.id.txtCon3);
        txtCon3.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());

        return view;
    }
    @Override
    protected int getFragmentLayout() {
        return R.layout.eye_care_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }
}
