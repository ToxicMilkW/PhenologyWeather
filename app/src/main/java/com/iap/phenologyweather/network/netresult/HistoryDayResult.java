package com.iap.phenologyweather.network.netresult;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chenxueqing on 2017/4/21.
 */

public class HistoryDayResult {


    /**
     * status : ok
     * data : {"sid":"CHM00054511","station":"BEIJING","lat":"39.933","lng":"116.283","date":"20170415","tmax":"25","tmin":"10","prcp":"1.5","prcp_posibility":"0.2"}
     */

    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private HistoryDay data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HistoryDay getData() {
        return data;
    }

    public void setData(HistoryDay data) {
        this.data = data;
    }

    public static class HistoryDay {
        /**
         * sid : CHM00054511
         * station : BEIJING
         * lat : 39.933
         * lng : 116.283
         * date : 20170415
         * tmax : 25
         * tmin : 10
         * prcp : 1.5
         * prcp_posibility : 0.2
         */

        @SerializedName("tmax")
        private String tmax;
        @SerializedName("tmin")
        private String tmin;
        @SerializedName("prcp")
        private String prcp;
        @SerializedName("prcp_posibility")
        private String prcp_posibility;

        public String getTmax() {
            return tmax;
        }

        public void setTmax(String tmax) {
            this.tmax = tmax;
        }

        public String getTmin() {
            return tmin;
        }

        public void setTmin(String tmin) {
            this.tmin = tmin;
        }

        public String getPrcp() {
            return prcp;
        }

        public void setPrcp(String prcp) {
            this.prcp = prcp;
        }

        public String getPrcp_posibility() {
            return prcp_posibility;
        }

        public void setPrcp_posibility(String prcp_posibility) {
            this.prcp_posibility = prcp_posibility;
        }

        @Override
        public String toString() {
            return "HistoryDay{" +
                    "tmax='" + tmax + '\'' +
                    ", tmin='" + tmin + '\'' +
                    ", prcp='" + prcp + '\'' +
                    ", prcp_posibility='" + prcp_posibility + '\'' +
                    '}';
        }
    }
}
