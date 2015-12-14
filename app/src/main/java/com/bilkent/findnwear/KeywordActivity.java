package com.bilkent.findnwear;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.bilkent.findnwear.Model.Response.ClothList;
import com.bilkent.findnwear.Utilities.API;
import com.google.gson.Gson;

import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class KeywordActivity extends AppCompatActivity {

    private MaterialSpinner typeSpinner;
    private MaterialSpinner colorSpinner;
    private MaterialSpinner textureSpinner;
    private String type;
    private String color;
    private String texture;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword);

        typeSpinner = (MaterialSpinner) findViewById(R.id.type_spinner);
        colorSpinner = (MaterialSpinner) findViewById(R.id.color_spinner);
        textureSpinner = (MaterialSpinner) findViewById(R.id.texture_spinner);

        searchButton = (Button) findViewById(R.id.search_button);

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.types_array, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(this,
                R.array.colors_array, android.R.layout.simple_spinner_item);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(colorAdapter);

        ArrayAdapter<CharSequence> textureAdapter = ArrayAdapter.createFromResource(this,
                R.array.textures_array, android.R.layout.simple_spinner_item);
        textureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        textureSpinner.setAdapter(textureAdapter);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                color = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        textureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                texture = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progress = ProgressDialog.show(KeywordActivity.this, null, getString(R.string.loading), true);
                API.instance.searchKeyword(type, color, texture, new Callback<ClothList>() {
                    @Override
                    public void success(ClothList clothList, Response response) {
                        Log.d("Upload", "success " + clothList.message);
                        Intent intent = new Intent(KeywordActivity.this, SearchResultsActivity.class);
                        intent.putExtra("data", new Gson().toJson(clothList));
                        startActivity(intent);
                        progress.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progress.dismiss();
                    }
                });
            }
        });
    }
}
