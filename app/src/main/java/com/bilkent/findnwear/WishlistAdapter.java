package com.bilkent.findnwear;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by 1 on 06.12.2015.
 */
public class WishlistAdapter extends ArrayAdapter {
    ArrayList<Cloth> data;
    ArrayList<String> description;
    Activity context;
    int deletedPosition;
    public WishlistAdapter(Activity context,ArrayList<String> description, ArrayList<Cloth> data) {
        super(context, R.layout.wishlist_list_item, description);
        this.description = description;
        this.data = data;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.wishlist_list_item, null, true);
        //Views inside the wiahlist item
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);
        Button deleteButton = (Button) rowView.findViewById(R.id.deleteButton);
        TextView desc = (TextView) rowView.findViewById(R.id.description);
        desc.setText(description.get(position));
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletedPosition = position;
                new DeleteTask().execute();
            }
        });
        return rowView;
    }
    public class DeleteTask extends AsyncTask<Void, Void, Boolean> {

        DeleteTask() {
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
                URL url = new URL("http://"+MainActivity.IPAddress+"/deleteItemWishlist.php");
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
                Log.e("in DeleteFromWishlistTask" , "Result: " + result);
            } catch (Exception e) {
                return false;
            }
            try {
                JSONObject json_data = new JSONObject(result);
                int code = (json_data.getInt("success"));
                String message = json_data.getString("message");
                Log.e("message", message);
                //Sign up is complete
                if (code == 1) {
                    WishlistActivity.wishlist.remove(deletedPosition);
                } else {//Item cannot be deleted there's a problem
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            ArrayList<String> desciption= new ArrayList<>();
            for(int k=0;k<WishlistActivity.wishlist.size();k++){
                desciption.add(WishlistActivity.wishlist.get(k).getDescription());
            }
            ArrayAdapter adapter = new WishlistAdapter(context,desciption,WishlistActivity.wishlist);
            WishlistActivity.lv.setAdapter(adapter);
        }
    }
}
