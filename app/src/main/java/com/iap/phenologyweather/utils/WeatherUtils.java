package com.iap.phenologyweather.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.iap.phenologyweather.R;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class WeatherUtils {
    public final static int UNIT_TYPE_ONE = 0;
    public final static int UNIT_TYPE_TWO = 1;
    public final static int UNIT_TYPE_THREE = 2;

    public static String formatSunTimeInto24Date(String sunTime) {
        Date formatter = null;
        String sunTime24Date = null;
        try {
            formatter = new SimpleDateFormat("hh:mmaa", Locale.ENGLISH).parse(sunTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SimpleDateFormat ft = new SimpleDateFormat("HH:mm");
        sunTime24Date = ft.format(formatter);
        return sunTime24Date;
    }

    public static String getWindSpeedTypeByState(int speedType, int speed_metric) {
        String speed;
        String resultSpeed = null;
        switch (speedType) {
            case UNIT_TYPE_ONE:
                Double windSpeed2 = (double) (speed_metric * 0.277777778);
                NumberFormat formater = NumberFormat.getNumberInstance();
                formater.setMaximumFractionDigits(1);
                formater.setMinimumFractionDigits(1);
                speed = formater.format(windSpeed2);
                resultSpeed = speed;
                // resultSpeed=speed+context.getString(R.string.setting_speed_common);
                break;
            case UNIT_TYPE_TWO:
                speed = Integer.toString(speed_metric);
                resultSpeed = speed;
                // resultSpeed=speed+context.getString(R.string.setting_speed_metric);
                break;
            case UNIT_TYPE_THREE:
                int windSpeed = (int) (speed_metric * 0.62);
                speed = Integer.toString(windSpeed);
                resultSpeed = speed;
                // resultSpeed=speed+context.getString(R.string.setting_speed_imperial);
                break;
        }
        if ("0".equals(resultSpeed)) {
            resultSpeed = "0.1";
        }
        return resultSpeed;
    }

    public static String getDistanceTypeByState(int distanceType, int distance_metric) {
        String distance;
        String resultDistance = null;
        switch (distanceType) {
            case UNIT_TYPE_ONE:
                distance = Integer.toString(distance_metric);
                resultDistance = distance;
                // resultDistance=distance+context.getString(R.string.setting_dist_metric);
                break;
            case UNIT_TYPE_TWO:
                int distance_int = (int) (distance_metric * 0.62);
                distance = Integer.toString(distance_int);
                resultDistance = distance;
                // resultDistance=distance+context.getString(R.string.setting_dist_imperial);
                break;
        }
        if ("0".equals(resultDistance)) {
            resultDistance = "1";
        }
        return resultDistance;
    }

    private static final String TAG = WeatherUtils.class.getName();

    public static String getPressureTypeByState(int pressureType, float pres_kpa) {
        Log.d(TAG, "pres_kpa ------ > " + pres_kpa);
        String pressure;
        String resultPres = null;
        switch (pressureType) {
            case UNIT_TYPE_ONE:
                resultPres = (pres_kpa * 10) + "";
                // resultPres=Integer.toString(pres_kpa)+context.getString(R.string.setting_pres_common);
                break;
            case UNIT_TYPE_TWO:
                Double pressure_double_mm = (Double) (pres_kpa * 7.5);
                NumberFormat formater_mm = NumberFormat.getNumberInstance();
                formater_mm.setMaximumFractionDigits(1);
                formater_mm.setMinimumFractionDigits(1);
                pressure = formater_mm.format(pressure_double_mm);
                resultPres = pressure;
                // resultPres=pressure+context.getString(R.string.setting_pres_metric);
                break;
            case UNIT_TYPE_THREE:
                Double pressure_double_in = (Double) (pres_kpa * 0.2953);
                NumberFormat formater_in = NumberFormat.getNumberInstance();
                formater_in.setMaximumFractionDigits(1);
                formater_in.setMinimumFractionDigits(1);
                pressure = formater_in.format(pressure_double_in);
                resultPres = pressure;
                // resultPres=pressure+context.getString(R.string.setting_pres_imperial);
                break;
        }
        return resultPres;
    }

    public static String getTempTypeByState(int tempType, int temp_celsius) {
        String resultTemp = null;
        switch (tempType) {
            case UNIT_TYPE_ONE:
                resultTemp = Integer.toString(temp_celsius);
                break;
            case UNIT_TYPE_TWO:
                int temp_int = (int) (32 + temp_celsius * 1.8);
                resultTemp = Integer.toString(temp_int);
                break;

        }
        return resultTemp;

    }

    public static boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

    public static String transferDegreeFromDirection(String paramString) {

        if (TextUtils.equals(paramString, "N")) {
            return "0";
        }
        if (TextUtils.equals(paramString, "NNE")) {
            return "22.5";
        }
        if (TextUtils.equals(paramString, "NE")) {
            return "45";
        }
        if (TextUtils.equals(paramString, "ENE")) {
            return "67.5";
        }
        if (TextUtils.equals(paramString, "E")) {
            return "90";
        }
        if (TextUtils.equals(paramString, "ESE")) {
            return "112.5";
        }
        if (TextUtils.equals(paramString, "SE")) {
            return "135";
        }
        if (TextUtils.equals(paramString, "SSE")) {
            return "157.5";
        }
        if (TextUtils.equals(paramString, "S")) {
            return "180";
        }

        if (TextUtils.equals(paramString, "SSW")) {
            return "202.5";
        }

        if (TextUtils.equals(paramString, "SW")) {
            return "225";
        }
        if (TextUtils.equals(paramString, "WSW")) {
            return "247.5";
        }
        if (TextUtils.equals(paramString, "W")) {
            return "270";
        }
        if (TextUtils.equals(paramString, "WNW")) {
            return "292.5";
        }
        if (TextUtils.equals(paramString, "NW")) {
            return "315";
        }
        if (TextUtils.equals(paramString, "NNW")) {
            return "337.5";
        }

        return paramString;
    }

    public static double getRandomRealFeel(double realfeel) {
        double resultRealfeel = realfeel;
        Date dt = new Date();
        int hours = dt.getHours();

        int m = (int) (hours % 3);
        if (m == 0) {
        } else if (m == 1) {
            resultRealfeel = resultRealfeel + 1;
        } else {
            resultRealfeel = resultRealfeel - 1;
        }
        return resultRealfeel;

    }

    public static String LocaleSunTimeFromUTC(Context context, String suntime_utc, int weatherDataId) {
        Date utcDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mmaa", Locale.ENGLISH);
        try {
            utcDate = sdf.parse(suntime_utc);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String gmtOffset_string = Preferences.getGMTOffset(context, weatherDataId);
        // String daylightOffset_string =
        // CommonPreferences.getDayLightOffset(context,
        // weatherDataId);

        return new SimpleDateFormat("hh:mmaa", Locale.ENGLISH).format(setUtcDateFromOffsetString(utcDate, gmtOffset_string));

    }

    public static Date setUtcDateFromOffsetString(Date utcTime, String gmtOffset_string) {
        int gmtOffset = 0;
        // int daylightOffset=0;

        if (gmtOffset_string.contains(".5")) {
            try {
                gmtOffset = Integer.parseInt(gmtOffset_string.substring(0, gmtOffset_string.indexOf(".5")));
                gmtOffset = gmtOffset + 1;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            utcTime.setMinutes(utcTime.getMinutes() + 30);
        } else {
            if (gmtOffset_string.contains(".")) {
                try {
                    gmtOffset = Integer.parseInt(gmtOffset_string.substring(0, gmtOffset_string.indexOf(".")));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    gmtOffset = Integer.parseInt(gmtOffset_string);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

        }
        utcTime.setHours(utcTime.getHours() + gmtOffset);
        return utcTime;

    }

    public static int getUtcDateOffset(Date utcTime, String gmtOffset_string) {
        int gmtOffset = 0;
        // int daylightOffset=0;

        if (gmtOffset_string.contains(".5")) {
            try {
                gmtOffset = Integer.parseInt(gmtOffset_string.substring(0, gmtOffset_string.indexOf(".5")));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            utcTime.setMinutes(utcTime.getMinutes() + 30);
        } else {
            if (gmtOffset_string.contains(".")) {
                try {
                    gmtOffset = Integer.parseInt(gmtOffset_string.substring(0, gmtOffset_string.indexOf(".")));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    gmtOffset = Integer.parseInt(gmtOffset_string);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

        }
        return gmtOffset;
    }

    public static long getMillsecondsFromSunTimeString(String time) {

        Date formate = null;
        Date currentDate = new Date();

        try {
            formate = new SimpleDateFormat("hh:mmaa", Locale.ENGLISH).parse(time);
            currentDate.setHours(formate.getHours());
            currentDate.setMinutes(formate.getMinutes());
            return currentDate.getTime();

            // CommonUtils.l("target_date:" + currentDate);
            // CommonUtils.l("target_millis:" + ms);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }

    public static int getRandomRealFeel(String realfeel) {
        int realfeel_int = -999;
        try {
            realfeel_int = Integer.parseInt(realfeel);
        } catch (Exception e) {
            e.printStackTrace();
            return realfeel_int;
        }
        return realfeel_int;

    }

    public static int getWeatherVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo("com.amber.weather", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getTranslatedUV(Context context, String uv) {
        String translated_UV = "";
        if ("Low".equals(uv)) {
            translated_UV = context.getResources().getString(R.string.uv_index_low);
        } else if ("Moderate".equals(uv)) {
            translated_UV = context.getResources().getString(R.string.uv_index_moderate);
        } else if ("High".equals(uv)) {
            translated_UV = context.getResources().getString(R.string.uv_index_high);
        } else if ("Very High".equals(uv)) {
            translated_UV = context.getResources().getString(R.string.uv_index_very_high);
        } else if ("Extreme".equals(uv)) {
            translated_UV = context.getResources().getString(R.string.uv_index_extreme);
        } else {
            translated_UV = uv;
        }
        return translated_UV;
    }
}
