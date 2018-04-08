package com.iap.phenologyweather.request.weather;

import android.content.Context;

public interface WeatherDataFetcherInterface {
	void fetchAndParseWeatherData(Context context, CityParams cityParams, OnFetchResultEventListener onFetchResultEventListener);
	
	interface OnFetchResultEventListener {
		/*
		 * @param json the data that you have download and parse 
		 */
		void onSucceed();
		void onFailed();
		void onNoData();
		void onNoKey();
	}
}
