package com.iap.phenologyweather.config.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.iap.phenologyweather.data.model.ChinaCity;
import com.iap.phenologyweather.data.model.ConfigData;
import com.iap.phenologyweather.data.model.Location;
import com.iap.phenologyweather.data.model.MainPhenology;
import com.iap.phenologyweather.data.model.WeatherRawInfo;

import com.iap.phenologyweather.config.greendao.ChinaCityDao;
import com.iap.phenologyweather.config.greendao.ConfigDataDao;
import com.iap.phenologyweather.config.greendao.LocationDao;
import com.iap.phenologyweather.config.greendao.MainPhenologyDao;
import com.iap.phenologyweather.config.greendao.WeatherRawInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig chinaCityDaoConfig;
    private final DaoConfig configDataDaoConfig;
    private final DaoConfig locationDaoConfig;
    private final DaoConfig mainPhenologyDaoConfig;
    private final DaoConfig weatherRawInfoDaoConfig;

    private final ChinaCityDao chinaCityDao;
    private final ConfigDataDao configDataDao;
    private final LocationDao locationDao;
    private final MainPhenologyDao mainPhenologyDao;
    private final WeatherRawInfoDao weatherRawInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        chinaCityDaoConfig = daoConfigMap.get(ChinaCityDao.class).clone();
        chinaCityDaoConfig.initIdentityScope(type);

        configDataDaoConfig = daoConfigMap.get(ConfigDataDao.class).clone();
        configDataDaoConfig.initIdentityScope(type);

        locationDaoConfig = daoConfigMap.get(LocationDao.class).clone();
        locationDaoConfig.initIdentityScope(type);

        mainPhenologyDaoConfig = daoConfigMap.get(MainPhenologyDao.class).clone();
        mainPhenologyDaoConfig.initIdentityScope(type);

        weatherRawInfoDaoConfig = daoConfigMap.get(WeatherRawInfoDao.class).clone();
        weatherRawInfoDaoConfig.initIdentityScope(type);

        chinaCityDao = new ChinaCityDao(chinaCityDaoConfig, this);
        configDataDao = new ConfigDataDao(configDataDaoConfig, this);
        locationDao = new LocationDao(locationDaoConfig, this);
        mainPhenologyDao = new MainPhenologyDao(mainPhenologyDaoConfig, this);
        weatherRawInfoDao = new WeatherRawInfoDao(weatherRawInfoDaoConfig, this);

        registerDao(ChinaCity.class, chinaCityDao);
        registerDao(ConfigData.class, configDataDao);
        registerDao(Location.class, locationDao);
        registerDao(MainPhenology.class, mainPhenologyDao);
        registerDao(WeatherRawInfo.class, weatherRawInfoDao);
    }
    
    public void clear() {
        chinaCityDaoConfig.clearIdentityScope();
        configDataDaoConfig.clearIdentityScope();
        locationDaoConfig.clearIdentityScope();
        mainPhenologyDaoConfig.clearIdentityScope();
        weatherRawInfoDaoConfig.clearIdentityScope();
    }

    public ChinaCityDao getChinaCityDao() {
        return chinaCityDao;
    }

    public ConfigDataDao getConfigDataDao() {
        return configDataDao;
    }

    public LocationDao getLocationDao() {
        return locationDao;
    }

    public MainPhenologyDao getMainPhenologyDao() {
        return mainPhenologyDao;
    }

    public WeatherRawInfoDao getWeatherRawInfoDao() {
        return weatherRawInfoDao;
    }

}
