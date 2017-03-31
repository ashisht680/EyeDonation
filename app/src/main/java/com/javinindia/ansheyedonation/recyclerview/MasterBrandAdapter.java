package com.javinindia.ansheyedonation.recyclerview;

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

import com.javinindia.ansheyedonation.R;
import com.javinindia.ansheyedonation.apiparsing.eventparsing.Event_details_images;
import com.javinindia.ansheyedonation.font.FontAsapRegularSingleTonClass;
import com.javinindia.ansheyedonation.utility.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ashish on 15-11-2016.
 */
public class MasterBrandAdapter extends RecyclerView.Adapter<MasterBrandAdapter.ViewHolder> {
    List<Event_details_images> list;
    Context context;
    MyClickListener myClickListener;
    ArrayList<Event_details_images> shopCategoryListArrayList;

    public MasterBrandAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Event_details_images> list) {
        this.list = list;
        this.shopCategoryListArrayList = new ArrayList<>();
        this.shopCategoryListArrayList.addAll(list);
    }

    public List<Event_details_images> getData() {
        return list;
    }

    @Override
    public MasterBrandAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.brand_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final Event_details_images brandList = (Event_details_images) list.get(position);

        if (!TextUtils.isEmpty(brandList.getType().trim())) {
            String catName = brandList.getType().trim();
            viewHolder.txtBrand.setText(Utility.fromHtml(catName));
        }
        if (!TextUtils.isEmpty(brandList.getImage_url().trim())) {
            String pic = brandList.getImage_url().trim();
             Utility.imageLoadGlideLibrary(context, viewHolder.progressBar, viewHolder.imgBrand, pic);
        }else {
            viewHolder.imgBrand.setImageResource(R.drawable.default_avatar);
        }

        viewHolder.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onItemClick(position,brandList);
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
            txtBrand.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
            rlMain = (CardView) itemLayoutView.findViewById(R.id.rlMain);
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public interface MyClickListener {
        void onItemClick(int position, Event_details_images model);

    }

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

}
