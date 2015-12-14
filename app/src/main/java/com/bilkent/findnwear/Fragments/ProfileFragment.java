package com.bilkent.findnwear.Fragments;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bilkent.findnwear.Adapters.WishlistAdapter;
import com.bilkent.findnwear.ClothActivity;
import com.bilkent.findnwear.Model.Cloth;
import com.bilkent.findnwear.Model.Response.Base;
import com.bilkent.findnwear.Model.Response.Wishlist;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    private View rootView;
    private TextView nameTextView;
    private TextView surnameTextView;
    private RecyclerView recyclerView;
    private WishlistAdapter wishlistAdapter;
    private TextView emptyTextView;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        nameTextView = (TextView) rootView.findViewById(R.id.name_textview);
        surnameTextView = (TextView) rootView.findViewById(R.id.surname_textview);
        emptyTextView = (TextView) rootView.findViewById(R.id.wishlist_empty_textview);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.wishlist_recyclerview);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        else{
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }

        nameTextView.setText(Storage.getUser().name);
        surnameTextView.setText(Storage.getUser().surname);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        wishlistAdapter = new WishlistAdapter(getActivity());
        wishlistAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position, int id, RecyclerView.ViewHolder holder) {
                Intent intent = new Intent(getActivity(), ClothActivity.class);
                intent.putExtra("data", new Gson().toJson(wishlistAdapter.getItem(position)));
                startActivity(intent);
            }

            @Override
            public void onDelete(int position, int id, RecyclerView.ViewHolder v) {
                API.instance.removeWishlist(
                        Storage.getUser().id,
                        wishlistAdapter.getItem(position).id,
                        new Callback<Base>() {
                            @Override
                            public void success(Base clothList, Response response) {
                                if (clothList.success) {
                                    refreshList();
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                            }
                        });
            }
        });
        recyclerView.setAdapter(wishlistAdapter);

        if(savedInstanceState!=null){
            wishlistAdapter.setData((ArrayList<Cloth>) new Gson().fromJson(savedInstanceState.getString("LIST_DATA"),
                    new TypeToken<ArrayList<Cloth>>() {}.getType()));
            if(wishlistAdapter.getItemCount()==0){
                emptyTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void refreshList() {
        API.instance.getWishlist(Storage.getUser().id, new Callback<Wishlist>() {
            @Override
            public void success(Wishlist clothList, Response response) {
                if (clothList.success) {
                    wishlistAdapter.setData(clothList.items);
                    if (clothList.items.size() == 0) {
                        emptyTextView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        refreshList();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Gson gson = new Gson();
        outState.putString("LIST_DATA", gson.toJson(wishlistAdapter.getData()));
        super.onSaveInstanceState(outState);
    }

}
