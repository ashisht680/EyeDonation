package com.javinindia.ansheyedonation.recyclerview;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.javinindia.ansheyedonation.R;
import com.javinindia.ansheyedonation.apiparsing.faqaparsing.FaqDetail;
import com.javinindia.ansheyedonation.apiparsing.hospitalparsing.HospitalDetail;
import com.javinindia.ansheyedonation.font.FontAsapBoldSingleTonClass;
import com.javinindia.ansheyedonation.font.FontAsapRegularSingleTonClass;
import com.javinindia.ansheyedonation.utility.Utility;
import com.javinindia.ansheyedonation.volleycustomrequest.VolleySingleTon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ashish on 30-03-2017.
 */

public class FAQsAdapter extends RecyclerView.Adapter<FAQsAdapter.ViewHolder> {
    List<Object> list;
    Context context;
    MyClickListener myClickListener;

    public FAQsAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Object> list) {
        this.list = list;
    }

    public List<Object> getData() {
        return list;
    }

    @Override
    public FAQsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.faq_item_layout, parent, false);

        FAQsAdapter.ViewHolder viewHolder = new FAQsAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FAQsAdapter.ViewHolder viewHolder, final int position) {
        final VolleySingleTon volleySingleTon = VolleySingleTon.getInstance(context);
        final FaqDetail requestDetail = (FaqDetail) list.get(position);

        if (!TextUtils.isEmpty(requestDetail.getQuestion().trim())) {
            String Name = requestDetail.getQuestion().trim();
            viewHolder.txtQuestion.setText(Utility.fromHtml(position+1 + ". " + Name));
        }
        if (!TextUtils.isEmpty(requestDetail.getAnswer().trim())) {
            String address = requestDetail.getAnswer().trim();
            viewHolder.txtAnswer.setText(address);
        } else {
            viewHolder.txtAnswer.setText("N/A");
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView txtQuestion, txtAnswer;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtQuestion = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtQuestion);
            txtQuestion.setTypeface(FontAsapBoldSingleTonClass.getInstance(context).getTypeFace());
            txtAnswer = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtAnswer);
            txtAnswer.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
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