package com.bilkent.findnwear.Utilities;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by absolute on 12/8/15.
 */
public class API {

    public static FindNWearInterface instance;

    public static void init(){

        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://46.101.123.217")
                .setClient(new OkClient(okHttpClient))
                .build();

        instance = restAdapter.create(FindNWearInterface.class);

    }
}
