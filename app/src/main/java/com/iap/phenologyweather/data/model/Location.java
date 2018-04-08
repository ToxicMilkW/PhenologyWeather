package com.iap.phenologyweather.data.model;

import android.content.Context;
import android.text.TextUtils;

import com.iap.phenologyweather.R;
import com.iap.phenologyweather.utils.AmberSdkConstants;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;


@Entity(nameInDb = "location_table")
public class Location {
    @Id(autoincrement = true)
    private Long id;


    @Property(nameInDb = "level1") @NotNull
    private String level0 = "";
    @Property(nameInDb = "level2") @NotNull
    private String level1 = "";
    @Property(nameInDb = "level3") @NotNull
    private String level2 = "";
    @Property(nameInDb = "level4")
    private String level3;
    @Property(nameInDb = "lat")
    private double lat;
    @Property(nameInDb = "lon")
    private double lon;
    @Property(nameInDb = "formatted_addr")
    private String formattedAddr;
    @Property(nameInDb = "lable")
    private String lable;
    @Property(nameInDb = "gmt_offset")
    private long gmtOffset;
    @Property(nameInDb = "daylight_offset")
    private long dayLightOffset;

    @Transient
    private boolean isCurrentLocation;
    @Transient
    public static final String TAG = "Location";


    @Generated(hash = 2049383898)
    public Location(Long id, @NotNull String level0, @NotNull String level1,
                    @NotNull String level2, String level3, double lat, double lon,
                    String formattedAddr, String lable, long gmtOffset, long dayLightOffset) {
        this.id = id;
        this.level0 = level0;
        this.level1 = level1;
        this.level2 = level2;
        this.level3 = level3;
        this.lat = lat;
        this.lon = lon;
        this.formattedAddr = formattedAddr;
        this.lable = lable;
        this.gmtOffset = gmtOffset;
        this.dayLightOffset = dayLightOffset;
    }

    @Generated(hash = 375979639)
    public Location() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isCurrentLocation() {
        return isCurrentLocation;
    }

    public void setCurrentLocation(boolean isCurrentLocation) {
        this.isCurrentLocation = isCurrentLocation;
    }

    public boolean isAddressInited() {
        return !(level1.equals(AmberSdkConstants.CITY_NAME_NOTSET) &&
                level2.equals(AmberSdkConstants.CITY_NAME_NOTSET));
    }

    public String getAddress(String defaultValue) {
        String[] level = new String[]{level3, level2, level1, level0};
        for (int i = 0; i < level.length; i++) {
            if (!TextUtils.isEmpty(level[i])) {
                return level[i];
            }
        }
        return defaultValue;
    }

    /**
     * 判断level1和level2是否为Current Loaction或My Place
     *
     * @param context
     * @return
     */
    public boolean isAddressCurrentLocation(Context context) {
        if (context.getResources().getString(R.string.current_location).equals(level1) ||
                context.getResources().getString(R.string.current_location).equals(level2)) {
            return true;
        } else {
            return false;
        }
    }


    public String getAddressReverse(String defaultValue) {
        String[] level = new String[]{level3, level2, level1, level0};
        for (int i = 0; i < level.length - 1; i++) {
            if (!TextUtils.isEmpty(level[i])) {
                if (i < level.length - 1) {
                    return level[i] + ", " + level[i + 1];
                } else {
                    return level[i];
                }
            }
        }
        return defaultValue;
    }

    public String getLevel0() {
        return level0;
    }

    public void setLevel0(String level0) {
        this.level0 = level0;
    }

    public String getLevel1() {
        return level1;
    }

    public void setLevel1(String level1) {
        this.level1 = level1;
    }

    public String getLevel2() {
        return level2;
    }

    public void setLevel2(String level2) {
        this.level2 = level2;
    }

    public String getLevel3() {
        return level3;
    }

    public void setLevel3(String level3) {
        this.level3 = level3;
    }

    public boolean isLocationInited() {
        return !(lat == 0 && lon == 0);
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getFormattedAddr() {
        return formattedAddr;
    }

    public void setFormattedAddr(String formattedAddr) {
        this.formattedAddr = formattedAddr;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public long getDayLightOffset() {
        return dayLightOffset;
    }

    public void setDayLightOffset(long dayLightOffset) {
        this.dayLightOffset = dayLightOffset;
    }

    public long getGmtOffset() {
        return gmtOffset;
    }

    public void setGmtOffset(long gmtOffset) {
        this.gmtOffset = gmtOffset;
    }

    @Override
    public boolean equals(Object o) {
        if (null == o) {
            return false;
        }
        return this.getLevel2().equals(((Location)o).getLevel2());
    }

    @Override
    public String toString() {
        return "Location [id=" + id + ", isCurrentLocation="
                + isCurrentLocation + ", level0=" + level0 + ", level1="
                + level1 + ", level2=" + level2 + ", level3=" + level3
                + ", lat=" + lat + ", lon=" + lon + ", formattedAddr="
                + formattedAddr + ", lable=" + lable + ", dayLightOffset="
                + dayLightOffset + ", gmtOffset=" + gmtOffset + "]";
    }


}
