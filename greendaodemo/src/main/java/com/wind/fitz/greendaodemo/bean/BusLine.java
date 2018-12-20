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

    @Id(autoincrement = true)
    private Long busID;

    @NotNull
    private String cityID;

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

    @Generated(hash = 1098651909)
    public BusLine(Long busID, @NotNull String cityID, @NotNull String lineID, @NotNull String lineName, int stationID,
            @NotNull String stationName) {
        this.busID = busID;
        this.cityID = cityID;
        this.lineID = lineID;
        this.lineName = lineName;
        this.stationID = stationID;
        this.stationName = stationName;
    }

    @Generated(hash = 1871503588)
    public BusLine() {
    }

    @Override
    public String toString(){
        return  "busID:"+busID +",cityID:"+cityID+",lineID:"+lineID+",lineName:"+lineName+",stationID:"+stationID+",stationName:"+stationName;
    }

    public Long getBusID() {
        return this.busID;
    }

    public void setBusID(Long busID) {
        this.busID = busID;
    }

    public String getCityID() {
        return this.cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
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


}
