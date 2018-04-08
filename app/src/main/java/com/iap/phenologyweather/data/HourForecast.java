package com.iap.phenologyweather.data;

import android.content.Context;
import android.graphics.Bitmap;

import com.iap.phenologyweather.data.loader.WeatherInfoLoader;
import com.iap.phenologyweather.utils.AmberSdkConstants;
import com.iap.phenologyweather.utils.Constants;
import com.iap.phenologyweather.utils.DCTUtils;
import com.iap.phenologyweather.utils.IconLoader;
import com.iap.phenologyweather.utils.WeatherConditionID;
import com.iap.phenologyweather.utils.WeatherDataFormatUtils;

import java.util.List;

/**
 * Created by Linn on 9/16/15.
 */
public class HourForecast {
    public static final int NO_BAD_WEATHER = 0;
    public static final int RAIN_BAD_WEATHER = 1;
    public static final int SNOW_BAD_WEATHER = 2;
    public static final int NOW_ID_IS_NOW = 1;
    public static final int NOW_ID_NOT_NOW = 0;
    public static final int NOW_ID_TITLE = -1;
    private String time, temp, humidity, windSpeed, date, windDirection;
    private int iconResId, topColor;
    private int weatherConditionID;
    private boolean isHighest, isLowest, isSun = false;
    private Bitmap iconBmp;
    private int x, y;
    private int badWeatherId;
    private int nowId;
    private String windSpeedUnit;

    public int getWeatherConditionID() {
        return weatherConditionID;
    }

    public void setWeatherConditionID(int weatherConditionID) {
        this.weatherConditionID = weatherConditionID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public int getTopColor() {
        return topColor;
    }

    public void setTopColor(int topColor) {
        this.topColor = topColor;
    }

    public boolean isSun() {
        return isSun;
    }

    public void setSun(boolean sun) {
        isSun = sun;
    }


    public int getIntTemp() {
        int temp = 0;
        try {
            temp = Integer.parseInt(this.temp.replace("°", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    public void setHighest(boolean isHighest) {
        this.isHighest = isHighest;
    }

    public boolean isHighest() {
        return isHighest;
    }

    public boolean isLowest() {
        return isLowest;
    }

    public void setLowest(boolean isLowest) {
        this.isLowest = isLowest;
    }

    private static long parseLong(String str, long defaultValue) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public Bitmap getIconBmp() {
        return iconBmp;
    }

    public void setIconBmp(Bitmap iconBmp) {
        this.iconBmp = iconBmp;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getBadWeatherId() {
        return badWeatherId;
    }

    public void setBadWeatherId(int badWeatherId) {
        this.badWeatherId = badWeatherId;
    }

    public static int witchWeatherId(String weatherId) {
        int id;
        int weatherIdInt;
        try {
            weatherIdInt = Integer.parseInt(weatherId);
        } catch (Exception e) {
            weatherIdInt = 999;
            e.printStackTrace();
        }
        switch (weatherIdInt) {
            case WeatherConditionID.RAIN_LIGHT:// rain_light
            case WeatherConditionID.STORM_RAIN_LIGHT:// storm_rain_light
            case WeatherConditionID.CLEAR_TO_BIG_RAIN_LIGHT:// clear_to_big_rain_light
            case WeatherConditionID.STORM_BIG_RAIN_NIGHT:// storm_big_rain_night
            case WeatherConditionID.HEAVY_RAIN_NIGHT:// heavy_rain_night
            case WeatherConditionID.BIG_RAIN_STORM_NIGHT: // big rain storm night
            case WeatherConditionID.RAIN_STORM_NIGHT: // rain storm night
            case WeatherConditionID.BIG_RAIN_NIGHT: // big rain night
            case WeatherConditionID.CLEAR_SMALL_RAIN_NIGHT: // clear small rain
            case WeatherConditionID.CLEAR_THUNDER_BIG_RAIN_LIGHT:// clear_thunder_big_rain_light
            case WeatherConditionID.CLEAR_THUNDER_RAIN_LIGHT:// clear_thunder_rain_light
            case WeatherConditionID.SLEET_NIGHT: // sleet night
            case WeatherConditionID.BIG_HAIL: // big hail
            case WeatherConditionID.SNOW_RAIN_NIGHT: // snow and rain night
                id = RAIN_BAD_WEATHER;
                break;
            case WeatherConditionID.SNOW_NIGHT:// snow night
            case WeatherConditionID.SNOW_BIG_NIGHT:// snow big night
            case WeatherConditionID.BIG_SNOW_NIGHT: // big snow night
            case WeatherConditionID.SMALL_SNOW_NIGHT: // small snow night
            case WeatherConditionID.CLEAR_SNOW_NORMAL:// clear+snow normal
            case WeatherConditionID.CLEAR_SNOW_SMALL:// clear snow small
            case WeatherConditionID.CLEAR_SNOW_BIG:// clear snow big
                id = SNOW_BAD_WEATHER;
                break;
            default:
                id = NO_BAD_WEATHER;
                break;
        }
        return id;
    }

    public static void fillData(Context context, WeatherInfoLoader weatherInfoLoader, int weatherDataId, List<HourForecast> list) {
//        boolean hasSunrise = false, hasSunset = false;
//        long sunrise1 = parseLong(weatherInfoLoader.getCurrentSunRiseTime(), weatherInfoLoader.getDaySunriseMillis(0));
//        long sunset1 = parseLong(weatherInfoLoader.getCurrentSunSetTime(), weatherInfoLoader.getDaySunsetMillis(0));
//        long sunrise2 = weatherInfoLoader.getDaySunriseMillis(1);
//        long sunset2 = weatherInfoLoader.getDaySunsetMillis(1);
//        if (sunrise1 == -999) {
//            sunrise1 = sunrise2 - ConstantsLibrary.ONE_DAY;
//        }
//        if (sunset1 == -999) {
//            sunset1 = sunset2 - ConstantsLibrary.ONE_DAY;
//        }
        list.clear();
        String hourName;
        boolean isLight;
        int nowIndex = DCTUtils.getCurrentHourIndex(context, weatherDataId, weatherInfoLoader) - 1;
        if (nowIndex < 0) {
            nowIndex = 0;
        }
        if (weatherInfoLoader != null) {
            int gridSize = Math.min(weatherInfoLoader.getHourItems(), 24);
            int startIndex, endIndex;
            endIndex = gridSize - 1;
            if (gridSize <= 24) {
                startIndex = 0;
            } else {
                if ((gridSize - 1 - nowIndex) >= 23) {
                    startIndex = nowIndex;
                } else {
                    startIndex = gridSize - 24;
                }
            }
            if (nowIndex == -1) {
                startIndex = -1;
            }
            if (endIndex - startIndex > 23) {
                endIndex = startIndex + 23;
            }
            for (int i = startIndex; i <= endIndex; i++) {
                if (i == nowIndex) {
                    HourForecast f = new HourForecast();
                    f.setSun(false);
                    String name = weatherInfoLoader.getHourNameByFormat(0, "dd.MMMM");
                    if (name.startsWith("0")) {
                        name = name.replaceFirst("0", "");
                    }
                    name = weatherInfoLoader.getHourNameByFormat(0, "E") + " " + name;
                    f.setNowId(NOW_ID_IS_NOW);
                    f.setDate(name);
                    f.setBadWeatherId(witchWeatherId(weatherInfoLoader.getCurrentIcon()));
                    if (0 == nowIndex) {
                        f.setIconResId(IconLoader.getWeatherImageId(weatherInfoLoader.getCurrentIcon(), DCTUtils.isCurrentCityIsLight(context, weatherInfoLoader, weatherDataId)));
                        f.setWeatherConditionID(Integer.parseInt(weatherInfoLoader.getCurrentIcon()));
                    } else {
                        f.setIconResId(IconLoader.getWeatherImageId(weatherInfoLoader.getCurrentIcon(), weatherInfoLoader.isHourLight(i)));
                        try {
                            f.setWeatherConditionID(Integer.parseInt(weatherInfoLoader.getCurrentIcon()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    f.setTemp(weatherInfoLoader.getCurrentIntTemp());
                    if (weatherInfoLoader.getCurrentHumidity().equals(AmberSdkConstants.DEFAULT_SHOW_STRING)) {
                        f.setHumidity(AmberSdkConstants.DEFAULT_SHOW_STRING);
                    } else {
                        f.setHumidity(weatherInfoLoader.getCurrentHumidity() + "%");
                    }
                    if (weatherInfoLoader.getCurrentWindSpeed().equals(AmberSdkConstants.DEFAULT_SHOW_STRING) || weatherInfoLoader.getCurrentWindSpeed().equals("-999.0")) {
                        f.setWindSpeed(AmberSdkConstants.DEFAULT_SHOW_STRING);
                        f.setWindSpeedUnit(AmberSdkConstants.DEFAULT_SHOW_STRING);
                    } else {
                        f.setWindSpeed(weatherInfoLoader.getCurrentWindSpeed());
                        f.setWindSpeedUnit(weatherInfoLoader.getCurrentWindSpeedUnit());
                    }
                    String direction = WeatherDataFormatUtils.getWindDirectionFromDegree(context, String.valueOf(weatherInfoLoader.getCurrentWindDirection()));
                    if (direction.equals(AmberSdkConstants.DEFAULT_INTEGER_STRING)
                            || direction.equals(AmberSdkConstants.DEFAULT_SHOW_STRING)) {
                        f.setWindDirection(AmberSdkConstants.DEFAULT_SHOW_STRING);
                    } else {
                        f.setWindDirection(WeatherDataFormatUtils.getWindDirection(context, direction));
                    }
                    if (i == -1) {
                        hourName = weatherInfoLoader.getHourNameByFormat(0, "HH:mm").substring(0, 2);
                        if ((Integer.parseInt(hourName) - 1) < 0) {
                            hourName = "23";
                        } else {
                            hourName = String.valueOf((Integer.parseInt(hourName) - 1));
                        }
                    } else {
                        hourName = weatherInfoLoader.getFormattedHourTimeName(i);
                    }
                    f.setTime(hourName);
                    if (weatherInfoLoader.getHourHumidity(i + 2).equals(AmberSdkConstants.DEFAULT_SHOW_STRING)) {
                        f.setHumidity(AmberSdkConstants.DEFAULT_SHOW_STRING);
                    }
                    list.add(f);
                } else if (i >= 0) {
                    HourForecast f = new HourForecast();
                    f.setNowId(NOW_ID_NOT_NOW);
                    f.setSun(false);
                    String name = weatherInfoLoader.getHourNameByFormat(i, "dd.MMMM");
                    if (name.startsWith("0")) {
                        name = name.replaceFirst("0", "");
                    }
                    name = weatherInfoLoader.getHourNameByFormat(i, "E") + " " + name;
                    f.setDate(name.toUpperCase());

                    isLight = weatherInfoLoader.isHourLight(i);
                    f.setBadWeatherId(witchWeatherId(weatherInfoLoader.getHourIcon(i)));
                    f.setIconResId(IconLoader.getWeatherImageId(weatherInfoLoader.getHourIcon(i), isLight));
                    f.setWeatherConditionID(Integer.parseInt(weatherInfoLoader.getHourIcon(i)));
                    f.setTemp(weatherInfoLoader.getHourIntTemp(i));
                    if (weatherInfoLoader.getHourHumidity(i).equals(AmberSdkConstants.DEFAULT_SHOW_STRING)) {
                        f.setHumidity(AmberSdkConstants.DEFAULT_SHOW_STRING);
                    } else {
                        f.setHumidity(weatherInfoLoader.getHourHumidity(i) + "%");
                    }

                    if (weatherInfoLoader.getHourWindSpeed(i).equals(AmberSdkConstants.DEFAULT_SHOW_STRING) || weatherInfoLoader.getHourWindSpeed(i).equals("-999.0")) {
                        f.setWindSpeed(AmberSdkConstants.DEFAULT_SHOW_STRING);
                        f.setWindSpeedUnit(AmberSdkConstants.DEFAULT_SHOW_STRING);
                    } else {
                        f.setWindSpeed(weatherInfoLoader.getHourWindSpeed(i));
                        f.setWindSpeedUnit(weatherInfoLoader.getCurrentWindSpeedUnit());
                    }
                    String direction = WeatherDataFormatUtils.getWindDirectionFromDegree(context, weatherInfoLoader.getHourWindDirection(i) + "");
                    if (direction.equals("-999.0") || direction.equals(AmberSdkConstants.DEFAULT_SHOW_STRING)) {
                        f.setWindDirection(AmberSdkConstants.DEFAULT_SHOW_STRING);
                    } else {
                        f.setWindDirection(WeatherDataFormatUtils.getWindDirection(context, direction));
                    }

                    hourName = weatherInfoLoader.getFormattedHourTimeName(i);

                    f.setTime(hourName);
                    list.add(f);
                }

                //*******for sunrise sunset and others that we don't need now, just now. So don't delete it
//                long time = weatherInfoLoader.getHourMillis(i);
//                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//                boolean hasSun = false;
//                boolean isSunRise = false;
//                String sunTime = null;
////          sunrise/sunset
//                if (isInThisHour(sunrise1, time)) {
//                    sunTime = sdf.format(sunrise1);
//                    isSunRise = true;
//                    hasSun = true;
//                } else if (isInThisHour(sunrise2, time)) {
//                    sunTime = sdf.format(sunrise2);
//                    isSunRise = true;
//                    hasSun = true;
//                } else if (isInThisHour(sunset1, time)) {
//                    sunTime = sdf.format(sunset1);
//                    isSunRise = false;
//                    hasSun = true;
//                } else if (isInThisHour(sunset2, time)) {
//                    sunTime = sdf.format(sunset2);
//                    isSunRise = false;
//                    hasSun = true;
//                }
//                if (hasSun && ((!hasSunrise && isSunRise) || (!hasSunset && !isSunRise))) {
//                    HourForecast f = new HourForecast();
//                    if (list.size() != 0) {
//                        if (isSunRise) {
//                            list.get(list.size() - 1).setIconResId(iconManager.getWeatherIcon(weatherInfoLoader.getHourIcon(i), false, true));
//                            f.setWindDirection(context.getResources().getString(R.string.sunrise));
//                        } else {
//                            list.get(list.size() - 1).setIconResId(iconManager.getWeatherIcon(weatherInfoLoader.getHourIcon(i), true, true));
//                            f.setWindDirection(context.getResources().getString(R.string.sunset));
//                        }
//                    }
//                    f.setNowId(NOW_ID_NOT_NOW);
//                    f.setSun(true);
//                    f.setTime(sunTime);
//                    if (isSunRise) {
//                        f.setIconResId(R.drawable.ic_sunrise);
//                        hasSunrise = true;
//                    } else {
//                        f.setIconResId(R.drawable.ic_sunset);
//                        hasSunset = true;
//                    }
//                    f.setBadWeatherId(witchWeatherId(weatherInfoLoader.getHourIcon(i)));
//                    f.setTemp(weatherInfoLoader.getHourIntTemp(i));
//                    f.setHumidity(AmberSdkConstants.DEFAULT_SHOW_STRING);
//                    f.setWindSpeed(AmberSdkConstants.DEFAULT_SHOW_STRING);
//                    f.setWindSpeedUnit(AmberSdkConstants.DEFAULT_SHOW_STRING);
//                    list.add(f);
//                }
            }
        }
    }

    public int getNowId() {
        return nowId;
    }

    public void setNowId(int nowId) {
        this.nowId = nowId;
    }

    private static boolean isInThisHour(long sun, long now) {
        return now <= sun && sun - now < Constants.ONE_HOUR;
    }

    public String getWindSpeedUnit() {
        return windSpeedUnit;
    }

    public void setWindSpeedUnit(String windSpeedUnit) {
        this.windSpeedUnit = windSpeedUnit;
    }
}
