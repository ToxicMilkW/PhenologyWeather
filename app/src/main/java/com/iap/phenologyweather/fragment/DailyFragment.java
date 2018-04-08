package com.iap.phenologyweather.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iap.phenologyweather.activity.WeatherDetailActivity;
import com.iap.phenologyweather.data.loader.WeatherInfoLoader;
import com.iap.phenologyweather.view.daily.NewDailyView;
import com.iap.phenologyweather.view.hourly.NewHourlyView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyFragment extends BaseFragment {

    private int weatherDataId = 1;
    private Context mContext;
    private NewDailyView dailyView;
    private boolean hasShowData = false;

    public DailyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dailyView = new NewDailyView(mContext);
        weatherDataId = getBaseActivity().getWeatherDataId();
//        getData();
        return dailyView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mContext != null
                && !hasShowData) {
            getData();
        }
    }

    private void getData() {
        final WeatherInfoLoader weatherInfoLoader = WeatherInfoLoader.getInstance(mContext);
        weatherInfoLoader.getAllData(new WeatherInfoLoader.OnLoadFullWeatherDataListener() {
            @Override
            public void onSuccess() {
                dailyView.fillData(weatherDataId, weatherInfoLoader);
            }

            @Override
            public void onFailed(String str) {

            }
        }, mContext, weatherDataId);
        hasShowData = true;
    }

    public WeatherDetailActivity getBaseActivity() {
        return (WeatherDetailActivity) getActivity();
    }

    @Override
    public String getPageTitle() {
        return "15天预报";
    }

}
