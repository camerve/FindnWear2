package com.bilkent.findnwear.Utilities;

import retrofit.RestAdapter;

/**
 * Created by absolute on 12/8/15.
 */
public class API {

    public static FindNWearInterface instance;

    public static void init(){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://46.101.123.217")
                .build();

        instance = restAdapter.create(FindNWearInterface.class);

    }
}
