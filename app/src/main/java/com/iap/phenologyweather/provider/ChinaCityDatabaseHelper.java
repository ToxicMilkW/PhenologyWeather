package com.iap.phenologyweather.provider;

import android.content.Context;


import com.iap.phenologyweather.config.greendao.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Created by infolife on 2017/2/28.
 */

public class ChinaCityDatabaseHelper extends DaoMaster.OpenHelper {

    public ChinaCityDatabaseHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onCreate(Database db) {

    }
}
