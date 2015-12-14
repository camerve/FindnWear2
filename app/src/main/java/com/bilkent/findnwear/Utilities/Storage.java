package com.bilkent.findnwear.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.bilkent.findnwear.Model.User;
import com.google.gson.Gson;

/**
 * Created by absolute on 7/7/15.
 */
public class Storage {
    private static SharedPreferences prefs;

    public static void init(Context context){
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static User getUser(){
        String userStr = prefs.getString("user", "");
        if(userStr.isEmpty()){
            return null;
        }
        else{
            return new Gson().fromJson(userStr, User.class);
        }
    }

    public static void setUser(User user){
        prefs.edit().putString("user", new Gson().toJson(user)).commit();
    }
}
