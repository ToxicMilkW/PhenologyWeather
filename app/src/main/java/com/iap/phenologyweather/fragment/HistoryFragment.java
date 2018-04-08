package com.iap.phenologyweather.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iap.phenologyweather.R;
import com.iap.phenologyweather.activity.WeatherDetailActivity;
import com.iap.phenologyweather.data.loader.WeatherInfoLoader;
import com.iap.phenologyweather.view.daily.NewDailyView;

/**
 * Created by chenxueqing on 2017/4/21.
 */

public class HistoryFragment extends BaseFragment {
    private Context mContext;
    private boolean hasShowData = false;

    public HistoryFragment() {
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
        View view = inflater.inflate(R.layout.fragment_history, null);
//        getData();
        return view;
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
        //request


        hasShowData = true;
    }

    public WeatherDetailActivity getBaseActivity() {
        return (WeatherDetailActivity) getActivity();
    }

    @Override
    public String getPageTitle() {
        return "平均气候";
    }

}
