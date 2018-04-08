package com.iap.phenologyweather.network;

import com.iap.phenologyweather.network.netresult.HistoryDayResult;
import com.iap.phenologyweather.network.netresult.HistoryMonthResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by chenxueqing on 2017/4/21.
 */

public interface IHistoryMonth {
    String preUrl = "v2/history.php";

    @GET(preUrl)
    Observable<HistoryMonthResult> requestHistoryMonth(@Query("lat") double lat, @Query("lng") double lng);
}
