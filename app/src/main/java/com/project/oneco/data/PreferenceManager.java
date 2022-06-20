package com.project.oneco.data;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private static PreferenceManager instance;
    private final SharedPreferences pref;

    private PreferenceManager(Context context) {
        this.pref = context.getSharedPreferences("on-eco", Context.MODE_PRIVATE);
    }

    public static PreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferenceManager(context);
        }
        return instance;
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key, String defaultValue) {
        return pref.getString(key, defaultValue);
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key, int defaultValue) {
        return pref.getInt(key, defaultValue);
    }

}
