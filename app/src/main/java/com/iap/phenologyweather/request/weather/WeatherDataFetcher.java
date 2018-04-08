package com.iap.phenologyweather.request.weather;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import com.iap.phenologyweather.R;
import com.iap.phenologyweather.data.loader.ConfigDataLoader;
import com.iap.phenologyweather.data.model.ConfigData;
import com.iap.phenologyweather.data.model.Location;
import com.iap.phenologyweather.data.model.WeatherRawInfo;
import com.iap.phenologyweather.provider.Item;
import com.iap.phenologyweather.provider.WeatherDatabaseManager;
import com.iap.phenologyweather.utils.AmberSdkConstants;
import com.iap.phenologyweather.utils.CommonUtils;
import com.iap.phenologyweather.utils.Constants;
import com.iap.phenologyweather.utils.DCTUtils;
import com.iap.phenologyweather.utils.Preferences;
import com.iap.phenologyweather.utils.UnitNameUtils;
import com.iap.phenologyweather.utils.WeatherConditionID;
import com.iap.phenologyweather.utils.WeatherDataFormatUtils;
import com.iap.phenologyweather.utils.WeatherUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class WeatherDataFetcher implements WeatherDataFetcherInterface {

    public static final String TAG = WeatherDataFetcher.class.getName();
    private Context mContext;
    private final static String DATA_SOURCE_URL_HEAD = "http://pw.ws.amberweather.com/api/v1/weather?";
    private final static int HOUR_NAME_MILLIS = 0;  //小时时间戳的标志位
    private final static int DAY_NAME_MILLIS = 1;   //天时间戳标志位
    private final static int FORECA = 1;
    private final static int ACU = 2;
    private final static int BACKUP = 3;
    private CityParams mCityParams;

    public void fetchAndParseWeatherData(Context context, CityParams cityParams, OnFetchResultEventListener onFetchResultEventListener) {
        mContext = context;
        mCityParams = cityParams;
        if (0.0 == cityParams.getLatitude() && 0.0 == cityParams.getLongitude()) {
            return;
        }
        if (180.0f < Math.abs(cityParams.getLatitude()) && 90.0f < Math.abs(cityParams.getLongitude())) {
            return;
        }
        TempWeatherInfo weatherInfo = null;
        String url;
        String data;
        url = getWeatherDataUrl(cityParams, FORECA, true);
        Log.d(TAG, "-----url---1-- " + url);
        data = requestWeatherData(mContext, url);
        boolean isDataUsable;
        if (data == null || noWeatherData(data)) {
            url = getWeatherDataUrl(cityParams, FORECA, false);
            Log.d(TAG, "------url--2--- " + url);
            data = requestWeatherData(mContext, url);
        }
        if (data == null || !isDataSourceUsable(data)) {
            isDataUsable = false;
        } else {
            weatherInfo = parseForecaWeatherInfoFromJSON(data);
            isDataUsable = null != weatherInfo;
        }


        if (!isDataUsable) {
            onFetchResultEventListener.onFailed();
            return;
        }

        String secondData = formatWeatherInfoIntoJsonText(weatherInfo);
        if (secondData == null) {
            onFetchResultEventListener.onFailed();
            return;
        }

        writeWeatherRawInfoIntoSQL(context, cityParams.getWeatherDataID(), secondData);
        onFetchResultEventListener.onSucceed();


    }


    private void writeWeatherRawInfoIntoSQL(Context context, int weatherDataId, String jsonString) {

        WeatherDatabaseManager dManager = WeatherDatabaseManager.getInstance(context);
        ConfigData configData = ConfigDataLoader.getInstance(context, true);
        if (configData == null) {
            configData = new ConfigData(1, true, 0,
                    0, UnitNameUtils.getTempUnitName(context, 0),
                    0, UnitNameUtils.getDistanceUnitName(context, 0),
                    0, UnitNameUtils.getSpeedUnitName(context, 0),
                    0, UnitNameUtils.getPressureUnitName(context, 0),
                    false, true);
            try {
                dManager.insertConfigData(configData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        WeatherRawInfo currentWeatherRawInfo = new WeatherRawInfo();
        currentWeatherRawInfo.setInfoType(AmberSdkConstants.InfoType.CURRENT);

        ArrayList<WeatherRawInfo> dayRawInfoList = new ArrayList<>();
        ArrayList<WeatherRawInfo> hourRawInfoList = new ArrayList<>();

        int tempType = configData.getTempUnit();
        int speedType = configData.getSpeedUnit();
        int pressureType = configData.getPressureUnit();
        int distanceType = configData.getDistanceUnit();
        boolean isLocalTime = true;
        long gmtOffset = 0;
        long dayLightOffset = 0;

        if (TextUtils.isEmpty(jsonString)) {
            return;
        }

        JSONObject currentlyWeatherInfoObject;
        JSONArray dailyWeatherInfoArray;
        JSONArray hourlyWeatherInfoArray;
        try {
            JSONObject weatherDetailObject = new JSONObject(jsonString);
            currentlyWeatherInfoObject = weatherDetailObject.getJSONObject("current_conditions");
            if (currentlyWeatherInfoObject.has("temperature") && !currentlyWeatherInfoObject.isNull("temperature")) {
                double currentTemp = currentlyWeatherInfoObject.optDouble("temperature", AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE);
                currentWeatherRawInfo.setTemp(WeatherDataFormatUtils.getRawTemp(currentTemp, tempType));
            }

            if (currentlyWeatherInfoObject.has("humidity") && !currentlyWeatherInfoObject.isNull("humidity")) {
                currentWeatherRawInfo.setHumidity(currentlyWeatherInfoObject.optString("humidity", AmberSdkConstants.DEFAULT_SHOW_STRING));
            }
            if (currentlyWeatherInfoObject.has("wind_speed") && !currentlyWeatherInfoObject.isNull("wind_speed")) {
                double windSpeed = currentlyWeatherInfoObject.optDouble("wind_speed", AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE);
                currentWeatherRawInfo.setWindSpeed(WeatherDataFormatUtils.getRawWindSpeed(windSpeed, speedType));
            }
            if (currentlyWeatherInfoObject.has("wind_direction") && !currentlyWeatherInfoObject.isNull("wind_direction")) {

                String windDrection = currentlyWeatherInfoObject.optString("wind_direction", AmberSdkConstants.DEFAULT_SHOW_STRING);
                currentWeatherRawInfo.setWindDirection(WeatherDataFormatUtils.getRawWindDirection(windDrection));
                String formattedWindDir = WeatherDataFormatUtils.getWindDirectionFromDegree(context,
                        currentWeatherRawInfo.getWindDirection() + "");
                currentWeatherRawInfo.setFormattedWindDirection(formattedWindDir);
            }
            if (currentlyWeatherInfoObject.has("pressure") && !currentlyWeatherInfoObject.isNull("pressure")) {
                double pressure = currentlyWeatherInfoObject.optDouble("pressure", AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE);
                currentWeatherRawInfo.setPressure(WeatherDataFormatUtils.getRawPressure(pressure, pressureType));

            }
            if (currentlyWeatherInfoObject.has("dewpoint") && !currentlyWeatherInfoObject.isNull("dewpoint")) {
                double dewpoint = currentlyWeatherInfoObject.optDouble("dewpoint", AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE);
                currentWeatherRawInfo.setDewPoint(WeatherDataFormatUtils.getRawDewPoint(dewpoint, tempType));

            }
            if (currentlyWeatherInfoObject.has("realfeel") && !currentlyWeatherInfoObject.isNull("realfeel")) {
                double realfeel = currentlyWeatherInfoObject.optDouble("realfeel", AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE);
                currentWeatherRawInfo.setRealFeel(WeatherDataFormatUtils.getRawRealFeel(realfeel, tempType));
            }
            if (currentlyWeatherInfoObject.has("weather_summary") && !currentlyWeatherInfoObject.isNull("weather_summary"))
                currentWeatherRawInfo.setCondition(currentlyWeatherInfoObject.optString("weather_summary", AmberSdkConstants.DEFAULT_SHOW_STRING));
            if (currentlyWeatherInfoObject.has("distance") && !currentlyWeatherInfoObject.isNull("distance")) {
                double distance = currentlyWeatherInfoObject.optDouble("distance", AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE);
                currentWeatherRawInfo.setVisibility(WeatherDataFormatUtils.getRawVisibility(distance, distanceType));
            }

            if (currentlyWeatherInfoObject.has("high_temp") && !currentlyWeatherInfoObject.isNull("high_temp")) {
                double highTemp = currentlyWeatherInfoObject.optDouble("high_temp", AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE);
                currentWeatherRawInfo.setHighTemp(WeatherDataFormatUtils.getRawTemp(highTemp, tempType));
            }
            if (currentlyWeatherInfoObject.has("low_temp") && !currentlyWeatherInfoObject.isNull("low_temp")) {
                double lowTemp = currentlyWeatherInfoObject.optDouble("low_temp", AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE);
                currentWeatherRawInfo.setLowTemp(WeatherDataFormatUtils.getRawTemp(lowTemp, tempType));
            }
            if (currentlyWeatherInfoObject.has("is_suntime_localetime") && !currentlyWeatherInfoObject.isNull("is_suntime_localetime")) {
                isLocalTime = currentlyWeatherInfoObject.getBoolean("is_suntime_localetime");
            }

            if (currentlyWeatherInfoObject.has("gmt_offset") && !currentlyWeatherInfoObject.isNull("gmt_offset")) {
                double gmtOffsetDouble = currentlyWeatherInfoObject.optDouble("gmt_offset", AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE);
                Preferences.setGMTOffset(context, currentlyWeatherInfoObject.optString("gmt_offset"), weatherDataId);
                gmtOffset = WeatherDataFormatUtils.getMillisecondsForHours(gmtOffsetDouble);
            }

            if (currentlyWeatherInfoObject.has(Item.Current.IS_DEWPOINT_EXIST) && !currentlyWeatherInfoObject.isNull(Item.Current.IS_DEWPOINT_EXIST)) {
                currentWeatherRawInfo.setDewpointExist(currentlyWeatherInfoObject.getBoolean(Item.Current.IS_DEWPOINT_EXIST));
            }

            if (currentlyWeatherInfoObject.has("is_press_exist") && !currentlyWeatherInfoObject.isNull("is_press_exist")) {
                currentWeatherRawInfo.setPressureExist(currentlyWeatherInfoObject.getBoolean("is_press_exist"));
            }

            if (currentlyWeatherInfoObject.has(Item.Current.IS_UVINDEX_EXIST) && !currentlyWeatherInfoObject.isNull(Item.Current.IS_UVINDEX_EXIST)) {
                currentWeatherRawInfo.setUvindexExist(currentlyWeatherInfoObject.getBoolean(Item.Current.IS_UVINDEX_EXIST));
            }

            if (currentlyWeatherInfoObject.has(Item.Current.IS_VISIBILITY_EXIST) && !currentlyWeatherInfoObject.isNull(Item.Current.IS_VISIBILITY_EXIST)) {
                currentWeatherRawInfo.setVisibilityExist(currentlyWeatherInfoObject.getBoolean(Item.Current.IS_VISIBILITY_EXIST));
            }

            String formattedSunTime;
            String rawSunTime;
            long lSunTime;
            // get formatted sunRise time
            if (currentlyWeatherInfoObject.has("is_sunrise_exist") && currentlyWeatherInfoObject.getBoolean("is_sunrise_exist")
                    && currentlyWeatherInfoObject.has("sunrise") && !currentlyWeatherInfoObject.isNull("sunrise")) {
                rawSunTime = currentlyWeatherInfoObject.optString("sunrise", AmberSdkConstants.DEFAULT_SHOW_STRING);
                lSunTime = WeatherDataFormatUtils.getRawSunTimeMillisSeconds(rawSunTime, isLocalTime, gmtOffset);
                if (isLocalTime) {
                    formattedSunTime = WeatherDataFormatUtils.getFormattedSunTime(lSunTime);
                } else {
                    formattedSunTime = WeatherUtils.LocaleSunTimeFromUTC(mContext, rawSunTime, weatherDataId);
                }
                if (configData.isClock24Formate()) {
                    formattedSunTime = DCTUtils.formatSunTimeInto24Date(formattedSunTime);
                }
                if (null != formattedSunTime) {
                    Log.d("WeatherDataFileParser", "-----lSunTime---- " + lSunTime);
                    currentWeatherRawInfo.setFormattedSunRise(formattedSunTime);
                    currentWeatherRawInfo.setSunriseMillis(lSunTime);
                    currentWeatherRawInfo.setSunRiseExist(true);
                } else {
                    Log.d("WeatherDataFileParser", "------formattedSunTime  null----- ");
                    currentWeatherRawInfo.setSunRiseExist(false);
                }
            }

            // get formatted sunSet time
            if (currentlyWeatherInfoObject.has("is_sunset_exist") && currentlyWeatherInfoObject.getBoolean("is_sunset_exist")
                    && currentlyWeatherInfoObject.has("sunset") && !currentlyWeatherInfoObject.isNull("sunset")) {

                rawSunTime = currentlyWeatherInfoObject.optString("sunset", AmberSdkConstants.DEFAULT_SHOW_STRING);
                lSunTime = WeatherDataFormatUtils.getRawSunTimeMillisSeconds(rawSunTime, isLocalTime, gmtOffset);
                if (isLocalTime) {
                    formattedSunTime = WeatherDataFormatUtils.getFormattedSunTime(lSunTime);
                } else {
                    formattedSunTime = WeatherUtils.LocaleSunTimeFromUTC(mContext, rawSunTime, weatherDataId);
                }
                if (configData.isClock24Formate()) {
                    formattedSunTime = DCTUtils.formatSunTimeInto24Date(formattedSunTime);
                }
                if (null != formattedSunTime) {
                    currentWeatherRawInfo.setFormattedSunSet(formattedSunTime);
                    currentWeatherRawInfo.setSunsetMillis(lSunTime);
                    currentWeatherRawInfo.setSunSetExist(true);
                } else {
                    currentWeatherRawInfo.setSunSetExist(false);
                }
            }

            if (currentWeatherRawInfo.isSunRiseExist() && currentWeatherRawInfo.isSunRiseExist()) {
                currentWeatherRawInfo.setDayTimeMillis(currentWeatherRawInfo.getSunsetMillis() - currentWeatherRawInfo.getSunriseMillis());
            }

            if (currentlyWeatherInfoObject.has("daylight_offset") && !currentlyWeatherInfoObject.isNull("daylight_offset")) {
                double daylightOffset = currentlyWeatherInfoObject.optDouble("daylight_offset", AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE);
                dayLightOffset = WeatherDataFormatUtils.getMillisecondsForHours(daylightOffset);
            }

            if (currentlyWeatherInfoObject.has("uvindex") && !currentlyWeatherInfoObject.isNull("uvindex")) {
                String formattedUv = WeatherUtils.getTranslatedUV(mContext,
                        currentlyWeatherInfoObject.optString("uvindex", AmberSdkConstants.DEFAULT_SHOW_STRING));
                currentWeatherRawInfo.setUv(formattedUv);
            }

            if (currentlyWeatherInfoObject.has("weather_icon") && !currentlyWeatherInfoObject.isNull("weather_icon")) {
                currentWeatherRawInfo.setIcon(currentlyWeatherInfoObject.optString("weather_icon", AmberSdkConstants.DEFAULT_SHOW_STRING));
            }
            currentWeatherRawInfo.setWindSpeedUnit(UnitNameUtils.getSpeedUnitName(context, speedType));
            currentWeatherRawInfo.setId(weatherDataId);
//==========================================================================================day
            dailyWeatherInfoArray = weatherDetailObject.getJSONArray("daily_conditions");
            WeatherRawInfo dayWeatherRawInfo = null;
            for (int i = 0; i < dailyWeatherInfoArray.length(); i++) {
                dayWeatherRawInfo = new WeatherRawInfo();
                JSONObject everyDayDetail = dailyWeatherInfoArray.getJSONObject(i);
                if (everyDayDetail.has("day_name_millis") && !everyDayDetail.isNull("day_name_millis")) {
                    long dayTimeMills = everyDayDetail.optLong("day_name_millis", AmberSdkConstants.DEFAULT_EMPTY_VALUE_LONG);
                    dayWeatherRawInfo.setNameMillis(dayTimeMills);
                    dayWeatherRawInfo.setFormattedTimeName(DCTUtils.getShortDayNameFromMillseconds(dayTimeMills + ""));
                }
                if (everyDayDetail.has("high_temp") && !everyDayDetail.isNull("high_temp")) {
                    double highTemp = everyDayDetail.optDouble("high_temp", AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE);
                    dayWeatherRawInfo.setHighTemp(WeatherDataFormatUtils.getRawTemp(highTemp, tempType));

                }
                if (everyDayDetail.has("low_temp") && !everyDayDetail.isNull("low_temp")) {
                    double lowTemp = everyDayDetail.optDouble("low_temp", AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE);
                    dayWeatherRawInfo.setLowTemp(WeatherDataFormatUtils.getRawTemp(lowTemp, tempType));
                }
                if (everyDayDetail.has("day_weather_summary") && !everyDayDetail.isNull("day_weather_summary")) {
                    dayWeatherRawInfo.setCondition(everyDayDetail.optString("day_weather_summary", AmberSdkConstants.DEFAULT_SHOW_STRING));
                }
                if (everyDayDetail.has("day_icon") && !everyDayDetail.isNull("day_icon")) {
                    dayWeatherRawInfo.setIcon(everyDayDetail.optString("day_icon", AmberSdkConstants.DEFAULT_SHOW_STRING));
                }
//=========start
                if (everyDayDetail.has("day_humididty") && !everyDayDetail.isNull("day_humididty")) {
                    dayWeatherRawInfo.setHumidity(everyDayDetail.optString("day_humididty", AmberSdkConstants.DEFAULT_SHOW_STRING));
                }

                if (everyDayDetail.has("day_wind_speed") && !everyDayDetail.isNull("day_wind_speed")) {
                    double windSpeed = everyDayDetail.optDouble("day_wind_speed", AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE);
                    dayWeatherRawInfo.setWindSpeed(WeatherDataFormatUtils.getRawWindSpeed(windSpeed, speedType));
                }
                if (everyDayDetail.has("day_wind_direction") && !everyDayDetail.isNull("day_wind_direction")) {
                    String windDrection = everyDayDetail.optString("day_wind_direction", AmberSdkConstants.DEFAULT_SHOW_STRING);
                    dayWeatherRawInfo.setWindDirection(WeatherDataFormatUtils.getRawWindDirection(windDrection));
                    String formattedWindDir = WeatherDataFormatUtils.getWindDirectionFromDegree(context,
                            dayWeatherRawInfo.getWindDirection() + "");
                    dayWeatherRawInfo.setFormattedWindDirection(formattedWindDir);
                }

                if (everyDayDetail.has("day_rain_amount") && !everyDayDetail.isNull("day_rain_amount")) {
                    double rainAmount = everyDayDetail.optDouble("day_rain_amount", AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE);
                    dayWeatherRawInfo.setRainAmount(Double.isNaN(rainAmount) ? AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE : rainAmount);
                }

                if (everyDayDetail.has("is_dayrainpro_exist") && everyDayDetail.optBoolean("is_dayrainpro_exist")
                        && everyDayDetail.has("day_rain_prob") && !everyDayDetail.isNull("day_rain_prob")) {
                    dayWeatherRawInfo.setRainProbility(everyDayDetail.optString("day_rain_prob"));
                }

                if (everyDayDetail.has("day_uvmax") && !everyDayDetail.isNull("day_uvmax")) {
                    dayWeatherRawInfo.setUv(everyDayDetail.optString("day_uvmax", AmberSdkConstants.DEFAULT_SHOW_STRING));
                }

                if (everyDayDetail.has("day_realfeel_high") && !everyDayDetail.isNull("day_realfeel_high")) {
                    double realFeel = everyDayDetail.optDouble("day_realfeel_high", AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE);
                    dayWeatherRawInfo.setRealFeelHigh(realFeel == -999 ?
                            -999 : WeatherDataFormatUtils.getTempTypeByState(tempType, realFeel));
                }
                if (everyDayDetail.has("day_realfeel_low") && !everyDayDetail.isNull("day_realfeel_low")) {
                    double realFeel = everyDayDetail.optDouble("day_realfeel_low", AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE);
                    dayWeatherRawInfo.setRealFeelLow(realFeel == -999 ?
                            -999 : WeatherDataFormatUtils.getTempTypeByState(tempType, realFeel));
                }

                if (everyDayDetail.has("is_daysunrise_exist") && everyDayDetail.getBoolean("is_daysunrise_exist")
                        && everyDayDetail.has("day_sunrise") && !everyDayDetail.isNull("day_sunrise")) {
                    rawSunTime = everyDayDetail.optString("day_sunrise", AmberSdkConstants.DEFAULT_SHOW_STRING);
                    lSunTime = WeatherDataFormatUtils.getRawSunTimeMillisSeconds(rawSunTime, isLocalTime, gmtOffset);
                    Log.d("WeatherDataFileParser", "--------gmtOffset----- " + gmtOffset);
                    Log.d("WeatherDataFileParser", "------isLocalTime------ " + isLocalTime);
                    if (isLocalTime) {
                        formattedSunTime = WeatherDataFormatUtils.getFormattedSunTime(lSunTime);
                    } else {
                        formattedSunTime = WeatherUtils.LocaleSunTimeFromUTC(mContext, rawSunTime, weatherDataId);
                    }
                    if (configData.isClock24Formate()) {
                        formattedSunTime = DCTUtils.formatSunTimeInto24Date(formattedSunTime);
                    }
                    if (null != formattedSunTime) {
                        dayWeatherRawInfo.setFormattedSunRise(formattedSunTime);
                        dayWeatherRawInfo.setSunriseMillis(lSunTime);
                        dayWeatherRawInfo.setSunRiseExist(true);
                    } else {
                        dayWeatherRawInfo.setSunRiseExist(false);
                    }
                }

                // get formatted sunSet time
                if (everyDayDetail.has("is_daysunset_exist") && everyDayDetail.getBoolean("is_daysunset_exist")
                        && everyDayDetail.has("day_sunset") && !everyDayDetail.isNull("day_sunset")) {

                    rawSunTime = everyDayDetail.optString("day_sunset", AmberSdkConstants.DEFAULT_SHOW_STRING);
                    lSunTime = WeatherDataFormatUtils.getRawSunTimeMillisSeconds(rawSunTime, isLocalTime, gmtOffset);
                    if (isLocalTime) {
                        formattedSunTime = WeatherDataFormatUtils.getFormattedSunTime(lSunTime);
                    } else {
                        formattedSunTime = WeatherUtils.LocaleSunTimeFromUTC(mContext, rawSunTime, weatherDataId);
                    }
                    if (configData.isClock24Formate()) {
                        formattedSunTime = DCTUtils.formatSunTimeInto24Date(formattedSunTime);
                    }
                    if (null != formattedSunTime) {
                        dayWeatherRawInfo.setFormattedSunSet(formattedSunTime);
                        dayWeatherRawInfo.setSunsetMillis(lSunTime);
                        dayWeatherRawInfo.setSunSetExist(true);
                    } else {
                        dayWeatherRawInfo.setSunSetExist(false);
                    }

                }

                if (dayWeatherRawInfo.isSunRiseExist() && dayWeatherRawInfo.isSunSetExist()) {
                    dayWeatherRawInfo.setDayTimeMillis(dayWeatherRawInfo.getSunsetMillis() - dayWeatherRawInfo.getSunriseMillis());
                }

                String formattedMoonTime;
                String rawMoonTime;
                long lMoonTime;
                if (everyDayDetail.has("is_daymoonrise_exist") && everyDayDetail.getBoolean("is_daymoonrise_exist")
                        && everyDayDetail.has("day_moonrise") && !everyDayDetail.isNull("day_moonrise")) {
                    rawMoonTime = everyDayDetail.optString("day_moonrise", AmberSdkConstants.DEFAULT_SHOW_STRING);
                    lMoonTime = WeatherDataFormatUtils.getRawSunTimeMillisSeconds(rawMoonTime, isLocalTime, gmtOffset);
                    if (isLocalTime) {
                        formattedMoonTime = WeatherDataFormatUtils.getFormattedSunTime(lMoonTime);
                    } else {
                        formattedMoonTime = WeatherUtils
                                .LocaleSunTimeFromUTC(mContext, rawMoonTime, weatherDataId);
                    }
                    if (configData.isClock24Formate()) {
                        formattedMoonTime = DCTUtils.formatSunTimeInto24Date(formattedMoonTime);
                    }
                    if (null != formattedMoonTime && lMoonTime > 0) {
                        dayWeatherRawInfo.setFormattedMoonRise(formattedMoonTime);
                        dayWeatherRawInfo.setMoonRiseMillis(lMoonTime);
                        dayWeatherRawInfo.setIsMoonRiseExist(true);
                    } else {
                        dayWeatherRawInfo.setIsMoonRiseExist(false);
                    }
                }

                // get formatted sunSet time
                if (everyDayDetail.has("is_daymoonset_exist") && everyDayDetail.getBoolean("is_daymoonset_exist")
                        && everyDayDetail.has("day_moonset") && !everyDayDetail.isNull("day_moonset")) {

                    rawMoonTime = everyDayDetail.optString("day_moonset", AmberSdkConstants.DEFAULT_SHOW_STRING);
                    lMoonTime = WeatherDataFormatUtils.getRawSunTimeMillisSeconds(rawMoonTime, isLocalTime, gmtOffset);
                    if (isLocalTime) {
                        formattedMoonTime = WeatherDataFormatUtils.getFormattedSunTime(lMoonTime);
                    } else {
                        formattedMoonTime = WeatherUtils.LocaleSunTimeFromUTC(mContext, rawMoonTime, weatherDataId);
                    }
                    if (configData.isClock24Formate()) {
                        formattedMoonTime = DCTUtils.formatSunTimeInto24Date(formattedMoonTime);
                    }
                    if (null != formattedMoonTime) {
                        dayWeatherRawInfo.setFormattedMoonSet(formattedMoonTime);
                        dayWeatherRawInfo.setMoonSetMillis(lMoonTime);
                        dayWeatherRawInfo.setIsMoonSetExist(true);
                    } else {
                        dayWeatherRawInfo.setIsMoonSetExist(false);
                    }

                }

                dayWeatherRawInfo.setInfoType(AmberSdkConstants.InfoType.DAY);
                dayWeatherRawInfo.setId(weatherDataId);
                dayRawInfoList.add(dayWeatherRawInfo);

            }
//			=================================================================================hour
            hourlyWeatherInfoArray = weatherDetailObject.getJSONArray("hourly_conditions");
            WeatherRawInfo hourWeatherRawInfo = null;
            long sunRiseMills = currentWeatherRawInfo.getSunriseMillis();
            long sunSetMills = currentWeatherRawInfo.getSunsetMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.ENGLISH);
            String sunR = sdf.format(new Date(sunRiseMills));
            String sunS = sdf.format(new Date(sunSetMills));
            for (int i = 0; i < hourlyWeatherInfoArray.length(); i++) {
                hourWeatherRawInfo = new WeatherRawInfo();
                JSONObject everyHourDetail = hourlyWeatherInfoArray.getJSONObject(i);
                long timeMills = 0L;
                if (everyHourDetail.has("hour_name_millis") && !everyHourDetail.isNull("hour_name_millis")) {
                    timeMills = everyHourDetail.optLong("hour_name_millis", AmberSdkConstants.DEFAULT_EMPTY_VALUE_LONG);
                    hourWeatherRawInfo.setNameMillis(timeMills);
                    String formattedHourTimeName = WeatherDataFormatUtils.getFormattedTimeName(timeMills);
                    if (configData.isClock24Formate()) {
                        formattedHourTimeName = DCTUtils.get24HourName(formattedHourTimeName);
                    }
                    hourWeatherRawInfo.setFormattedTimeName(formattedHourTimeName);
                    String currentT = sdf.format(new Date(timeMills));

                    hourWeatherRawInfo.setLight(DCTUtils.isLight(sunR, sunS, currentT));
                }
                if (everyHourDetail.has("hour_weather_summary") && !everyHourDetail.isNull("hour_weather_summary")) {
                    hourWeatherRawInfo.setCondition(everyHourDetail.optString("hour_weather_summary", AmberSdkConstants.DEFAULT_SHOW_STRING));
                }
                if (everyHourDetail.has("hour_icon") && !everyHourDetail.isNull("hour_icon")) {
                    hourWeatherRawInfo.setIcon(everyHourDetail.optString("hour_icon", AmberSdkConstants.DEFAULT_SHOW_STRING));
                }
                if (everyHourDetail.has("hourtemp") && !everyHourDetail.isNull("hourtemp")) {
                    double temp = everyHourDetail.optDouble("hourtemp", AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE);
                    hourWeatherRawInfo.setTemp(WeatherDataFormatUtils.getRawTemp(temp, tempType));
                }

//				======
                if (everyHourDetail.has("hour_humidity") && !everyHourDetail.isNull("hour_humidity")) {
                    hourWeatherRawInfo.setHumidity(everyHourDetail.optString("hour_humidity", AmberSdkConstants.DEFAULT_SHOW_STRING));
                }

                if (everyHourDetail.has("hour_wind_speed") && !everyHourDetail.isNull("hour_wind_speed")) {
                    double windSpeed = everyHourDetail.optDouble("hour_wind_speed", AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE);
                    hourWeatherRawInfo.setWindSpeed(WeatherDataFormatUtils.getRawWindSpeed(windSpeed, speedType));
                }
                if (everyHourDetail.has("hour_wind_direction") && !everyHourDetail.isNull("hour_wind_direction")) {
                    String windDrection = everyHourDetail.optString("hour_wind_direction", AmberSdkConstants.DEFAULT_SHOW_STRING);
                    hourWeatherRawInfo.setWindDirection(WeatherDataFormatUtils.getRawWindDirection(windDrection));
                    String formattedWindDir = WeatherDataFormatUtils.getWindDirectionFromDegree(context,
                            hourWeatherRawInfo.getWindDirection() + "");
                    hourWeatherRawInfo.setFormattedWindDirection(formattedWindDir);
                }

                if (everyHourDetail.has("hour_reel_feel") && !everyHourDetail.isNull("hour_reel_feel")) {
                    double realfeel = everyHourDetail.optDouble("hour_reel_feel", AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE);
                    hourWeatherRawInfo.setRealFeel(WeatherDataFormatUtils.getRawRealFeel(realfeel, tempType));
                }
                if (everyHourDetail.has("hour_rain_prob") && !everyHourDetail.isNull("hour_rain_prob")) {
                    String rainProb = everyHourDetail.optString("hour_rain_prob", AmberSdkConstants.DEFAULT_INTEGER_STRING);
                    hourWeatherRawInfo.setRainProbility(rainProb);
                }

                hourWeatherRawInfo.setInfoType(AmberSdkConstants.InfoType.HOUR);
                hourWeatherRawInfo.setId(weatherDataId);
                hourRawInfoList.add(hourWeatherRawInfo);
            }

            configData.setLocalTime(isLocalTime);
            dManager.updateConfigData(configData);
            Location currentLocation = dManager.queryLocationByKey(weatherDataId);
            if (currentLocation != null) {
                currentLocation.setGmtOffset(gmtOffset);
                currentLocation.setDayLightOffset(dayLightOffset);
                dManager.updateLocation(currentLocation);
            }
            List<WeatherRawInfo> allWeatherRawInfoList = new ArrayList<>();
            allWeatherRawInfoList.add(currentWeatherRawInfo);
            allWeatherRawInfoList.addAll(dayRawInfoList);
            allWeatherRawInfoList.addAll(hourRawInfoList);

            dManager.deleteWeatherRawInfoByCityId(weatherDataId);
            dManager.insertWeatherRawInfo(allWeatherRawInfoList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private boolean noWeatherData(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            return "error".equals(jsonObject.optString("status")) ||
                    "Not found!".equals(jsonObject.optString("error"));
        } catch (Exception e) {
            return true;
        }
    }

    private String requestWeatherData(Context context, String url) {
        String data;
        try {
            NetworkManager networkManager = new NetworkManager(context, url);
            data = networkManager.excute();
        } catch (Exception e) {
            e.printStackTrace();
            data = Constants.NOTSET;
        }
        return data;
    }

    private boolean isCorrectData(String data) {
        return data.contains("current_condition") || data.contains("currentconditions") || data.contains("current_observation");
    }

    private String formatWeatherInfoIntoJsonText(TempWeatherInfo weatherInfo) {
        if (weatherInfo == null)
            return null;
        JSONStringer weatherInfoJsonText = new JSONStringer();
        try {
            weatherInfoJsonText.object();
            weatherInfoJsonText.key("current_conditions");
            weatherInfoJsonText.object();
            String currentTemp = weatherInfo.getCurrentTemp();
            String highTemp = weatherInfo.getDayHighTempList(0);
            String lowTemp = weatherInfo.getDayLowTempList(0);
            try {
                if (Integer.parseInt(currentTemp) < Integer.parseInt(lowTemp)) {
                    currentTemp = lowTemp;
                } else if (Integer.parseInt(currentTemp) > Integer.parseInt(highTemp)) {
                    currentTemp = highTemp;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            weatherInfoJsonText.key("temperature").value(currentTemp);
            weatherInfoJsonText.key("humidity").value(weatherInfo.getCurrentHumidity());
            weatherInfoJsonText.key("wind_speed").value(weatherInfo.getCurrentWindSpeed());
            weatherInfoJsonText.key("wind_direction").value(weatherInfo.getCurrentWindDirection());
            if (weatherInfo.is_visibility_exist()) {
                weatherInfoJsonText.key("distance").value(weatherInfo.getCurrentVisibility());
            }
            weatherInfoJsonText.key("pressure").value(weatherInfo.getCurrentPressure());
            weatherInfoJsonText.key("dewpoint").value(weatherInfo.getCurrentDewPoint());
            weatherInfoJsonText.key("realfeel").value(weatherInfo.getCurrentRealFeel());
            weatherInfoJsonText.key("uvindex").value(weatherInfo.getCurrentUVindex());
            weatherInfoJsonText.key("sunrise").value(weatherInfo.getSunRiseList(0));
            weatherInfoJsonText.key("sunset").value(weatherInfo.getSunSetList(0));
            weatherInfoJsonText.key("high_temp").value(highTemp);
            weatherInfoJsonText.key("low_temp").value(lowTemp);
            weatherInfoJsonText.key("weather_summary").value(weatherInfo.getCurrentWeatherSummary());
            weatherInfoJsonText.key("weather_icon").value(weatherInfo.getCurrentWeatherIcon());
            weatherInfoJsonText.key("hour_offset").value("1");
            weatherInfoJsonText.key("daylight_offset").value(weatherInfo.getDaylightOffset());
            weatherInfoJsonText.key("gmt_offset").value(weatherInfo.getGmtOffset());
            weatherInfoJsonText.key("is_suntime_localetime").value(true);
            weatherInfoJsonText.key("is_visibility_exist").value(weatherInfo.is_visibility_exist());
            weatherInfoJsonText.key("is_dewpoint_exist").value(weatherInfo.is_dewpoint_exist());
            weatherInfoJsonText.key("is_uvindex_exist").value(weatherInfo.is_uvindex_exist());
            weatherInfoJsonText.key("is_press_exist").value(weatherInfo.is_press_exist());
            weatherInfoJsonText.key("is_sunrise_exist").value(weatherInfo.is_sunrise_exist());
            weatherInfoJsonText.key("is_sunset_exist").value(weatherInfo.is_sunset_exist());
            weatherInfoJsonText.endObject();
            weatherInfoJsonText.key("daily_conditions");
            weatherInfoJsonText.array();

            for (int i = 0; i < weatherInfo.getDayItemSize(); i++) {
                weatherInfoJsonText.object();
                weatherInfoJsonText.key("dayname").value(weatherInfo.getDayTimeList(i));
                weatherInfoJsonText.key("high_temp").value(weatherInfo.getDayHighTempList(i));
                weatherInfoJsonText.key("low_temp").value(weatherInfo.getDayLowTempList(i));
                weatherInfoJsonText.key("day_weather_summary").value(weatherInfo.getDaySummaryList(i));
                weatherInfoJsonText.key("day_icon").value(weatherInfo.getDayIconList(i));
                weatherInfoJsonText.key("day_name_millis").value(weatherInfo.getDayNameMillisList(i));
                weatherInfoJsonText.key("day_rain_amount").value(weatherInfo.getRainAmountList(i));
                weatherInfoJsonText.key("day_snow_amount").value(weatherInfo.getDaySnowAmountList(i));
                weatherInfoJsonText.key("day_wind_speed").value(weatherInfo.getDayWindSpeedList(i));
                weatherInfoJsonText.key("day_wind_direction").value(weatherInfo.getDayWindDirectionList(i));
                weatherInfoJsonText.key("day_thunderstrom_prob").value(weatherInfo.getDayThunderStormProbList(i));
                weatherInfoJsonText.key("day_rain_prob").value(weatherInfo.getDayRainProbList(i));
                weatherInfoJsonText.key("is_daydewpoint_exist").value(weatherInfo.is_daydewpoint_exist());
                weatherInfoJsonText.key("is_daywindspeed_exist").value(weatherInfo.is_daywindspeed_exist());
                weatherInfoJsonText.key("is_daywinddirection_exist").value(weatherInfo.is_daywinddirection_exist());
                weatherInfoJsonText.key("is_dayrainamount_exist").value(weatherInfo.is_dayrainamount_exist());
                weatherInfoJsonText.key("is_daysnowamount_exist").value(weatherInfo.is_daysnowamount_exist());
                weatherInfoJsonText.key("is_daythundestormpro_exist").value(weatherInfo.is_daythundestormpro_exist());
                weatherInfoJsonText.key("is_dayrainpro_exist").value(weatherInfo.is_dayrainpro_exist());
                weatherInfoJsonText.key("is_dayhumidity_exist").value(weatherInfo.is_dayhumidity_exist());
                weatherInfoJsonText.key("is_daypressure_exist").value(weatherInfo.is_daypressure_exist());
                weatherInfoJsonText.key("day_humididty").value(weatherInfo.getDayHumidityList(i));
                weatherInfoJsonText.key("day_realfeel_high").value(weatherInfo.getDayRealFeelHigh(i));
                weatherInfoJsonText.key("day_realfeel_low").value(weatherInfo.getDayRealFeelLow(i));
                weatherInfoJsonText.key("day_sunset").value(weatherInfo.getSunSetList(i));
                weatherInfoJsonText.key("day_sunrise").value(weatherInfo.getSunRiseList(i));
                weatherInfoJsonText.key("is_daysunrise_exist").value(weatherInfo.is_day_sunrise_exist());
                weatherInfoJsonText.key("is_daysunset_exist").value(weatherInfo.is_day_sunset_exist());
                weatherInfoJsonText.key("day_moonset").value(weatherInfo.getMoonSetList(i));
                weatherInfoJsonText.key("day_moonrise").value(weatherInfo.getMoonRiseList(i));
                weatherInfoJsonText.key("is_daymoonrise_exist").value(weatherInfo.is_day_moonrise_exist());
                weatherInfoJsonText.key("is_daymoonset_exist").value(weatherInfo.is_day_moonset_exist());
                weatherInfoJsonText.key("day_uvmax").value(weatherInfo.getDayUVIndex(i));
//                weatherInfoJsonText.key("is_dayrealfeel_exist").value(weatherInfo.getDayRealfeelList(i));
                weatherInfoJsonText.endObject();
            }
            weatherInfoJsonText.endArray();
            weatherInfoJsonText.key("hourly_conditions");
            weatherInfoJsonText.array();

            for (int i = 0; i < weatherInfo.getHourItemSize(); i++) {
                weatherInfoJsonText.object();
                weatherInfoJsonText.key("hourname").value(weatherInfo.getHourTimeList(i));
                weatherInfoJsonText.key("hourtemp").value(weatherInfo.getHourTempList(i));
                weatherInfoJsonText.key("hour_weather_summary").value(weatherInfo.getHourSummaryList(i));
                weatherInfoJsonText.key("hour_icon").value(weatherInfo.getHourIconList(i));
                weatherInfoJsonText.key("hour_name_millis").value(weatherInfo.getHourNameMillisList(i));
                weatherInfoJsonText.key("hour_wind_speed").value(weatherInfo.getHourWindSpeedList(i));
                weatherInfoJsonText.key("hour_wind_direction").value(weatherInfo.getHourWindDirectionList(i));
                weatherInfoJsonText.key("hour_reel_feel").value(weatherInfo.getHourReelFeelList(i));
                weatherInfoJsonText.key("hour_rain_prob").value(weatherInfo.getHourRainProbList(i));
                weatherInfoJsonText.key("is_hourwindspeed_exist").value(weatherInfo.is_hourdewpoint_exist());
                weatherInfoJsonText.key("is_hourwinddirection_exist").value(weatherInfo.is_hourwinddirection_exist());
                weatherInfoJsonText.key("is_hourreelfeel_exist").value(weatherInfo.is_hourreelfeel_exist());
                weatherInfoJsonText.key("is_hourhumidity_exist").value(weatherInfo.is_hourhumidity_exist());
                weatherInfoJsonText.key("is_hourpressur_exist").value(weatherInfo.is_hourpressur_exist());
                weatherInfoJsonText.key("is_hourdewpoint_exist").value(weatherInfo.is_hourdewpoint_exist());
                weatherInfoJsonText.key("is_hour_rain_prob_exist").value(weatherInfo.is_hour_rain_prob_exist());

                weatherInfoJsonText.key("hour_humidity").value(weatherInfo.getHourHumidityList(i));

                weatherInfoJsonText.endObject();
            }
            weatherInfoJsonText.endArray();
            weatherInfoJsonText.endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "-------weatherInfoJsonText------ " + weatherInfoJsonText.toString());
        return weatherInfoJsonText.toString();
    }

    /**
     * 将json字符串转换为TempWeatherInfo对象
     *
     * @return TempWeatherInfo
     */
    private TempWeatherInfo parseForecaWeatherInfoFromJSON(String forecaWeatherData) {
        TempWeatherInfo forecaWeatherInfo = new TempWeatherInfo();
        String forecaWeatherSymbol;
        try {
            JSONObject rootObject = new JSONObject(forecaWeatherData);
            JSONObject currentWeather = rootObject.getJSONObject("cc");//实时预报
            JSONArray hourWeather = rootObject.getJSONArray("fch");  //小时预报
            JSONArray dayWeather = rootObject.getJSONArray("fcd");  //7天内预报
            JSONObject weatherLocation = rootObject.getJSONObject("loc");  //地理位置

            parseCityId(rootObject.optJSONObject("loc"));
            forecaWeatherInfo.setGmtOffset(getGmtOffset(weatherLocation.optString("tz")));//时区
            forecaWeatherInfo.setCurrentDewPoint("" + currentWeather.optInt("td"));//露点
            forecaWeatherInfo.set_dewpoint_exist(true);
            forecaWeatherInfo.setCurrentPressure("" + (currentWeather.optInt("p") / 10.0f));//气压
            forecaWeatherInfo.set_press_exist(true);
            forecaWeatherInfo.setCurrentVisibility(getVisbity(currentWeather.optInt("v")));//可见度
            forecaWeatherInfo.set_visibility_exist(true);
            forecaWeatherInfo.setCurrentUVindex(CommonUtils.getUVLevelByUVIndex(mContext, currentWeather.optInt("uv")));//紫外线
            forecaWeatherInfo.set_uvindex_exist(true);
            forecaWeatherInfo.setCurrentRealFeel("" + currentWeather.optInt("tf"));//体感温度
            forecaWeatherInfo.set_rainamount_exist(true);
            forecaWeatherInfo.setCurrentHumidity(currentWeather.optString("rh") + "%");//湿度
            forecaWeatherInfo.setCurrentTemp(currentWeather.optString("t"));//气温
            forecaWeatherSymbol = currentWeather.optString("s");
            forecaWeatherInfo.setCurrentWeatherIcon(getForecaWeatherIcon(forecaWeatherSymbol));//天气icon
            forecaWeatherInfo.setCurrentWindDirection(currentWeather.optString("wn"));//风向
            forecaWeatherInfo.setCurrentWindSpeed(getWindHourSpeed(currentWeather.optDouble("ws")));//风速
            forecaWeatherInfo.set_rainamount_exist(true);//降水量
            forecaWeatherInfo.setCurrentWeatherSummary(getForecaWeatherConditionStr(forecaWeatherSymbol));//天气描述
            forecaWeatherInfo.setDaylightOffset("N/A");
            forecaWeatherInfo.set_sunrise_exist(true);
            forecaWeatherInfo.set_sunset_exist(true);
            for (int i = 0; i < hourWeather.length(); i++) {
                JSONObject object = hourWeather.getJSONObject(i);
                forecaWeatherInfo.addHourHumidityList(object.optInt("rh") + "%");
                forecaWeatherSymbol = object.optString("s");
                forecaWeatherInfo.addHourIconList(getForecaWeatherIcon(forecaWeatherSymbol));
                String date = object.optString("dt");
                forecaWeatherInfo.addHourNameDateList(date.substring(date.indexOf("T") + 1, date.length()));
                forecaWeatherInfo.addHourReelFeelList("" + object.optInt("tf"));
                forecaWeatherInfo.addDayThunderStormProbList("N/A");
                forecaWeatherInfo.addHourNameMillisList(getTimeMills(object.optString("dt"), 0));
                forecaWeatherInfo.addHourSummaryList(getForecaWeatherConditionStr(forecaWeatherSymbol));
                forecaWeatherInfo.addHourTempList("" + object.optInt("t"));
                forecaWeatherInfo.addHourTimeList(object.optString("dt"));
                forecaWeatherInfo.addHourWindDirectionList(object.optString("wn"));
                forecaWeatherInfo.addHourWindSpeedList(getWindHourSpeed(object.optDouble("ws")));
                forecaWeatherInfo.addHourUVIndex(CommonUtils.getUVLevelByUVIndex(mContext, object.optInt("uv")));
                forecaWeatherInfo.addHourRainProbList("" + object.optInt("pp"));
                forecaWeatherInfo.set_hourdewpoint_exist(true);
                forecaWeatherInfo.set_hourhumidity_exist(true);
                forecaWeatherInfo.set_hourpressur_exist(true);
                forecaWeatherInfo.set_hourreelfeel_exist(true);
                forecaWeatherInfo.set_hourwinddirection_exist(true);
                forecaWeatherInfo.set_hourwindspeed_exist(true);
                forecaWeatherInfo.set_hour_rain_prob_exist(true);
            }
            for (int i = 0; i < dayWeather.length(); i++) {
                JSONObject object = dayWeather.getJSONObject(i);
                forecaWeatherInfo.addDayHighTempList("" + object.optInt("tx"));
                forecaWeatherSymbol = object.optString("s");
                forecaWeatherInfo.addDayIconList(getForecaWeatherIcon(forecaWeatherSymbol));
                forecaWeatherInfo.addDayNameList(object.optString("dt"));
                forecaWeatherInfo.addDayHumidityList("N/A");
                forecaWeatherInfo.addDayRealFeelHigh("N/A");
                forecaWeatherInfo.addDaySnowAmountList("N/A");
                forecaWeatherInfo.addDaySummaryList(getForecaWeatherConditionStr(forecaWeatherSymbol));
                forecaWeatherInfo.addDayThunderStormProbList("N/A");
                forecaWeatherInfo.addDayNameMillisList("" + getTimeMills(object.optString("dt"), 1));
                forecaWeatherInfo.addRainAmountList("" + object.optDouble("pr"));
                forecaWeatherInfo.addDayRainProbList("" + object.optInt("pp"));
                forecaWeatherInfo.addDayLowTempList("" + object.optInt("tn"));
                forecaWeatherInfo.addDayWindDirectionList(object.optString("wn"));
                forecaWeatherInfo.addDayWindSpeedList(getWindHourSpeed(object.optDouble("wsx")));
                forecaWeatherInfo.addSunRiseList(timeFormatTranslate(object.optString("rise"))); //日出时间
                forecaWeatherInfo.addSunSetList(timeFormatTranslate(object.optString("set")));  //日落时间
                forecaWeatherInfo.addMoonRiseList(timeFormatTranslate(object.optString("mrise")));//月升时间
                forecaWeatherInfo.addMoonSetList(timeFormatTranslate(object.optString("mset")));//月落时间
                forecaWeatherInfo.addDayUVIndex(CommonUtils.getUVLevelByUVIndex(mContext, object.optInt("uv")));
                forecaWeatherInfo.addDayRealFeelLow("N/A");
                forecaWeatherInfo.addDayRealFeelHigh("N/A");
                forecaWeatherInfo.set_day_sunrise_exist(true);
                forecaWeatherInfo.set_day_sunset_exist(true);
                forecaWeatherInfo.set_dayrainamount_exist(true);
                forecaWeatherInfo.set_daythundestormpro_exist(true);
                forecaWeatherInfo.set_dayrainpro_exist(true);
                forecaWeatherInfo.set_daywinddirection_exist(true);
                forecaWeatherInfo.set_daywindspeed_exist(true);
                forecaWeatherInfo.set_day_moonrise_exist(true);
                forecaWeatherInfo.set_day_moonset_exist(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        Log.d(TAG, "-------forcaWeatherInfo------ " + forecaWeatherInfo.toString());
        return forecaWeatherInfo;
    }

    private void parseCityId(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        String cityId = jsonObject.optString("lid");
        if (!"".equals(cityId)) {
            Preferences.saveCityId(mContext, mCityParams.getWeatherDataID(), cityId);
        }
    }

    private long getTimeMills(String formatDate, int flags) {
        String pattern;
        if (flags == HOUR_NAME_MILLIS) {
            pattern = "yyyy-MM-dd HH:mm";
        } else {
            pattern = "yyyy-MM-dd";
        }
        formatDate = formatDate.replace("T", " ");
        DateFormat df = new SimpleDateFormat(pattern);
        Date date;
        long timeMills = 0L;
        try {
            date = df.parse(formatDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            timeMills = cal.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeMills;
    }


    private String getWindHourSpeed(double windSecondSpeed) {
        double windHourSpeed = windSecondSpeed / 1000.0d * (60 * 60);
        return windHourSpeed + "";
    }


    private String getForecaWeatherIcon(String forecaWeatherSymbolCode) {
        int index;
        String forecaSymbolsPre = "d";
        int forecaSymbolsCode = 0;
        if (forecaWeatherSymbolCode.length() != 0) {
            forecaSymbolsPre = forecaWeatherSymbolCode.substring(0, 1);
            forecaSymbolsCode = Integer.valueOf(forecaWeatherSymbolCode.substring(1));
        }
        switch (forecaSymbolsCode) {
            case 0:
                if (isForcaWeatherLight(forecaSymbolsPre)) {
                    index = WeatherConditionID.CLEAR_LIGHT;
                } else {
                    index = WeatherConditionID.CLEAR_NIGHT;
                }
                break;
            case 100:
                if (isForcaWeatherLight(forecaSymbolsPre)) {
                    index = WeatherConditionID.CLEAR_WITH_CLOUD_SMALL_LIGHT;
                } else {
                    index = WeatherConditionID.SMALL_CLOUDY_NIGHT;
                }
                break;
            case 110:
                index = WeatherConditionID.CLEAR_WITH_CLOUD_SMALL_LIGHT;
                break;
            case 111:
                index = WeatherConditionID.CLEAR_WITH_CLOUD_SMALL_LIGHT;
                break;
            case 112:
                index = WeatherConditionID.CLEAR_WITH_CLOUD_SMALL_LIGHT;
                break;
            case 120:
                index = WeatherConditionID.CLEAR_WITH_CLOUD_SMALL_LIGHT;
                break;
            case 121:
                index = WeatherConditionID.CLEAR_WITH_CLOUD_SMALL_LIGHT;
                break;
            case 122:
                index = WeatherConditionID.CLEAR_WITH_CLOUD_SMALL_LIGHT;
                break;
            case 130:
                index = WeatherConditionID.CLEAR_WITH_CLOUD_SMALL_LIGHT;
                break;
            case 131:
                index = WeatherConditionID.CLEAR_WITH_CLOUD_SMALL_LIGHT;
                break;
            case 132:
                index = WeatherConditionID.CLEAR_WITH_CLOUD_SMALL_LIGHT;
                break;
            case 140:
                index = WeatherConditionID.CLEAR_WITH_CLOUD_SMALL_LIGHT;
                break;
            case 141:
                index = WeatherConditionID.CLEAR_WITH_CLOUD_SMALL_LIGHT;
                break;
            case 142:
                index = WeatherConditionID.CLEAR_WITH_CLOUD_SMALL_LIGHT;
                break;
            case 200:
                index = WeatherConditionID.SMALL_CLOUDY_NIGHT;
                break;
            case 210:
                index = WeatherConditionID.RAIN_LIGHT;
                break;
            case 220:
                index = WeatherConditionID.RAIN_LIGHT;
                break;
            case 230:
                index = WeatherConditionID.RAIN_LIGHT;
                break;
            case 231:
                index = WeatherConditionID.SNOW_RAIN_NIGHT;
                break;
            case 232:
                index = WeatherConditionID.CLEAR_SNOW_NORMAL;
                break;
            case 240:
                if (isForcaWeatherLight(forecaSymbolsPre)) {
                    index = WeatherConditionID.CLEAR_THUNDER_RAIN_LIGHT;
                } else {
                    index = WeatherConditionID.RAIN_STORM_NIGHT;
                }
                break;
            case 241:
                index = WeatherConditionID.NORMAL_CLOUDY_NIGHT;
                break;
            case 242:
                index = WeatherConditionID.NORMAL_CLOUDY_NIGHT;
                break;
            case 211:
                index = WeatherConditionID.SNOW_RAIN_NIGHT;
                break;
            case 221:
                index = WeatherConditionID.SNOW_RAIN_NIGHT;
                break;
            case 212:
                index = WeatherConditionID.SMALL_SNOW_NIGHT;
                break;
            case 222:
                index = WeatherConditionID.SMALL_SNOW_NIGHT;
                break;
            case 300:
                index = WeatherConditionID.BLACK_CLOUD_LIGHT;
                break;
            case 310:
                index = WeatherConditionID.RAIN_LIGHT;
                break;
            case 320:
                index = WeatherConditionID.RAIN_LIGHT;
                break;
            case 330:
                index = WeatherConditionID.RAIN_LIGHT;
                break;
            case 331:
                index = WeatherConditionID.SLEET_NIGHT;
                break;
            case 332:
                index = WeatherConditionID.SNOW_NIGHT;
                break;
            case 340:
                if (isForcaWeatherLight(forecaSymbolsPre)) {
                    index = WeatherConditionID.CLEAR_THUNDER_RAIN_LIGHT;
                } else {
                    index = WeatherConditionID.RAIN_STORM_NIGHT;
                }
                break;
            case 341:
                index = WeatherConditionID.SLEET_NIGHT;
                break;
            case 342:
                index = WeatherConditionID.SNOW_NIGHT;
                break;
            case 311:
                index = WeatherConditionID.SNOW_RAIN_NIGHT;
                break;
            case 321:
                index = WeatherConditionID.SNOW_RAIN_NIGHT;
                break;
            case 312:
                index = WeatherConditionID.SMALL_SNOW_NIGHT;
                break;
            case 322:
                index = WeatherConditionID.SNOW_NIGHT;
                break;
            case 400:
                if (isForcaWeatherLight(forecaSymbolsPre)) {
                    index = WeatherConditionID.CLEAR_WITH_CLOUD_NOMAL_LIGHT;
                } else {
                    index = WeatherConditionID.BIG_CLOUDY_NIGHT;
                }

                break;
            case 410:
                index = WeatherConditionID.RAIN_LIGHT;
                break;
            case 441:
                index = WeatherConditionID.SNOW_NIGHT;
                break;
            case 442:
                index = WeatherConditionID.SNOW_NIGHT;
                break;
            case 420:
                index = WeatherConditionID.RAIN_LIGHT;
                break;
            case 430:
                index = WeatherConditionID.RAIN_LIGHT;
                break;
            case 440:
                if (isForcaWeatherLight(forecaSymbolsPre)) {
                    index = WeatherConditionID.CLEAR_THUNDER_RAIN_LIGHT;
                } else {
                    index = WeatherConditionID.RAIN_STORM_NIGHT;
                }

                break;
            case 411:
                index = WeatherConditionID.SNOW_RAIN_NIGHT;
                break;
            case 421:
                index = WeatherConditionID.SNOW_RAIN_NIGHT;
                break;
            case 431:
                index = WeatherConditionID.SNOW_RAIN_NIGHT;
                break;
            case 412:
                index = WeatherConditionID.CLEAR_SNOW_SMALL;
                break;
            case 422:
                index = WeatherConditionID.CLEAR_SNOW_SMALL;
                break;
            case 432:
                index = WeatherConditionID.SNOW_NIGHT;
                break;
            case 500:
                if (isForcaWeatherLight(forecaSymbolsPre)) {
                    index = WeatherConditionID.CLEAR_WITH_CLOUD_SMALL_LIGHT;
                } else {
                    index = WeatherConditionID.SMALL_CLOUDY_NIGHT;
                }
                break;
            case 600:
                if (isForcaWeatherLight(forecaSymbolsPre)) {
                    index = WeatherConditionID.FOG_LIGHT;
                } else {
                    index = WeatherConditionID.FOG_NIGHT;
                }
                break;
            default:
                index = WeatherConditionID.CLEAR_LIGHT;
                break;
        }
        return index + "";
    }


    private boolean isForcaWeatherLight(String preview) {
        return "d".equals(preview);
    }

    private String getWeatherDataUrl(CityParams cityParams, int urlFlg, boolean isUsedCityId) {

        String url;

        String cityId =
                Preferences.readCityId(mContext, mCityParams.getWeatherDataID());
        Log.d(TAG, "-------cityId------ " + cityId);
        if (!"".equals(cityId) && isUsedCityId) {
            url = getDataResourceUrlByCityId("en_us", Constants.REQUEST_WEATHER_APP_ID, Constants.REQUEST_WEATHER_TOKEN, cityId);
        } else {
            url = getDataResourceUrlByLatLon(cityParams, "en_US", Constants.REQUEST_WEATHER_APP_ID, Constants.REQUEST_WEATHER_TOKEN);
        }

        return url;
    }

    /**
     * @anther changzhiwei 2016/7/5 获取数据源的
     */
    private String getDataResourceUrlByLatLon(CityParams cityParams, String lang, String appid, String token) {
        String builder;
//        if (Constants.isForWanDouJia) {
            builder = DATA_SOURCE_URL_HEAD + "lat=" +
                    cityParams.getLatitude() +
                    "&lon=" +
                    cityParams.getLongitude() +
                    "&lang=" +
                    lang +
                    "&appid=" +
                    appid +
                    "&token=" +
                    token;
//        } else {
//            builder = DATA_SOURCE_URL_HEAD + "lat=" +
//                    cityParams.getLatitude() +
//                    "&lon=" +
//                    cityParams.getLongitude() +
//                    "&lang=" +
//                    lang +
//                    "&appid=" +
//                    appid +
//                    "&token=" +
//                    token +
//                    "&version=" + WeatherUtils.getWeatherVersionCode(mContext.getApplicationContext());
//        }
        return builder;
    }

    private String getDataResourceUrlByCityId(String lang, String appid, String token, String cityId) {
        String builder;
//        if (Constants.isForWanDouJia) {
            builder = DATA_SOURCE_URL_HEAD +
                    "lang=" +
                    lang +
                    "&appid=" +
                    appid +
                    "&token=" +
                    token +
                    "&lid=" + cityId;
//        } else {
//            builder = DATA_SOURCE_URL_HEAD +
//                    "lang=" +
//                    lang +
//                    "&appid=" +
//                    appid +
//                    "&token=" +
//                    token +
//                    "&version=" + WeatherUtils.getWeatherVersionCode(mContext.getApplicationContext()) +
//                    "&lid=" + cityId;
//        }
        return builder;
    }

    /**
     * 判断数据源是否可用
     *
     * @param data json数据源
     * @return 是否可用
     */
    private boolean isDataSourceUsable(String data) {
        Log.d(TAG, "-----data------ " + data);
        boolean isUsable = true;
        try {
            JSONObject jsonObject = new JSONObject(data);
            int dataSourceId = jsonObject.optInt("d_id");
            if (dataSourceId != 1) {
                return false;
            }
            String status = jsonObject.optString("status");
            if ("".equals(status) || !status.equals("ok")) //多语言？
                isUsable = false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return isUsable;
    }


    /**
     * 根据天气icon返回天气描述
     *
     * @param weatherIconIndex 天气icon
     * @return 天气描述
     */
    private String getForecaWeatherConditionStr(String weatherIconIndex) {
        if (weatherIconIndex == null || weatherIconIndex.length() != 4) {
            return null;
        }
        String index = weatherIconIndex.substring(1, weatherIconIndex.length());
        String weatherConditionStr = null;
        Resources resources = mContext.getResources();
        switch (index) {
            case "000":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_000);
                break;
            case "100":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_100);
                break;
            case "110":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_110);
                break;
            case "111":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_111);
                break;
            case "112":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_112);
                break;
            case "120":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_120);
                break;
            case "121":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_121);
                break;
            case "122":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_122);
                break;
            case "130":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_130);
                break;
            case "131":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_131);
                break;
            case "132":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_132);
                break;
            case "140":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_140);
                break;
            case "141":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_141);
                break;
            case "142":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_142);
                break;
            case "200":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_200);
                break;
            case "210":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_210);
                break;
            case "211":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_211);
                break;
            case "212":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_212);
                break;
            case "220":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_220);
                break;
            case "221":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_221);
                break;
            case "222":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_222);
                break;
            case "230":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_230);
                break;
            case "231":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_231);
                break;
            case "232":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_232);
                break;
            case "240":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_240);
                break;
            case "241":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_241);
                break;
            case "242":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_242);
                break;
            case "300":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_300);
                break;
            case "310":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_310);
                break;
            case "311":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_311);
                break;
            case "312":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_312);
                break;
            case "320":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_320);
                break;
            case "321":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_321);
                break;
            case "322":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_322);
                break;
            case "330":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_330);
                break;
            case "331":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_331);
                break;
            case "332":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_332);
                break;
            case "340":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_340);
                break;
            case "341":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_341);
                break;
            case "342":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_342);
                break;
            case "400":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_400);
                break;
            case "410":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_410);
                break;
            case "411":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_411);
                break;
            case "412":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_412);
                break;
            case "420":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_420);
                break;
            case "421":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_421);
                break;
            case "422":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_422);
                break;
            case "430":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_430);
                break;
            case "431":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_431);
                break;
            case "432":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_432);
                break;
            case "440":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_440);
                break;
            case "441":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_441);
                break;
            case "442":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_442);
                break;
            case "500":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_500);
                break;
            case "600":
                weatherConditionStr = resources.getString(R.string.foreca_weather_condition_600);
                break;
        }
        return weatherConditionStr;
    }

    /**
     * 将数据源的24小时格式的时间转换为12小时制
     *
     * @param time 24小时格式的时间
     * @return 12小时制的时间
     */
    private String timeFormatTranslate(String time) {
        if (time.length() != 5 || !isCorrectTime(time)) {
            return "Error Time Format!";
        }
        String amOrPm;
        StringBuilder strBuilder = new StringBuilder();
        int hour = Integer.parseInt(time.substring(0, 2));
        if (hour > 12) {
            amOrPm = " PM";
            hour -= 12;
            if (hour < 10) {
                strBuilder.append("0");
            }
            strBuilder.append(hour);
            strBuilder.append(time.substring(2, time.length()));
        } else {
            amOrPm = " AM";
            strBuilder.append(time);
        }
        strBuilder.append(amOrPm);
        return strBuilder.toString();
    }

    private boolean isCorrectTime(String time) {
        int hour = Integer.parseInt(time.substring(0, 2));
        return !(hour >= 24 || time.charAt(3) >= '6');
    }

    /**
     * 将将可见度值转化百帕
     *
     * @param visibity 可见度值
     * @return 保留一位小数的值
     */
    private String getVisbity(int visibity) {
        float values = visibity / 1000.0f;
        DecimalFormat format = new DecimalFormat("##0.0");
        return format.format(values);
    }

    /**
     * GmtOffset转换
     *
     * @param gmtOffset 参数
     * @return 结果
     */
    private String getGmtOffset(String gmtOffset) {
        StringBuffer buffer = new StringBuffer(gmtOffset.substring(0, 1));
        int value = Integer.parseInt(gmtOffset.substring(1, 3));
        float param = Float.parseFloat(gmtOffset.substring(4, gmtOffset.length()));
        param = (value + param / 60.0f);
        buffer.append(param);
        return buffer.toString();
    }

}
