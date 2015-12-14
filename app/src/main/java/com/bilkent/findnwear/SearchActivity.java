package com.bilkent.findnwear;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.bilkent.findnwear.Model.Response.ClothList;
import com.bilkent.findnwear.Utilities.API;
import com.google.gson.Gson;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class SearchActivity extends AppCompatActivity {

    private ImageView searchImageView;
    private MaterialSpinner typeSpinner;
    private Button searchButton;
    private File photoFile;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchImageView = (ImageView) findViewById(R.id.search_imageview);
        typeSpinner = (MaterialSpinner) findViewById(R.id.type_spinner);
        searchButton = (Button) findViewById(R.id.search_button);

        photoFile = (File) getIntent().getSerializableExtra("File");
        Log.d("path", photoFile.getAbsolutePath());

        Picasso.with(this)
                .load(photoFile)
                .fit()
                .centerInside()
                .into(searchImageView);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri source = Uri.fromFile(photoFile);
                Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
                Crop.of(source, destination).start(SearchActivity.this);
            }
        });

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progress = ProgressDialog.show(SearchActivity.this, null, getString(R.string.loading), true);

                File uploadFile = new File(getCacheDir(), "upload");
                try {
                    FileOutputStream fos = new FileOutputStream(uploadFile);
                    Bitmap bitmap = ((BitmapDrawable) searchImageView.getDrawable()).getBitmap();

                    int height = bitmap.getHeight();
                    int width = bitmap.getWidth();

                    float scale = (height > width) ? 800f / (float)height : 800f / (float)width;

                    Log.d("SendingSize", ((int)((float)width * scale)) + " " + ((int)((float)height * scale)));
                    bitmap = Bitmap.createScaledBitmap(bitmap, ((int)((float)width * scale)), ((int)((float)height * scale)), false);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                    fos.close();
                } catch (FileNotFoundException e) {
                    Log.d("StoreImage", "File not found: " + e.getMessage());
                    progress.dismiss();
                } catch (IOException e) {
                    Log.d("StoreImage", "Error accessing file: " + e.getMessage());
                    progress.dismiss();
                }

                TypedFile typedFile = new TypedFile("multipart/form-data", uploadFile);

                API.instance.search(typedFile, type, new Callback<ClothList>() {
                    @Override
                    public void success(ClothList clothList, Response response) {
                        Log.d("Upload", "success " + clothList.message);
                        Intent intent = new Intent(SearchActivity.this, SearchResultsActivity.class);
                        intent.putExtra("data", new Gson().toJson(clothList));
                        startActivity(intent);
                        progress.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("Upload", "error type: " + type + " message: " + error.getMessage());
                        progress.dismiss();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Crop.REQUEST_CROP) {
                searchImageView.setImageURI(Crop.getOutput(data));
                photoFile = new File(getCacheDir(), "cropped");
            }
        }

    }

}
