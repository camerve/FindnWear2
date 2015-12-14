package com.bilkent.findnwear;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.bilkent.findnwear.Adapters.ClothAdapter;
import com.bilkent.findnwear.Model.Cloth;
import com.bilkent.findnwear.Model.Response.ClothList;
import com.bilkent.findnwear.Utilities.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {
    RecyclerView resultsRecyclerView;
    private ClothList data;
    private ClothAdapter clothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        resultsRecyclerView = (RecyclerView)findViewById(R.id.results_recyclerview);

        data = new Gson().fromJson(getIntent().getStringExtra("data"), ClothList.class);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            resultsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        else{
            resultsRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        clothAdapter = new ClothAdapter(this);
        clothAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position, int id, RecyclerView.ViewHolder holder) {
                Intent intent = new Intent(SearchResultsActivity.this, ClothActivity.class);
                intent.putExtra("data", new Gson().toJson(clothAdapter.getItem(position)));
                startActivity(intent);
            }
        });
        resultsRecyclerView.setAdapter(clothAdapter);

        if(savedInstanceState!=null){
            clothAdapter.setData(
                    (ArrayList<Cloth>) new Gson().fromJson(
                            savedInstanceState.getString("LIST_DATA"),
                            new TypeToken<ArrayList<Cloth>>() {}
                                    .getType()
                    )
            );
        }
        else{
            clothAdapter.setData(data.items);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Gson gson = new Gson();
        outState.putString("LIST_DATA", gson.toJson(clothAdapter.getData()));
        super.onSaveInstanceState(outState);
    }

}
