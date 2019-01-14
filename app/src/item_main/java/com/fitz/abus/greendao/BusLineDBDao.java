package com.fitz.abus.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.fitz.abus.bean.BusLineDB;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "BUS_LINE_DB".
*/
public class BusLineDBDao extends AbstractDao<BusLineDB, Long> {

    public static final String TABLENAME = "BUS_LINE_DB";

    /**
     * Properties of entity BusBaseInfoDB.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property BusID = new Property(0, Long.class, "busID", true, "_id");
        public final static Property CityID = new Property(1, String.class, "cityID", false, "CITY_ID");
        public final static Property LineID = new Property(2, String.class, "lineID", false, "LINE_ID");
        public final static Property LineName = new Property(3, String.class, "lineName", false, "LINE_NAME");
        public final static Property StationID = new Property(4, String.class, "stationID", false, "STATION_ID");
        public final static Property StationName = new Property(5, String.class, "stationName", false, "STATION_NAME");
        public final static Property Direction = new Property(6, int.class, "direction", false, "DIRECTION");
        public final static Property StartStop = new Property(7, String.class, "startStop", false, "START_STOP");
        public final static Property EndStop = new Property(8, String.class, "endStop", false, "END_STOP");
        public final static Property StartEarlyTime = new Property(9, String.class, "startEarlyTime", false, "START_EARLY_TIME");
        public final static Property StartLateTime = new Property(10, String.class, "startLateTime", false, "START_LATE_TIME");
        public final static Property EndEarlyTime = new Property(11, String.class, "endEarlyTime", false, "END_EARLY_TIME");
        public final static Property EndLateTime = new Property(12, String.class, "endLateTime", false, "END_LATE_TIME");
    }


    public BusLineDBDao(DaoConfig config) {
        super(config);
    }
    
    public BusLineDBDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"BUS_LINE_DB\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: busID
                "\"CITY_ID\" TEXT NOT NULL ," + // 1: cityID
                "\"LINE_ID\" TEXT NOT NULL ," + // 2: lineID
                "\"LINE_NAME\" TEXT NOT NULL ," + // 3: lineName
                "\"STATION_ID\" TEXT NOT NULL ," + // 4: stationID
                "\"STATION_NAME\" TEXT NOT NULL UNIQUE ," + // 5: stationName
                "\"DIRECTION\" INTEGER NOT NULL ," + // 6: direction
                "\"START_STOP\" TEXT NOT NULL ," + // 7: startStop
                "\"END_STOP\" TEXT NOT NULL ," + // 8: endStop
                "\"START_EARLY_TIME\" TEXT NOT NULL ," + // 9: startEarlyTime
                "\"START_LATE_TIME\" TEXT NOT NULL ," + // 10: startLateTime
                "\"END_EARLY_TIME\" TEXT NOT NULL ," + // 11: endEarlyTime
                "\"END_LATE_TIME\" TEXT NOT NULL );"); // 12: endLateTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BUS_LINE_DB\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, BusLineDB entity) {
        stmt.clearBindings();
 
        Long busID = entity.getBusID();
        if (busID != null) {
            stmt.bindLong(1, busID);
        }
        stmt.bindString(2, entity.getCityID());
        stmt.bindString(3, entity.getLineID());
        stmt.bindString(4, entity.getLineName());
        stmt.bindString(5, entity.getStationID());
        stmt.bindString(6, entity.getStationName());
        stmt.bindLong(7, entity.getDirection());
        stmt.bindString(8, entity.getStartStop());
        stmt.bindString(9, entity.getEndStop());
        stmt.bindString(10, entity.getStartEarlyTime());
        stmt.bindString(11, entity.getStartLateTime());
        stmt.bindString(12, entity.getEndEarlyTime());
        stmt.bindString(13, entity.getEndLateTime());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, BusLineDB entity) {
        stmt.clearBindings();
 
        Long busID = entity.getBusID();
        if (busID != null) {
            stmt.bindLong(1, busID);
        }
        stmt.bindString(2, entity.getCityID());
        stmt.bindString(3, entity.getLineID());
        stmt.bindString(4, entity.getLineName());
        stmt.bindString(5, entity.getStationID());
        stmt.bindString(6, entity.getStationName());
        stmt.bindLong(7, entity.getDirection());
        stmt.bindString(8, entity.getStartStop());
        stmt.bindString(9, entity.getEndStop());
        stmt.bindString(10, entity.getStartEarlyTime());
        stmt.bindString(11, entity.getStartLateTime());
        stmt.bindString(12, entity.getEndEarlyTime());
        stmt.bindString(13, entity.getEndLateTime());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public BusLineDB readEntity(Cursor cursor, int offset) {
        BusLineDB entity = new BusLineDB( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // busID
            cursor.getString(offset + 1), // cityID
            cursor.getString(offset + 2), // lineID
            cursor.getString(offset + 3), // lineName
            cursor.getString(offset + 4), // stationID
            cursor.getString(offset + 5), // stationName
            cursor.getInt(offset + 6), // direction
            cursor.getString(offset + 7), // startStop
            cursor.getString(offset + 8), // endStop
            cursor.getString(offset + 9), // startEarlyTime
            cursor.getString(offset + 10), // startLateTime
            cursor.getString(offset + 11), // endEarlyTime
            cursor.getString(offset + 12) // endLateTime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, BusLineDB entity, int offset) {
        entity.setBusID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCityID(cursor.getString(offset + 1));
        entity.setLineID(cursor.getString(offset + 2));
        entity.setLineName(cursor.getString(offset + 3));
        entity.setStationID(cursor.getString(offset + 4));
        entity.setStationName(cursor.getString(offset + 5));
        entity.setDirection(cursor.getInt(offset + 6));
        entity.setStartStop(cursor.getString(offset + 7));
        entity.setEndStop(cursor.getString(offset + 8));
        entity.setStartEarlyTime(cursor.getString(offset + 9));
        entity.setStartLateTime(cursor.getString(offset + 10));
        entity.setEndEarlyTime(cursor.getString(offset + 11));
        entity.setEndLateTime(cursor.getString(offset + 12));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(BusLineDB entity, long rowId) {
        entity.setBusID(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(BusLineDB entity) {
        if(entity != null) {
            return entity.getBusID();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(BusLineDB entity) {
        return entity.getBusID() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
