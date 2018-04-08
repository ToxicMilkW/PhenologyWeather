package com.iap.phenologyweather.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.iap.phenologyweather.config.greendao.ChinaCityDao;
import com.iap.phenologyweather.config.greendao.ConfigDataDao;
import com.iap.phenologyweather.config.greendao.DaoMaster;
import com.iap.phenologyweather.config.greendao.DaoSession;
import com.iap.phenologyweather.config.greendao.LocationDao;
import com.iap.phenologyweather.config.greendao.MainPhenologyDao;
import com.iap.phenologyweather.config.greendao.WeatherRawInfoDao;
import com.iap.phenologyweather.data.model.ChinaCity;
import com.iap.phenologyweather.data.model.ConfigData;
import com.iap.phenologyweather.data.model.Location;
import com.iap.phenologyweather.data.model.MainPhenology;
import com.iap.phenologyweather.data.model.WeatherRawInfo;
import com.iap.phenologyweather.utils.AmberSdkConstants;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import static com.iap.phenologyweather.utils.Constants.CITY_DB_NAME;



public class WeatherDatabaseManager {

    private final static String dbName = DBC.NAME;

    private static WeatherDatabaseManager mInstance;
    private WeatherDatabaseHelper openHelper;
    private ChinaCityDatabaseHelper cityOpenHelper;
    private Context context;

    private WeatherDatabaseManager(Context context) {
        this.context = context;
    }

    public static WeatherDatabaseManager getInstance(Context context) {

        if (mInstance == null) {
            synchronized (WeatherDatabaseManager.class) {
                if (mInstance == null) {
                    mInstance = new WeatherDatabaseManager(context);
                }
            }
        }
        return mInstance;
    }

    public SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new WeatherDatabaseHelper(context, dbName);
        }
        Log.d("WeatherDatabaseManager", "------db version ------- " + openHelper.getReadableDatabase().getVersion());
        return openHelper.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new WeatherDatabaseHelper(context, dbName);
        }
        return openHelper.getWritableDatabase();
    }

    private DaoSession getDaoSession(boolean isWritable) {
        DaoMaster daoMaster = new DaoMaster(isWritable ? getWritableDatabase() : getReadableDatabase());
        return daoMaster.newSession();
    }


    //city
    public SQLiteDatabase getCityDBReadableDatabase() {
        if (cityOpenHelper == null) {
            cityOpenHelper = new ChinaCityDatabaseHelper(context, CITY_DB_NAME);
        }
        return cityOpenHelper.getReadableDatabase();
    }

    public SQLiteDatabase getCityDBWritableDatabase() {
        if (cityOpenHelper == null) {
            cityOpenHelper = new ChinaCityDatabaseHelper(context, CITY_DB_NAME);
        }
        return cityOpenHelper.getWritableDatabase();
    }

    private DaoSession getCityDBDaoSession(boolean isWritable) {
        DaoMaster daoMaster = new DaoMaster(isWritable ? getCityDBWritableDatabase() : getCityDBReadableDatabase());
        return daoMaster.newSession();
    }

    //******insert start*******
    public int insertWeatherRawInfo(WeatherRawInfo weatherRawInfo) {
        WeatherRawInfoDao weatherRawInfoDao = getDaoSession(true).getWeatherRawInfoDao();
        return (int) weatherRawInfoDao.insert(weatherRawInfo);
    }

    public void insertWeatherRawInfo(List<WeatherRawInfo> weatherRawInfos) {
        if (weatherRawInfos == null || weatherRawInfos.isEmpty()) {
            return;
        }
        WeatherRawInfoDao weatherRawInfoDao = getDaoSession(true).getWeatherRawInfoDao();
        weatherRawInfoDao.insertInTx(weatherRawInfos);
    }

    public int insertLocation(Location location) {
        LocationDao locationDao = getDaoSession(true).getLocationDao();
        return (int) locationDao.insert(location);
    }

    public void insertLocation(List<Location> locations) {
        if (locations == null || locations.isEmpty()) {
            return;
        }
        LocationDao locationDao = getDaoSession(true).getLocationDao();
        locationDao.insertInTx(locations);
    }

    public int insertConfigData(ConfigData configData) {
        ConfigDataDao configDataDao = getDaoSession(true).getConfigDataDao();
        return (int) configDataDao.insert(configData);
    }

    public int insertMainPheno(MainPhenology mainPhenology) {
        MainPhenologyDao mainPhenologyDao = getDaoSession(true).getMainPhenologyDao();
        return (int) mainPhenologyDao.insert(mainPhenology);
    }

    public void insertMainPhenos(List<MainPhenology> mainPhenologies) {
        if (mainPhenologies == null || mainPhenologies.isEmpty()) {
            return;
        }
        MainPhenologyDao mainPhenologyDao = getDaoSession(true).getMainPhenologyDao();
        mainPhenologyDao.insertInTx(mainPhenologies);
    }


    //******insert end*******


    //******update start*******
    public void updateLocation(Location location) {
        if (location.getLevel0() == null || location.getLevel1() == null
                || location.getLevel2() == null) {
            return;
        }
        LocationDao locationDao = getDaoSession(true).getLocationDao();
        locationDao.update(location);
    }

    public void updateConfigData(ConfigData configData) {
        ConfigDataDao configDataDao = getDaoSession(true).getConfigDataDao();
        configDataDao.update(configData);
    }

    //******update end*******

    //******query start*******
    public List<MainPhenology> queryMainPhenosByCityId(int weatherDataId) {
        MainPhenologyDao mainPhenologyDao = getDaoSession(false).getMainPhenologyDao();
        QueryBuilder<MainPhenology> qb = mainPhenologyDao.queryBuilder();
        qb.where(MainPhenologyDao.Properties.WeatherDataId.eq(weatherDataId));
        return qb.list();
    }

    public Location queryLocationByKey(int key) {
        LocationDao locationDao = getDaoSession(false).getLocationDao();
        return locationDao.load((long) key);
    }

    public List<Location> queryAllLocations() {
        LocationDao locationDao = getDaoSession(false).getLocationDao();
        return locationDao.loadAll();
    }

    public ConfigData queryConfigDataByKey(int key) {
        ConfigDataDao configDataDao = getDaoSession(false).getConfigDataDao();
        return configDataDao.load((long) key);
    }

    public WeatherRawInfo queryCurrentWeatherRawInfoByCityId(int cityId) {
        WeatherRawInfoDao weatherRawInfoDao = getDaoSession(false).getWeatherRawInfoDao();
        QueryBuilder<WeatherRawInfo> qb = weatherRawInfoDao.queryBuilder();
        qb.where(WeatherRawInfoDao.Properties.Id.eq(cityId),
                WeatherRawInfoDao.Properties.InfoType.eq(AmberSdkConstants.InfoType.CURRENT));
        List<WeatherRawInfo> weatherRawInfos = qb.list();
        if (weatherRawInfos != null && !weatherRawInfos.isEmpty()) {
            return weatherRawInfos.get(0);
        } else {
            return null;
        }
    }

    public List<WeatherRawInfo> queryHoursWeatherRawInfoListByCityId(int cityId) {
        WeatherRawInfoDao weatherRawInfoDao = getDaoSession(false).getWeatherRawInfoDao();
        QueryBuilder<WeatherRawInfo> qb = weatherRawInfoDao.queryBuilder();
        qb.where(WeatherRawInfoDao.Properties.Id.eq(cityId),
                WeatherRawInfoDao.Properties.InfoType.eq(AmberSdkConstants.InfoType.HOUR));
        return qb.list();
    }

    public List<WeatherRawInfo> queryNextDaysWeatherRawInfoListByCityId(int cityId) {
        WeatherRawInfoDao weatherRawInfoDao = getDaoSession(false).getWeatherRawInfoDao();
        QueryBuilder<WeatherRawInfo> qb = weatherRawInfoDao.queryBuilder();
        qb.where(WeatherRawInfoDao.Properties.Id.eq(cityId),
                WeatherRawInfoDao.Properties.InfoType.eq(AmberSdkConstants.InfoType.DAY));
        return qb.list();
    }

    public List<ChinaCity> queryChinaCitiesByPID(int PID) {
        ChinaCityDao chinaCityDao = getCityDBDaoSession(false).getChinaCityDao();
        QueryBuilder<ChinaCity> qb = chinaCityDao.queryBuilder();
        qb.where(ChinaCityDao.Properties.Pid.eq(PID));
        return qb.list();
    }

    //******query end*******


    //******delete start*******
    public void deleteMainPhenosByCityId(int weatherDataId) {
        MainPhenologyDao mainPhenologyDao = getDaoSession(true).getMainPhenologyDao();
        mainPhenologyDao.queryBuilder()
                .where(MainPhenologyDao.Properties.WeatherDataId.eq(weatherDataId))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }


    public void deleteLocationByKey(int key) {
        LocationDao locationDao = getDaoSession(true).getLocationDao();
        locationDao.deleteByKey((long) key);
    }

    public void deleteWeatherRawInfoByCityId(int cityId) {
        WeatherRawInfoDao weatherRawInfoDao = getDaoSession(true).getWeatherRawInfoDao();
        weatherRawInfoDao.queryBuilder()
                .where(WeatherRawInfoDao.Properties.Id.eq(cityId))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    //******delete end*******




    public void destroySelf() {
        if (context != null) {
            context = null;
        }
        if (openHelper != null) {
            openHelper = null;
        }
        if (mInstance != null) {
            mInstance = null;
        }
    }
}
