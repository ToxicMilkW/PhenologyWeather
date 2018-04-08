package com.iap.phenologyweather.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.iap.phenologyweather.data.loader.WeatherInfoLoader;
import com.iap.phenologyweather.utils.AmberSdkConstants;
import com.iap.phenologyweather.utils.Constants;
import com.iap.phenologyweather.utils.DCTUtils;
import com.iap.phenologyweather.utils.IconLoader;
import com.iap.phenologyweather.utils.WeatherDataFormatUtils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DayForecast {
    private String weekName, dateName, highLowTemp, condition, sunrise, sunset, moonrise, moonset, humidity, windDirection, windSpeed, realFeel;
    private int iconResId, topColor, bottomColor;
    private int highTemp, lowTemp;
    private int highY, lowY;
    private Bitmap iconBitmap;
    private String windSpeedUnit;
    private double rainAmount;//mm
    private String rainProb;//%
    private String formattedDayTime;//日照
    private String uvMax;
    private boolean isMoonRiseExist;
    private boolean isMoonSetExist;
    private boolean isToday;
    private boolean isYesterday;

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    public boolean isYesterday() {
        return isYesterday;
    }

    public void setYesterday(boolean yesterday) {
        isYesterday = yesterday;
    }

    public boolean isMoonRiseExist() {
        return isMoonRiseExist;
    }

    public void setMoonRiseExist(boolean moonRiseExist) {
        isMoonRiseExist = moonRiseExist;
    }

    public boolean isMoonSetExist() {
        return isMoonSetExist;
    }

    public void setMoonSetExist(boolean moonSetExist) {
        isMoonSetExist = moonSetExist;
    }

    public String getUvMax() {
        return uvMax;
    }

    public void setUvMax(String uvMax) {
        this.uvMax = uvMax;
    }

    public String getFormattedDayTime() {
        return formattedDayTime;
    }

    public void setFormattedDayTime(String formattedDayTime) {
        this.formattedDayTime = formattedDayTime;
    }

    public String getMoonrise() {
        return moonrise;
    }

    public void setMoonrise(String moonrise) {
        this.moonrise = moonrise;
    }

    public String getMoonset() {
        return moonset;
    }

    public void setMoonset(String moonset) {
        this.moonset = moonset;
    }

    public double getRainAmount() {
        return rainAmount;
    }

    public void setRainAmount(double rainAmount) {
        this.rainAmount = rainAmount;
    }

    public String getRainProb() {
        return rainProb;
    }

    public void setRainProb(String rainProb) {
        this.rainProb = rainProb;
    }

    public String getRealFeelHigh() {
        return realFeelHigh;
    }

    public void setRealFeelHigh(String realFeelHigh) {
        this.realFeelHigh = realFeelHigh;
    }

    private String realFeelHigh;

    public String getRealFeelLow() {
        return realFeelLow;
    }

    public void setRealFeelLow(String realFeelLow) {
        this.realFeelLow = realFeelLow;
    }

    private String realFeelLow;

    public String getWeekName() {
        return weekName;
    }

    public void setWeekName(String weekName) {
        this.weekName = weekName;
    }

    public String getDateName() {
        return dateName;
    }

    public void setDateName(String dateName) {
        this.dateName = dateName;
    }

    public String getHighLowTemp() {
        return highLowTemp;
    }

    public void setHighLowTemp(String highLowTemp) {
        this.highLowTemp = highLowTemp;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getRealFeel() {
        return realFeel;
    }

    public void setRealFeel(String realFeel) {
        this.realFeel = realFeel;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public int getTopColor() {
        return topColor;
    }

    public void setTopColor(int topColor) {
        this.topColor = topColor;
    }

    public int getBottomColor() {
        return bottomColor;
    }

    public void setBottomColor(int bottomColor) {
        this.bottomColor = bottomColor;
    }

    public int getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(int highTemp) {
        this.highTemp = highTemp;
    }

    public int getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(int lowTemp) {
        this.lowTemp = lowTemp;
    }

    public String getHighTempStr() {
        return this.highTemp + AmberSdkConstants.TEMP_UNIT;
    }

    public String getLowTempStr() {
        return this.lowTemp + AmberSdkConstants.TEMP_UNIT;
    }

    public int getHighY() {
        return highY;
    }

    public void setHighY(int highY) {
        this.highY = highY;
    }

    public int getLowY() {
        return lowY;
    }

    public void setLowY(int lowY) {
        this.lowY = lowY;
    }

    public Bitmap getIconBitmap() {
        return iconBitmap;
    }

    public void setIconBitmap(Bitmap iconBitmap) {
        this.iconBitmap = iconBitmap;
    }

    public String getWindSpeedUnit() {
        return windSpeedUnit;
    }

    public void setWindSpeedUnit(String windSpeedUnit) {
        this.windSpeedUnit = windSpeedUnit;
    }

    public static int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static void fillData(Context context, WeatherInfoLoader weatherInfoLoader, int weatherDataId, List<DayForecast> list) {
        //yesterday
        list.clear();
        boolean isYesterdayHasData;
        String yesterdayWeatherJson = null;
        DayForecast yesterday = null;
        long yesterdayMills = 0;
        if (!TextUtils.isEmpty(yesterdayWeatherJson)) {
            yesterday = new DayForecast();
            try {
                JSONObject jsonObject = new JSONObject(yesterdayWeatherJson);
                yesterdayMills = jsonObject.getLong("day_name_millis");
                long todayLocaleStartMills = DCTUtils.getLocaleTodayZeroTime(weatherInfoLoader.getmLocation().getGmtOffset());
                double interval = (todayLocaleStartMills - yesterdayMills) - Constants.ONE_DAY;
                isYesterdayHasData = todayLocaleStartMills > yesterdayMills && interval <= 0;

                if (weatherDataId == Constants.AUTO_ID) {
                    if (isYesterdayHasData) {
                        double yesterdayLat = jsonObject.getDouble("lat");
                        double yesterdayLon = jsonObject.getDouble("lon");
//                        isYesterdayHasData = LocationUtils.isInSameCity(context, yesterdayLat, yesterdayLon);
                    }
                }

                int tempType = weatherInfoLoader.getTempUnit();
                int speedType = weatherInfoLoader.getSpeedUnit();
                boolean isClock24 = weatherInfoLoader.isClock24Formate();
                int dateFormat = weatherInfoLoader.getDateFormate();
                String format = dateFormat == 1 ? "MM/dd" : "dd/MM";
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());

                yesterday.setYesterday(true);
                yesterday.setToday(false);
                yesterday.setWeekName(jsonObject.getString("week_name"));
                yesterday.setDateName(sdf.format(new Date(yesterdayMills)));
                yesterday.setHighTemp((int) WeatherDataFormatUtils.getRawTemp(jsonObject.getInt("high_temp"), tempType));
                yesterday.setLowTemp((int) WeatherDataFormatUtils.getRawTemp(jsonObject.getInt("low_temp"), tempType));
                yesterday.setHighLowTemp(yesterday.getHighTempStr() + " / " + yesterday.getLowTempStr());
                yesterday.setCondition(jsonObject.getString("day_condition"));
                yesterday.setIconResId(IconLoader.getWeatherImageId(jsonObject.getString("day_icon_num"), true));
                yesterday.setRainAmount(jsonObject.getDouble("day_rain_amount"));
                String windSpeed = jsonObject.getString("day_wind_speed");
                if (windSpeed.equals(AmberSdkConstants.DEFAULT_EMPTY_VALUE_INT + "") || windSpeed.equals("-999.0")) {
                    yesterday.setWindSpeed(AmberSdkConstants.DEFAULT_SHOW_STRING);
                    yesterday.setWindSpeedUnit(AmberSdkConstants.DEFAULT_SHOW_STRING);
                } else {
                    yesterday.setWindSpeed(String.valueOf(WeatherDataFormatUtils.getRawWindSpeed(Double.parseDouble(jsonObject.getString("day_wind_speed")), speedType)));
                    yesterday.setWindSpeedUnit(weatherInfoLoader.getCurrentWindSpeedUnit());
                }
                yesterday.setWindDirection(jsonObject.getString("day_wind_direction"));
                yesterday.setRainProb(jsonObject.getString("day_rain_prob"));
                yesterday.setHumidity(jsonObject.getString("day_humididty"));
                yesterday.setRealFeelHigh(jsonObject.getString("day_realfeel_high"));
                yesterday.setRealFeelLow(jsonObject.getString("day_realfeel_low"));
                yesterday.setSunrise(WeatherDataFormatUtils.getFormatSunTime(jsonObject.getString("day_sunrise"), isClock24));
                yesterday.setSunset(WeatherDataFormatUtils.getFormatSunTime(jsonObject.getString("day_sunset"), isClock24));
                yesterday.setMoonrise(WeatherDataFormatUtils.getFormatSunTime(jsonObject.getString("day_moonrise"), isClock24));
                yesterday.setMoonset(WeatherDataFormatUtils.getFormatSunTime(jsonObject.getString("day_moonset"), isClock24));
                yesterday.setMoonRiseExist(jsonObject.getBoolean("is_daymoonrise_exist"));
                yesterday.setMoonSetExist(jsonObject.getBoolean("is_daymoonset_exist"));
                yesterday.setUvMax(jsonObject.getString("day_uvmax"));
                yesterday.setFormattedDayTime(jsonObject.getString("day_day_time"));

            } catch (Exception e) {
                isYesterdayHasData = false;
                e.printStackTrace();
            }
        } else {
            isYesterdayHasData = false;
        }

        if (isYesterdayHasData) {
            list.add(yesterday);
        }

        for (int i = 0; i < weatherInfoLoader.getDayItems(); i++) {
            DayForecast f = new DayForecast();
            long nameMills = weatherInfoLoader.getDayNameMills(i);
            if (isYesterdayHasData && nameMills == yesterdayMills) {
                list.remove(yesterday);//avoid repeat yesterday
            }
            long localeTodayStartMills = DCTUtils.getLocaleTodayZeroTime(weatherInfoLoader.getmLocation().getGmtOffset());
            if (nameMills < localeTodayStartMills && nameMills >= localeTodayStartMills - Constants.ONE_DAY) {
                f.setYesterday(true);
                f.setToday(false);
            } else if (nameMills >= localeTodayStartMills && nameMills < localeTodayStartMills + Constants.ONE_DAY) {
                f.setYesterday(false);
                f.setToday(true);
            }

            f.setWeekName(weatherInfoLoader.getDayNameByFormate(i, "E"));
            String name = weatherInfoLoader.getDayNameByFormate(i,
                    WeatherInfoLoader.getInstance(context).getConfigData().getDateFormate() == 1 ?
                            "MM/dd" : "dd/MM");
            f.setDateName(name);
            if (weatherInfoLoader.getDayIntHighTemp(i).equals(AmberSdkConstants.DEFAULT_EMPTY_VALUE_INT + "") || weatherInfoLoader.getDayIntLowTemp(i).equals(AmberSdkConstants.DEFAULT_EMPTY_VALUE_INT + "")) {
                f.setHighLowTemp(AmberSdkConstants.DEFAULT_SHOW_STRING);
            } else {
                f.setHighLowTemp(weatherInfoLoader.getDayIntHighTemp(i) + " / " + weatherInfoLoader.getDayIntLowTemp(i));
            }
            f.setHighTemp(parseInt(weatherInfoLoader.getDayIntHighTemp(i).replace("°", ""), AmberSdkConstants.DEFAULT_EMPTY_VALUE_INT));
            f.setLowTemp(parseInt(weatherInfoLoader.getDayIntLowTemp(i).replace("°", ""), AmberSdkConstants.DEFAULT_EMPTY_VALUE_INT));
            f.setCondition(weatherInfoLoader.getDayCondition(i));
            f.setIconResId(IconLoader.getWeatherImageId(weatherInfoLoader.getDayIcon(i), true));
            f.setSunrise(weatherInfoLoader.getDayFormattedSunRiseTime(i));
            f.setSunset(weatherInfoLoader.getDayFormattedSunSetTime(i));
            f.setMoonrise(weatherInfoLoader.getDayFormattedMoonRiseTime(i));
            f.setMoonset(weatherInfoLoader.getDayFormattedMoonSetTime(i));
            if (weatherInfoLoader.getDayHumidity(i).equals(AmberSdkConstants.DEFAULT_SHOW_STRING)) {
                f.setHumidity(AmberSdkConstants.DEFAULT_SHOW_STRING);
            } else {
                f.setHumidity(weatherInfoLoader.getDayHumidity(i) + "%");
            }

            if (WeatherDataFormatUtils.getWindDirectionFromDegree(context, weatherInfoLoader.getDayWindDirection(i) + "").equals("-999.0")) {
                f.setWindDirection(AmberSdkConstants.DEFAULT_SHOW_STRING);
            } else {
                f.setWindDirection(WeatherDataFormatUtils.getWindDirectionFromDegree(context, weatherInfoLoader.getDayWindDirection(i) + ""));
            }

            if (weatherInfoLoader.getDayWindSpeed(i).equals(AmberSdkConstants.DEFAULT_EMPTY_VALUE_INT + "") || weatherInfoLoader.getDayWindSpeed(i).equals("-999.0")) {
                f.setWindSpeed(AmberSdkConstants.DEFAULT_SHOW_STRING);
                f.setWindSpeedUnit(AmberSdkConstants.DEFAULT_SHOW_STRING);
            } else {
                f.setWindSpeed(weatherInfoLoader.getDayWindSpeed(i));
                f.setWindSpeedUnit(weatherInfoLoader.getCurrentWindSpeedUnit());
            }
            f.setRealFeelHigh((int) (weatherInfoLoader.getDayRealFeelHigh(i)) + "");
            f.setRealFeelLow((int) (weatherInfoLoader.getDayRealFeelLow(i)) + "");

            f.setRainAmount(weatherInfoLoader.getDayRainAmount(i));
            f.setRainProb(weatherInfoLoader.getDayRainProb(i));
            f.setFormattedDayTime(weatherInfoLoader.getDayFormattedDayTime(i));
            f.setUvMax(weatherInfoLoader.getDayUvMax(i));
            f.setMoonRiseExist(weatherInfoLoader.isDayMoonRiseExist(i));
            f.setMoonSetExist(weatherInfoLoader.isDayMoonSetExist(i));

            list.add(f);
        }
    }
}
