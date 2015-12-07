package com.bilkent.findnwear;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class ResultsActivity extends AppCompatActivity {
    ListView resultsLv;
    Button home, search,profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        resultsLv = (ListView)findViewById(R.id.listView);
        home = (Button) findViewById(R.id.button);
        search = (Button) findViewById(R.id.button2);
        profile= (Button) findViewById(R.id.button3);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResultsActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResultsActivity.this, SearchActivity.class);
                startActivity(i);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResultsActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });
        //new DownloadResultsTask().execute();

    }

}
