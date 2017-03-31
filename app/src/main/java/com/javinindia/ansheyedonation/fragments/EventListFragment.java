package com.javinindia.ansheyedonation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
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
import com.javinindia.ansheyedonation.R;
import com.javinindia.ansheyedonation.apiparsing.eventparsing.DetailEventItem;
import com.javinindia.ansheyedonation.apiparsing.eventparsing.EventResponseParsing;
import com.javinindia.ansheyedonation.apiparsing.eventparsing.Event_details_images;
import com.javinindia.ansheyedonation.apiparsing.faqaparsing.FaqsResponseParsing;
import com.javinindia.ansheyedonation.constant.Constants;
import com.javinindia.ansheyedonation.recyclerview.EventAdapter;
import com.javinindia.ansheyedonation.recyclerview.FAQsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 30-03-2017.
 */

public class EventListFragment extends BaseFragment implements EventAdapter.MyClickListener {

    private RecyclerView recyclerview;
    private int startLimit = 0;
    private int countLimit = 10;
    private boolean loading = true;
    private RequestQueue requestQueue;
    private EventAdapter adapter;
    ArrayList arrayList;
    String offerId;
    ProgressBar progressBar;
    private ArrayList<Event_details_images> images;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setToolbarTitle("Events");
        initializeMethod(view);
        sendRequestOnReplyFeed(startLimit, countLimit);
        return view;
    }

    private void sendRequestOnReplyFeed(final int AstartLimit, final int AcountLimit) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.EVENT_LIST_URL,
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
        adapter = new EventAdapter(activity);
        LinearLayoutManager layoutMangerDestination
                = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutMangerDestination);
        recyclerview.addOnScrollListener(new replyScrollListener());
        recyclerview.setAdapter(adapter);
        adapter.setMyClickListener(EventListFragment.this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.event_list_layout;
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
            images = new ArrayList<>();
            images = detailEventItem.getEvent_details_images();
            Bundle bundle = new Bundle();
            bundle.putSerializable("images", images);
            bundle.putInt("position", 0);
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            PostImagesFragment newFragment = PostImagesFragment.newInstance().newInstance();
            newFragment.setArguments(bundle);
            newFragment.show(ft, "slideshow");
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
