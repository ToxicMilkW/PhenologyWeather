package com.iap.phenologyweather.utils;

import android.content.Context;

import com.iap.phenologyweather.R;

/**
 * Created by infolife on 2017/2/13.
 */

public class CommonUtils {

    public static String getUVLevelByUVIndex(Context mContext, int uvIndex) {
        String uvLevel;
        switch (uvIndex) {
            case 0:
            case 1:
            case 2:
                uvLevel = mContext.getResources().getString(R.string.uv_index_low);
                break;
            case 3:
            case 4:
            case 5:
                uvLevel = mContext.getString(R.string.uv_index_moderate);
                break;
            case 6:
            case 7:
                uvLevel = mContext.getString(R.string.uv_index_high);
                break;
            case 8:
            case 9:
            case 10:
                uvLevel = mContext.getString(R.string.uv_index_very_high);
                break;
            default:
                uvLevel = mContext.getString(R.string.uv_index_extreme);
                break;
        }
        return uvLevel;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
