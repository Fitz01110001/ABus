package com.wind.fitz.greendaodemo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class BusLine {

    @Id
    private Long busID;

    @Unique
    @NotNull
    private String lineID;

    @Unique
    @NotNull
    private String lineName;

    @Unique
    @NotNull
    private int stationID;

    @Unique
    @NotNull
    private String stationName;

    private Long c_id;
    @ToOne(joinProperty = "c_id")
    private City city;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 780868089)
    private transient BusLineDao myDao;



    @Generated(hash = 1589562230)
    public BusLine(Long busID, @NotNull String lineID, @NotNull String lineName,
            int stationID, @NotNull String stationName, Long c_id) {
        this.busID = busID;
        this.lineID = lineID;
        this.lineName = lineName;
        this.stationID = stationID;
        this.stationName = stationName;
        this.c_id = c_id;
    }

    @Generated(hash = 1871503588)
    public BusLine() {
    }

    @Generated(hash = 1696970556)
    private transient Long city__resolvedKey;



    public long getBusID() {
        return this.busID;
    }

    public void setBusID(long busID) {
        this.busID = busID;
    }

    public String getLineID() {
        return this.lineID;
    }

    public void setLineID(String lineID) {
        this.lineID = lineID;
    }

    public String getLineName() {
        return this.lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public int getStationID() {
        return this.stationID;
    }

    public void setStationID(int stationID) {
        this.stationID = stationID;
    }

    public String getStationName() {
        return this.stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public void setBusID(Long busID) {
        this.busID = busID;
    }

    public Long getC_id() {
        return this.c_id;
    }

    public void setC_id(Long c_id) {
        this.c_id = c_id;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 305276241)
    public City getCity() {
        Long __key = this.c_id;
        if (city__resolvedKey == null || !city__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CityDao targetDao = daoSession.getCityDao();
            City cityNew = targetDao.load(__key);
            synchronized (this) {
                city = cityNew;
                city__resolvedKey = __key;
            }
        }
        return city;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1543385931)
    public void setCity(City city) {
        synchronized (this) {
            this.city = city;
            c_id = city == null ? null : city.getCityID();
            city__resolvedKey = c_id;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1574118397)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBusLineDao() : null;
    }
}
