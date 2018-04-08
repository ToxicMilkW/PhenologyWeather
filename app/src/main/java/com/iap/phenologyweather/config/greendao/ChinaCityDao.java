package com.iap.phenologyweather.config.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.iap.phenologyweather.data.model.ChinaCity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "area".
*/
public class ChinaCityDao extends AbstractDao<ChinaCity, Long> {

    public static final String TABLENAME = "area";

    /**
     * Properties of entity ChinaCity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property _id = new Property(0, Long.class, "_id", true, "_id");
        public final static Property Id = new Property(1, int.class, "id", false, "id");
        public final static Property Pid = new Property(2, int.class, "pid", false, "pid");
        public final static Property Name = new Property(3, String.class, "name", false, "name");
        public final static Property Deep = new Property(4, int.class, "deep", false, "deep");
        public final static Property Lat = new Property(5, double.class, "lat", false, "lat");
        public final static Property Lng = new Property(6, double.class, "lng", false, "lng");
        public final static Property CityId = new Property(7, String.class, "cityId", false, "cityid");
    }


    public ChinaCityDao(DaoConfig config) {
        super(config);
    }
    
    public ChinaCityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"area\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: _id
                "\"id\" INTEGER NOT NULL ," + // 1: id
                "\"pid\" INTEGER NOT NULL ," + // 2: pid
                "\"name\" TEXT," + // 3: name
                "\"deep\" INTEGER NOT NULL ," + // 4: deep
                "\"lat\" REAL NOT NULL ," + // 5: lat
                "\"lng\" REAL NOT NULL ," + // 6: lng
                "\"cityid\" TEXT);"); // 7: cityId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"area\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ChinaCity entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
        stmt.bindLong(2, entity.getId());
        stmt.bindLong(3, entity.getPid());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
        stmt.bindLong(5, entity.getDeep());
        stmt.bindDouble(6, entity.getLat());
        stmt.bindDouble(7, entity.getLng());
 
        String cityId = entity.getCityId();
        if (cityId != null) {
            stmt.bindString(8, cityId);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ChinaCity entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
        stmt.bindLong(2, entity.getId());
        stmt.bindLong(3, entity.getPid());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
        stmt.bindLong(5, entity.getDeep());
        stmt.bindDouble(6, entity.getLat());
        stmt.bindDouble(7, entity.getLng());
 
        String cityId = entity.getCityId();
        if (cityId != null) {
            stmt.bindString(8, cityId);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public ChinaCity readEntity(Cursor cursor, int offset) {
        ChinaCity entity = new ChinaCity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // _id
            cursor.getInt(offset + 1), // id
            cursor.getInt(offset + 2), // pid
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // name
            cursor.getInt(offset + 4), // deep
            cursor.getDouble(offset + 5), // lat
            cursor.getDouble(offset + 6), // lng
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // cityId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ChinaCity entity, int offset) {
        entity.set_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setId(cursor.getInt(offset + 1));
        entity.setPid(cursor.getInt(offset + 2));
        entity.setName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setDeep(cursor.getInt(offset + 4));
        entity.setLat(cursor.getDouble(offset + 5));
        entity.setLng(cursor.getDouble(offset + 6));
        entity.setCityId(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ChinaCity entity, long rowId) {
        entity.set_id(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ChinaCity entity) {
        if(entity != null) {
            return entity.get_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ChinaCity entity) {
        return entity.get_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
