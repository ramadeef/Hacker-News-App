package com.adeef.hackernewsapp;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Efutures on 11/2/2015.
 */
public class HackerNewsApplication extends Application {
    private static ConnectivityManager cm;

    @Override
    public void onCreate() {
        super.onCreate();
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static final boolean isOnline() {
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}
