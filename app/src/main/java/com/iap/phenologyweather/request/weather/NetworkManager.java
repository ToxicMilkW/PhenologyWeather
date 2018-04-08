package com.iap.phenologyweather.request.weather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


import com.iap.phenologyweather.utils.HttpUtils;

import java.nio.charset.Charset;

public class NetworkManager {

    private String url;
    private boolean isNetWorkAvailable;
    private Context mContext;

    public NetworkManager(Context context, String url) {
        this.url = url;
        this.mContext = context;
        this.isNetWorkAvailable = isNetworkAvailable(context.getApplicationContext());
    }

    public String excute() {
        String data = "";
        if (!isNetWorkAvailable || url == null) {
            return data;
        }
        Charset.forName("utf-8").encode(url);
        data = HttpUtils.RequestToString(url, data);
        Log.d("cxq", "data:\n" + data);
        return data;
    }


    private boolean isNetworkAvailable(Context applicationContext) {
        ConnectivityManager cm = (ConnectivityManager) applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo[] networks = cm.getAllNetworkInfo();
            if (networks != null) {
                for (NetworkInfo network : networks) {
                    if (network.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
