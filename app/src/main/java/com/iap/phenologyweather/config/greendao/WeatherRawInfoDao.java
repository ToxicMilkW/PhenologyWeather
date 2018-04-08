package com.iap.phenologyweather.config.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.iap.phenologyweather.data.model.WeatherRawInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "current_table".
*/
public class WeatherRawInfoDao extends AbstractDao<WeatherRawInfo, Long> {

    public static final String TABLENAME = "current_table";

    /**
     * Properties of entity WeatherRawInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property TableKeyId = new Property(0, Long.class, "tableKeyId", true, "_id");
        public final static Property InfoType = new Property(1, int.class, "infoType", false, "info_type");
        public final static Property Id = new Property(2, int.class, "id", false, "city_id");
        public final static Property NameMillis = new Property(3, long.class, "nameMillis", false, "time_temp");
        public final static Property Temp = new Property(4, double.class, "temp", false, "temp");
        public final static Property Humidity = new Property(5, String.class, "humidity", false, "humidity");
        public final static Property Pressure = new Property(6, double.class, "pressure", false, "pressure");
        public final static Property Uv = new Property(7, String.class, "uv", false, "uvindex");
        public final static Property WindSpeed = new Property(8, double.class, "windSpeed", false, "wind_speed");
        public final static Property WindDirection = new Property(9, double.class, "windDirection", false, "wind_direction");
        public final static Property Visibility = new Property(10, double.class, "visibility", false, "distance");
        public final static Property DewPoint = new Property(11, double.class, "dewPoint", false, "dew_point");
        public final static Property Condition = new Property(12, String.class, "condition", false, "condition");
        public final static Property Icon = new Property(13, String.class, "icon", false, "weather_icon");
        public final static Property RainAmount = new Property(14, double.class, "rainAmount", false, "rain_amount");
        public final static Property RainProbility = new Property(15, String.class, "rainProbility", false, "rain_probility");
        public final static Property SnowAmount = new Property(16, double.class, "snowAmount", false, "snow_amount");
        public final static Property SnowProbility = new Property(17, String.class, "snowProbility", false, "snow_probility");
        public final static Property ThunderStormProbility = new Property(18, String.class, "thunderStormProbility", false, "rain_storm_probility");
        public final static Property RealFeelHigh = new Property(19, double.class, "realFeelHigh", false, "real_feel_high");
        public final static Property RealFeelLow = new Property(20, double.class, "realFeelLow", false, "real_feel_low");
        public final static Property RealFeel = new Property(21, double.class, "realFeel", false, "real_feel");
        public final static Property SunriseMillis = new Property(22, long.class, "sunriseMillis", false, "sunrise");
        public final static Property SunsetMillis = new Property(23, long.class, "sunsetMillis", false, "sunset");
        public final static Property HighTemp = new Property(24, double.class, "highTemp", false, "high_temp");
        public final static Property LowTemp = new Property(25, double.class, "lowTemp", false, "low_temp");
        public final static Property WindSpeedUnit = new Property(26, String.class, "windSpeedUnit", false, "wind_speed_unit");
        public final static Property IsSunRiseExist = new Property(27, boolean.class, "isSunRiseExist", false, "is_sunrise_exist");
        public final static Property IsSunSetExist = new Property(28, boolean.class, "isSunSetExist", false, "is_sunset_exist");
        public final static Property Pm25 = new Property(29, double.class, "pm25", false, "pm25");
        public final static Property FormattedSunRise = new Property(30, String.class, "formattedSunRise", false, "formatted_sun_rise");
        public final static Property FormattedSunSet = new Property(31, String.class, "formattedSunSet", false, "formatted_sun_set");
        public final static Property FormattedWindDirection = new Property(32, String.class, "formattedWindDirection", false, "formatted_wind_direction");
        public final static Property FormattedTimeName = new Property(33, String.class, "formattedTimeName", false, "formatted_time_name");
        public final static Property IsLight = new Property(34, boolean.class, "isLight", false, "is_light");
        public final static Property IsDewpointExist = new Property(35, boolean.class, "isDewpointExist", false, "is_dewpoint_exist");
        public final static Property IsPressureExist = new Property(36, boolean.class, "isPressureExist", false, "is_pressure_exist");
        public final static Property IsUvindexExist = new Property(37, boolean.class, "isUvindexExist", false, "is_uvindex_exist");
        public final static Property IsVisibilityExist = new Property(38, boolean.class, "isVisibilityExist", false, "is_visibility_exist");
        public final static Property MoonRiseMillis = new Property(39, long.class, "moonRiseMillis", false, "moonrise");
        public final static Property MoonSetMillis = new Property(40, long.class, "moonSetMillis", false, "moonset");
        public final static Property IsMoonRiseExist = new Property(41, boolean.class, "isMoonRiseExist", false, "is_moonrise_exist");
        public final static Property IsMoonSetExist = new Property(42, boolean.class, "isMoonSetExist", false, "is_moonset_exist");
        public final static Property FormattedMoonRise = new Property(43, String.class, "formattedMoonRise", false, "formatted_moon_rise");
        public final static Property FormattedMoonSet = new Property(44, String.class, "formattedMoonSet", false, "formatted_moon_set");
        public final static Property DayTimeMillis = new Property(45, long.class, "dayTimeMillis", false, "daytime");
    }


    public WeatherRawInfoDao(DaoConfig config) {
        super(config);
    }
    
    public WeatherRawInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"current_table\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: tableKeyId
                "\"info_type\" INTEGER NOT NULL ," + // 1: infoType
                "\"city_id\" INTEGER NOT NULL ," + // 2: id
                "\"time_temp\" INTEGER NOT NULL ," + // 3: nameMillis
                "\"temp\" REAL NOT NULL ," + // 4: temp
                "\"humidity\" TEXT," + // 5: humidity
                "\"pressure\" REAL NOT NULL ," + // 6: pressure
                "\"uvindex\" TEXT," + // 7: uv
                "\"wind_speed\" REAL NOT NULL ," + // 8: windSpeed
                "\"wind_direction\" REAL NOT NULL ," + // 9: windDirection
                "\"distance\" REAL NOT NULL ," + // 10: visibility
                "\"dew_point\" REAL NOT NULL ," + // 11: dewPoint
                "\"condition\" TEXT," + // 12: condition
                "\"weather_icon\" TEXT," + // 13: icon
                "\"rain_amount\" REAL NOT NULL ," + // 14: rainAmount
                "\"rain_probility\" TEXT," + // 15: rainProbility
                "\"snow_amount\" REAL NOT NULL ," + // 16: snowAmount
                "\"snow_probility\" TEXT," + // 17: snowProbility
                "\"rain_storm_probility\" TEXT," + // 18: thunderStormProbility
                "\"real_feel_high\" REAL NOT NULL ," + // 19: realFeelHigh
                "\"real_feel_low\" REAL NOT NULL ," + // 20: realFeelLow
                "\"real_feel\" REAL NOT NULL ," + // 21: realFeel
                "\"sunrise\" INTEGER NOT NULL ," + // 22: sunriseMillis
                "\"sunset\" INTEGER NOT NULL ," + // 23: sunsetMillis
                "\"high_temp\" REAL NOT NULL ," + // 24: highTemp
                "\"low_temp\" REAL NOT NULL ," + // 25: lowTemp
                "\"wind_speed_unit\" TEXT," + // 26: windSpeedUnit
                "\"is_sunrise_exist\" INTEGER NOT NULL ," + // 27: isSunRiseExist
                "\"is_sunset_exist\" INTEGER NOT NULL ," + // 28: isSunSetExist
                "\"pm25\" REAL NOT NULL ," + // 29: pm25
                "\"formatted_sun_rise\" TEXT," + // 30: formattedSunRise
                "\"formatted_sun_set\" TEXT," + // 31: formattedSunSet
                "\"formatted_wind_direction\" TEXT," + // 32: formattedWindDirection
                "\"formatted_time_name\" TEXT," + // 33: formattedTimeName
                "\"is_light\" INTEGER NOT NULL ," + // 34: isLight
                "\"is_dewpoint_exist\" INTEGER NOT NULL ," + // 35: isDewpointExist
                "\"is_pressure_exist\" INTEGER NOT NULL ," + // 36: isPressureExist
                "\"is_uvindex_exist\" INTEGER NOT NULL ," + // 37: isUvindexExist
                "\"is_visibility_exist\" INTEGER NOT NULL ," + // 38: isVisibilityExist
                "\"moonrise\" INTEGER NOT NULL ," + // 39: moonRiseMillis
                "\"moonset\" INTEGER NOT NULL ," + // 40: moonSetMillis
                "\"is_moonrise_exist\" INTEGER NOT NULL ," + // 41: isMoonRiseExist
                "\"is_moonset_exist\" INTEGER NOT NULL ," + // 42: isMoonSetExist
                "\"formatted_moon_rise\" TEXT," + // 43: formattedMoonRise
                "\"formatted_moon_set\" TEXT," + // 44: formattedMoonSet
                "\"daytime\" INTEGER NOT NULL );"); // 45: dayTimeMillis
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"current_table\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, WeatherRawInfo entity) {
        stmt.clearBindings();
 
        Long tableKeyId = entity.getTableKeyId();
        if (tableKeyId != null) {
            stmt.bindLong(1, tableKeyId);
        }
        stmt.bindLong(2, entity.getInfoType());
        stmt.bindLong(3, entity.getId());
        stmt.bindLong(4, entity.getNameMillis());
        stmt.bindDouble(5, entity.getTemp());
 
        String humidity = entity.getHumidity();
        if (humidity != null) {
            stmt.bindString(6, humidity);
        }
        stmt.bindDouble(7, entity.getPressure());
 
        String uv = entity.getUv();
        if (uv != null) {
            stmt.bindString(8, uv);
        }
        stmt.bindDouble(9, entity.getWindSpeed());
        stmt.bindDouble(10, entity.getWindDirection());
        stmt.bindDouble(11, entity.getVisibility());
        stmt.bindDouble(12, entity.getDewPoint());
 
        String condition = entity.getCondition();
        if (condition != null) {
            stmt.bindString(13, condition);
        }
 
        String icon = entity.getIcon();
        if (icon != null) {
            stmt.bindString(14, icon);
        }
        stmt.bindDouble(15, entity.getRainAmount());
 
        String rainProbility = entity.getRainProbility();
        if (rainProbility != null) {
            stmt.bindString(16, rainProbility);
        }
        stmt.bindDouble(17, entity.getSnowAmount());
 
        String snowProbility = entity.getSnowProbility();
        if (snowProbility != null) {
            stmt.bindString(18, snowProbility);
        }
 
        String thunderStormProbility = entity.getThunderStormProbility();
        if (thunderStormProbility != null) {
            stmt.bindString(19, thunderStormProbility);
        }
        stmt.bindDouble(20, entity.getRealFeelHigh());
        stmt.bindDouble(21, entity.getRealFeelLow());
        stmt.bindDouble(22, entity.getRealFeel());
        stmt.bindLong(23, entity.getSunriseMillis());
        stmt.bindLong(24, entity.getSunsetMillis());
        stmt.bindDouble(25, entity.getHighTemp());
        stmt.bindDouble(26, entity.getLowTemp());
 
        String windSpeedUnit = entity.getWindSpeedUnit();
        if (windSpeedUnit != null) {
            stmt.bindString(27, windSpeedUnit);
        }
        stmt.bindLong(28, entity.getIsSunRiseExist() ? 1L: 0L);
        stmt.bindLong(29, entity.getIsSunSetExist() ? 1L: 0L);
        stmt.bindDouble(30, entity.getPm25());
 
        String formattedSunRise = entity.getFormattedSunRise();
        if (formattedSunRise != null) {
            stmt.bindString(31, formattedSunRise);
        }
 
        String formattedSunSet = entity.getFormattedSunSet();
        if (formattedSunSet != null) {
            stmt.bindString(32, formattedSunSet);
        }
 
        String formattedWindDirection = entity.getFormattedWindDirection();
        if (formattedWindDirection != null) {
            stmt.bindString(33, formattedWindDirection);
        }
 
        String formattedTimeName = entity.getFormattedTimeName();
        if (formattedTimeName != null) {
            stmt.bindString(34, formattedTimeName);
        }
        stmt.bindLong(35, entity.getIsLight() ? 1L: 0L);
        stmt.bindLong(36, entity.getIsDewpointExist() ? 1L: 0L);
        stmt.bindLong(37, entity.getIsPressureExist() ? 1L: 0L);
        stmt.bindLong(38, entity.getIsUvindexExist() ? 1L: 0L);
        stmt.bindLong(39, entity.getIsVisibilityExist() ? 1L: 0L);
        stmt.bindLong(40, entity.getMoonRiseMillis());
        stmt.bindLong(41, entity.getMoonSetMillis());
        stmt.bindLong(42, entity.getIsMoonRiseExist() ? 1L: 0L);
        stmt.bindLong(43, entity.getIsMoonSetExist() ? 1L: 0L);
 
        String formattedMoonRise = entity.getFormattedMoonRise();
        if (formattedMoonRise != null) {
            stmt.bindString(44, formattedMoonRise);
        }
 
        String formattedMoonSet = entity.getFormattedMoonSet();
        if (formattedMoonSet != null) {
            stmt.bindString(45, formattedMoonSet);
        }
        stmt.bindLong(46, entity.getDayTimeMillis());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, WeatherRawInfo entity) {
        stmt.clearBindings();
 
        Long tableKeyId = entity.getTableKeyId();
        if (tableKeyId != null) {
            stmt.bindLong(1, tableKeyId);
        }
        stmt.bindLong(2, entity.getInfoType());
        stmt.bindLong(3, entity.getId());
        stmt.bindLong(4, entity.getNameMillis());
        stmt.bindDouble(5, entity.getTemp());
 
        String humidity = entity.getHumidity();
        if (humidity != null) {
            stmt.bindString(6, humidity);
        }
        stmt.bindDouble(7, entity.getPressure());
 
        String uv = entity.getUv();
        if (uv != null) {
            stmt.bindString(8, uv);
        }
        stmt.bindDouble(9, entity.getWindSpeed());
        stmt.bindDouble(10, entity.getWindDirection());
        stmt.bindDouble(11, entity.getVisibility());
        stmt.bindDouble(12, entity.getDewPoint());
 
        String condition = entity.getCondition();
        if (condition != null) {
            stmt.bindString(13, condition);
        }
 
        String icon = entity.getIcon();
        if (icon != null) {
            stmt.bindString(14, icon);
        }
        stmt.bindDouble(15, entity.getRainAmount());
 
        String rainProbility = entity.getRainProbility();
        if (rainProbility != null) {
            stmt.bindString(16, rainProbility);
        }
        stmt.bindDouble(17, entity.getSnowAmount());
 
        String snowProbility = entity.getSnowProbility();
        if (snowProbility != null) {
            stmt.bindString(18, snowProbility);
        }
 
        String thunderStormProbility = entity.getThunderStormProbility();
        if (thunderStormProbility != null) {
            stmt.bindString(19, thunderStormProbility);
        }
        stmt.bindDouble(20, entity.getRealFeelHigh());
        stmt.bindDouble(21, entity.getRealFeelLow());
        stmt.bindDouble(22, entity.getRealFeel());
        stmt.bindLong(23, entity.getSunriseMillis());
        stmt.bindLong(24, entity.getSunsetMillis());
        stmt.bindDouble(25, entity.getHighTemp());
        stmt.bindDouble(26, entity.getLowTemp());
 
        String windSpeedUnit = entity.getWindSpeedUnit();
        if (windSpeedUnit != null) {
            stmt.bindString(27, windSpeedUnit);
        }
        stmt.bindLong(28, entity.getIsSunRiseExist() ? 1L: 0L);
        stmt.bindLong(29, entity.getIsSunSetExist() ? 1L: 0L);
        stmt.bindDouble(30, entity.getPm25());
 
        String formattedSunRise = entity.getFormattedSunRise();
        if (formattedSunRise != null) {
            stmt.bindString(31, formattedSunRise);
        }
 
        String formattedSunSet = entity.getFormattedSunSet();
        if (formattedSunSet != null) {
            stmt.bindString(32, formattedSunSet);
        }
 
        String formattedWindDirection = entity.getFormattedWindDirection();
        if (formattedWindDirection != null) {
            stmt.bindString(33, formattedWindDirection);
        }
 
        String formattedTimeName = entity.getFormattedTimeName();
        if (formattedTimeName != null) {
            stmt.bindString(34, formattedTimeName);
        }
        stmt.bindLong(35, entity.getIsLight() ? 1L: 0L);
        stmt.bindLong(36, entity.getIsDewpointExist() ? 1L: 0L);
        stmt.bindLong(37, entity.getIsPressureExist() ? 1L: 0L);
        stmt.bindLong(38, entity.getIsUvindexExist() ? 1L: 0L);
        stmt.bindLong(39, entity.getIsVisibilityExist() ? 1L: 0L);
        stmt.bindLong(40, entity.getMoonRiseMillis());
        stmt.bindLong(41, entity.getMoonSetMillis());
        stmt.bindLong(42, entity.getIsMoonRiseExist() ? 1L: 0L);
        stmt.bindLong(43, entity.getIsMoonSetExist() ? 1L: 0L);
 
        String formattedMoonRise = entity.getFormattedMoonRise();
        if (formattedMoonRise != null) {
            stmt.bindString(44, formattedMoonRise);
        }
 
        String formattedMoonSet = entity.getFormattedMoonSet();
        if (formattedMoonSet != null) {
            stmt.bindString(45, formattedMoonSet);
        }
        stmt.bindLong(46, entity.getDayTimeMillis());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public WeatherRawInfo readEntity(Cursor cursor, int offset) {
        WeatherRawInfo entity = new WeatherRawInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // tableKeyId
            cursor.getInt(offset + 1), // infoType
            cursor.getInt(offset + 2), // id
            cursor.getLong(offset + 3), // nameMillis
            cursor.getDouble(offset + 4), // temp
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // humidity
            cursor.getDouble(offset + 6), // pressure
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // uv
            cursor.getDouble(offset + 8), // windSpeed
            cursor.getDouble(offset + 9), // windDirection
            cursor.getDouble(offset + 10), // visibility
            cursor.getDouble(offset + 11), // dewPoint
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // condition
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // icon
            cursor.getDouble(offset + 14), // rainAmount
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // rainProbility
            cursor.getDouble(offset + 16), // snowAmount
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // snowProbility
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // thunderStormProbility
            cursor.getDouble(offset + 19), // realFeelHigh
            cursor.getDouble(offset + 20), // realFeelLow
            cursor.getDouble(offset + 21), // realFeel
            cursor.getLong(offset + 22), // sunriseMillis
            cursor.getLong(offset + 23), // sunsetMillis
            cursor.getDouble(offset + 24), // highTemp
            cursor.getDouble(offset + 25), // lowTemp
            cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26), // windSpeedUnit
            cursor.getShort(offset + 27) != 0, // isSunRiseExist
            cursor.getShort(offset + 28) != 0, // isSunSetExist
            cursor.getDouble(offset + 29), // pm25
            cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30), // formattedSunRise
            cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31), // formattedSunSet
            cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32), // formattedWindDirection
            cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33), // formattedTimeName
            cursor.getShort(offset + 34) != 0, // isLight
            cursor.getShort(offset + 35) != 0, // isDewpointExist
            cursor.getShort(offset + 36) != 0, // isPressureExist
            cursor.getShort(offset + 37) != 0, // isUvindexExist
            cursor.getShort(offset + 38) != 0, // isVisibilityExist
            cursor.getLong(offset + 39), // moonRiseMillis
            cursor.getLong(offset + 40), // moonSetMillis
            cursor.getShort(offset + 41) != 0, // isMoonRiseExist
            cursor.getShort(offset + 42) != 0, // isMoonSetExist
            cursor.isNull(offset + 43) ? null : cursor.getString(offset + 43), // formattedMoonRise
            cursor.isNull(offset + 44) ? null : cursor.getString(offset + 44), // formattedMoonSet
            cursor.getLong(offset + 45) // dayTimeMillis
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, WeatherRawInfo entity, int offset) {
        entity.setTableKeyId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setInfoType(cursor.getInt(offset + 1));
        entity.setId(cursor.getInt(offset + 2));
        entity.setNameMillis(cursor.getLong(offset + 3));
        entity.setTemp(cursor.getDouble(offset + 4));
        entity.setHumidity(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setPressure(cursor.getDouble(offset + 6));
        entity.setUv(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setWindSpeed(cursor.getDouble(offset + 8));
        entity.setWindDirection(cursor.getDouble(offset + 9));
        entity.setVisibility(cursor.getDouble(offset + 10));
        entity.setDewPoint(cursor.getDouble(offset + 11));
        entity.setCondition(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setIcon(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setRainAmount(cursor.getDouble(offset + 14));
        entity.setRainProbility(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setSnowAmount(cursor.getDouble(offset + 16));
        entity.setSnowProbility(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setThunderStormProbility(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setRealFeelHigh(cursor.getDouble(offset + 19));
        entity.setRealFeelLow(cursor.getDouble(offset + 20));
        entity.setRealFeel(cursor.getDouble(offset + 21));
        entity.setSunriseMillis(cursor.getLong(offset + 22));
        entity.setSunsetMillis(cursor.getLong(offset + 23));
        entity.setHighTemp(cursor.getDouble(offset + 24));
        entity.setLowTemp(cursor.getDouble(offset + 25));
        entity.setWindSpeedUnit(cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26));
        entity.setIsSunRiseExist(cursor.getShort(offset + 27) != 0);
        entity.setIsSunSetExist(cursor.getShort(offset + 28) != 0);
        entity.setPm25(cursor.getDouble(offset + 29));
        entity.setFormattedSunRise(cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30));
        entity.setFormattedSunSet(cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31));
        entity.setFormattedWindDirection(cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32));
        entity.setFormattedTimeName(cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33));
        entity.setIsLight(cursor.getShort(offset + 34) != 0);
        entity.setIsDewpointExist(cursor.getShort(offset + 35) != 0);
        entity.setIsPressureExist(cursor.getShort(offset + 36) != 0);
        entity.setIsUvindexExist(cursor.getShort(offset + 37) != 0);
        entity.setIsVisibilityExist(cursor.getShort(offset + 38) != 0);
        entity.setMoonRiseMillis(cursor.getLong(offset + 39));
        entity.setMoonSetMillis(cursor.getLong(offset + 40));
        entity.setIsMoonRiseExist(cursor.getShort(offset + 41) != 0);
        entity.setIsMoonSetExist(cursor.getShort(offset + 42) != 0);
        entity.setFormattedMoonRise(cursor.isNull(offset + 43) ? null : cursor.getString(offset + 43));
        entity.setFormattedMoonSet(cursor.isNull(offset + 44) ? null : cursor.getString(offset + 44));
        entity.setDayTimeMillis(cursor.getLong(offset + 45));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(WeatherRawInfo entity, long rowId) {
        entity.setTableKeyId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(WeatherRawInfo entity) {
        if(entity != null) {
            return entity.getTableKeyId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(WeatherRawInfo entity) {
        return entity.getTableKeyId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}