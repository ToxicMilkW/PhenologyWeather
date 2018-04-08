package com.iap.phenologyweather.application;

import android.app.Application;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.iap.phenologyweather.provider.WeatherDatabaseManager;
import com.iap.phenologyweather.utils.DBUtils;

/**
 * Created by infolife on 2017/2/14.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initSQL();
        initXLog();
    }

    private void initXLog() {
        XLog.init(LogLevel.ALL, new LogConfiguration.Builder().b().build());
    }

    private void initSQL() {
        WeatherDatabaseManager.getInstance(this).getWritableDatabase();
        DBUtils.copyDBToDatabases(this, false);
    }
}
