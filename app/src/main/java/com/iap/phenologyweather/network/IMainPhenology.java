package com.iap.phenologyweather.network;

import com.iap.phenologyweather.network.netresult.MainPhenologyResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by chenxueqing on 2017/4/19.
 */

public interface IMainPhenology {
    String preUrl = "v2/activity.php";

    @GET(preUrl)
    Observable<MainPhenologyResult> requestMainByLocation(@Query("province") String province,
                                                          @Query("city") String city,
                                                          @Query("district") String district);
}
