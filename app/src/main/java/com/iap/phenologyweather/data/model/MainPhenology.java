package com.iap.phenologyweather.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenxueqing on 2017/4/19.
 */

@Entity(nameInDb = "main_pheno_table")
public class MainPhenology {
    /**
     * id : 1
     * type : spot
     * image : http://research.iap.ac.cn:8888/images/species/plants/桃花.jpg
     * title : 沈阳南湖公园桃花
     */

    @Id(autoincrement = true)
    @Expose(serialize = false, deserialize = false)
    private Long _id;

    @Property(nameInDb = "weather_data_id")
    @Expose(serialize = false, deserialize = false)
    private int weatherDataId;

    @Property(nameInDb = "pheno_id")
    @SerializedName("id")
    private int id;

    @Property(nameInDb = "type")
    @SerializedName("type")
    private String type;

    @Property(nameInDb = "img_url")
    @SerializedName("image")
    private String image;

    @Property(nameInDb = "title")
    @SerializedName("title")
    private String title;

    @Generated(hash = 745050871)
    public MainPhenology(Long _id, int weatherDataId, int id, String type,
            String image, String title) {
        this._id = _id;
        this.weatherDataId = weatherDataId;
        this.id = id;
        this.type = type;
        this.image = image;
        this.title = title;
    }

    @Generated(hash = 1838312473)
    public MainPhenology() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "MainPhenology{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", image='" + image + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public int getWeatherDataId() {
        return this.weatherDataId;
    }

    public void setWeatherDataId(int weatherDataId) {
        this.weatherDataId = weatherDataId;
    }
}
