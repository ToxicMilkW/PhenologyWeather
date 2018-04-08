/**
 * 
 */
package com.iap.phenologyweather.utils;



import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.iap.phenologyweather.R;
import com.iap.phenologyweather.data.loader.WeatherInfoLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DCTUtils {







	public static String formatSunTimeInto24Date(String sunTime) {
		Date formater = null;
		String suntime24Date = null;
		try {
			formater = new SimpleDateFormat("hh:mmaa", Locale.ENGLISH).parse(sunTime);
			SimpleDateFormat ft = new SimpleDateFormat("HH:mm");
			suntime24Date = ft.format(formater);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return suntime24Date;
	}


	public static String getShortDayNameFromMillseconds(String time) {
		if ("--".equals(time)) {
			return "--";
		}
		Long timestamp = null;
		try {
			timestamp = Long.parseLong(time);
			Date d = new Date(timestamp);
			return String.format("%ta", d).toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "--"; 
	}

	public static long getTodayZeroTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTimeInMillis();
	}

	public static long getLocaleTodayZeroTime(long gmtOffset) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(c.getTimeInMillis() - TimeZone.getDefault().getRawOffset() + gmtOffset);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis();
	}

	public static String get24HourName(String name) {
		try {
			if (TextUtils.equals(name.trim(), "12AM")) {
				name = "00AM";
			} else if (TextUtils.equals(name.trim(), "12PM")) {
				name = "12AM";
			}
			if (name.endsWith("AM"))
				return name.substring(0, name.indexOf("AM")) + ":00";
			else {
				int time = 0;
				try {
					time = Integer.parseInt(name.substring(0, name.indexOf("PM")).trim());
					time += 12;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return time + ":00";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "--";
		}
	}

	public static boolean isLight(String sunRise, String sunSet, String currentTime) {
		boolean isLight = true;
		try {
			int riseTime = Integer.parseInt(sunRise);
			int setTime = Integer.parseInt(sunSet);
			int currTime = Integer.parseInt(currentTime);
//            if (currTime > setTime || currTime <= riseTime) {
//                isLight = false;
//            }
			if (currTime >= riseTime && currTime < setTime) {
				isLight = true;
			} else {
				isLight = false;
			}
		} catch (Exception e) {
			Date date = new Date(System.currentTimeMillis());
			if (date.getHours() < 7 || date.getHours() > 18) {
				isLight = false;
			} else {
				isLight = true;
			}
		}
		return isLight;
	}

	public static boolean isCurrentCityIsLight(Context context, WeatherInfoLoader weatherInfoLoader, int cityId) {
		boolean isLight;
		float currentCityGMT;
		float locateCityGMT;
		long currentCityTime;
		try {
			currentCityGMT = Float.parseFloat(Preferences.getGMTOffset(context, cityId));
			TimeZone timeZone = TimeZone.getDefault();
			locateCityGMT = timeZone.getRawOffset() / (3600000);
			float hourOffset = currentCityGMT - locateCityGMT;
			currentCityTime = System.currentTimeMillis() + (long) (hourOffset * Constants.ONE_HOUR);
			isLight = isWeatherIconLight(weatherInfoLoader, currentCityTime);
		} catch (Exception e) {
			e.printStackTrace();
			isLight = isWeatherIconLight(weatherInfoLoader, System.currentTimeMillis());
		}
		return isLight;
	}

	private static boolean isWeatherIconLight(WeatherInfoLoader weatherInfoLoader, long currentCityTime) {
		if (weatherInfoLoader.getCurrentSunRiseTime() != null) {
			Log.d("oldwidget", "---------currentSunRiseTime----- null ");
			long sunriseTime = 0;
			long sunsetTime = 0;
			try {
				String sunRiseTime = weatherInfoLoader.getCurrentSunRiseTime();
				String sunSetTime = weatherInfoLoader.getCurrentSunSetTime();
				Log.d("oldwidget", "-----sunRiseTime----- " + sunRiseTime);
				Log.d("oldwidget", "----sunSetTime------ " + sunSetTime);
				if (AmberSdkConstants.DEFAULT_SHOW_STRING.equals(sunRiseTime)
						|| AmberSdkConstants.DEFAULT_INTEGER_STRING.equals(sunRiseTime)) {
					sunriseTime = weatherInfoLoader.getDaySunriseMillis(0);
				} else {
					sunriseTime = Long.parseLong(sunRiseTime);
				}
				if (AmberSdkConstants.DEFAULT_INTEGER_STRING.equals(sunSetTime)
						|| AmberSdkConstants.DEFAULT_INTEGER_STRING.equals(sunSetTime)) {
					sunsetTime = weatherInfoLoader.getDaySunsetMillis(0);
				} else {
					sunsetTime = Long.parseLong(sunSetTime);
				}
//                sunsetTime = Long.parseLong(weatherInfoLoader.getCurrentSunSetTime());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (sunriseTime == 0 || sunsetTime == 0) {
				Date date = new Date(System.currentTimeMillis());
				return !(date.getHours() < 7 || date.getHours() > 18);
			}
			int sunRiseHour = (new Date(sunriseTime)).getHours();
			int sunSetHour = (new Date(sunsetTime)).getHours();
			int currentHour = (new Date(currentCityTime)).getHours();
			return currentHour >= sunRiseHour && currentHour < sunSetHour;
		} else {
			Date date = new Date(System.currentTimeMillis());
			return !(date.getHours() < 7 || date.getHours() > 18);
		}
	}

	public static int getCurrentHourIndex(Context context, int weatherDataId, WeatherInfoLoader weatherInfoLoader) {
		if (weatherInfoLoader == null) {
			return 0;
		}
		int gridSize = Math.min(weatherInfoLoader.getHourItems(), 24);
		long currentFixedTime = getFixedHourByCurrentTime(context, weatherDataId);
		int currentIndex = 0;
		long gridTime;
		for (int i = 0; i <= gridSize; i++) {
			gridTime = weatherInfoLoader.getHourMillis(i);
			if (gridTime > currentFixedTime || (gridSize - i) <=
					context.getResources().getInteger(R.integer.main_page_min_out_of_data_grid)) {
				currentIndex = i;
				break;
			}
		}
		return currentIndex;
	}

	private static long getFixedHourByCurrentTime(Context c, int weatherDataId) {
		Calendar uc = Calendar.getInstance();

		if (weatherDataId != Constants.AUTO_ID) {
			uc.setTimeInMillis(getWorldDate(c, weatherDataId).getTime());
		}
		uc.set(Calendar.MINUTE, 0);
		uc.set(Calendar.SECOND, 0);
		uc.set(Calendar.MILLISECOND, 0);
		return uc.getTimeInMillis();
	}

	public static Date getWorldDate(Context context, int weatherDataId) {
		String gmtOffset_str = Preferences.getGMTOffset(context, weatherDataId);
		Date utcTime = getUTCtime();
		if ("0".equals(gmtOffset_str))
			return utcTime;
		return WeatherUtils.setUtcDateFromOffsetString(utcTime, gmtOffset_str);
	}

	public static Date getUTCtime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String utcTime = sdf.format(new Date());

		Date utcDate = null;
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

		try {
			utcDate = (Date) sdf2.parse(utcTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return utcDate;
	}

}
