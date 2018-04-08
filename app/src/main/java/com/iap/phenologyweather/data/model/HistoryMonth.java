package com.iap.phenologyweather.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by chenxueqing on 2017/4/21.
 */

public class HistoryMonth {
    /**
     * sid : CHM00054511
     * station : BEIJING
     * lat : 39.933
     * lng : 116.283
     * tmax_vmean_month : ["1.88","5.23","11.90","20.46","26.39","30.29","31.15","29.91","26.03","19.23","10.18","3.49"]
     * tmin_vmean_month : ["-8.08","-5.19","0.69","8.27","14.11","19.16","22.25","21.07","15.26","8.14","0.22","-5.69"]
     * prcp_days_in_month : ["1.50","2.49","3.12","4.45","5.92","9.62","12.73","10.71","6.90","4.59","3.08","1.50"]
     * prcp_in_month : ["1.50","2.49","3.12","4.45","5.92","9.62","12.73","10.71","6.90","4.59","3.08","1.50"]
     */

    @SerializedName("tmax_vmean_month")
    private List<String> tmax_vmean_month;
    @SerializedName("tmin_vmean_month")
    private List<String> tmin_vmean_month;
    @SerializedName("prcp_days_in_month")
    private List<String> prcp_days_in_month;
    @SerializedName("prcp_in_month")
    private List<String> prcp_in_month;

    public List<String> getTmax_vmean_month() {
        return tmax_vmean_month;
    }

    public void setTmax_vmean_month(List<String> tmax_vmean_month) {
        this.tmax_vmean_month = tmax_vmean_month;
    }

    public List<String> getTmin_vmean_month() {
        return tmin_vmean_month;
    }

    public void setTmin_vmean_month(List<String> tmin_vmean_month) {
        this.tmin_vmean_month = tmin_vmean_month;
    }

    public List<String> getPrcp_days_in_month() {
        return prcp_days_in_month;
    }

    public void setPrcp_days_in_month(List<String> prcp_days_in_month) {
        this.prcp_days_in_month = prcp_days_in_month;
    }

    public List<String> getPrcp_in_month() {
        return prcp_in_month;
    }

    public void setPrcp_in_month(List<String> prcp_in_month) {
        this.prcp_in_month = prcp_in_month;
    }

    @Override
    public String toString() {
        return "HistoryMonth{" +
                "tmax_vmean_month=" + tmax_vmean_month +
                ", tmin_vmean_month=" + tmin_vmean_month +
                ", prcp_days_in_month=" + prcp_days_in_month +
                ", prcp_in_month=" + prcp_in_month +
                '}';
    }
}
