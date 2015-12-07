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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.cengalabs.flatui.FlatUI;

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

public class MainActivity extends AppCompatActivity {
    public static String IPAddress = "46.101.123.217";
    //"139.179.153.214" Dorm
    public static String email;
    ListView lv ;
    public static ArrayList<Cloth> clothes;
    Button home, search,profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clothes = new ArrayList<>();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        home = (Button) findViewById(R.id.button);
        search = (Button) findViewById(R.id.button2);
        profile= (Button) findViewById(R.id.button3);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this,SearchActivity.class);
                startActivity(i);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(i);
            }
        });
        lv = (ListView) findViewById(R.id.listView);
        new DownloadPhotosTask().execute();

// Getting action bar drawable and setting it.
// Sometimes weird problems may occur while changing action bar drawable at runtime.
// You can try to set title of the action bar to invalidate it after setting background.
        //getActionBar().setBackgroundDrawable(FlatUI.getActionBarDrawable(MainActivity.this, FlatUI.DEEP, false));
        //getSupportActionBar().setBackgroundDrawable(FlatUI.getActionBarDrawable(MainActivity.this,FlatUI.DEEP, false));
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public class DownloadPhotosTask extends AsyncTask<Void, Void, Boolean> {

        DownloadPhotosTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String result ="";
            InputStream is;

            StringBuffer chaine = new StringBuffer("");
            try {
                String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
                String query = String.format("email=%s",
                        URLEncoder.encode(email, charset));
                URL url = new URL("http://"+IPAddress+"/getReco.php");
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
                Log.e("in DownloadClothesTask" , "Result: " + result);
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
                    clothes.add(cloth);
                }

                } catch (JSONException e1) {
                e1.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            ArrayList<String> isimler= new ArrayList<>();
            ArrayList<String> photoURLs= new ArrayList<>();
            for(int k=0;k<clothes.size();k++){
                isimler.add(clothes.get(k).getDescription());
                photoURLs.add(clothes.get(k).getPhotoURL());
            }
            int size = (clothes.size()/3) + 1;
            ArrayList<String> temp = new ArrayList<>();
            for(int i =0; i<size;i++){
                temp.add("etc");
            }
            ArrayAdapter adapter = new HomeAdapter(MainActivity.this,temp,isimler,photoURLs);
            lv.setAdapter(adapter);
        }
    }
}
