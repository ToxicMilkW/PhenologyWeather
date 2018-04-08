package com.iap.phenologyweather.request.weather;

import java.util.ArrayList;
import java.util.List;


public class TempWeatherInfo {
    private String currentTemp = "";
    private String currentHumidity = "";
    private String currentWindSpeed = "";
    private String currentWindDirection = "";
    private String currentVisibility = "";
    private String currentPressure = "";
    private String currentDewPoint = "";
    private String currentWeatherSummary = "";
    private String currentWeatherIcon = "";
    private String currentUVindex = "";
    private String daylightOffset = "";
    private String gmtOffset = "";
    private String currentRealFeel = "";

    private boolean is_visibility_exist = false;
    private boolean is_press_exist = false;
    private boolean is_dewpoint_exist = false;
    private boolean is_uvindex_exist = false;
    private boolean is_sunrise_exist = false;
    private boolean is_sunset_exist = false;
    private boolean is_rainamount_exist = false;
    private ArrayList<String> dayNameList = new ArrayList<String>();
    private ArrayList<String> daySummaryList = new ArrayList<String>();
    private ArrayList<String> dayNameMillisList = new ArrayList<String>();
    private ArrayList<Long> hourNameMillisList = new ArrayList<Long>();
    private ArrayList<String> dayIconList = new ArrayList<String>();

    private ArrayList<String> dayHighTempList = new ArrayList<String>();
    private ArrayList<String> dayLowTempList = new ArrayList<String>();
    private ArrayList<String> dayRainAmountList = new ArrayList<String>();
    private ArrayList<String> daySnowAmountList = new ArrayList<String>();
    private ArrayList<String> dayThunderStormProbList = new ArrayList<String>();
    private ArrayList<String> dayRainProbList = new ArrayList<String>();

    private boolean is_dayrainamount_exist = true;
    private boolean is_dayrainprob_exist = false;
    private boolean is_daysnowamount_exist = true;
    private boolean is_daythundestormpro_exist = true;
    private boolean is_daypressure_exist = false;
    private boolean is_daydewpoint_exist = false;

    private ArrayList<String> hourTimeList = new ArrayList<String>();
    private ArrayList<String> hourSummaryList = new ArrayList<String>();
    private ArrayList<String> hourTempList = new ArrayList<String>();
    private ArrayList<String> hourIconList = new ArrayList<String>();
    public ArrayList<String> hourNameDateList = new ArrayList<String>();

    private boolean is_hourpressur_exist = false;
    private boolean is_hourdewpoint_exist = false;

    //==============================================================hour
    private ArrayList<String> hourHumidityList = new ArrayList<>();
    private ArrayList<String> hourWindSpeedList = new ArrayList<>();
    private ArrayList<String> hourWindDirectionList = new ArrayList<>();
    private ArrayList<String> hourReelFeelList = new ArrayList<>();
    private List<String> hourUVIndexList = new ArrayList<>();
    private List<String> hourRainProbList = new ArrayList<>();

    private boolean is_hourhumidity_exist = false;
    private boolean is_hourwindspeed_exist = true;
    private boolean is_hourwinddirection_exist = true;
    private boolean is_hourreelfeel_exist = true;
    private boolean is_hour_rain_prob_exist = true;
    //===============================================================day
    private ArrayList<String> dayHumidityList = new ArrayList<>();//detail
    private ArrayList<String> dayWindSpeedList = new ArrayList<>();
    private ArrayList<String> dayWindDirectionList = new ArrayList<>();
    //    private ArrayList<String> dayRealFeelList = new ArrayList<>();//detail
    private ArrayList<String> sunRiseList = new ArrayList<>();
    private ArrayList<String> sunSetList = new ArrayList<>();
    private ArrayList<String> moonRiseList = new ArrayList<>();
    private ArrayList<String> moonSetList = new ArrayList<>();
    private ArrayList<String> dayRealFeelHighList = new ArrayList<>();
    private ArrayList<String> dayRealFeelLowList = new ArrayList<>();
    private List<String> dayUVIndexList = new ArrayList<>();

    private boolean is_dayhumidity_exist = false;
    private boolean is_daywindspeed_exist = true;
    private boolean is_daywinddirection_exist = true;
    private boolean is_dayrealfeel_exist;
    private boolean is_day_sunrise_exist;
    private boolean is_day_sunset_exist;
    private boolean is_day_moonrise_exist;
    private boolean is_day_moonset_exist;

    /**
     * 警告类别
     */
    private String type;

    /**
     * 警告等级
     */
    private String grade;

    /**
     * 警告编号
     */
    private String number;

    /**
     * 警告的恶劣天气开始时间
     */
    private String startTime;

    /**
     * 警告的恶劣天气结束时间
     */
    private String endTime;

    /**
     * 警告的恶劣天气开始时间（UTC时间）
     */
    private String startTimeUTC;

    /**
     * 警告的恶劣天气结束时间（UTC时间）
     */
    private String endTimeUTC;

    /**
     * 发布机构
     */
    private String attr;

    /**
     * 美国国家气象服务使用的天气编号
     */
    private String numberNWS;

    /**
     * 警告内容
     */
    private String content;

    /**
     * 预警详情页的颜色
     */
    private int mBackgroundColor;

    /**
     * 是否显示预警
     */
    private boolean isShowWarning;

    private String releaseText;

    private String releaseTime;

    private String displayTime;

    public String getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(String displayTime) {
        this.displayTime = displayTime;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getReleaseText() {
        return releaseText;
    }

    public void setReleaseText(String releaseText) {
        this.releaseText = releaseText;
    }

    public boolean isShowWarning() {
        return isShowWarning;
    }

    public void setShowWarning(boolean showWarning) {
        isShowWarning = showWarning;
    }

    public int getmBackgroundColor() {
        return mBackgroundColor;
    }

    public void setmBackgroundColor(int mBackgroundColor) {
        this.mBackgroundColor = mBackgroundColor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTimeUTC() {
        return startTimeUTC;
    }

    public void setStartTimeUTC(String startTimeUTC) {
        this.startTimeUTC = startTimeUTC;
    }

    public String getEndTimeUTC() {
        return endTimeUTC;
    }

    public void setEndTimeUTC(String endTimeUTC) {
        this.endTimeUTC = endTimeUTC;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getNumberNWS() {
        return numberNWS;
    }

    public void setNumberNWS(String numberNWS) {
        this.numberNWS = numberNWS;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
//-----------------------------------------------
    public boolean is_dayrealfeel_exist() {
        return is_dayrealfeel_exist;
    }

    public void set_dayrealfeel_exist(boolean is_dayrealfeel_exist) {
        this.is_dayrealfeel_exist = is_dayrealfeel_exist;
    }

    //
    public boolean is_visibility_exist() {
        return is_visibility_exist;
    }

    public void set_visibility_exist(boolean is_visibility_exist) {
        this.is_visibility_exist = is_visibility_exist;
    }

    public boolean is_press_exist() {
        return is_press_exist;
    }

    public void set_press_exist(boolean is_press_exist) {
        this.is_press_exist = is_press_exist;
    }

    public boolean is_dewpoint_exist() {
        return is_dewpoint_exist;
    }

    public void set_dewpoint_exist(boolean is_dewpoint_exist) {
        this.is_dewpoint_exist = is_dewpoint_exist;
    }

    public boolean is_uvindex_exist() {
        return is_uvindex_exist;
    }

    public void set_uvindex_exist(boolean is_uvindex_exist) {
        this.is_uvindex_exist = is_uvindex_exist;
    }

    public boolean is_sunrise_exist() {
        return is_sunrise_exist;
    }

    public void set_sunrise_exist(boolean is_sunrise_exist) {
        this.is_sunrise_exist = is_sunrise_exist;
    }

    public boolean is_sunset_exist() {
        return is_sunset_exist;
    }

    public void set_sunset_exist(boolean is_sunset_exist) {
        this.is_sunset_exist = is_sunset_exist;
    }

    public boolean is_rainamount_exist() {
        return is_rainamount_exist;
    }

    public void set_rainamount_exist(boolean is_rainamount_exist) {
        this.is_rainamount_exist = is_rainamount_exist;
    }

    public String getDaylightOffset() {
        if (daylightOffset.length() > 0) {
            return daylightOffset;
        } else return "unknown";

    }

    public void setDaylightOffset(String daylightOffset) {
        if (daylightOffset.length() > 0) {
            this.daylightOffset = daylightOffset;
        }
    }

    public String getGmtOffset() {
        if (gmtOffset.length() > 0) {
            return gmtOffset;
        } else return "unknown";

    }

    public void setGmtOffset(String gmtOffset) {
        if (gmtOffset.length() > 0) {
            this.gmtOffset = gmtOffset;
        }

    }

    public int getHourItemSize() {
        return this.hourIconList.size();
    }

    public int getDayItemSize() {
        return this.dayIconList.size();
    }

    public String getCurrentRealFeel() {
        if (currentRealFeel.length() > 0) {
            return currentRealFeel;
        } else return "unknown";

    }

    public void setCurrentRealFeel(String currentRealFeel) {
        if (currentRealFeel.length() > 0) {
            this.currentRealFeel = currentRealFeel;
        }
    }


    public String getCurrentTemp() {
        if (currentTemp.length() > 0) {
            return currentTemp;
        } else return "unknown";

    }

    public void setCurrentTemp(String currentTemp) {
        if (currentTemp.length() > 0) {
            this.currentTemp = currentTemp;
        }
    }

    public String getCurrentHumidity() {
        if (currentHumidity.length() > 0) {
            return currentHumidity;
        } else return "unknow";

    }

    public void setCurrentHumidity(String currentHumidity) {
        if (currentHumidity.length() > 0) {
            this.currentHumidity = currentHumidity;
        }

    }

    public String getCurrentWindSpeed() {
        return currentWindSpeed;
    }

    public void setCurrentWindSpeed(String currentWindSpeed) {
        this.currentWindSpeed = currentWindSpeed;
    }

    public String getCurrentWeatherIcon() {
        return currentWeatherIcon;
    }

    public void setCurrentWeatherIcon(String currentWeatherIcon) {
        this.currentWeatherIcon = currentWeatherIcon;
    }

    //标记：Direction来源
    public String getCurrentWindDirection() {
        return currentWindDirection;
    }

    public void setCurrentWindDirection(String currentWindDirection) {
        this.currentWindDirection = currentWindDirection;
    }

    public String getCurrentVisibility() {
        return currentVisibility;
    }

    public void setCurrentVisibility(String currentVisibility) {
        this.currentVisibility = currentVisibility;
    }

    public String getCurrentPressure() {
        return currentPressure;
    }

    public void setCurrentPressure(String currentPressure) {
        this.currentPressure = currentPressure;
    }

    public String getCurrentDewPoint() {
        return currentDewPoint;
    }

    public void setCurrentDewPoint(String currentDewPoint) {
        this.currentDewPoint = currentDewPoint;
    }

    public String getCurrentWeatherSummary() {
        return currentWeatherSummary;
    }

    public void setCurrentWeatherSummary(String currentWeatherSummary) {
        this.currentWeatherSummary = currentWeatherSummary;
    }

    public String getCurrentUVindex() {
        return currentUVindex;
    }

    public void setCurrentUVindex(String currentUVindex) {
        this.currentUVindex = currentUVindex;
    }

    public String getDayNameMillisList(int index) {
        if (this.dayNameMillisList.size() > index) {
            return this.dayNameMillisList.get(index);
        } else
            return "--";
    }

    public void addDayNameMillisList(String data) {
        if (data.length() > 1)
            this.dayNameMillisList.add(data);
    }

    public long getHourNameMillisList(int index) {
        if (this.hourNameMillisList.size() > index) {
            return this.hourNameMillisList.get(index);
        } else
            return 0;
    }

    public void addHourNameMillisList(long data) {
        if (data > 0)
            this.hourNameMillisList.add(data);
    }

    public String getDayTimeList(int index) {
        if (this.dayNameList.size() > index) {
            return this.dayNameList.get(index);
        } else
            return "--";
    }

    public void addDayNameList(String data) {
        if (data.length() > 1)

            this.dayNameList.add(data);
    }

    public String getDaySummaryList(int index) {
        if (daySummaryList.size() > index) {
            return this.daySummaryList.get(index);
        } else
            return "--";

    }

    public void addDaySummaryList(String data) {
        if (data.length() > 0) {
            this.daySummaryList.add(data);
        }
    }

    public String getDayHighTempList(int index) {
        if (dayHighTempList.size() > index) {
            return this.dayHighTempList.get(index);
        } else
            return "--";
    }

    public void addDayHighTempList(String data) {
        if (data.length() > 0)

            this.dayHighTempList.add(data);
    }

    public String getDayLowTempList(int index) {
        if (dayLowTempList.size() > index) {
            return this.dayLowTempList.get(index);
        } else
            return "--";
    }

    public void addDayLowTempList(String data) {
        if (data.length() > 0)

            this.dayLowTempList.add(data);
    }

    public String getSunSetList(int index) {
        if (sunSetList.size() > index) {
            return sunSetList.get(index);
        } else
            return "--";
    }

    public void addSunSetList(String data) {
        if (data.length() > 1)

            this.sunSetList.add(data);
    }

    public String getSunRiseList(int index) {
        if (sunRiseList.size() > index) {
            return sunRiseList.get(index);
        } else
            return "--";
    }

    public void addSunRiseList(String data) {
        if (data.length() > 1)
            this.sunRiseList.add(data);
    }
    public String getMoonSetList(int index) {
        if (moonSetList.size() > index) {
            return moonSetList.get(index);
        } else
            return "--";
    }

    public void addMoonSetList(String data) {
        if (data.length() > 1)

            this.moonSetList.add(data);
    }

    public String getMoonRiseList(int index) {
        if (moonRiseList.size() > index) {
            return moonRiseList.get(index);
        } else
            return "--";
    }

    public void addMoonRiseList(String data) {
        if (data.length() > 1)
            this.moonRiseList.add(data);
    }

    public String getRainAmountList(int index) {
        if (dayRainAmountList.size() > index) {
            return dayRainAmountList.get(index);
        } else
            return "--";
    }

    public void addRainAmountList(String data) {
        if (data.length() > 0) {
            this.dayRainAmountList.add(data);
        }
    }

    public String getHourNameDateList(int index) {
        if (hourNameDateList.size() > index) {
            return hourNameDateList.get(index);
        } else
            return "--";
    }

    public void addHourNameDateList(String data) {
        if (data.length() > 1)
            this.hourNameDateList.add(data);
    }

    public String getHourTimeList(int index) {
        if (hourTimeList.size() > index) {
            return hourTimeList.get(index);
        } else
            return "--";
    }

    public void addHourTimeList(String data) {
        if (data.length() > 1)

            this.hourTimeList.add(data);
    }

    public String getHourSummaryList(int index) {
        if (hourSummaryList.size() > index) {
            return hourSummaryList.get(index);
        } else
            return "--";
    }

    public void addHourSummaryList(String data) {
        if (data.length() > 0) {
            this.hourSummaryList.add(data);
        }

    }

    public String getHourTempList(int index) {
        if (hourTempList.size() > index) {
            return hourTempList.get(index);
        } else
            return "--";
    }

    public void addHourTempList(String data) {
        if (data.length() > 0)

            this.hourTempList.add(data);
    }

    public String getDayIconList(int index) {
        if (dayIconList.size() > index) {
            return dayIconList.get(index);
        } else
            return "--";

    }

    public void addDayIconList(String data) {
        if (data.length() > 0) {
            this.dayIconList.add(data);
        }

    }

    public String getHourIconList(int index) {
        if (hourIconList.size() > index) {
            return hourIconList.get(index);
        } else
            return "--";
    }

    public void addHourIconList(String data) {
        if (data.length() > 0) {
            this.hourIconList.add(data);
        }
    }

    public String getDayWindSpeedList(int index) {
        if (dayWindSpeedList.size() > index) {
            return dayWindSpeedList.get(index);
        } else {
            return "--";
        }
    }

    public void addDayWindSpeedList(String data) {
        if (data.length() > 0) {
            this.dayWindSpeedList.add(data);
        }
    }

    public String getDayWindDirectionList(int index) {
        if (dayWindDirectionList.size() > index) {
            return dayWindDirectionList.get(index);
        } else return "--";
    }

    public void addDayWindDirectionList(String data) {
        if (data.length() > 0) {
            this.dayWindDirectionList.add(data);
        }
    }

    public String getDaySnowAmountList(int index) {
        if (daySnowAmountList.size() > index) {
            return daySnowAmountList.get(index);
        } else return "--";

    }

    public void addDaySnowAmountList(String data) {
        if (data.length() > 0) {
            this.daySnowAmountList.add(data);
        }
    }

    public String getDayThunderStormProbList(int index) {
        if (dayThunderStormProbList.size() > index) {
            return dayThunderStormProbList.get(index);
        } else return "--";
    }

    public String getDayRainProbList(int index) {
        if (dayRainProbList.size() > index) {
            return dayRainProbList.get(index);
        } else {
            return "--";
        }
    }

    public void addDayUVIndex(String data) {
        if (null != data && 0 < data.length()) {
            this.dayUVIndexList.add(data);
        }
    }

    public String getDayUVIndex(int index) {
        if (index >= 0 && dayUVIndexList.size() > index) {
            return dayUVIndexList.get(index);
        } else {
            return "--";
        }
    }

    public void addDayThunderStormProbList(String data) {
        if (data.length() > 0) {
            this.dayThunderStormProbList.add(data);
        }
    }

    public void addDayRainProbList(String data) {
        if (data.length() > 0) {
            this.dayRainProbList.add(data);
        }
    }

    public String getHourWindSpeedList(int index) {
        if (hourWindSpeedList.size() > index) {
            return hourWindSpeedList.get(index);
        } else return "--";
    }

    public void addHourWindSpeedList(String data) {
        if (data.length() > 0) {
            this.hourWindSpeedList.add(data);
        }
    }

    public void addHourUVIndex(String data) {
        if (null != data && data.length() > 0) {
            this.hourUVIndexList.add(data);
        }
    }

    public void addHourRainProbList(String data) {
        if (null != data && data.length() > 0) {
            this.hourRainProbList.add(data);
        }
    }

    public String getHourRainProbList(int index) {
        if (index >= 0 && index < hourRainProbList.size()) {
            return hourRainProbList.get(index);
        } else {
            return "--";
        }
    }

    public String getHourUVIndex(int index) {
        if (index >= 0 && index < hourUVIndexList.size()) {
            return hourUVIndexList.get(index);
        } else {
            return "--";
        }
    }

    public String getHourWindDirectionList(int index) {
        if (hourWindDirectionList.size() > index) {
            return hourWindDirectionList.get(index);
        } else return "--";
    }

    public void addHourWindDirectionList(String data) {
        if (data.length() > 0) {
            this.hourWindDirectionList.add(data);
        }
    }

    public String getHourReelFeelList(int index) {
        if (hourReelFeelList.size() > index) {
            return hourReelFeelList.get(index);
        } else return "--";
    }

    public void addHourReelFeelList(String data) {
        if (data.length() > 0) {
            this.hourReelFeelList.add(data);
        }
    }

    public String getHourHumidityList(int index) {
        if (hourHumidityList.size() > index) {
            return hourHumidityList.get(index);
        } else return "--";
    }

    public void addHourHumidityList(String data) {
        if (data.length() > 0) {
            this.hourHumidityList.add(data);
        }
    }

    public String getDayHumidityList(int index) {
        if (dayHumidityList.size() > index) {
            return dayHumidityList.get(index);
        } else return "--";
    }

    public void addDayHumidityList(String data) {
        if (data.length() > 0) {
            this.dayHumidityList.add(data);
        }
    }

    public ArrayList<String> getDayRealFeelHighList() {
        return dayRealFeelHighList;
    }

    public void setDayRealFeelHighList(ArrayList<String> dayRealFeelHighList) {
        this.dayRealFeelHighList = dayRealFeelHighList;
    }

    public ArrayList<String> getDayRealFeelLowList() {
        return dayRealFeelLowList;
    }

    public void setDayRealFeelLowList(ArrayList<String> dayRealFeelLowList) {
        this.dayRealFeelLowList = dayRealFeelLowList;
    }

    public boolean is_daywindspeed_exist() {
        return is_daywindspeed_exist;
    }

    public void set_daywindspeed_exist(boolean is_daywindspeed_exist) {
        this.is_daywindspeed_exist = is_daywindspeed_exist;
    }

    public boolean is_daywinddirection_exist() {
        return is_daywinddirection_exist;
    }

    public void set_daywinddirection_exist(boolean is_daywinddirection_exist) {
        this.is_daywinddirection_exist = is_daywinddirection_exist;
    }

    public boolean is_dayrainamount_exist() {
        return is_dayrainamount_exist;
    }

    public void set_dayrainamount_exist(boolean is_dayrainamount_exist) {
        this.is_dayrainamount_exist = is_dayrainamount_exist;
    }

    public boolean is_daysnowamount_exist() {
        return is_daysnowamount_exist;
    }

    public void set_daysnowamount_exist(boolean is_daysnowamount_exist) {
        this.is_daysnowamount_exist = is_daysnowamount_exist;
    }

    public boolean is_dayhumidity_exist() {
        return is_dayhumidity_exist;
    }

    public void set_dayhumidity_exist(boolean is_dayhumidity_exist) {
        this.is_dayhumidity_exist = is_dayhumidity_exist;
    }

    public boolean is_daypressure_exist() {
        return is_daypressure_exist;
    }

    public void set_daypressure_exist(boolean is_daypressure_exist) {
        this.is_daypressure_exist = is_daypressure_exist;
    }

    public boolean is_daydewpoint_exist() {
        return is_daydewpoint_exist;
    }

    public void set_daydewpoint_exist(boolean is_daydewpoint_exist) {
        this.is_daydewpoint_exist = is_daydewpoint_exist;
    }

    public boolean is_daythundestormpro_exist() {
        return is_daythundestormpro_exist;
    }

    public void set_daythundestormpro_exist(boolean is_dayrainpro_exist) {
        this.is_daythundestormpro_exist = is_dayrainpro_exist;
    }
    public boolean is_dayrainpro_exist() {
        return is_dayrainprob_exist;
    }

    public void set_dayrainpro_exist(boolean is_dayrainpro_exist) {
        this.is_dayrainprob_exist = is_dayrainpro_exist;
    }

    public boolean is_hourwindspeed_exist() {
        return is_hourwindspeed_exist;
    }

    public void set_hourwindspeed_exist(boolean is_hourwindspeed_exist) {
        this.is_hourwindspeed_exist = is_hourwindspeed_exist;
    }

    public boolean is_hourwinddirection_exist() {
        return is_hourwinddirection_exist;
    }

    public void set_hourwinddirection_exist(boolean is_hourwinddirection_exist) {
        this.is_hourwinddirection_exist = is_hourwinddirection_exist;
    }

    public boolean is_hourreelfeel_exist() {
        return is_hourreelfeel_exist;
    }

    public void set_hourreelfeel_exist(boolean is_hourreelfeel_exist) {
        this.is_hourreelfeel_exist = is_hourreelfeel_exist;
    }

    public boolean is_hour_rain_prob_exist() {
        return is_hour_rain_prob_exist;
    }

    public void set_hour_rain_prob_exist(boolean is_hour_rain_prob_exist) {
        this.is_hour_rain_prob_exist = is_hour_rain_prob_exist;
    }

    public boolean is_hourhumidity_exist() {
        return is_hourhumidity_exist;
    }

    public void set_hourhumidity_exist(boolean is_hourhumidity_exist) {
        this.is_hourhumidity_exist = is_hourhumidity_exist;
    }

    public boolean is_hourpressur_exist() {
        return is_hourpressur_exist;
    }

    public void set_hourpressur_exist(boolean is_hourpressur_exist) {
        this.is_hourpressur_exist = is_hourpressur_exist;
    }

    public boolean is_hourdewpoint_exist() {
        return is_hourdewpoint_exist;
    }

    public void set_hourdewpoint_exist(boolean is_hourdewpoint_exist) {
        this.is_hourdewpoint_exist = is_hourdewpoint_exist;
    }

    public boolean is_day_sunrise_exist() {
        return (sunRiseList != null && sunRiseList.size() > 1);
    }

    public void set_day_sunrise_exist(boolean is_day_sunrise_exist) {
        this.is_day_sunrise_exist = is_day_sunrise_exist;
    }

    public boolean is_day_sunset_exist() {
        return (sunSetList != null && sunSetList.size() > 1);
    }

    public void set_day_sunset_exist(boolean is_day_sunset_exist) {
        this.is_day_sunset_exist = is_day_sunset_exist;
    }

    public boolean is_day_moonrise_exist() {
        return (moonRiseList != null && moonRiseList.size() > 1);
    }

    public void set_day_moonrise_exist(boolean is_day_moonrise_exist) {
        this.is_day_moonrise_exist = is_day_moonrise_exist;
    }

    public boolean is_day_moonset_exist() {
        return (moonSetList != null && moonSetList.size() > 1);
    }

    public void set_day_moonset_exist(boolean is_day_moonset_exist) {
        this.is_day_moonset_exist = is_day_moonset_exist;
    }

    public void addDayRealFeelHigh(String realFeelHigh) {
        this.dayRealFeelHighList.add(realFeelHigh);
    }

    public void addDayRealFeelLow(String realFeelLow) {
        this.dayRealFeelLowList.add(realFeelLow);
    }

    public String getDayRealFeelHigh(int index) {
        if (index >= 0 && index < dayRealFeelHighList.size()) {
            return dayRealFeelHighList.get(index);
        } else {
            return "9999";
        }
    }

    public String getDayRealFeelLow(int index) {
        if (index >= 0 && index < dayRealFeelLowList.size()) {
            return dayRealFeelLowList.get(index);
        } else {
            return "9999";
        }
    }

    @Override
    public String toString() {
        return "TempWeatherInfo{" +
                "currentTemp='" + currentTemp + '\'' +
                ", currentHumidity='" + currentHumidity + '\'' +
                ", currentWindSpeed='" + currentWindSpeed + '\'' +
                ", currentWindDirection='" + currentWindDirection + '\'' +
                ", currentVisibility='" + currentVisibility + '\'' +
                ", currentPressure='" + currentPressure + '\'' +
                ", currentDewPoint='" + currentDewPoint + '\'' +
                ", currentWeatherSummary='" + currentWeatherSummary + '\'' +
                ", currentWeatherIcon='" + currentWeatherIcon + '\'' +
                ", currentUVindex='" + currentUVindex + '\'' +
                ", daylightOffset='" + daylightOffset + '\'' +
                ", gmtOffset='" + gmtOffset + '\'' +
                ", currentRealFeel='" + currentRealFeel + '\'' +
                ", is_visibility_exist=" + is_visibility_exist +
                ", is_press_exist=" + is_press_exist +
                ", is_dewpoint_exist=" + is_dewpoint_exist +
                ", is_uvindex_exist=" + is_uvindex_exist +
                ", is_sunrise_exist=" + is_sunrise_exist +
                ", is_sunset_exist=" + is_sunset_exist +
                ", is_rainamount_exist=" + is_rainamount_exist +
                ", dayNameList=" + dayNameList +
                ", daySummaryList=" + daySummaryList +
                ", dayNameMillisList=" + dayNameMillisList +
                ", hourNameMillisList=" + hourNameMillisList +
                ", dayIconList=" + dayIconList +
                ", dayHighTempList=" + dayHighTempList +
                ", dayLowTempList=" + dayLowTempList +
                ", dayRainAmountList=" + dayRainAmountList +
                ", daySnowAmountList=" + daySnowAmountList +
                ", dayThunderStormProbList=" + dayThunderStormProbList +
                ", is_dayrainamount_exist=" + is_dayrainamount_exist +
                ", is_daysnowamount_exist=" + is_daysnowamount_exist +
                ", is_daythundestormpro_exist=" + is_daythundestormpro_exist +
                ", is_daypressure_exist=" + is_daypressure_exist +
                ", is_daydewpoint_exist=" + is_daydewpoint_exist +
                ", hourTimeList=" + hourTimeList +
                ", hourSummaryList=" + hourSummaryList +
                ", hourTempList=" + hourTempList +
                ", hourIconList=" + hourIconList +
                ", hourNameDateList=" + hourNameDateList +
                ", is_hourpressur_exist=" + is_hourpressur_exist +
                ", is_hourdewpoint_exist=" + is_hourdewpoint_exist +
                ", hourHumidityList=" + hourHumidityList +
                ", hourWindSpeedList=" + hourWindSpeedList +
                ", hourWindDirectionList=" + hourWindDirectionList +
                ", hourReelFeelList=" + hourReelFeelList +
                ", hourUVIndexList=" + hourUVIndexList +
                ", is_hourhumidity_exist=" + is_hourhumidity_exist +
                ", is_hourwindspeed_exist=" + is_hourwindspeed_exist +
                ", is_hourwinddirection_exist=" + is_hourwinddirection_exist +
                ", is_hourreelfeel_exist=" + is_hourreelfeel_exist +
                ", dayHumidityList=" + dayHumidityList +
                ", dayWindSpeedList=" + dayWindSpeedList +
                ", dayWindDirectionList=" + dayWindDirectionList +
                ", sunRiseList=" + sunRiseList +
                ", sunSetList=" + sunSetList +
                ", dayRealFeelHighList=" + dayRealFeelHighList +
                ", dayRealFeelLowList=" + dayRealFeelLowList +
                ", dayUVIndexList=" + dayUVIndexList +
                ", is_dayhumidity_exist=" + is_dayhumidity_exist +
                ", is_daywindspeed_exist=" + is_daywindspeed_exist +
                ", is_daywinddirection_exist=" + is_daywinddirection_exist +
                ", is_dayrealfeel_exist=" + is_dayrealfeel_exist +
                ", is_day_sunrise_exist=" + is_day_sunrise_exist +
                ", is_day_sunset_exist=" + is_day_sunset_exist +
                '}';
    }
}
