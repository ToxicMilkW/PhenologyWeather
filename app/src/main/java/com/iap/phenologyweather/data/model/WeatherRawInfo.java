package com.iap.phenologyweather.data.model;

import com.iap.phenologyweather.utils.AmberSdkConstants;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.math.BigDecimal;


@Entity(nameInDb = "current_table")
public class WeatherRawInfo {

    @Id(autoincrement = true)
    private Long tableKeyId;
    @Property(nameInDb = "info_type")
    private int infoType;
    @Property(nameInDb = "city_id")
    private int id;
    @Property(nameInDb = "time_temp")
    private long nameMillis = AmberSdkConstants.DEFAULT_EMPTY_VALUE_LONG;
    @Property(nameInDb = "temp")
    private double temp = AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE;
    @Property(nameInDb = "humidity")
    private String humidity = AmberSdkConstants.DEFAULT_SHOW_STRING;
    @Property(nameInDb = "pressure")
    private double pressure = AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE;
    @Property(nameInDb = "uvindex")
    private String uv = AmberSdkConstants.DEFAULT_SHOW_STRING;
    @Property(nameInDb = "wind_speed")
    private double windSpeed = AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE;
    @Property(nameInDb = "wind_direction")
    private double windDirection = AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE;
    @Property(nameInDb = "distance")
    private double visibility = AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE;
    @Property(nameInDb = "dew_point")
    private double dewPoint = AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE;
    @Property(nameInDb = "condition")
    private String condition = AmberSdkConstants.DEFAULT_SHOW_STRING;
    @Property(nameInDb = "weather_icon")
    private String icon = AmberSdkConstants.DEFAULT_SHOW_STRING;
    @Property(nameInDb = "rain_amount")
    private double rainAmount = AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE;
    @Property(nameInDb = "rain_probility")
    private String rainProbility = AmberSdkConstants.DEFAULT_SHOW_STRING;
    @Property(nameInDb = "snow_amount")
    private double snowAmount = AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE;
    @Property(nameInDb = "snow_probility")
    private String snowProbility = AmberSdkConstants.DEFAULT_SHOW_STRING;
    @Property(nameInDb = "rain_storm_probility")
    private String thunderStormProbility = AmberSdkConstants.DEFAULT_SHOW_STRING;
    @Property(nameInDb = "real_feel_high")
    private double realFeelHigh = AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE;
    @Property(nameInDb = "real_feel_low")
    private double realFeelLow = AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE;
    @Property(nameInDb = "real_feel")
    private double realFeel = AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE;
    @Property(nameInDb = "sunrise")
    private long sunriseMillis = AmberSdkConstants.DEFAULT_EMPTY_VALUE_LONG;
    @Property(nameInDb = "sunset")
    private long sunsetMillis = AmberSdkConstants.DEFAULT_EMPTY_VALUE_LONG;
    @Property(nameInDb = "high_temp")
    private double highTemp = AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE;
    @Property(nameInDb = "low_temp")
    private double lowTemp = AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE;
    @Property(nameInDb = "wind_speed_unit")
    private String windSpeedUnit = AmberSdkConstants.DEFAULT_SHOW_STRING;
    @Property(nameInDb = "is_sunrise_exist")
    private boolean isSunRiseExist = false;
    @Property(nameInDb = "is_sunset_exist")
    private boolean isSunSetExist = false;
    @Property(nameInDb = "pm25")
    private double pm25 = AmberSdkConstants.DEFAULT_EMPTY_VALUE_DOUBLE;
    @Property(nameInDb = "formatted_sun_rise")
    private String formattedSunRise = AmberSdkConstants.DEFAULT_SHOW_STRING;
    @Property(nameInDb = "formatted_sun_set")
    private String formattedSunSet = AmberSdkConstants.DEFAULT_SHOW_STRING;
    @Property(nameInDb = "formatted_wind_direction")
    private String formattedWindDirection = AmberSdkConstants.DEFAULT_SHOW_STRING;
    @Property(nameInDb = "formatted_time_name")
    private String formattedTimeName = AmberSdkConstants.DEFAULT_SHOW_STRING;
    @Property(nameInDb = "is_light")
    private boolean isLight = true;
    @Property(nameInDb = "is_dewpoint_exist")
    private boolean isDewpointExist = false;
    @Property(nameInDb = "is_pressure_exist")
    private boolean isPressureExist = false;
    @Property(nameInDb = "is_uvindex_exist")
    private boolean isUvindexExist = false;
    @Property(nameInDb = "is_visibility_exist")
    private boolean isVisibilityExist = false;
    @Property(nameInDb = "moonrise")
    private long moonRiseMillis = AmberSdkConstants.DEFAULT_EMPTY_VALUE_LONG;
    @Property(nameInDb = "moonset")
    private long moonSetMillis = AmberSdkConstants.DEFAULT_EMPTY_VALUE_LONG;
    @Property(nameInDb = "is_moonrise_exist")
    private boolean isMoonRiseExist = false;
    @Property(nameInDb = "is_moonset_exist")
    private boolean isMoonSetExist = false;
    @Property(nameInDb = "formatted_moon_rise")
    private String formattedMoonRise = AmberSdkConstants.DEFAULT_SHOW_STRING;
    @Property(nameInDb = "formatted_moon_set")
    private String formattedMoonSet = AmberSdkConstants.DEFAULT_SHOW_STRING;
    @Property(nameInDb = "daytime")
    private long dayTimeMillis = AmberSdkConstants.DEFAULT_EMPTY_VALUE_LONG;

//    @Property
//    private String keep;

    @Generated(hash = 560356731)
    public WeatherRawInfo(Long tableKeyId, int infoType, int id, long nameMillis, double temp, String humidity, double pressure, String uv, double windSpeed,
                          double windDirection, double visibility, double dewPoint, String condition, String icon, double rainAmount, String rainProbility, double snowAmount,
                          String snowProbility, String thunderStormProbility, double realFeelHigh, double realFeelLow, double realFeel, long sunriseMillis, long sunsetMillis,
                          double highTemp, double lowTemp, String windSpeedUnit, boolean isSunRiseExist, boolean isSunSetExist, double pm25, String formattedSunRise,
                          String formattedSunSet, String formattedWindDirection, String formattedTimeName, boolean isLight, boolean isDewpointExist, boolean isPressureExist,
                          boolean isUvindexExist, boolean isVisibilityExist, long moonRiseMillis, long moonSetMillis, boolean isMoonRiseExist, boolean isMoonSetExist,
                          String formattedMoonRise, String formattedMoonSet, long dayTimeMillis) {
        this.tableKeyId = tableKeyId;
        this.infoType = infoType;
        this.id = id;
        this.nameMillis = nameMillis;
        this.temp = temp;
        this.humidity = humidity;
        this.pressure = pressure;
        this.uv = uv;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.visibility = visibility;
        this.dewPoint = dewPoint;
        this.condition = condition;
        this.icon = icon;
        this.rainAmount = rainAmount;
        this.rainProbility = rainProbility;
        this.snowAmount = snowAmount;
        this.snowProbility = snowProbility;
        this.thunderStormProbility = thunderStormProbility;
        this.realFeelHigh = realFeelHigh;
        this.realFeelLow = realFeelLow;
        this.realFeel = realFeel;
        this.sunriseMillis = sunriseMillis;
        this.sunsetMillis = sunsetMillis;
        this.highTemp = highTemp;
        this.lowTemp = lowTemp;
        this.windSpeedUnit = windSpeedUnit;
        this.isSunRiseExist = isSunRiseExist;
        this.isSunSetExist = isSunSetExist;
        this.pm25 = pm25;
        this.formattedSunRise = formattedSunRise;
        this.formattedSunSet = formattedSunSet;
        this.formattedWindDirection = formattedWindDirection;
        this.formattedTimeName = formattedTimeName;
        this.isLight = isLight;
        this.isDewpointExist = isDewpointExist;
        this.isPressureExist = isPressureExist;
        this.isUvindexExist = isUvindexExist;
        this.isVisibilityExist = isVisibilityExist;
        this.moonRiseMillis = moonRiseMillis;
        this.moonSetMillis = moonSetMillis;
        this.isMoonRiseExist = isMoonRiseExist;
        this.isMoonSetExist = isMoonSetExist;
        this.formattedMoonRise = formattedMoonRise;
        this.formattedMoonSet = formattedMoonSet;
        this.dayTimeMillis = dayTimeMillis;
    }

    @Generated(hash = 207415145)
    public WeatherRawInfo() {
    }


    public double getRealFeelLow() {
        return realFeelLow;
    }

    public void setRealFeelLow(double realFeelLow) {
        this.realFeelLow = realFeelLow;
    }

    public double getRealFeelHigh() {
        return realFeelHigh;
    }

    public void setRealFeelHigh(double realFeelHigh) {
        this.realFeelHigh = realFeelHigh;
    }

    public boolean isVisibilityExist() {
        return isVisibilityExist;
    }

    public void setVisibilityExist(boolean isVisibilityExist) {
        this.isVisibilityExist = isVisibilityExist;
    }

    public boolean isDewpointExist() {
        return isDewpointExist;
    }

    public void setDewpointExist(boolean isDewpointExist) {
        this.isDewpointExist = isDewpointExist;
    }

    public boolean isUvindexExist() {
        return isUvindexExist;
    }

    public void setUvindexExist(boolean isUvindexExist) {
        this.isUvindexExist = isUvindexExist;
    }

    public boolean isPressureExist() {
        return isPressureExist;
    }

    public void setPressureExist(boolean isPressureExist) {
        this.isPressureExist = isPressureExist;
    }


    public boolean isLight() {
        return isLight;
    }

    public void setLight(boolean isLight) {
        this.isLight = isLight;
    }

    public String getFormattedTimeName() {
        return formattedTimeName;
    }

    public void setFormattedTimeName(String formattedTimeName) {
        this.formattedTimeName = formattedTimeName;
    }

    public String getFormattedWindDirection() {
        return formattedWindDirection;
    }

    public void setFormattedWindDirection(String formattedWindDirection) {
        this.formattedWindDirection = formattedWindDirection;
    }

    public String getFormattedSunRise() {
        return formattedSunRise;
    }

    public void setFormattedSunRise(String formattedSunRise) {
        this.formattedSunRise = formattedSunRise;
    }

    public String getFormattedSunSet() {
        return formattedSunSet;
    }

    public void setFormattedSunSet(String formattedSunSet) {
        this.formattedSunSet = formattedSunSet;
    }

    public String getWindSpeedUnit() {
        return windSpeedUnit;
    }

    public boolean isSunRiseExist() {
        return isSunRiseExist;
    }

    public void setSunRiseExist(boolean isSunRiseExist) {
        this.isSunRiseExist = isSunRiseExist;
    }

    public boolean isSunSetExist() {
        return isSunSetExist;
    }

    public void setSunSetExist(boolean isSunSetExist) {
        this.isSunSetExist = isSunSetExist;
    }

    public void setWindSpeedUnit(String windSpeedUnit) {
        this.windSpeedUnit = windSpeedUnit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRainAmount() {
        return rainAmount;
    }

    public void setRainAmount(double rainAmount) {
        this.rainAmount = rainAmount;
    }

    public String getRainProbility() {
        return rainProbility;
    }

    public void setRainProbility(String rainProbility) {
        this.rainProbility = rainProbility;
    }

    public double getSnowAmount() {
        return snowAmount;
    }

    public void setSnowAmount(double snowAmount) {
        this.snowAmount = snowAmount;
    }

    public String getSnowProbility() {
        return snowProbility;
    }

    public void setSnowProbility(String snowProbility) {
        this.snowProbility = snowProbility;
    }

    public String getThunderStormProbility() {
        return thunderStormProbility;
    }

    public void setThunderStormProbility(String thunderStormProbility) {
        this.thunderStormProbility = thunderStormProbility;
    }

    public long getNameMillis() {
        return nameMillis;
    }

    public void setNameMillis(long nameMillis) {
        this.nameMillis = nameMillis;
    }

    public double getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(double highTemp) {
        this.highTemp = highTemp;
    }

    public double getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(double lowTemp) {
        this.lowTemp = lowTemp;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getWindSpeed() {

        BigDecimal bd = new BigDecimal(windSpeed);
        double dWindSpeed = bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        // return Double.parseDouble(df.format(windSpeed));
        return dWindSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(double windDirection) {
        this.windDirection = windDirection;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getDewPoint() {
        return dewPoint;
    }

    public void setDewPoint(double dewPoint) {
        this.dewPoint = dewPoint;
    }

    public double getVisibility() {
        BigDecimal b = new BigDecimal(visibility);
        double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        // return Double.parseDouble(String.format("%.1f"));
        return f1;
    }

    public void setVisibility(double visibility) {
        this.visibility = visibility;
    }

    public String getUv() {
        return uv;
    }

    public void setUv(String uv) {
        this.uv = uv;
    }

    public double getPm25() {
        return pm25;
    }

    public void setPm25(double pm25) {
        this.pm25 = pm25;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getRealFeel() {
        return realFeel;
    }

    public void setRealFeel(double realFeel) {
        this.realFeel = realFeel;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getSunriseMillis() {
        return sunriseMillis;
    }

    public void setSunriseMillis(long sunriseMillis) {
        this.sunriseMillis = sunriseMillis;
    }

    public long getSunsetMillis() {
        return sunsetMillis;
    }

    public void setSunsetMillis(long sunsetMillis) {
        this.sunsetMillis = sunsetMillis;
    }

    @Override
    public String toString() {
        return "WeatherRawInfo [id=" + id + ", rainAmount=" + rainAmount + ", rainProbility=" + rainProbility + ", snowAmount=" + snowAmount
                + ", snowProbility=" + snowProbility + ", thunderStormProbility=" + thunderStormProbility + ", nameMillis=" + nameMillis + ", highTemp="
                + highTemp + ", lowTemp=" + lowTemp + ", condition=" + condition + ", windSpeed=" + windSpeed + ", windDirection=" + windDirection
                + ", humidity=" + humidity + ", pressure=" + pressure + ", dewPoint=" + dewPoint + ", visibility=" + visibility + ", sunriseMillis="
                + sunriseMillis + ", sunsetMillis=" + sunsetMillis + ", uv=" + uv + ", pm25=" + pm25 + ", temp=" + temp + ", realFeel=" + realFeel + ", icon="
                + icon + ", windSpeedUnit=" + windSpeedUnit + ", isSunRiseExist=" + isSunRiseExist + ", isSunSetExist=" + isSunSetExist
                + ", isVisibilityExist=" + isVisibilityExist + ", isDewpointExist=" + isDewpointExist + ", isUvindexExist=" + isUvindexExist
                + ", isPressureExist=" + isPressureExist + ", formattedSunRise=" + formattedSunRise + ", formattedSunSet=" + formattedSunSet
                + ", formattedWindDirection=" + formattedWindDirection + ", formattedTimeName=" + formattedTimeName + ", isLight=" + isLight + "]";
    }

    public boolean getIsVisibilityExist() {
        return this.isVisibilityExist;
    }

    public void setIsVisibilityExist(boolean isVisibilityExist) {
        this.isVisibilityExist = isVisibilityExist;
    }

    public boolean getIsUvindexExist() {
        return this.isUvindexExist;
    }

    public void setIsUvindexExist(boolean isUvindexExist) {
        this.isUvindexExist = isUvindexExist;
    }

    public boolean getIsPressureExist() {
        return this.isPressureExist;
    }

    public void setIsPressureExist(boolean isPressureExist) {
        this.isPressureExist = isPressureExist;
    }

    public boolean getIsDewpointExist() {
        return this.isDewpointExist;
    }

    public void setIsDewpointExist(boolean isDewpointExist) {
        this.isDewpointExist = isDewpointExist;
    }

    public boolean getIsLight() {
        return this.isLight;
    }

    public void setIsLight(boolean isLight) {
        this.isLight = isLight;
    }

    public int getInfoType() {
        return this.infoType;
    }

    public void setInfoType(int infoType) {
        this.infoType = infoType;
    }

    public Long getTableKeyId() {
        return this.tableKeyId;
    }

    public void setTableKeyId(Long tableKeyId) {
        this.tableKeyId = tableKeyId;
    }

    public long getDayTimeMillis() {
        return this.dayTimeMillis;
    }

    public void setDayTimeMillis(long dayTimeMillis) {
        this.dayTimeMillis = dayTimeMillis;
    }

    public String getFormattedMoonSet() {
        return this.formattedMoonSet;
    }

    public void setFormattedMoonSet(String formattedMoonSet) {
        this.formattedMoonSet = formattedMoonSet;
    }

    public String getFormattedMoonRise() {
        return this.formattedMoonRise;
    }

    public void setFormattedMoonRise(String formattedMoonRise) {
        this.formattedMoonRise = formattedMoonRise;
    }

    public boolean getIsMoonSetExist() {
        return this.isMoonSetExist;
    }

    public void setIsMoonSetExist(boolean isMoonSetExist) {
        this.isMoonSetExist = isMoonSetExist;
    }

    public boolean getIsMoonRiseExist() {
        return this.isMoonRiseExist;
    }

    public void setIsMoonRiseExist(boolean isMoonRiseExist) {
        this.isMoonRiseExist = isMoonRiseExist;
    }

    public long getMoonSetMillis() {
        return this.moonSetMillis;
    }

    public void setMoonSetMillis(long moonSetMillis) {
        this.moonSetMillis = moonSetMillis;
    }

    public long getMoonRiseMillis() {
        return this.moonRiseMillis;
    }

    public void setMoonRiseMillis(long moonRiseMillis) {
        this.moonRiseMillis = moonRiseMillis;
    }

    public boolean getIsSunSetExist() {
        return this.isSunSetExist;
    }

    public void setIsSunSetExist(boolean isSunSetExist) {
        this.isSunSetExist = isSunSetExist;
    }

    public boolean getIsSunRiseExist() {
        return this.isSunRiseExist;
    }

    public void setIsSunRiseExist(boolean isSunRiseExist) {
        this.isSunRiseExist = isSunRiseExist;
    }

}
