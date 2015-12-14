package com.bilkent.findnwear.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bilkent.findnwear.MainActivity;
import com.bilkent.findnwear.Model.Response.Login;
import com.bilkent.findnwear.R;
import com.bilkent.findnwear.Utilities.API;
import com.bilkent.findnwear.Utilities.Storage;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {


    private EditText name;
    private EditText email;
    private EditText surname;
    private EditText password;
    private EditText passwordValidator;
    private Button signUp;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        name = (EditText)rootView.findViewById(R.id.name);
        surname = (EditText)rootView.findViewById(R.id.surname);
        email = (EditText)rootView.findViewById(R.id.email);
        password = (EditText)rootView.findViewById(R.id.password);
        passwordValidator = (EditText)rootView.findViewById(R.id.password_again);
        signUp = (Button) rootView.findViewById(R.id.sign_up_button);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals(passwordValidator.getText().toString())){
                    API.instance.register(
                            email.getText().toString(),
                            password.getText().toString(),
                            name.getText().toString(),
                            surname.getText().toString(),
                            new Callback<Login>() {
                                @Override
                                public void success(Login login, Response response) {
                                    if(login.success){
                                        Storage.setUser(login.user);
                                        gotoMainActivity();
                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Log.d("register", error.getMessage());
                                }
                            });
                }else{
                    Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    private void gotoMainActivity() {
        Intent intent  = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

}
