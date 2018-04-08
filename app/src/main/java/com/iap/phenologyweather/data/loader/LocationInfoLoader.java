package com.iap.phenologyweather.data.loader;

import android.content.Context;

import com.iap.phenologyweather.data.model.Location;
import com.iap.phenologyweather.provider.WeatherDatabaseManager;
import com.iap.phenologyweather.utils.AmberSdkConstants;

import java.util.ArrayList;
import java.util.List;


public class LocationInfoLoader {

	private static final List<Integer> cityIds = new ArrayList<>();
	private static final List<Location> locationList = new ArrayList<>();
	private static Context mContext;
	private static LocationInfoLoader loader;

	public static LocationInfoLoader getInstance(Context context) {
		mContext = context.getApplicationContext();
		return getInfoLoader(mContext);
	}

	private static LocationInfoLoader getInfoLoader(Context mContext) {
		if (loader == null) {
			loader = new LocationInfoLoader(mContext);
		}
		return loader;
	}

	public List<Integer> getCityIds() {
		if (cityIds.isEmpty()) {
			reQueryCityIds();
		}
		return cityIds;
	}

	public List<Integer> reQueryCityIds() {
		queryLocationList();
		return cityIds;
	}

	private LocationInfoLoader(Context mContext) {
		LocationInfoLoader.mContext = mContext.getApplicationContext();
	}

	public List<Location> queryLocationList() {
		List<Location> temp = WeatherDatabaseManager.getInstance(mContext).queryAllLocations();

		if (temp.size() > 0) {
			locationList.clear();
			cityIds.clear();
			for (Location l : temp) {
				locationList.add(l);
				cityIds.add(l.getId().intValue());
			}
		}
		return locationList;
	}

	public Location queryLocationById(int cityId) {
		return WeatherDatabaseManager.getInstance(mContext).queryLocationByKey(cityId);
	}

	public void initCurrentLocation(){
		Location location = new Location();
		location.setLevel0(AmberSdkConstants.CITY_NAME_NOTSET);
		location.setLevel1(AmberSdkConstants.CITY_NAME_NOTSET);
		location.setLevel2(AmberSdkConstants.CITY_NAME_NOTSET);
		location.setLevel3("");
		location.setLat(0);
		location.setLon(0);
		location.setGmtOffset(0);
		location.setDayLightOffset(0);
		WeatherDatabaseManager.getInstance(mContext).insertLocation(location);
		locationList.add(location);
	}

}
