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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final EditText name = (EditText)findViewById(R.id.name);
        final EditText surname = (EditText)findViewById(R.id.surname);
        final EditText email = (EditText)findViewById(R.id.email);
        final EditText password = (EditText)findViewById(R.id.password);
        final EditText passwordValidator = (EditText)findViewById(R.id.password_again);
        Button signUp = (Button) findViewById(R.id.sign_up_button);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals(passwordValidator.getText().toString())){
                    SignUpTask task = new SignUpTask(name.getText().toString(), surname.getText().toString(),
                            email.getText().toString(),password.getText().toString());
                    task.execute();
                }else{
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public class SignUpTask extends AsyncTask<Void, Void, Boolean> {
        private final String name;
        private final String surname;
        private final String email;
        private final String password;

        SignUpTask(String name, String surname, String email, String password) {
            this.email = email;
            this.password = password;
            this.name = name;
            this.surname = surname;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String result ="";
            int code;
            InputStream is;

            StringBuffer chaine = new StringBuffer("");
            try {
                String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
                String query = String.format("name=%s&surname=%s&email=%s&password=%s",
                        URLEncoder.encode(name, charset),
                        URLEncoder.encode(surname, charset),
                        URLEncoder.encode(email, charset),
                        URLEncoder.encode(password, charset));
                URL url = new URL("http://" +MainActivity.IPAddress+"/signUp.php");
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                String userCredentials = "name:surname:email:password";
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
                    Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(i);
                } else {//Sign up is not complete
                    Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }

            return true;
        }
    }

}
