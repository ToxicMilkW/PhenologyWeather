package com.iap.phenologyweather.data.loader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.iap.phenologyweather.R;
import com.iap.phenologyweather.data.model.ConfigData;
import com.iap.phenologyweather.data.model.Location;
import com.iap.phenologyweather.data.model.WeatherRawInfo;
import com.iap.phenologyweather.provider.WeatherDatabaseManager;
import com.iap.phenologyweather.utils.AmberSdkConstants;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WeatherInfoLoader {
    private static final String TAG = WeatherInfoLoader.class.getName();

    public static final int DEFAULT_CITY_ID = 1;
    private static Context sContext;
    private int cityId;

    private Location mLocation = null;
    private ConfigData mConfigData = null;
    private WeatherRawInfo mCurrentWeatherData = null;
    private List<WeatherRawInfo> mNext7DaysWeatherData = new ArrayList<>();
    private List<WeatherRawInfo> mNext24HoursWeatherData = new ArrayList<>();
    private List<Location> mAllLocations = new ArrayList<>();
    private OnLoadFullWeatherDataListener onLoadFullWeatherDataListener;
    private static Map<Integer, WeatherInfoLoader> mWeatherInfoLoaderMap = new ConcurrentHashMap<>();

    private OnLoadFullLocationListener onLoadFullLocationListener = null;
    private final static String DEFAULT_DATA = AmberSdkConstants.DEFAULT_SHOW_STRING;

    public static WeatherInfoLoader getInstance(Context context) {
        sContext = context;
        return getInstance(sContext, DEFAULT_CITY_ID);
    }

    public static WeatherInfoLoader getInstance(Context context, int cityId) {
        sContext = context.getApplicationContext();
        WeatherInfoLoader weatherInfoLoader = mWeatherInfoLoaderMap.get(cityId);
        Log.d(TAG, "----weather map size---- " + mWeatherInfoLoaderMap.size());
        if (weatherInfoLoader == null) {
            weatherInfoLoader = new WeatherInfoLoader(cityId);
            mWeatherInfoLoaderMap.put(cityId, weatherInfoLoader);
        }

        return weatherInfoLoader;
    }

    private WeatherInfoLoader(int cityId) {

        this.cityId = cityId;
    }

    public void clearWeatherInfoLoader() {

        mWeatherInfoLoaderMap.clear();
    }

    private void addWeatherInfoLoader(int cityId, WeatherInfoLoader weatherInfoLoader) {

        deleteWeatherInfoLoader(cityId);
        mWeatherInfoLoaderMap.put(cityId, weatherInfoLoader);
    }

    public void deleteWeatherInfoLoader(int cityId) {
        if (mWeatherInfoLoaderMap.containsKey(cityId)) {
            mWeatherInfoLoaderMap.remove(cityId);
        }
    }

    public void getAllData(OnLoadFullWeatherDataListener listener, final Context context, final int cityId) {

        this.onLoadFullWeatherDataListener = listener;
        if (this.mConfigData == null || this.mCurrentWeatherData == null) {
            mConfigData = ConfigDataLoader.getInstance(context, true);

            if (mConfigData == null) {
                onLoadFullWeatherDataListener.onFailed("Failed to query the config data");
            } else {
                mLocation = LocationInfoLoader.getInstance(sContext).queryLocationById(cityId);
                mCurrentWeatherData = WeatherDatabaseManager.getInstance(sContext).queryCurrentWeatherRawInfoByCityId(cityId);
                mNext24HoursWeatherData = WeatherDatabaseManager.getInstance(sContext).queryHoursWeatherRawInfoListByCityId(cityId);
                mNext7DaysWeatherData = WeatherDatabaseManager.getInstance(sContext).queryNextDaysWeatherRawInfoListByCityId(cityId);
                if (mCurrentWeatherData != null && mNext24HoursWeatherData != null && mNext7DaysWeatherData != null) {
                    addWeatherInfoLoader(cityId, WeatherInfoLoader.this);
                    onLoadFullWeatherDataListener.onSuccess();
                } else {
                    onLoadFullWeatherDataListener.onFailed("No data");
                }
            }
        } else {
            onLoadFullWeatherDataListener.onSuccess();
        }
    }

    public interface OnLoadFullWeatherDataListener {

        void onSuccess();

        void onFailed(String str);
    }

    public void getAllLocations(OnLoadFullLocationListener listener) {

        final WeatherInfoLoader weatherInfoLoader = getInstance(sContext);
        weatherInfoLoader.onLoadFullLocationListener = listener;
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    weatherInfoLoader.mAllLocations = LocationInfoLoader.getInstance(sContext).queryLocationList();
                    weatherInfoLoader.onLoadFullLocationListener.onSuccess();
                } catch (Exception e) {
                    weatherInfoLoader.onLoadFullLocationListener.onFailed("Failed to get all cities");
                }
            }
        }).start();
    }

    public interface OnLoadFullLocationListener {

        void onSuccess();

        void onFailed(String str);
    }

    public Location getmLocation() {
        return mLocation;
    }

    public void setmLocation(Location mLocation) {
        this.mLocation = mLocation;
    }

    public ConfigData getConfigData() {
        if (mConfigData == null) {
            mConfigData = ConfigDataLoader.getInstance(sContext, true);
        }
        return mConfigData;
    }

    public void setmConfigData(ConfigData mConfigData) {
        this.mConfigData = mConfigData;
    }

    public WeatherRawInfo getmCurrentWeatherData() {
        return mCurrentWeatherData;
    }

    public void setmCurrentWeatherData(WeatherRawInfo mCurrentWeatherData) {
        this.mCurrentWeatherData = mCurrentWeatherData;
    }

    public List<WeatherRawInfo> getmNext7DaysWeatherData() {
        return mNext7DaysWeatherData;
    }

    public void setmNext7DaysWeatherData(List<WeatherRawInfo> mNext7DaysWeatherData) {
        this.mNext7DaysWeatherData = mNext7DaysWeatherData;
    }

    public List<WeatherRawInfo> getmNext24HoursWeatherData() {
        return mNext24HoursWeatherData;
    }

    public void setmNext24HoursWeatherData(List<WeatherRawInfo> mNext24HoursWeatherData) {
        this.mNext24HoursWeatherData = mNext24HoursWeatherData;
    }

    public List<Location> getmAllLocations() {
        return this.mAllLocations;
    }

    public String getCurrentCondition() {

        String condition = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null) {
            condition = this.mCurrentWeatherData.getCondition();
        }
        return null == condition ? "" : condition;
    }

    public String getCurrentDoubleDistance() {

        String distance = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null && this.mConfigData != null) {
            distance = this.mCurrentWeatherData.getVisibility() + this.mConfigData.getDistanceUnitName();
        }
        return distance;
    }

    public String getCurrentIntDistance() {
        String distance = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null && this.mConfigData != null) {
            double dis = this.mCurrentWeatherData.getVisibility();
            distance = doubleToInt(dis) + this.mConfigData.getDistanceUnitName();
        }
        return distance;
    }

    public int getCurrentIntDistanceNoUnit() {
        int distance = 0;
        if (this.mCurrentWeatherData != null && this.mConfigData != null) {
            distance = (int) this.mCurrentWeatherData.getVisibility();
        }
        return distance;
    }

    public String getCurrentDoublePressure() {

        String pressure = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null && this.mConfigData != null) {
            BigDecimal bd = new BigDecimal(this.mCurrentWeatherData.getPressure());
            double dPressure = bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            pressure = dPressure + this.mConfigData.getPressureUnitName();
        }
        return pressure;
    }

    public String getCurrentDoublePressureNoUnit() {

        String pressure = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null && this.mConfigData != null) {
            BigDecimal bd = new BigDecimal(this.mCurrentWeatherData.getPressure());
            double dPressure = bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            pressure = dPressure + "";
        }
        return pressure;
    }

    public String getCurrentIntPressure() {

        String pressure = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null && this.mConfigData != null) {
            double pre = this.mCurrentWeatherData.getPressure();
            pressure = doubleToInt(pre) + this.mConfigData.getPressureUnitName();
        }
        return pressure;
    }

    public String getCurrentDoubleDewPoint() {

        String dewPoint = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null) {
            dewPoint = this.mCurrentWeatherData.getDewPoint() + AmberSdkConstants.TEMP_UNIT;
        }
        return dewPoint;
    }

    public String getCurrentIntDewPoint() {
        String dewPoint = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null) {
            double dew = this.mCurrentWeatherData.getDewPoint();
            dewPoint = doubleToInt(dew) + AmberSdkConstants.TEMP_UNIT;
        }
        return dewPoint;
    }

    public String getCurrentUVIndex() {
        String uvIndex = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null) {
            uvIndex = this.mCurrentWeatherData.getUv();
        }
        return uvIndex;
    }

    public String getCurrentSunRiseTime() {
        String sunRiseTime = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null) {
            sunRiseTime = this.mCurrentWeatherData.getSunriseMillis() + "";
        }
        return sunRiseTime;
    }

    @SuppressLint("SimpleDateFormat")
    public String getCurrentSunRiseTimeFormated() {
        String sunRisString = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null) {
            long time = this.mCurrentWeatherData.getSunriseMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
            sunRisString = sdf.format(new Date(time));
        }
        return sunRisString;
    }

    public String getCurrentSunSetTime() {
        String sunSetTime = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null) {
            sunSetTime = this.mCurrentWeatherData.getSunsetMillis() + "";
        }
        return sunSetTime;
    }

    public String getCurrentSunsetTimeFormated() {
        String sunSetString = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null) {
            long time = this.mCurrentWeatherData.getSunsetMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
            sunSetString = sdf.format(new Date(time));
        }
        return sunSetString;
    }

    public boolean isLocaleTime() {
        boolean isLocalTime;
        if (this.mConfigData == null) {
            this.mConfigData = ConfigDataLoader.getInstance(sContext, true);
        }
        isLocalTime = mConfigData.isLocalTime();
        return isLocalTime;
    }

    public String getCurrentDoubleTemp() {
        String temp = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null) {
            temp = this.mCurrentWeatherData.getTemp() + AmberSdkConstants.TEMP_UNIT;
        }
        return temp;
    }

    public String getCurrentIntTemp() {
        String hourTemp = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null) {
            double temp = this.mCurrentWeatherData.getTemp();
            hourTemp = doubleToInt(temp) + AmberSdkConstants.TEMP_UNIT;
        }
        return hourTemp;
    }

    public String getCurrentDoubleTempNoUnit() {
        String temp = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null) {
            temp = (int) this.mCurrentWeatherData.getTemp() + "";
        }
        return temp;
    }

    public String getCurrentIntTempNoUnit() {

        String hourTemp = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null) {
            double temp = this.mCurrentWeatherData.getTemp();
            hourTemp = doubleToInt(temp) + "";
        }
        return hourTemp;
    }

    public String getCurrentIcon() {

        String icon = null;
        if (this.mCurrentWeatherData != null) {
            icon = this.mCurrentWeatherData.getIcon();
        }
        return icon;
    }

    public String getCurrentDoubleHighTemp() {

        String highTemp = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null) {
            highTemp = this.mCurrentWeatherData.getHighTemp() + AmberSdkConstants.TEMP_UNIT;
        }
        return highTemp;
    }

    public String getCurrentIntHighTemp() {

        String highTemp = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null) {
            double temp = this.mCurrentWeatherData.getHighTemp();
            highTemp = doubleToInt(temp) + AmberSdkConstants.TEMP_UNIT;
        }
        return highTemp;
    }

    public String getCurrentDoubleHighTempNoUnit() {

        String highTemp = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null) {
            highTemp = (int) this.mCurrentWeatherData.getHighTemp() + "";
        }
        return highTemp;
    }

    public String getCurrentIntHighTempNoUnit() {
        String highTemp = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null) {
            double temp = this.mCurrentWeatherData.getHighTemp();
            highTemp = doubleToInt(temp) + "";
        }
        return highTemp;
    }

    public String getCurrentDoubleLowTemp() {

        String lowTemp = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null) {
            lowTemp = this.mCurrentWeatherData.getLowTemp() + AmberSdkConstants.TEMP_UNIT;
        }
        return lowTemp;
    }

    public String getCurrentIntLowTemp() {
        String lowTemp = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null) {
            double temp = this.mCurrentWeatherData.getLowTemp();
            lowTemp = doubleToInt(temp) + AmberSdkConstants.TEMP_UNIT;
        }
        return lowTemp;
    }

    public String getCurrentDoubleLowTempNoUnit() {

        String lowTemp = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null) {
            lowTemp = (int) this.mCurrentWeatherData.getLowTemp() + "";
        }
        return lowTemp;
    }

    public String getCurrentIntLowTempNoUnit() {

        String lowTemp = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null) {
            double temp = this.mCurrentWeatherData.getLowTemp();
            lowTemp = doubleToInt(temp) + "";
        }
        return lowTemp;
    }

    public String getCurrentDoubleRealFeel() {
        String realFeel = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null) {
            realFeel = this.mCurrentWeatherData.getRealFeel() + AmberSdkConstants.TEMP_UNIT;
        }
        return realFeel;
    }

    public String getCurrentIntRealFeel() {

        String realFeel = DEFAULT_DATA;
        if (this.mCurrentWeatherData != null) {
            double temp = this.mCurrentWeatherData.getRealFeel();
            realFeel = doubleToInt(temp) + AmberSdkConstants.TEMP_UNIT;
        }
        return realFeel;
    }

    public double getCurrentWindDirection() {

        double windDirection = -999;
        if (mCurrentWeatherData != null) {
            windDirection = this.mCurrentWeatherData.getWindDirection();
        }
        return windDirection;
    }

    public String getCurrentHumidity() {
        String humidity = DEFAULT_DATA;
        if (mCurrentWeatherData != null) {
            String str = this.mCurrentWeatherData.getHumidity();
            humidity = str.substring(0, str.length() - 1);
        }
        return humidity;
    }

    public String getCurrentWindSpeed() {

        String windSpeed = DEFAULT_DATA;
        if (mCurrentWeatherData != null) {
            windSpeed = this.mCurrentWeatherData.getWindSpeed() + "";
        }
        return windSpeed;
    }

    public String getCurrentWindSpeedUnit() {

        String speedUnit = DEFAULT_DATA;
        if (this.mConfigData != null) {
            speedUnit = this.mConfigData.getSpeedUnitName();
        }
        return speedUnit;
    }

    public int getHourItems() {

        int hourItems = 0;
        if (this.mNext24HoursWeatherData != null) {
            hourItems = this.mNext24HoursWeatherData.size();
        }
        return hourItems;
    }

    public long getHourMillis(int index) {
        long hourMillis = AmberSdkConstants.DEFAULT_EMPTY_VALUE_LONG;
        if (index >= 0 && mNext24HoursWeatherData != null && index < this.mNext24HoursWeatherData.size()) {
            hourMillis = this.mNext24HoursWeatherData.get(index).getNameMillis();
        }
        return hourMillis;
    }

    public String getHourName(int index) {

        String hourName = DEFAULT_DATA;
        if (index >= 0 && mNext24HoursWeatherData != null && index < this.mNext24HoursWeatherData.size()) {
            Date date = new Date(this.mNext24HoursWeatherData.get(index).getNameMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            hourName = sdf.format(date);
        }
        return hourName;
    }


    public String getHourCondition(int index) {
        String hourCondition = DEFAULT_DATA;
        if (index >= 0 && mNext24HoursWeatherData != null && index < this.mNext24HoursWeatherData.size()) {
            hourCondition = this.mNext24HoursWeatherData.get(index).getCondition();
        }
        return hourCondition;
    }

    public String getHourIcon(int index) {
        String hourIcon = "2";
        if (index >= 0 && mNext24HoursWeatherData != null && index < this.mNext24HoursWeatherData.size()) {
            hourIcon = this.mNext24HoursWeatherData.get(index).getIcon();
        }
        return hourIcon;
    }

    public String getHourDoubleTemp(int index) {

        String hourTemp = DEFAULT_DATA;
        if (index >= 0 && mNext24HoursWeatherData != null && index < this.mNext24HoursWeatherData.size()) {
            hourTemp = this.mNext24HoursWeatherData.get(index).getTemp() + AmberSdkConstants.TEMP_UNIT;
        }
        return hourTemp;
    }

    public String getHourIntTemp(int index) {

        String hourTemp = DEFAULT_DATA;
        if (index >= 0 && mNext24HoursWeatherData != null && index < this.mNext24HoursWeatherData.size()) {
            double temp = this.mNext24HoursWeatherData.get(index).getTemp();
            hourTemp = doubleToInt(temp) + AmberSdkConstants.TEMP_UNIT;
        }
        return hourTemp;
    }

    public String getHourDoubleTempNoUnit(int index) {

        String hourTemp = DEFAULT_DATA;
        if (index >= 0 && mNext24HoursWeatherData != null && index < this.mNext24HoursWeatherData.size()) {
            hourTemp = (int) this.mNext24HoursWeatherData.get(index).getTemp() + "";
        }
        return hourTemp;
    }

    public String getHourIntTempNoUnit(int index) {
        String hourTemp = DEFAULT_DATA;
        if (index >= 0 && mNext24HoursWeatherData != null && index < this.mNext24HoursWeatherData.size()) {
            double temp = this.mNext24HoursWeatherData.get(index).getTemp();
            hourTemp = doubleToInt(temp) + "";
        }
        return hourTemp;
    }

    public int getDayItems() {

        int dayItems = 0;
        if (this.mNext7DaysWeatherData != null) {
            dayItems = this.mNext7DaysWeatherData.size();
        }
        return dayItems;
    }

    public String getDayIcon(int index) {

        String dayIcon = DEFAULT_DATA;
        if (index >= 0 && mNext7DaysWeatherData != null && index < mNext7DaysWeatherData.size()) {
            dayIcon = this.mNext7DaysWeatherData.get(index).getIcon();
        }
        return dayIcon;
    }

    public String getDayCondition(int index) {

        String dayCondition = DEFAULT_DATA;

        if (index >= 0 && mNext7DaysWeatherData != null && index < mNext7DaysWeatherData.size()) {
            dayCondition = this.mNext7DaysWeatherData.get(index).getCondition();
        }
        return dayCondition;
    }

    public String getDayDoubleHighTemp(int index) {
        String dayHighTemp = DEFAULT_DATA;
        if (index >= 0 && mNext7DaysWeatherData != null && index < this.mNext7DaysWeatherData.size()) {
            dayHighTemp = this.mNext7DaysWeatherData.get(index).getHighTemp() + AmberSdkConstants.TEMP_UNIT;
        }
        return dayHighTemp;
    }

    public String getDayIntHighTemp(int index) {

        String hourTemp = DEFAULT_DATA;
        if (index >= 0 && mNext7DaysWeatherData != null && index < this.mNext7DaysWeatherData.size()) {
            double temp = this.mNext7DaysWeatherData.get(index).getHighTemp();
            hourTemp = doubleToInt(temp) + AmberSdkConstants.TEMP_UNIT;
        }
        return hourTemp;
    }

    public String getDayDoubleLowTemp(int index) {
        String dayLowTemp = DEFAULT_DATA;
        if (index >= 0 && mNext7DaysWeatherData != null && index < this.mNext7DaysWeatherData.size()) {
            dayLowTemp = this.mNext7DaysWeatherData.get(index).getLowTemp() + AmberSdkConstants.TEMP_UNIT;
        }
        return dayLowTemp;
    }

    public String getDayIntLowTemp(int index) {
        String hourTemp = DEFAULT_DATA;
        if (index >= 0 && mNext7DaysWeatherData != null && index < this.mNext7DaysWeatherData.size()) {
            double temp = this.mNext7DaysWeatherData.get(index).getLowTemp();
            hourTemp = doubleToInt(temp) + AmberSdkConstants.TEMP_UNIT;
        }
        return hourTemp;
    }

    public String getDayMills(int index) {
        String dayMills = DEFAULT_DATA;
        if (index >= 0 && mNext7DaysWeatherData != null && index < this.mNext7DaysWeatherData.size()) {
            dayMills = this.mNext7DaysWeatherData.get(index).getNameMillis() + "";
        }
        return dayMills;
    }

    public String getCity() {
        if (mLocation != null) {
            return mLocation.getLevel2();
        } else {
            return sContext.getResources().getString(R.string.current_location);
        }
    }

    public boolean isUseWorldClock() {
        boolean isUseWorldCock = false;
        if (this.mConfigData != null) {
            isUseWorldCock = this.mConfigData.isUseWorldClock();
        }
        return isUseWorldCock;
    }

    public boolean isClock24Formate() {
        boolean isClock24Formate = true;
        if (this.mConfigData != null) {
            isClock24Formate = this.mConfigData.isClock24Formate();
        }
        return isClock24Formate;
    }

    public int getDateFormate() {
        int dateFormate = 0;
        if (this.mConfigData != null) {
            dateFormate = this.mConfigData.getDateFormate();
        }
        return dateFormate;
    }

    public int getTempUnit() {
        int tempUnit = 0;
        if (this.mConfigData != null) {
            tempUnit = this.mConfigData.getTempUnit();
        }
        return tempUnit;
    }

    public String getTempUnitName() {
        String tempUnitName = DEFAULT_DATA;
        if (this.mConfigData != null) {
            tempUnitName = this.mConfigData.getTempUnitName();
        }
        return tempUnitName;
    }

    public int getSpeedUnit() {
        int speedUnit = 0;
        if (this.mConfigData != null) {
            speedUnit = this.mConfigData.getSpeedUnit();
        }
        return speedUnit;
    }

    public String getSpeedUnitName() {
        String speedUnitName = DEFAULT_DATA;
        if (this.mConfigData != null) {
            speedUnitName = this.mConfigData.getSpeedUnitName();
        }
        return speedUnitName;
    }

    public int getPressureUnit() {
        int pressureUnit = 0;
        if (this.mConfigData != null) {
            pressureUnit = this.mConfigData.getPressureUnit();
        }
        return pressureUnit;
    }

    public String getPressureUnitName() {
        String pressureUnitName = DEFAULT_DATA;
        if (this.mConfigData != null) {
            pressureUnitName = this.mConfigData.getPressureUnitName();
        }
        return pressureUnitName;
    }

    public int getDistanceUnit() {
        int distanceUnit = 0;
        if (this.mConfigData != null) {
            distanceUnit = this.mConfigData.getDistanceUnit();
        }
        return distanceUnit;
    }

    public String getDistanceUnitName() {
        String distanceUnitName = DEFAULT_DATA;
        if (this.mConfigData != null) {
            distanceUnitName = this.mConfigData.getDistanceUnitName();
        }
        return distanceUnitName;
    }

    private int doubleToInt(double num) {

        BigDecimal bd = new BigDecimal(num);
        return bd.setScale(1, RoundingMode.HALF_UP).intValue();
    }

    public String getFormattedSunRiseTime() {
        String time = DEFAULT_DATA;
        if (null != this.mCurrentWeatherData) {
            time = this.mCurrentWeatherData.getFormattedSunRise();
        }
        return time;
    }

    public String getFormattedSunSetTime() {
        String time = DEFAULT_DATA;
        if (null != this.mCurrentWeatherData) {
            time = this.mCurrentWeatherData.getFormattedSunSet();
        }
        return time;
    }

    public String getFormattedWindDirection() {
        String dir = DEFAULT_DATA;
        if (null != this.mCurrentWeatherData) {
            dir = this.mCurrentWeatherData.getFormattedWindDirection();
        }
        return dir;
    }

    public String getFormattedHourTimeName(int index) {
        String timeName = DEFAULT_DATA;
        if (null != this.mNext24HoursWeatherData && index >= 0 && index < this.mNext24HoursWeatherData.size()) {
            timeName = this.mNext24HoursWeatherData.get(index).getFormattedTimeName();
        }
        return timeName;
    }

    public String getFormattedDayTimeName(int index) {
        String timeName = DEFAULT_DATA;
        if (null != this.mNext7DaysWeatherData && index >= 0 && index < this.mNext7DaysWeatherData.size()) {
            timeName = this.mNext7DaysWeatherData.get(index).getFormattedTimeName();
        }
        return timeName;
    }

    public boolean isHourLight(int index) {
        boolean isLight = true;
        if (this.mNext24HoursWeatherData != null && index >= 0 && index < this.mNext24HoursWeatherData.size()) {
            isLight = this.mNext24HoursWeatherData.get(index).isLight();
        }
        return isLight;
    }

    public boolean isVisibilityExist() {
        boolean isVisibilityExist = true;
        if (null != this.mCurrentWeatherData) {
            isVisibilityExist = this.mCurrentWeatherData.isVisibilityExist();
        }
        return isVisibilityExist;
    }

    public boolean isDewpointExist() {
        boolean isDewpointExist = true;
        if (null != this.mCurrentWeatherData) {
            isDewpointExist = this.mCurrentWeatherData.isDewpointExist();
        }
        return isDewpointExist;
    }

    public boolean isUvindexExist() {
        boolean isUvindexExist = true;
        if (null != this.mCurrentWeatherData) {
            isUvindexExist = this.mCurrentWeatherData.isUvindexExist();
        }
        return isUvindexExist;
    }

    public boolean isPressureExist() {
        boolean isPressureExist = true;
        if (null != this.mCurrentWeatherData) {
            isPressureExist = this.mCurrentWeatherData.isPressureExist();
        }
        return isPressureExist;
    }


    //	============================================== add
    public String getDayHumidity(int index) {
        return getHumidityFromList(mNext7DaysWeatherData, index);
    }

    public String getHourHumidity(int index) {
        return getHumidityFromList(mNext24HoursWeatherData, index);
    }

    private String getHumidityFromList(List<WeatherRawInfo> list, int index) {
        String humidity = DEFAULT_DATA;

        if (isElementExist(list, index)) {
            String str = list.get(index).getHumidity();
            if (str != null && str.length() > 0 && !str.equals(AmberSdkConstants.DEFAULT_SHOW_STRING)) {
                humidity = str.substring(0, str.length() - 1);
            }
        }

        return humidity;
    }

    public double getDayWindDirection(int index) {
        return getWindDirectionFromList(mNext7DaysWeatherData, index);
    }

    public double getHourWindDirection(int index) {
        return getWindDirectionFromList(mNext24HoursWeatherData, index);
    }

    private double getWindDirectionFromList(List<WeatherRawInfo> list, int index) {
        double windDirection = -999;
        if (isElementExist(list, index)) {
            windDirection = list.get(index).getWindDirection();
        }
        return windDirection;
    }

    private String getWindSpeedFromList(List<WeatherRawInfo> list, int index) {
        String windSpeed = DEFAULT_DATA;

        if (isElementExist(list, index)) {
//			try{
            windSpeed = list.get(index).getWindSpeed() + "";
//			}catch (Exception e){
//				e.printStackTrace();
//			}
        }
        return windSpeed;
    }

    public String getDayWindSpeed(int index) {
        return getWindSpeedFromList(mNext7DaysWeatherData, index);
    }

    public String getHourWindSpeed(int index) {
        return getWindSpeedFromList(mNext24HoursWeatherData, index);
    }

    public String getHourRainProb(int index) {
        String rainProb = DEFAULT_DATA;
        if (index >= 0 && mNext24HoursWeatherData != null && index < mNext24HoursWeatherData.size()) {
            rainProb = mNext24HoursWeatherData.get(index).getRainProbility();
        }
        Log.d("cxq", "hour size:" + mNext24HoursWeatherData.size() + "\nindex:" + index + "   rainProb:" + rainProb);
        return rainProb;
    }

    private String getRealFeelFromList(List<WeatherRawInfo> list, int index) {
        String realFeel = DEFAULT_DATA;
        if (isElementExist(list, index)) {
            double d = list.get(index).getRealFeel();
//			Log.d("zhangbowei", "realfeel="+d);
            int feel = doubleToInt(d);
            if (feel > -997) {
                realFeel = doubleToInt(d) + AmberSdkConstants.TEMP_UNIT;
            }
        }
        return realFeel;
    }

    public String getDayRealfeel(int index) {
        return getRealFeelFromList(mNext7DaysWeatherData, index);
    }

    public String getHourRealfeel(int index) {
        return getRealFeelFromList(mNext24HoursWeatherData, index);
    }

    private String getFormattedSunSetTimeFromList(List<WeatherRawInfo> list, int index) {

        String time = DEFAULT_DATA;
        if (isElementExist(list, index)) {
            time = list.get(index).getFormattedSunSet();
        }
        return time;
    }

    public String getDayFormattedSunSetTime(int index) {
        return getFormattedSunSetTimeFromList(mNext7DaysWeatherData, index);
    }

    private String getFormattedSunRiseTimeFromList(List<WeatherRawInfo> list, int index) {

        String time = DEFAULT_DATA;
        if (isElementExist(list, index)) {
            time = list.get(index).getFormattedSunRise();
        }
        return time;
    }

    public String getDayFormattedSunRiseTime(int index) {
        return getFormattedSunRiseTimeFromList(mNext7DaysWeatherData, index);
    }

    private String getFormattedMoonSetTimeFromList(List<WeatherRawInfo> list, int index) {

        String time = DEFAULT_DATA;
        if (isElementExist(list, index)) {
            time = list.get(index).getFormattedMoonSet();
        }
        return time;
    }

    public String getDayFormattedMoonSetTime(int index) {
        return getFormattedMoonSetTimeFromList(mNext7DaysWeatherData, index);
    }

    private String getFormattedMoonRiseTimeFromList(List<WeatherRawInfo> list, int index) {

        String time = DEFAULT_DATA;
        if (isElementExist(list, index)) {
            time = list.get(index).getFormattedMoonRise();
        }
        return time;
    }

    public String getDayFormattedMoonRiseTime(int index) {
        return getFormattedMoonRiseTimeFromList(mNext7DaysWeatherData, index);
    }


    private boolean isElementExist(List<WeatherRawInfo> list, int index) {
        return list != null && index < list.size();
    }

    public String getHourNameByFormat(int index, String format) {
        return getDateNameByFormate(mNext24HoursWeatherData, index, format);
    }

    public String getDayNameByFormate(int index, String format) {
        return getDateNameByFormate(mNext7DaysWeatherData, index, format);
    }

    private String getDateNameByFormate(List<WeatherRawInfo> list, int index, String format) {
        String hourName = DEFAULT_DATA;
        if (index >= 0 && list != null && index < list.size()) {
            Date date = new Date(list.get(index).getNameMillis());
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());

            hourName = sdf.format(date);
        }
        return hourName;
    }

    public long getDaySunriseMillis(int index) {
        long nameMillis = 0;
        if (index >= 0 && mNext7DaysWeatherData != null && index < mNext7DaysWeatherData.size()) {
            nameMillis = mNext7DaysWeatherData.get(index).getSunriseMillis();
        }
        return nameMillis;
    }

    public long getDaySunsetMillis(int index) {
        long nameMillis = 0;
        if (index >= 0 && mNext7DaysWeatherData != null && index < mNext7DaysWeatherData.size()) {
            nameMillis = mNext7DaysWeatherData.get(index).getSunsetMillis();
        }
        return nameMillis;
    }

    public double getDayRealFeelHigh(int index) {
        double realFeelHigh = 0;
        if (index >= 0 && mNext7DaysWeatherData != null && index < mNext7DaysWeatherData.size()) {
            realFeelHigh = mNext7DaysWeatherData.get(index).getRealFeelHigh();
        }
        return realFeelHigh;
    }

    public double getDayRealFeelLow(int index) {
        double realFeelLow = 0;
        if (index >= 0 && mNext7DaysWeatherData != null && index < mNext7DaysWeatherData.size()) {
            realFeelLow = mNext7DaysWeatherData.get(index).getRealFeelLow();

        }
        return realFeelLow;
    }

    public double getDayRainAmount(int index) {
        double rainAmount = 0;
        if (index >= 0 && mNext7DaysWeatherData != null && index < mNext7DaysWeatherData.size()) {
            rainAmount = mNext7DaysWeatherData.get(index).getRainAmount();
        }
        return rainAmount;
    }

    public String getDayRainProb(int index) {
        String rainProb = DEFAULT_DATA;
        if (index >= 0 && mNext7DaysWeatherData != null && index < mNext7DaysWeatherData.size()) {
            rainProb = mNext7DaysWeatherData.get(index).getRainProbility();
        }
        return rainProb;
    }

    public String getDayFormattedDayTime(int index) {
        String daytime = DEFAULT_DATA;
        if (index >= 0 && mNext7DaysWeatherData != null && index < mNext7DaysWeatherData.size()) {
            WeatherRawInfo weatherRawInfo = mNext7DaysWeatherData.get(index);
            long mills = weatherRawInfo.getDayTimeMillis();
            if (mills > 0) {
                daytime = String.format("%.1f", mills / 3600000f);
            }
        }
        return daytime;
    }

    public String getDayUvMax(int index) {
        String uv = DEFAULT_DATA;
        if (index >= 0 && mNext7DaysWeatherData != null && index < mNext7DaysWeatherData.size()) {
            uv = mNext7DaysWeatherData.get(index).getUv();
        }
        return uv;
    }

    public boolean isDayMoonRiseExist(int index) {
        boolean isExist = false;
        if (index >= 0 && mNext7DaysWeatherData != null && index < mNext7DaysWeatherData.size()) {
            isExist = mNext7DaysWeatherData.get(index).getIsMoonRiseExist();
        }
        return isExist;
    }

    public boolean isDayMoonSetExist(int index) {
        boolean isExist = false;
        if (index >= 0 && mNext7DaysWeatherData != null && index < mNext7DaysWeatherData.size()) {
            isExist = mNext7DaysWeatherData.get(index).getIsMoonSetExist();
        }
        return isExist;
    }

    public long getDayNameMills(int index) {
        long mills = 0;
        if (index >= 0 && mNext7DaysWeatherData != null && index < mNext7DaysWeatherData.size()) {
            mills = mNext7DaysWeatherData.get(index).getNameMillis();
        }
        return mills;
    }

    @Override
    public String toString() {
        return "WeatherInfoLoader{" +
                "cityId=" + cityId +
                ", mLocation=" + mLocation +
                ", mConfigData=" + mConfigData +
                ", mCurrentWeatherData=" + mCurrentWeatherData +
                ", mNext7DaysWeatherData=" + mNext7DaysWeatherData +
                ", mNext24HoursWeatherData=" + mNext24HoursWeatherData +
                ", mAllLocations=" + mAllLocations +
                ", onLoadFullWeatherDataListener=" + onLoadFullWeatherDataListener +
                ", onLoadFullLocationListener=" + onLoadFullLocationListener +
                '}';
    }
}
