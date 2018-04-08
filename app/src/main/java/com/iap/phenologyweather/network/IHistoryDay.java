package com.iap.phenologyweather.network;

import com.iap.phenologyweather.network.netresult.HistoryDayResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by chenxueqing on 2017/4/21.
 */

public interface IHistoryDay {
    String preUrl = "v2/history2.php";

    @GET(preUrl)
    Observable<HistoryDayResult> requestHistoryDay(@Query("lat") double lat, @Query("lng") double lng, @Query("date") String date);
}
