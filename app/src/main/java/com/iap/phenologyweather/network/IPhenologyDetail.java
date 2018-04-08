package com.iap.phenologyweather.network;

import com.iap.phenologyweather.network.netresult.PhenologyDetailResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by chenxueqing on 2017/4/19.
 */

public interface IPhenologyDetail {
    String preUrl = "v2/spot.php";

    @GET(preUrl)
    Observable<PhenologyDetailResult> requestPhenologyBySpotId(@Query("spot_id") int spotId);
}
