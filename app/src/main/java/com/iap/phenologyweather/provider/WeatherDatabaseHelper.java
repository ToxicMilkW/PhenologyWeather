package com.iap.phenologyweather.provider;

import android.content.Context;

import com.iap.phenologyweather.config.greendao.DaoMaster;
import com.iap.phenologyweather.config.greendao.MainPhenologyDao;
import com.iap.phenologyweather.data.model.ConfigData;
import com.iap.phenologyweather.utils.UnitNameUtils;

import org.greenrobot.greendao.database.Database;


public class WeatherDatabaseHelper extends DaoMaster.OpenHelper {

    private Context context;

    public WeatherDatabaseHelper(Context context, String name) {
        super(context, name);
        this.context = context;
    }

    @Override
    public void onCreate(Database db) {
        super.onCreate(db);
        insertDefaultConfigData(db);
    }

    @Override
    public void onUpgrade(Database db, int oldV, int newV) {
        if (oldV == 1) {
            updateFrom1(db);
        }
    }

    private void updateFrom1(Database db) {
        //create new main phenology table
        MainPhenologyDao.createTable(db, false);
        //drop old table
        String sql = "DROP TABLE " + "\"activity_table\"";
        db.execSQL(sql);
    }


    private void insertDefaultConfigData(Database db) {
        ConfigData configData = new ConfigData(1, true, 0,
                0, UnitNameUtils.getTempUnitName(context, 0),
                0, UnitNameUtils.getDistanceUnitName(context, 0),
                0, UnitNameUtils.getSpeedUnitName(context, 0),
                0, UnitNameUtils.getPressureUnitName(context, 0),
                false, true);
        DaoMaster daoMaster = new DaoMaster(db);
        daoMaster.newSession().getConfigDataDao().insert(configData);
    }


}
