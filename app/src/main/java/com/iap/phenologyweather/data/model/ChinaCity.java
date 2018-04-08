package com.iap.phenologyweather.data.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by infolife on 2017/2/27.
 */

@Entity(nameInDb = "area")
public class ChinaCity {
    @Id(autoincrement = true)
    private Long _id;

    @Property(nameInDb = "id")
    private int id;
    @Property(nameInDb = "pid")
    private int pid;
    @Property(nameInDb = "name")
    private String name;
    @Property(nameInDb = "deep")
    private int deep;
    @Property(nameInDb = "lat")
    private double lat;
    @Property(nameInDb = "lng")
    private double lng;
    @Property(nameInDb = "cityid")
    private String cityId;
    @Generated(hash = 736519294)
    public ChinaCity(Long _id, int id, int pid, String name, int deep, double lat,
            double lng, String cityId) {
        this._id = _id;
        this.id = id;
        this.pid = pid;
        this.name = name;
        this.deep = deep;
        this.lat = lat;
        this.lng = lng;
        this.cityId = cityId;
    }
    @Generated(hash = 2047360224)
    public ChinaCity() {
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getPid() {
        return this.pid;
    }
    public void setPid(int pid) {
        this.pid = pid;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getDeep() {
        return this.deep;
    }
    public void setDeep(int deep) {
        this.deep = deep;
    }
    public double getLat() {
        return this.lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public double getLng() {
        return this.lng;
    }
    public void setLng(double lng) {
        this.lng = lng;
    }
    public String getCityId() {
        return this.cityId;
    }
    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

}
