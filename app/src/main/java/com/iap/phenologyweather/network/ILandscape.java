package com.iap.phenologyweather.network;

import com.iap.phenologyweather.network.netresult.LandscapeResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by chenxueqing on 2017/4/20.
 */

public interface ILandscape {
    String preUrl = "v2/landscape.php";

    @GET(preUrl)
    Observable<LandscapeResult> requestLandscapeById(@Query("landscape_id") int lanId);

}
