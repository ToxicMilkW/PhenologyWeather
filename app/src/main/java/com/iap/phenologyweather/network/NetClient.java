package com.iap.phenologyweather.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iap.phenologyweather.config.gson.DoubleDefaultAdapter;
import com.iap.phenologyweather.config.gson.IntegerDefaultAdapter;
import com.iap.phenologyweather.config.gson.LongDefaultAdapter;
import com.iap.phenologyweather.config.gson.retrofit2.converter.gson.StringDefaultAdapter;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chenxueqing on 2017/4/19.
 */

public class NetClient {

    public static final String MAIN_PAGE_LIST_HOST = "http://research.iap.ac.cn:8888/";
    public static final String PHENOLOGY_DETAIL_HOST = "http://research.iap.ac.cn:8888/";
    public static final String LANDSCAPE_HOST = "http://research.iap.ac.cn:8888/";
    public static final String HISTORY_DAY_HOST = "http://research.iap.ac.cn:8888/";
    public static final String HISTORY_MONTH_HOST = "http://research.iap.ac.cn:8888/";

    private static final Object monitor = new Object();

    private static Gson gson;
    private static OkHttpClient okHttpClient;

    private static IMainPhenology iMainPhenology;
    private static IPhenologyDetail iPhenologyDetail;
    private static ILandscape iLandscape;
    private static IHistoryDay iHistoryDay;
    private static IHistoryMonth iHistoryMonth;

    private static Retrofit mPRetrofit;
    private static Retrofit pdRetrofit;
    private static Retrofit lsRetrofit;
    private static Retrofit hdRetrofit;
    private static Retrofit hmRetrofit;


    private NetClient() {}

    public static IMainPhenology getMainPheloRetrofitInstance() {
        synchronized (monitor) {
            if (mPRetrofit == null) {
                mPRetrofit = new Retrofit.Builder()
                        .baseUrl(MAIN_PAGE_LIST_HOST)
                        .addConverterFactory(GsonConverterFactory.create(getGson()))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(getOkHttpClient())
                        .build();
            }
            if (iMainPhenology == null) {
                iMainPhenology = mPRetrofit.create(IMainPhenology.class);
            }
            return iMainPhenology;
        }
    }

    public static IPhenologyDetail getPhenoDetailRetrofitInstance() {
        synchronized (monitor) {
            if (pdRetrofit == null) {
                pdRetrofit = new Retrofit.Builder()
                        .baseUrl(PHENOLOGY_DETAIL_HOST)
                        .addConverterFactory(GsonConverterFactory.create(getGson()))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(getOkHttpClient())
                        .build();
            }
            if (iPhenologyDetail == null) {
                iPhenologyDetail = pdRetrofit.create(IPhenologyDetail.class);
            }
            return iPhenologyDetail;
        }
    }

    public static ILandscape getLandscapeRetrofitInstance() {
        synchronized (monitor) {
            if (lsRetrofit == null) {
                lsRetrofit = new Retrofit.Builder()
                        .baseUrl(LANDSCAPE_HOST)
                        .addConverterFactory(GsonConverterFactory.create(getGson()))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(getOkHttpClient())
                        .build();
            }
            if (iLandscape == null) {
                iLandscape = lsRetrofit.create(ILandscape.class);
            }
            return iLandscape;
        }
    }

    public static IHistoryDay getHistoryDayRetrofitInstance() {
        synchronized (monitor) {
            if (hdRetrofit == null) {
                hdRetrofit = new Retrofit.Builder()
                        .baseUrl(HISTORY_DAY_HOST)
                        .addConverterFactory(GsonConverterFactory.create(getGson()))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(getOkHttpClient())
                        .build();
            }
            if (iHistoryDay == null) {
                iHistoryDay = hdRetrofit.create(IHistoryDay.class);
            }
            return iHistoryDay;
        }
    }

    public static IHistoryMonth getHistoryMonthRetrofitInstance() {
        synchronized (monitor) {
            if (hmRetrofit == null) {
                hmRetrofit = new Retrofit.Builder()
                        .baseUrl(HISTORY_MONTH_HOST)
                        .addConverterFactory(GsonConverterFactory.create(getGson()))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(getOkHttpClient())
                        .build();
            }
            if (iHistoryMonth == null) {
                iHistoryMonth = hmRetrofit.create(IHistoryMonth.class);
            }
            return iHistoryMonth;
        }
    }


    private static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .setLenient()
                    .registerTypeAdapter(Integer.class, new IntegerDefaultAdapter())
                    .registerTypeAdapter(int.class, new IntegerDefaultAdapter())
                    .registerTypeAdapter(Double.class, new DoubleDefaultAdapter())
                    .registerTypeAdapter(double.class, new DoubleDefaultAdapter())
                    .registerTypeAdapter(Long.class, new LongDefaultAdapter())
                    .registerTypeAdapter(long.class, new LongDefaultAdapter())
                    .registerTypeAdapter(String.class, new StringDefaultAdapter())
                    .create();
        }
        return gson;
    }

    private static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient;
    }

}
