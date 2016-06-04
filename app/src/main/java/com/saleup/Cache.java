package com.saleup;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;


public class Cache extends Activity {
    static Cache Instance;
    static final String PrefName = "myPrefs";

    private Cache(){

    }

    public static Cache GetInstance(){
        if(Instance == null){
            Instance = new Cache();

        }

        return Instance;
    }

    public void Set(Activity a, String key, Object value){
        SharedPreferences sharedPref = a.getSharedPreferences(PrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key, json);
        editor.commit();

    }

    public Object Get(Activity a, String key){
        SharedPreferences sharedPref = a.getSharedPreferences(PrefName, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString(key, "");
        Object obj = gson.fromJson(json, Object.class);

        return obj;
    }

    public void Remove(Activity a, String key){
        SharedPreferences sharedPref = a.getSharedPreferences(PrefName, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(key);
        editor.apply();
    }
}
