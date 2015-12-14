package com.bilkent.findnwear.Fragments;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bilkent.findnwear.Adapters.ClothAdapter;
import com.bilkent.findnwear.ClothActivity;
import com.bilkent.findnwear.Model.Cloth;
import com.bilkent.findnwear.Model.Response.ClothList;
import com.bilkent.findnwear.R;
import com.bilkent.findnwear.Utilities.API;
import com.bilkent.findnwear.Utilities.OnItemClickListener;
import com.bilkent.findnwear.Utilities.Storage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RecommendationFragment extends Fragment {

    private RecyclerView recyclerView;
    private ClothAdapter clothAdapter;

    public RecommendationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.reco_recyclerview);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
        else{
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        clothAdapter = new ClothAdapter(getActivity());
        clothAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position, int id, RecyclerView.ViewHolder holder) {
                Intent intent = new Intent(getActivity(), ClothActivity.class);
                intent.putExtra("data", new Gson().toJson(clothAdapter.getItem(position)));
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(clothAdapter);

        if(savedInstanceState!=null){
            clothAdapter.setData((ArrayList<Cloth>) new Gson().fromJson(savedInstanceState.getString("LIST_DATA"),
                    new TypeToken<ArrayList<Cloth>>() {}.getType()));
        }
        else{
            API.instance.getReco(Storage.getUser().id, new Callback<ClothList>() {
                @Override
                public void success(ClothList clothList, Response response) {
                    if(clothList.success){
                        clothAdapter.setData(clothList.items);
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Gson gson = new Gson();
        outState.putString("LIST_DATA", gson.toJson(clothAdapter.getData()));
        super.onSaveInstanceState(outState);
    }

}
