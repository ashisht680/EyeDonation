package com.javinindia.ansheyedonate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javinindia.ansheyedonate.R;
import com.javinindia.ansheyedonate.apiparsing.eventparsing.DetailEventItem;
import com.javinindia.ansheyedonate.apiparsing.eventparsing.EventResponseParsing;
import com.javinindia.ansheyedonate.apiparsing.eventparsing.Event_details_images;
import com.javinindia.ansheyedonate.constant.Constants;
import com.javinindia.ansheyedonate.recyclerview.AlbumAdapter;
import com.javinindia.ansheyedonate.recyclerview.EventAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 10-04-2017.
 */

public class AlbumFragment extends BaseFragment implements AlbumAdapter.MyClickListener {

    private RecyclerView recyclerview;
    private int startLimit = 0;
    private int countLimit = 10;
    private boolean loading = true;
    private RequestQueue requestQueue;
    private AlbumAdapter adapter;
    ArrayList arrayList;
    String offerId;
    ProgressBar progressBar;
    private ArrayList<Event_details_images> images;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setToolbarTitle("Gallery");
        initializeMethod(view);
        sendRequestOnReplyFeed(startLimit, countLimit);
        return view;
    }

    private void sendRequestOnReplyFeed(final int AstartLimit, final int AcountLimit) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ALBUM_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        EventResponseParsing responseparsing = new EventResponseParsing();
                        responseparsing.responseParseMethod(response);
                        int status = responseparsing.getStatus();
                        if (status == 1) {
                            if (responseparsing.getDetailEventItems().size() > 0) {
                                arrayList = responseparsing.getDetailEventItems();
                                if (arrayList.size() > 0) {
                                    // txtDataNotFound.setVisibility(View.GONE);
                                    if (adapter.getData() != null && adapter.getData().size() > 0) {
                                        adapter.getData().addAll(arrayList);
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        adapter.setData(arrayList);
                                        adapter.notifyDataSetChanged();
                                    }
                                } else {
                                    //txtDataNotFound.setVisibility(View.VISIBLE);
                                }
                            } else {
                                // txtDataNotFound.setVisibility(View.VISIBLE);
                            }
                        } else {
                            // txtDataNotFound.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyErrorHandle(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("startLimit", String.valueOf(AstartLimit));
                params.put("countLimit", String.valueOf(AcountLimit));
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void initializeMethod(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerviewList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 3);
        recyclerview.setLayoutManager(gridLayoutManager);
        adapter = new AlbumAdapter(activity);
        recyclerview.addOnScrollListener(new AlbumFragment.replyScrollListener());
        recyclerview.setAdapter(adapter);
        adapter.setMyClickListener(AlbumFragment.this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.album_list_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    @Override
    public void onEventClick(int position, DetailEventItem detailEventItem) {
        if (detailEventItem.getEvent_details_images().size() > 0) {
            GalleryFragment fragment1 = new GalleryFragment();
            String name=detailEventItem.getEventdetails().getEvent_name().trim();
            images = new ArrayList<>();
            images = detailEventItem.getEvent_details_images();
            Bundle bundle = new Bundle();
            bundle.putString("name",name);
            bundle.putSerializable("images", images);
            fragment1.setArguments(bundle);
            callFragmentMethod(fragment1, this.getClass().getSimpleName(), R.id.container);

            /*FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            PostImagesFragment newFragment = PostImagesFragment.newInstance().newInstance();
            newFragment.setArguments(bundle);
            newFragment.show(ft, "slideshow");*/
        }

    }

    public class replyScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            LinearLayoutManager recyclerLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int visibleItemCount = recyclerView.getChildCount();
            int totalItemCount = recyclerLayoutManager.getItemCount();

            int visibleThreshold = ((totalItemCount / 2) < 20) ? totalItemCount / 2 : 20;
            int firstVisibleItem = recyclerLayoutManager.findFirstVisibleItemPosition();

            if (loading) {
                if (totalItemCount > startLimit) {
                    loading = false;
                    startLimit = totalItemCount;
                }
            } else {
                int nonVisibleItemCounts = totalItemCount - visibleItemCount;
                int effectiveVisibleThreshold = firstVisibleItem + visibleThreshold;

                if (nonVisibleItemCounts <= effectiveVisibleThreshold) {
                    startLimit = startLimit + 1;
                    countLimit = 10;

                    sendRequestOnReplyFeed(startLimit, countLimit);
                    loading = true;
                }
            }
            super.onScrollStateChanged(recyclerView, newState);
        }
    }
}
