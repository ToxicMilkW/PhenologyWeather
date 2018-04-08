package com.iap.phenologyweather.utils;

import com.iap.phenologyweather.R;

/**
 * Created by infolife on 2017/2/15.
 */

public class IconLoader {

    public static int getWeatherImageId(String icoId, boolean isLight) {
        int iconNumber;
        try {
            iconNumber = Integer.parseInt(icoId);
        } catch (Exception e) {
            e.printStackTrace();
            return R.drawable.wic_unkown;
        }
        switch (iconNumber) {
            case WeatherConditionID.CLEAR_LIGHT:// clear_light
            case WeatherConditionID.CLEAR_NIGHT: // clear night
                if (isLight) {
                    return R.drawable.wic_sunny;
                } else {
                    return R.drawable.wic_clear_n;
                }

            case WeatherConditionID.CLEAR_WITH_CLOUD_SMALL_LIGHT:// clear_with_cloud_small_light
            case WeatherConditionID.CLEAR_WITH_CLOUD_SMALL_2_LIGHT: // clear_with_cloud_small*2_light
            case WeatherConditionID.CLEAR_WITH_CLOUD_NOMAL_LIGHT:// clear_with_cloud_normal_light
            case WeatherConditionID.CLEAR_WITH_CLOUD_HEAVY_LIGHT: // clear_with_cloud_heavy_light
            case WeatherConditionID.SMALL_CLOUDY_NIGHT: // small cloudy night
            case WeatherConditionID.NORMAL_CLOUDY_NIGHT: // normal cloudy night
                if (isLight) {
                    return R.drawable.wic_cloudy_d;
                } else {
                    return R.drawable.wic_cloudy_n;
                }

            case WeatherConditionID.CLOUD_HEAVY_2_LIGHT:// cloud_heavy*2_light
            case WeatherConditionID.BLACK_CLOUD_LIGHT:// black_cloud_light
            case WeatherConditionID.BIG_CLOUDY_NIGHT: // big cloudy night
            case WeatherConditionID.HEAVY_CLOUDY_NIGHT: // heavy cloudy night
                return R.drawable.wic_big_cloudy;
            case WeatherConditionID.CLEAR_WITH_FOG_LIGHT: // clear_with_fog_light
                return R.drawable.wic_fog;
            case WeatherConditionID.FOG_NIGHT: // fog night
                return R.drawable.wic_fog_n;
            case WeatherConditionID.FOG_LIGHT: // fog_light
                return R.drawable.wic_fog_d;
            case WeatherConditionID.RAIN_LIGHT:// rain_light
            case WeatherConditionID.STORM_RAIN_LIGHT:// storm_rain_light
            case WeatherConditionID.CLEAR_TO_BIG_RAIN_LIGHT:// clear_to_big_rain_light
            case WeatherConditionID.STORM_BIG_RAIN_NIGHT:// storm_big_rain_night
            case WeatherConditionID.HEAVY_RAIN_NIGHT:// heavy_rain_night
            case WeatherConditionID.BIG_RAIN_STORM_NIGHT: // big rain storm night
            case WeatherConditionID.RAIN_STORM_NIGHT: // rain storm night
            case WeatherConditionID.BIG_RAIN_NIGHT: // big rain night
                /*if (isLight)
                    return drawableArray[Constants.INDEX_BIG_RAIN_DAY+Constants.mtransform_new_icon];
                else*/
                return R.drawable.wic_big_rain;

            case WeatherConditionID.CLEAR_SMALL_RAIN_NIGHT: // clear small rain
                // night
                return R.drawable.wic_rain;

            case WeatherConditionID.CLEAR_THUNDER_BIG_RAIN_LIGHT:// clear_thunder_big_rain_light
            case WeatherConditionID.CLEAR_THUNDER_RAIN_LIGHT:// clear_thunder_rain_light
                return R.drawable.wic_thunder;

            case WeatherConditionID.SNOW_NIGHT:// snow night
            case WeatherConditionID.SNOW_BIG_NIGHT:// snow big night
            case WeatherConditionID.BIG_SNOW_NIGHT: // big snow night
            case WeatherConditionID.SMALL_SNOW_NIGHT: // small snow night
            case WeatherConditionID.CLEAR_SNOW_NORMAL:// clear+snow normal
            case WeatherConditionID.CLEAR_SNOW_SMALL:// clear snow small
            case WeatherConditionID.CLEAR_SNOW_BIG:// clear snow big
                /*if (isLight)
                    return drawableArray[Constants.INDEX_SNOW_DAY+Constants.mtransform_new_icon];
                else*/
                return R.drawable.wic_snow;

            case WeatherConditionID.STORM: // storm
                return R.drawable.wic_storm;
            case WeatherConditionID.BIG_HAIL: // big hail
                return R.drawable.wic_hail;
            case WeatherConditionID.SLEET_NIGHT: // sleet night
                return R.drawable.wic_sleet;

            case WeatherConditionID.SNOW_RAIN_NIGHT: // snow and rain night
                return R.drawable.wic_sleet;

            case WeatherConditionID.VERY_HOT: // very hot
                return R.drawable.wic_very_hot;
            case WeatherConditionID.VERY_COLD:
                return R.drawable.wic_very_cold;

            default:
                return R.drawable.wic_unkown;
        }
    }
}
