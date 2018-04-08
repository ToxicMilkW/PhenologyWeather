package com.iap.phenologyweather.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class VolleySingleton {

    private static RequestQueue mRequestQueue;

    private VolleySingleton() {
    }

    public static synchronized RequestQueue getInstance(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }

}
