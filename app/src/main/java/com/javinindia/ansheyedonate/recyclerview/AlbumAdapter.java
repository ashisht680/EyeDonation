package com.javinindia.ansheyedonate.recyclerview;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.javinindia.ansheyedonate.R;
import com.javinindia.ansheyedonate.apiparsing.eventparsing.DetailEventItem;
import com.javinindia.ansheyedonate.font.FontAsapBoldSingleTonClass;
import com.javinindia.ansheyedonate.font.FontAsapRegularSingleTonClass;
import com.javinindia.ansheyedonate.utility.Utility;
import com.javinindia.ansheyedonate.volleycustomrequest.VolleySingleTon;

import java.util.List;


/**
 * Created by Ashish on 10-04-2017.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    List<Object> list;
    Context context;
    MyClickListener myClickListener;

    public AlbumAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Object> list) {
        this.list = list;
    }

    public List<Object> getData() {
        return list;
    }

    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_item_layout, parent, false);

       ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final VolleySingleTon volleySingleTon = VolleySingleTon.getInstance(context);
        final DetailEventItem requestDetail = (DetailEventItem) list.get(position);

        if (!TextUtils.isEmpty(requestDetail.getEventdetails().getEvent_name().trim())) {
            String catName = requestDetail.getEventdetails().getEvent_name().trim();
            viewHolder.txtBrand.setText(Utility.fromHtml(catName));
            viewHolder.txtBrand.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(requestDetail.getEvent_details_images().get(0).getImage_url().trim())) {
            String pic = requestDetail.getEvent_details_images().get(0).getImage_url().trim();
            Utility.imageLoadGlideLibrary(context, viewHolder.progressBar, viewHolder.imgBrand, pic);
        }else {
            viewHolder.imgBrand.setImageResource(R.drawable.default_avatar);
        }

        viewHolder.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onEventClick(position,requestDetail);
            }
        });



    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView txtBrand;
        public CardView rlMain;
        private ImageView imgBrand;
        public ProgressBar progressBar;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            imgBrand = (ImageView) itemLayoutView.findViewById(R.id.imgBrand);
            progressBar = (ProgressBar) itemLayoutView.findViewById(R.id.progress);
            txtBrand = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtBrand);
            txtBrand.setTypeface(FontAsapBoldSingleTonClass.getInstance(context).getTypeFace());
            rlMain = (CardView) itemLayoutView.findViewById(R.id.rlMain);
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