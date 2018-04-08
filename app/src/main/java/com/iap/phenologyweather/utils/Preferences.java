package com.iap.phenologyweather.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by chenxueqing on 2017/2/13.
 */

public class Preferences {

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPreferences(context).edit();
    }


    private static final String CITY_DATA_ID = "city_data_id";

    public static void saveCityId(Context context, int weatherDataId, String cityId) {
        getEditor(context).putString(CITY_DATA_ID + weatherDataId, cityId).apply();
    }

    public static String readCityId(Context context, int weatherDataId) {
        return getSharedPreferences(context).getString(CITY_DATA_ID + weatherDataId, "");
    }


    private static final String GMT_OFFSET = "gmt_offset-";
    public static String get(String aPref, int aAppWidgetId) {
        String str = NumberFormat.getIntegerInstance(Locale.ENGLISH).format(aAppWidgetId);
        // str = str.replace(",", "");
        try {
            aAppWidgetId = Integer.parseInt(str);
        } catch (Exception e) {
            // e.printStackTrace();
            str = str.replace(",", "");
            aAppWidgetId = Integer.parseInt(str);
        }
        String key = aPref + aAppWidgetId;
        return key;
    }

    public static String getGMTOffset(Context context, int id) {
        return getSharedPreferences(context).getString(get(GMT_OFFSET, id), "0");
    }

    public static void setGMTOffset(Context context, String gmtOffset, int id) {
        getEditor(context).putString(get(GMT_OFFSET, id), gmtOffset).apply();

    }

    private static final String CURRENT_WEATHER_DATA_ID = "curr_weather_data_id";

    public static int readCurrWeatherDataId(Context context) {
        return getSharedPreferences(context).getInt(CURRENT_WEATHER_DATA_ID, 1);
    }

    public static void saveCurrWeatherDataId(Context context, int weatherDataId) {
        getEditor(context).putInt(CURRENT_WEATHER_DATA_ID, weatherDataId).apply();
    }

    private static final String HOURLY_SLIDE_GUIDE_SHOW = "hourly_slide_guide_show";

    public static boolean isHourlySlideGuideShow(Context context) {
        return getSharedPreferences(context).getBoolean(HOURLY_SLIDE_GUIDE_SHOW, true);
    }

    public static void setHourlySlideGuideShow(Context context, boolean needShow) {
        getEditor(context).putBoolean(HOURLY_SLIDE_GUIDE_SHOW, needShow).apply();
    }

    private static final String DAILY_SLIDE_GUIDE_SHOW = "daily_slide_guide_show";

    public static boolean isDailySlideGuideShow(Context context) {
        return getSharedPreferences(context).getBoolean(DAILY_SLIDE_GUIDE_SHOW, true);
    }

    public static void setDailySlideGuideShow(Context context, boolean needShow) {
        getEditor(context).putBoolean(DAILY_SLIDE_GUIDE_SHOW, needShow).apply();
    }

    private static final String AQI_LAST_UPDATE_TIME = "aqi_last_time";

    public static long readAqiLastUpdateTime(Context context, int weatherDataId) {
        return getSharedPreferences(context).getLong(AQI_LAST_UPDATE_TIME + weatherDataId, -1L);
    }

    public static void saveAqiLastUpdateTime(Context context, int weatherDataId, long timeMills) {
        getEditor(context).putLong(AQI_LAST_UPDATE_TIME + weatherDataId, timeMills).apply();
    }

    public static final String AQI_VALUE = "aqi_value";

    public static int readAqiValue(Context context, int weatherDataId){
        return getSharedPreferences(context).getInt(AQI_VALUE + weatherDataId, -1);
    }

    public static void saveAqiValue(Context context, int weatherDataId, int aqiValue) {
        getEditor(context).putInt(AQI_VALUE + weatherDataId, aqiValue).apply();
    }

}
