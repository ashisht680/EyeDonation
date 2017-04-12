package com.javinindia.ansheyedonate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatEditText;
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
import com.javinindia.ansheyedonate.apiparsing.eventparsing.Event_details_images;
import com.javinindia.ansheyedonate.apiparsing.eventparsing.GalleryResponseParsing;
import com.javinindia.ansheyedonate.constant.Constants;
import com.javinindia.ansheyedonate.recyclerview.GalleryAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 30-03-2017.
 */

public class GalleryFragment extends BaseFragment implements GalleryAdapter.MyClickListener {

    RecyclerView brandRecyclerView;
    private RequestQueue requestQueue;
    private GalleryAdapter adapter;
    private int startLimit = 0;
    private int countLimit = 20;
    private boolean loading = true;
    ArrayList<Event_details_images> arrayList = new ArrayList<Event_details_images>();
    AppCompatEditText etSearch;
    String brand_edit = "";
    ProgressBar progressBar;
    String name = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getArguments().getString("name");
        arrayList = (ArrayList<Event_details_images>) getArguments().getSerializable("images");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setToolbarTitle(name);
        initialize(view);
       // sendRequestOnCatListFeed(0, 20);
        return view;
    }

    private void initialize(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        brandRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewBrand);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 3);
        brandRecyclerView.setLayoutManager(gridLayoutManager);
        adapter = new GalleryAdapter(activity);
        brandRecyclerView.setAdapter(adapter);
        adapter.setMyClickListener(GalleryFragment.this);
     //   brandRecyclerView.addOnScrollListener(new brandScrollListener());


        if (arrayList.size() > 0) {
            if (adapter.getData() != null && adapter.getData().size() > 0) {
                adapter.getData().addAll(arrayList);
                adapter.notifyDataSetChanged();
            } else {
                adapter.setData(arrayList);
                adapter.notifyDataSetChanged();

            }
        }
    }

    private void sendRequestOnCatListFeed(final int startLimit, final int countLimit) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GALLERY_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        GalleryResponseParsing responseparsing = new GalleryResponseParsing();
                        responseparsing.responseParseMethod(response);
                        if (responseparsing.getStatus() == 1) {
                            if (responseparsing.getDetailsArrayList().size() > 0) {
                                arrayList = responseparsing.getDetailsArrayList();
                                if (arrayList.size() > 0) {
                                    if (adapter.getData() != null && adapter.getData().size() > 0) {
                                        adapter.getData().addAll(arrayList);
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        adapter.setData(arrayList);
                                        adapter.notifyDataSetChanged();

                                    }
                                }
                            }

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
                //startlimit=0&countlimit=5
                Map<String, String> params = new HashMap<String, String>();
                params.put("startLimit", String.valueOf(startLimit));
                params.put("countLimit", String.valueOf(countLimit));
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.gallery_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    @Override
    public void onItemClick(int position, Event_details_images model) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("images", arrayList);
        bundle.putInt("position", position);

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        PostImagesFragment newFragment = PostImagesFragment.newInstance();
        newFragment.setArguments(bundle);
        newFragment.show(ft, "slideshow");
    }

    public class brandScrollListener extends RecyclerView.OnScrollListener {
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
                    countLimit = 20;
                    sendRequestOnCatListFeed(startLimit, countLimit);
                    loading = true;
                }
            }
            super.onScrollStateChanged(recyclerView, newState);
        }
    }
}
