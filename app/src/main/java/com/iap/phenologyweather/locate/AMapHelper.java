package com.iap.phenologyweather.locate;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by infolife on 2017/2/14.
 */

public class AMapHelper {

    private Context mContext;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;


    public AMapHelper(Context appContext) {
        this.mContext = appContext;
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setOnceLocation(true);
        mLocationClient = new AMapLocationClient(mContext);
        mLocationClient.setLocationOption(mLocationOption);
    }

    public void locate(AMapLocationListener listener) {
        mLocationClient.setLocationListener(listener);
        mLocationClient.startLocation();
    }

}
