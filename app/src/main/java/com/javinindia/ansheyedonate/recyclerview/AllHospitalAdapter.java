package com.javinindia.ansheyedonate.recyclerview;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.javinindia.ansheyedonate.R;
import com.javinindia.ansheyedonate.apiparsing.hospitalparsing.HospitalDetail;
import com.javinindia.ansheyedonate.font.FontAsapBoldSingleTonClass;
import com.javinindia.ansheyedonate.font.FontAsapRegularSingleTonClass;
import com.javinindia.ansheyedonate.utility.Utility;
import com.javinindia.ansheyedonate.volleycustomrequest.VolleySingleTon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ashish on 18-11-2016.
 */
public class AllHospitalAdapter extends RecyclerView.Adapter<AllHospitalAdapter.ViewHolder> {
    List<Object> list;
    Context context;
    MyClickListener myClickListener;

    public AllHospitalAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Object> list) {
        this.list = list;
    }

    public List<Object> getData() {
        return list;
    }

    @Override
    public AllHospitalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hospital_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AllHospitalAdapter.ViewHolder viewHolder, final int position) {
        final VolleySingleTon volleySingleTon = VolleySingleTon.getInstance(context);
        final HospitalDetail requestDetail = (HospitalDetail) list.get(position);

        if (!TextUtils.isEmpty(requestDetail.getHospitalName().trim())) {
            String Name = requestDetail.getHospitalName().trim();
            viewHolder.txtName.setText(Utility.fromHtml(Name));
        }
        if (!TextUtils.isEmpty(requestDetail.getHospitalAddress().trim())) {
            String address = requestDetail.getHospitalAddress().trim();
            viewHolder.txtAddress.setText("Address : "+address);
        }else {
            viewHolder.txtAddress.setText("Address : N/A");
        }

        final ArrayList<String> data = new ArrayList<>();
        if (!TextUtils.isEmpty(requestDetail.getContactNumber1().trim())){
            data.add(requestDetail.getContactNumber1().trim());
        }
        if (!TextUtils.isEmpty(requestDetail.getContactNumber2().trim())){
            data.add(requestDetail.getContactNumber2().trim());
        }

        if (data.size()>0){
            String str = Arrays.toString(data.toArray());
            String test = str.replaceAll("[\\[\\](){}]", "");
            viewHolder.txtContact.setText("Contact : "+test);
        }else {
            viewHolder.txtContact.setText("Contact : N/A");
        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView txtName, txtAddress,txtContact;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtName = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtName);
            txtName.setTypeface(FontAsapBoldSingleTonClass.getInstance(context).getTypeFace());
            txtAddress = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtAddress);
            txtAddress.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
            txtContact = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtContact);
            txtContact.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public interface MyClickListener {

    }

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public void deleteItem(int index) {
        list.remove(index);
        notifyItemRemoved(index);
    }
}