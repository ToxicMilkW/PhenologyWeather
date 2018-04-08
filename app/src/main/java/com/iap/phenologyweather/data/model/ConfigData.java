package com.iap.phenologyweather.data.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

@Entity(nameInDb = "config_table")
public class ConfigData {
    @Id(autoincrement = false)
    private long id;
    @Property(nameInDb = "clock_formate")
    private boolean isClock24Formate;
    @Property(nameInDb = "data_formate")
    private int dateFormate;
    @Property(nameInDb = "temp_unit")
    private int tempUnit;
    @Property(nameInDb = "temp_unit_name") @NotNull
    private String tempUnitName;
    @Property(nameInDb = "distance_unit")
    private int distanceUnit;
    @Property(nameInDb = "distance_unit_name") @NotNull
    private String distanceUnitName;
    @Property(nameInDb = "speed_unit")
    private int speedUnit;
    @Property(nameInDb = "speed_unit_name") @NotNull
    private String speedUnitName;
    @Property(nameInDb = "pressure_unit")
    private int pressureUnit;
    @Property(nameInDb = "pressure_unit_name") @NotNull
    private String pressureUnitName;
    @Property(nameInDb = "is_use_world_clock")
    private boolean isUseWorldClock;
    @Property(nameInDb = "is_locate_time")
    private boolean isLocalTime;


    @Generated(hash = 1757574837)
    public ConfigData(long id, boolean isClock24Formate, int dateFormate,
                      int tempUnit, @NotNull String tempUnitName, int distanceUnit,
                      @NotNull String distanceUnitName, int speedUnit,
                      @NotNull String speedUnitName, int pressureUnit,
                      @NotNull String pressureUnitName, boolean isUseWorldClock,
                      boolean isLocalTime) {
        this.id = id;
        this.isClock24Formate = isClock24Formate;
        this.dateFormate = dateFormate;
        this.tempUnit = tempUnit;
        this.tempUnitName = tempUnitName;
        this.distanceUnit = distanceUnit;
        this.distanceUnitName = distanceUnitName;
        this.speedUnit = speedUnit;
        this.speedUnitName = speedUnitName;
        this.pressureUnit = pressureUnit;
        this.pressureUnitName = pressureUnitName;
        this.isUseWorldClock = isUseWorldClock;
        this.isLocalTime = isLocalTime;
    }

    @Generated(hash = 2100648308)
    public ConfigData() {
    }


    public boolean isLocalTime() {
        return isLocalTime;
    }

    public void setLocalTime(boolean isLocalTime) {
        this.isLocalTime = isLocalTime;
    }

    public boolean isUseWorldClock() {
        return isUseWorldClock;
    }

    public void setUseWorldClock(boolean isUseWorldClock) {
        this.isUseWorldClock = isUseWorldClock;
    }

    public boolean isClock24Formate() {
        return isClock24Formate;
    }

    public void setClock24Formate(boolean clockFormate) {
        this.isClock24Formate = clockFormate;
    }

    public int getDateFormate() {
        return dateFormate;
    }

    public void setDateFormate(int dateFormate) {
        this.dateFormate = dateFormate;
    }

    public int getTempUnit() {
        return tempUnit;
    }

    public void setTempUnit(int tempUnit) {
        this.tempUnit = tempUnit;
    }

    public String getTempUnitName() {
        return tempUnitName;
    }

    public void setTempUnitName(String tempUnitName) {
        this.tempUnitName = tempUnitName;
    }

    public int getSpeedUnit() {
        return speedUnit;
    }

    public void setSpeedUnit(int speedUnit) {
        this.speedUnit = speedUnit;
    }

    public String getSpeedUnitName() {
        return speedUnitName;
    }

    public void setSpeedUnitName(String speedUnitName) {
        this.speedUnitName = speedUnitName;
    }

    public int getPressureUnit() {
        return pressureUnit;
    }

    public void setPressureUnit(int pressureUnit) {
        this.pressureUnit = pressureUnit;
    }

    public String getPressureUnitName() {
        return pressureUnitName;
    }

    public void setPressureUnitName(String pressureUnitName) {
        this.pressureUnitName = pressureUnitName;
    }

    public int getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(int distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

    public String getDistanceUnitName() {
        return distanceUnitName;
    }

    public void setDistanceUnitName(String distanceUnitName) {
        this.distanceUnitName = distanceUnitName;
    }

    @Override
    public String toString() {
        return "ConfigData [isLocalTime=" + isLocalTime + ", isUseWorldClock="
                + isUseWorldClock + ", isClock24Formate=" + isClock24Formate
                + ", dateFormate=" + dateFormate + ", tempUnit=" + tempUnit
                + ", tempUnitName=" + tempUnitName + ", speedUnit=" + speedUnit
                + ", speedUnitName=" + speedUnitName + ", pressureUnit="
                + pressureUnit + ", pressureUnitName=" + pressureUnitName
                + ", distanceUnit=" + distanceUnit + ", distanceUnitName="
                + distanceUnitName + "]";
    }

    public boolean getIsLocalTime() {
        return this.isLocalTime;
    }

    public void setIsLocalTime(boolean isLocalTime) {
        this.isLocalTime = isLocalTime;
    }

    public boolean getIsUseWorldClock() {
        return this.isUseWorldClock;
    }

    public void setIsUseWorldClock(boolean isUseWorldClock) {
        this.isUseWorldClock = isUseWorldClock;
    }

    public boolean getIsClock24Formate() {
        return this.isClock24Formate;
    }

    public void setIsClock24Formate(boolean isClock24Formate) {
        this.isClock24Formate = isClock24Formate;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
