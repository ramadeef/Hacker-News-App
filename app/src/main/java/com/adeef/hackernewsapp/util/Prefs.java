package com.adeef.hackernewsapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Efutures on 11/2/2015.
 */
public class Prefs {
    public static final String PREFS_NAME = "HackerPrefsFile";

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor prefsEditor;

    public Prefs(Context context)
    {
        this.mPrefs = context.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        this.prefsEditor = mPrefs.edit();
    }

    public boolean isDataCached() {
        return mPrefs.getBoolean("hacker_new_data_cached", false);
    }

    public void setDataCached(boolean value) {
        prefsEditor.putBoolean("hacker_new_data_cached", value);
        prefsEditor.commit();
    }
}
