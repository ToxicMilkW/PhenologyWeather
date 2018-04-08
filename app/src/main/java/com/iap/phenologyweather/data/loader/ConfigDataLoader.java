package com.iap.phenologyweather.data.loader;

import android.content.Context;

import com.iap.phenologyweather.data.model.ConfigData;
import com.iap.phenologyweather.provider.WeatherDatabaseManager;


public class ConfigDataLoader {

	private static ConfigData mConfigData = null;

	public static ConfigData getInstance(Context context, boolean isReload) {
		if (isReload) {
			mConfigData = null;
		}
		if (mConfigData == null) {
			mConfigData = WeatherDatabaseManager.getInstance(context).queryConfigDataByKey(1);
		}
		return mConfigData;
	}

	private ConfigDataLoader() {

	}

}
