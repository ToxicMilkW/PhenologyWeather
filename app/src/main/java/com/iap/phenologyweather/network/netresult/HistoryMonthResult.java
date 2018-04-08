package com.iap.phenologyweather.network.netresult;

import com.google.gson.annotations.SerializedName;
import com.iap.phenologyweather.data.model.HistoryMonth;

import java.util.List;

/**
 * Created by chenxueqing on 2017/4/21.
 */

public class HistoryMonthResult {

    /**
     * status : ok
     * data : {"sid":"CHM00054511","station":"BEIJING","lat":"39.933","lng":"116.283","tmax_vmean_month":["1.88","5.23","11.90","20.46","26.39","30.29","31.15","29.91","26.03","19.23","10.18","3.49"],"tmin_vmean_month":["-8.08","-5.19","0.69","8.27","14.11","19.16","22.25","21.07","15.26","8.14","0.22","-5.69"],"prcp_days_in_month":["1.50","2.49","3.12","4.45","5.92","9.62","12.73","10.71","6.90","4.59","3.08","1.50"],"prcp_in_month":["1.50","2.49","3.12","4.45","5.92","9.62","12.73","10.71","6.90","4.59","3.08","1.50"]}
     */

    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private HistoryMonth data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HistoryMonth getData() {
        return data;
    }

    public void setData(HistoryMonth data) {
        this.data = data;
    }

}
