package com.bilkent.findnwear;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class WishlistActivity extends AppCompatActivity {
    Button home, search,profile;
    static ListView lv ;
    public static ArrayList<Cloth> wishlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        wishlist = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listView);
        home = (Button) findViewById(R.id.button);
        search = (Button) findViewById(R.id.button2);
        profile= (Button) findViewById(R.id.button3);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WishlistActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WishlistActivity.this, SearchActivity.class);
                startActivity(i);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WishlistActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });
        new DownloadWishlistTask().execute();



    }
    public class DownloadWishlistTask extends AsyncTask<Void, Void, Boolean> {

        DownloadWishlistTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String result ="";
            InputStream is;

            StringBuffer chaine = new StringBuffer("");
            try {
                String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
                String query = String.format("email=%s",
                        URLEncoder.encode(MainActivity.email, charset));
                URL url = new URL("http://"+MainActivity.IPAddress+"/getWishlist.php");
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                String userCredentials = "email";
                String basicAuth = "Basic " + Base64.encode(userCredentials.getBytes(), Base64.DEFAULT);
                connection.setRequestProperty("Authorization", basicAuth);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
                //connection.setRequestProperty( "Accept", "*/*" );
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.connect();
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

                writer.write(query);
                writer.flush();

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(connection.getInputStream()));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    Log.e("line", line);
                    chaine.append(line);
                }
                result = chaine.toString();
                Log.e("in DownloadWishlistTask" , "Result: " + result);
            } catch (Exception e) {
                return false;
            }
            try {
                JSONArray issueObj = new JSONArray(result);
                for (int i = 0; i < issueObj.length(); i++) {
                    JSONObject issue = issueObj.getJSONObject(i);
                    String description = issue.optString("description");
                    String price = issue.optString("price");
                    String url = issue.optString("url");
                    String id = issue.optString("id");
                    String pictureURL = issue.optString("photo_url");
                    Cloth cloth = new Cloth(description,pictureURL, price, id);
                    wishlist.add(cloth);
                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            ArrayList<String> desciption= new ArrayList<>();
            for(int k=0;k<wishlist.size();k++){
                desciption.add(wishlist.get(k).getDescription());
            }
            ArrayAdapter adapter = new WishlistAdapter(WishlistActivity.this,desciption,wishlist);
            lv.setAdapter(adapter);
        }
    }

}
