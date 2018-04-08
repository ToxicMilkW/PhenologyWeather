package com.iap.phenologyweather.utils;

import android.content.Context;
import android.text.TextUtils;

import com.iap.phenologyweather.R;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherDataFormatUtils {
    public final static int UNIT_TYPE_ONE = 0;
    public final static int UNIT_TYPE_TWO = 1;
    public final static int UNIT_TYPE_THREE = 2;

    public static double getRawTemp(double temp, int tempType) {
        double currentTemp = AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE;
        try {
            currentTemp = getTempTypeByState(tempType, temp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return currentTemp;
    }

    public static double getRawWindSpeed(double windSpeed, int speedType) {
        double ResultWindSpeed = AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE;
        try {
            ResultWindSpeed = getWindSpeedTypeByState(speedType, windSpeed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultWindSpeed;

    }

    public static double getRawWindDirection(String wind_direction) {
        double windDirection = AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE;
        if (wind_direction.length() > 0) {

            if (WeatherDataFormatUtils.isNumeric(wind_direction)) {
                try {
                    windDirection = Double.parseDouble(wind_direction);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } else {
                windDirection = getDegreeFromDirection(wind_direction);
            }

        }
        return windDirection;

    }

    public static double getRawPressure(double pressure, int pressureType) {
        double resultPressure = AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE;
        try {
            resultPressure = getPressureTypeByState(pressureType, pressure);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultPressure;
    }

    public static double getRawDewPoint(double dewpoint, int tempType) {
        return getTempTypeByState(tempType, dewpoint);
    }

    public static double getRawRealFeel(double realFeel, int tempType) {

        double resultRealFeel = getRandomRealFeel(realFeel);
        resultRealFeel = getTempTypeByState(tempType, resultRealFeel);
        return resultRealFeel;
    }

    public static double getRawVisibility(double distance, int distanceType) {
        double resultDistance = AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE;
        resultDistance = distance;
        resultDistance = getDistanceTypeByState(distanceType, distance);

        return resultDistance;
    }

    public static String getFormatSunTime(String sunTime, boolean isClock24Formate) {
        String resultSunTime;
        if (isClock24Formate) {
            resultSunTime = formatTimeInto24Date(sunTime);
        } else {
            resultSunTime = formatTimeInto12Date(sunTime);
        }
        return resultSunTime;
    }

    public static long getRawSunTimeMillisSeconds(String sunTime, boolean isLocalTime, long gmtOffset) {
        long resultSunTimeMillis;
        long sunTimeMillis = getMillsSecondsFromSunTimeString(sunTime);
        if (!isLocalTime) {
            resultSunTimeMillis = sunTimeMillis + gmtOffset;
        } else {
            resultSunTimeMillis = sunTimeMillis;
        }
        return resultSunTimeMillis;
    }

    public static long getMillisecondsForHours(double hour) {
        return (long) (3600000.0 * hour);
    }

    public static double getWindSpeedTypeByState(int speedType, double speed_metric) {
        double resultSpeed = AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE;
        switch (speedType) {
            case UNIT_TYPE_ONE:
                // m/s
                double windSpeed2 = speed_metric * 0.277777778;
                NumberFormat formater = NumberFormat.getNumberInstance(Locale.ENGLISH);
                formater.setMaximumFractionDigits(1);
                formater.setMinimumFractionDigits(1);
                try {
                    String formatted = formater.format(windSpeed2);
                    resultSpeed = Double.parseDouble(formatted);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case UNIT_TYPE_TWO:
                // Km/h
                resultSpeed = speed_metric;
                break;
            case UNIT_TYPE_THREE:
                // MPH
                resultSpeed = speed_metric * 0.62;
                break;
        }
        if (resultSpeed == 0) {
            resultSpeed = 0.1;
        }
        return resultSpeed;
    }

    public static double getSpeedMph2ms(double mph) {
        return getWindSpeedTypeByState(UNIT_TYPE_ONE, mph / 0.62);
    }

    public static double getSpeedKm2ms(double km) {
        return getWindSpeedTypeByState(UNIT_TYPE_ONE, km);
    }

    public static double getWindSpeedMsFromOtherType(int speedUnit, double speed) {
        switch (speedUnit) {
            case UNIT_TYPE_ONE:
                return speed;
            case UNIT_TYPE_TWO:
                return getWindSpeedTypeByState(UNIT_TYPE_ONE, speed);
            case UNIT_TYPE_THREE:
                return getSpeedMph2ms(speed);
            default:
                return speed;
        }
    }

    public static double getDistanceTypeByState(int distanceType, double distance_metric) {
        double resultDistance = AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE;
        switch (distanceType) {
            case UNIT_TYPE_ONE:
                //KM
                resultDistance = distance_metric;
                break;
            case UNIT_TYPE_TWO:
                //Miles
                resultDistance = distance_metric * 0.62;
                break;
        }
        if (resultDistance == 0) {
            resultDistance = 1.0;
        }
        return resultDistance;
    }

    public static double getDistanceMile2KM(double mile) {
        return mile / 0.62;
    }

    private final static String TAG = WeatherDataFormatUtils.class.getName();

    public static double getPressureTypeByState(int pressureType, double pres_kpa) {
        double resultPres = AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE;
        switch (pressureType) {
            case UNIT_TYPE_ONE:
                //hPa
                resultPres = pres_kpa * 10;
                break;
            case UNIT_TYPE_TWO:
                // mm/Hg
                Double pressure_double_mm = (Double) (pres_kpa * 7.5);
                NumberFormat formater_mm = NumberFormat.getNumberInstance(Locale.ENGLISH);
                formater_mm.setMaximumFractionDigits(1);
                formater_mm.setMinimumFractionDigits(1);
                try {
                    resultPres = Double.parseDouble(formater_mm.format(pressure_double_mm));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                break;
            case UNIT_TYPE_THREE:
                // in/Hg
                Double pressure_double_in = (Double) (pres_kpa * 0.2953);
                NumberFormat formater_in = NumberFormat.getNumberInstance(Locale.ENGLISH);
                formater_in.setMaximumFractionDigits(1);
                formater_in.setMinimumFractionDigits(1);
                try {
                    resultPres = Double.parseDouble(formater_in.format(pressure_double_in));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                break;
        }
        return resultPres;

    }

    public static double getTempTypeByState(int tempType, double tempCelsius) {
        double resultTemp = AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE;
        switch (tempType) {
            case UNIT_TYPE_ONE:
                //C
                resultTemp = tempCelsius;
                break;
            case UNIT_TYPE_TWO:
                //F
                resultTemp = 32 + tempCelsius * 1.8;
                break;
        }
        return resultTemp;
    }

    public static double getTempF2C(double tempF) {
        return (tempF - 32) / 1.8;
    }

    private static String formatTimeInto12Date(String sunTime) {
        try {
            Date formater = new SimpleDateFormat("HH:mm", Locale.ENGLISH).parse(sunTime);
            SimpleDateFormat ft = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
            sunTime = ft.format(formater);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sunTime;
    }

    public static String formatTimeInto24Date(String sunTime) {
        try {
            Date formater = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH).parse(sunTime);
            SimpleDateFormat ft = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            sunTime = ft.format(formater);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sunTime;
    }

    public static boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

    public static double getDegreeFromDirection(String paramString) {

        if (TextUtils.equals(paramString, "N")) {
            return 0;
        }
        if (TextUtils.equals(paramString, "NNE")) {
            return 22.5;
        }
        if (TextUtils.equals(paramString, "NE")) {
            return 45;
        }
        if (TextUtils.equals(paramString, "ENE")) {
            return 67.5;
        }
        if (TextUtils.equals(paramString, "E")) {
            return 90;
        }
        if (TextUtils.equals(paramString, "ESE")) {
            return 112.5;
        }
        if (TextUtils.equals(paramString, "SE")) {
            return 135;
        }
        if (TextUtils.equals(paramString, "SSE")) {
            return 157.5;
        }
        if (TextUtils.equals(paramString, "S")) {
            return 180;
        }

        if (TextUtils.equals(paramString, "SSW")) {
            return 202.5;
        }

        if (TextUtils.equals(paramString, "SW")) {
            return 225;
        }
        if (TextUtils.equals(paramString, "WSW")) {
            return 247.5;
        }
        if (TextUtils.equals(paramString, "W")) {
            return 270;
        }
        if (TextUtils.equals(paramString, "WNW")) {
            return 292.5;
        }
        if (TextUtils.equals(paramString, "NW")) {
            return 315;
        }
        if (TextUtils.equals(paramString, "NNW")) {
            return 337.5;
        }

        return 0;
    }

    public static double getRandomRealFeel(double realFeel) {
        double resultRealFeel = realFeel;
        Date dt = new Date();
        int hours = dt.getHours();
        int m = hours % 3;
        if (m != 0) {
            if (m == 1) {
                resultRealFeel = resultRealFeel + 1;
            } else {
                resultRealFeel = resultRealFeel - 1;
            }
        }
        return resultRealFeel;
    }

    public static long getMillsSecondsFromSunTimeString(String time) {

        Date formatDate;
        Date currentDate = new Date();
        try {
            formatDate = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).parse(time);
            currentDate.setHours(formatDate.getHours());
            currentDate.setMinutes(formatDate.getMinutes());
            long l = currentDate.getTime();
            return currentDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getRandomRealFeel(String realfeel) {
        int realfeel_int = -1;
        try {
            realfeel_int = Integer.parseInt(realfeel);
        } catch (Exception e) {
            e.printStackTrace();
            return realfeel_int;
        }
        Date dt = new Date();
        int hours = dt.getHours();

        int m = (int) (hours % 3);
        if (m == 0) {
        } else if (m == 1) {
            realfeel_int = realfeel_int + 1;
        } else {
            realfeel_int = realfeel_int - 1;
        }
        return realfeel_int;

    }

    public static String getFormattedSunTime(long sunTime) {
        String formattedTime = "--";
        if (sunTime > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mmaa", Locale.ENGLISH);
            formattedTime = sdf.format(new Date(sunTime));
        }
        return formattedTime;
    }


    // the method get the wind direction describe from degree
    // String degree:the wind direction degree
    // return the wind direction in short name
    public static String getWindDirectionFromDegree(Context context, String degree) {
        int paramInt = 0;
        if (degree.contains(".")) {
            String windDirection = degree.substring(0, degree.indexOf("."));

            try {
                paramInt = Integer.parseInt(windDirection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                paramInt = Integer.parseInt(degree);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (paramInt == 0 || 360 == paramInt) {
            return context.getResources().getString(R.string.wind_direction_north);
        }
        if (paramInt > 0 && paramInt < 45) {
            return context.getResources().getString(R.string.wind_direction_north_north_east);
        }
        if (paramInt == 45) {
            return context.getResources().getString(R.string.wind_direction_north_east);
        }
        if (paramInt > 45 && paramInt < 90) {
            return context.getResources().getString(R.string.wind_direction_east_north_east);
        }
        if (paramInt == 90) {
            return context.getResources().getString(R.string.wind_direction_east);
        }
        if (paramInt > 90 && paramInt < 135) {
            return context.getResources().getString(R.string.wind_direction_east_south_east);
        }
        if (paramInt == 135) {
            return context.getResources().getString(R.string.wind_direction_south_east);
        }
        if (paramInt > 135 && paramInt < 180) {
            return context.getResources().getString(R.string.wind_direction_south_south_east);
        }
        if (paramInt == 180) {
            return context.getResources().getString(R.string.wind_direction_south);
        }
        if (paramInt > 180 && paramInt < 225) {
            return context.getResources().getString(R.string.wind_direction_south_south_west);
        }
        if (paramInt == 225) {
            return context.getResources().getString(R.string.wind_direction_south_west);
        }
        if (paramInt > 225 && paramInt < 270) {
            return context.getResources().getString(R.string.wind_direction_west_south_west);
        }
        if (paramInt == 270) {
            return context.getResources().getString(R.string.wind_direction_west);
        }
        if (paramInt > 270 && paramInt < 315) {
            return context.getResources().getString(R.string.wind_direction_west_north_west);
        }
        if (paramInt == 315) {
            return context.getResources().getString(R.string.wind_direction_north_west);
        }
        if (paramInt > 315 && paramInt < 360) {
            return context.getResources().getString(R.string.wind_direction_north_north_west);
        }

        return degree;

    }

    public static String getFormattedTimeName(long timeMills) {
        String timeName = "--";
        if (timeMills > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("hha", Locale.ENGLISH);
            timeName = sdf.format(new Date(timeMills));
        }
        return timeName;
    }

    public static String getWindDirection(Context context, String paramString) {
        if (TextUtils.equals(paramString, "0") || TextUtils.equals(paramString, "N")) {
            return context.getString(R.string.wind_direction_n);
        }
        if (TextUtils.equals(paramString, "22.5") || TextUtils.equals(paramString, "NNE")) {
            return context.getString(R.string.wind_direction_nne);
        }
        if (TextUtils.equals(paramString, "45") || TextUtils.equals(paramString, "NE")) {
            return context.getString(R.string.wind_direction_ne);
        }
        if (TextUtils.equals(paramString, "67.5") || TextUtils.equals(paramString, "ENE")) {
            return context.getString(R.string.wind_direction_ene);
        }
        if (TextUtils.equals(paramString, "90") || TextUtils.equals(paramString, "E")) {
            return context.getString(R.string.wind_direction_e);
        }
        if (TextUtils.equals(paramString, "112.5") || TextUtils.equals(paramString, "ESE")) {
            return context.getString(R.string.wind_direction_ese);
        }
        if (TextUtils.equals(paramString, "135") || TextUtils.equals(paramString, "SE")) {
            return context.getString(R.string.wind_direction_se);
        }
        if (TextUtils.equals(paramString, "157.5") || TextUtils.equals(paramString, "SSE")) {
            return context.getString(R.string.wind_direction_sse);
        }
        if (TextUtils.equals(paramString, "180") || TextUtils.equals(paramString, "S")) {
            return context.getString(R.string.wind_direction_s);
        }
        if (TextUtils.equals(paramString, "202.5") || TextUtils.equals(paramString, "SSW")) {
            return context.getString(R.string.wind_direction_ssw);
        }
        if (TextUtils.equals(paramString, "225") || TextUtils.equals(paramString, "SW")) {
            return context.getString(R.string.wind_direction_sw);
        }
        if (TextUtils.equals(paramString, "247.5") || TextUtils.equals(paramString, "WSW")) {
            return context.getString(R.string.wind_direction_wsw);
        }
        if (TextUtils.equals(paramString, "270") || TextUtils.equals(paramString, "W")) {
            return context.getString(R.string.wind_direction_w);
        }
        if (TextUtils.equals(paramString, "292.5") || TextUtils.equals(paramString, "WNW")) {
            return context.getString(R.string.wind_direction_wnw);
        }
        if (TextUtils.equals(paramString, "315") || TextUtils.equals(paramString, "NW")) {
            return context.getString(R.string.wind_direction_nw);
        }
        if (TextUtils.equals(paramString, "337.5") || TextUtils.equals(paramString, "NNW")) {
            return context.getString(R.string.wind_direction_nnw);
        }
        return paramString;
    }

}
