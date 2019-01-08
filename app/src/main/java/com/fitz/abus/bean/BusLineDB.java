package com.fitz.abus.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class BusLineDB {

    @Id(autoincrement = true) private Long busID;

    @NotNull private String cityID;

    @NotNull private String lineID;

    @NotNull private String lineName;

    @NotNull private String stationID;

    @Unique
    @NotNull
    private String stationName;

    @NotNull private int direction;

    @NotNull private String startStop;

    @NotNull private String endStop;

    @NotNull private String startEarlyTime;

    @NotNull private String startLateTime;

    @NotNull private String endEarlyTime;

    @NotNull private String endLateTime;


    @Generated(hash = 222292962)
    public BusLineDB(Long busID, @NotNull String cityID, @NotNull String lineID, @NotNull String lineName, @NotNull String stationID, @NotNull
            String stationName, int direction, @NotNull String startStop, @NotNull String endStop, @NotNull String startEarlyTime, @NotNull String
            startLateTime, @NotNull String endEarlyTime, @NotNull String endLateTime) {
        this.busID = busID;
        this.cityID = cityID;
        this.lineID = lineID;
        this.lineName = lineName;
        this.stationID = stationID;
        this.stationName = stationName;
        this.direction = direction;
        this.startStop = startStop;
        this.endStop = endStop;
        this.startEarlyTime = startEarlyTime;
        this.startLateTime = startLateTime;
        this.endEarlyTime = endEarlyTime;
        this.endLateTime = endLateTime;
    }

    @Generated(hash = 1982642731)
    public BusLineDB() {
    }

    @Override
    public String toString() {
        return "busID:" + busID + ",cityID:" + cityID + ",lineID:" + lineID + ",lineName:" + lineName + ",stationID:" + stationID + ",stationName:"
               + stationName;
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

    public String getStationID() {
        return this.stationID;
    }

    public void setStationID(String stationID) {
        this.stationID = stationID;
    }

    public String getStationName() {
        return this.stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public int getDirection() {
        return this.direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getStartStop() {
        return this.startStop;
    }

    public void setStartStop(String startStop) {
        this.startStop = startStop;
    }

    public String getEndStop() {
        return this.endStop;
    }

    public void setEndStop(String endStop) {
        this.endStop = endStop;
    }

    public String getStartEarlyTime() {
        return this.startEarlyTime;
    }

    public void setStartEarlyTime(String startEarlyTime) {
        this.startEarlyTime = startEarlyTime;
    }

    public String getStartLateTime() {
        return this.startLateTime;
    }

    public void setStartLateTime(String startLateTime) {
        this.startLateTime = startLateTime;
    }

    public String getEndEarlyTime() {
        return this.endEarlyTime;
    }

    public void setEndEarlyTime(String endEarlyTime) {
        this.endEarlyTime = endEarlyTime;
    }

    public String getEndLateTime() {
        return this.endLateTime;
    }

    public void setEndLateTime(String endLateTime) {
        this.endLateTime = endLateTime;
    }

}
