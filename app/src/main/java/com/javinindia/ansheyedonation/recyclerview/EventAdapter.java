package com.javinindia.ansheyedonation.recyclerview;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.javinindia.ansheyedonation.R;
import com.javinindia.ansheyedonation.apiparsing.eventparsing.DetailEventItem;
import com.javinindia.ansheyedonation.apiparsing.faqaparsing.FaqDetail;
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

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    List<Object> list;
    Context context;
    MyClickListener myClickListener;

    public EventAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Object> list) {
        this.list = list;
    }

    public List<Object> getData() {
        return list;
    }

    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item_layout, parent, false);

        EventAdapter.ViewHolder viewHolder = new EventAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventAdapter.ViewHolder viewHolder, final int position) {
        final VolleySingleTon volleySingleTon = VolleySingleTon.getInstance(context);
        final DetailEventItem requestDetail = (DetailEventItem) list.get(position);

        if (!TextUtils.isEmpty(requestDetail.getEventdetails().getEvent_name().trim())) {
            String Name = requestDetail.getEventdetails().getEvent_name().trim();
            viewHolder.txtEventName.setText(Utility.fromHtml(Name));
        }


        if (!TextUtils.isEmpty(requestDetail.getEventdetails().getDescription().trim())) {
            String dis = requestDetail.getEventdetails().getDescription().trim();
            viewHolder.txtDisc.setText(dis);
        }

        if (!TextUtils.isEmpty(requestDetail.getEventdetails().getAddress().trim())) {
            String address = requestDetail.getEventdetails().getAddress().trim();
            viewHolder.txtAddress.setText(address);
        }

        final ArrayList<String> data = new ArrayList<>();
        if (!TextUtils.isEmpty(requestDetail.getEventdetails().getEvent_date().trim())){
            data.add(requestDetail.getEventdetails().getEvent_date().trim());
        }
        if (!TextUtils.isEmpty(requestDetail.getEventdetails().getEvent_time().trim())){
            data.add("at "+requestDetail.getEventdetails().getEvent_time().trim());
        }


        if (data.size()>0) {
            String str = Arrays.toString(data.toArray());
            String test = str.replaceAll("[\\[\\](){}]", "");
            viewHolder.txtDateTime.setText("On "+test);
        }

        viewHolder.rlEventItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClickListener.onEventClick(position,requestDetail);
            }
        });


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView txtEventName, txtDateTime,txtDisc,txtAddress;

        public RelativeLayout rlEventItem;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtEventName = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtEventName);
            txtEventName.setTypeface(FontAsapBoldSingleTonClass.getInstance(context).getTypeFace());
            txtDateTime = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtDateTime);
            txtDateTime.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
            txtDisc = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtDisc);
            txtDisc.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
            txtAddress = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtAddress);
            txtAddress.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
            rlEventItem = (RelativeLayout)itemLayoutView.findViewById(R.id.rlEventItem);
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public interface MyClickListener {
        void onEventClick(int position, DetailEventItem detailEventItem);
    }

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public void deleteItem(int index) {
        list.remove(index);
        notifyItemRemoved(index);
    }
}