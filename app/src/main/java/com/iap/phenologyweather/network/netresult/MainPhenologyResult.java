package com.iap.phenologyweather.network.netresult;

import com.google.gson.annotations.SerializedName;
import com.iap.phenologyweather.data.model.MainPhenology;

import java.util.List;

/**
 * Created by cyhenxueqing on 2017/4/19.
 */

public class MainPhenologyResult {

    /**
     * status : ok
     * count : 2
     * mainPhenologies : [{"id":1,"type":"spot","image":"http://research.iap.ac.cn:8888/images/species/plants/桃花.jpg","title":"沈阳南湖公园桃花"},{"id":2,"type":"landscape","image":"http://research.iap.ac.cn:8888/images/species/plants/杜鹃花.jpg","title":"旅顺龙王塘樱花"}]
     */
    @SerializedName("status")
    private String status;
    @SerializedName("count")
    private int count;
    @SerializedName("activities")
    private List<MainPhenology> mainPhenologies;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<MainPhenology> getMainPhenologies() {
        return mainPhenologies;
    }

    public void setMainPhenologies(List<MainPhenology> mainPhenologies) {
        this.mainPhenologies = mainPhenologies;
    }

    @Override
    public String toString() {
        return "MainPhenologyResult{" +
                "status='" + status + '\'' +
                ", count=" + count +
                ", mainPhenologies=" + mainPhenologies +
                '}';
    }
}
