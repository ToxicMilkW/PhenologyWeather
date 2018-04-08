package com.iap.phenologyweather.utils;

import android.content.Context;

import com.iap.phenologyweather.R;

/**
 * Created by chenxueqing on 2016/7/14.
 */

public class UnitNameUtils {
    public static String getTempUnitName(Context mContext, int tempType) {
        String unitName = "";
        switch (tempType) {
            case WeatherUtils.UNIT_TYPE_ONE:
                unitName = "°C";
                break;
            case WeatherUtils.UNIT_TYPE_TWO:
                unitName = "°F";
                break;
            default:
                unitName = "°C";
                break;
        }
        return unitName;
    }

    public static String getTempUnitNameWithoutO(Context mContext, int tempType){
        String unitName = "";
        switch (tempType) {
            case WeatherUtils.UNIT_TYPE_ONE:
                unitName = "C";
                break;
            case WeatherUtils.UNIT_TYPE_TWO:
                unitName = "F";
                break;
            default:
                unitName = "C";
                break;
        }
        return unitName;
    }

    public static String getSpeedUnitName(Context mContext, int speedType) {
        String unit = "";
        switch (speedType) {
            case WeatherUtils.UNIT_TYPE_ONE:
                unit = mContext.getString(R.string.setting_speed_common);
                break;
            case WeatherUtils.UNIT_TYPE_TWO:
                unit = mContext.getString(R.string.setting_speed_metric);
                break;
            case WeatherUtils.UNIT_TYPE_THREE:
                unit = mContext.getString(R.string.setting_speed_imperial);
                break;
        }
        return unit;
    }

    public static String getPressureUnitName(Context mContext, int pressureType) {
        String unit = "";
        switch (pressureType) {
            case WeatherUtils.UNIT_TYPE_ONE:
                unit = mContext.getString(R.string.setting_pres_common);
                break;
            case WeatherUtils.UNIT_TYPE_TWO:
                unit = mContext.getString(R.string.setting_pres_metric);
                break;
            case WeatherUtils.UNIT_TYPE_THREE:
                unit = mContext.getString(R.string.setting_pres_imperial);
                break;
        }
        return unit;
    }

    public static String getDistanceUnitName(Context mContext, int distanceType) {
        String unit = "";
        switch (distanceType) {
            case WeatherUtils.UNIT_TYPE_ONE:
                unit = mContext.getString(R.string.setting_dist_metric);
                break;
            case WeatherUtils.UNIT_TYPE_TWO:
                unit = mContext.getString(R.string.setting_dist_imperial);
                break;
        }
        return unit;
    }
}

