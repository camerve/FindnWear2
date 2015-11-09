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
import android.widget.Button;
import android.widget.Toast;

import com.cengalabs.flatui.FlatUI;

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
    public static String email;
    public static ArrayList<Cloth> clothes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DownloadPhotosTask task = new DownloadPhotosTask();
// Getting action bar drawable and setting it.
// Sometimes weird problems may occur while changing action bar drawable at runtime.
// You can try to set title of the action bar to invalidate it after setting background.
        //getActionBar().setBackgroundDrawable(FlatUI.getActionBarDrawable(MainActivity.this, FlatUI.DEEP, false));
        //getSupportActionBar().setBackgroundDrawable(FlatUI.getActionBarDrawable(MainActivity.this,FlatUI.DEEP, false));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
            int code;
            InputStream is;

            StringBuffer chaine = new StringBuffer("");
            try {
                String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
                String query = String.format("email=%s",
                        URLEncoder.encode(email, charset));
                URL url = new URL("http://139.179.92.250/photoURLs.php");
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
                Log.e("in SignUpTask" , "Result: " + result);
            } catch (Exception e) {
                return false;
            }
            try {
                JSONObject json_data = new JSONObject(result);
                code = (json_data.getInt("success"));
                String message = json_data.getString("message");
                Log.e("message", message);
                //Sign up is complete
                if (code == 1) {
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {//Sign up is not complete

                }
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }

            return true;
        }
    }
}
