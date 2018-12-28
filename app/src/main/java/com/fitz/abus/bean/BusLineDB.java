package com.fitz.abus.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class BusLineDB {

    @Id(autoincrement = true)
    private Long busID;

    @NotNull
    private String cityID;

    @Unique
    @NotNull
    private String lineID;

    @NotNull
    private String lineName;

    @NotNull
    private String stationID;

    @NotNull
    private String stationName;

    @NotNull
    private int direction;

    @NotNull
    private String startStop;

    @NotNull
    private String endStop;

    @NotNull
    private String startTime;

    @NotNull
    private String endTime;



    @Generated(hash = 1650418293)
    public BusLineDB(Long busID, @NotNull String cityID, @NotNull String lineID, @NotNull String lineName, @NotNull String stationID, @NotNull String stationName,
            int direction, @NotNull String startStop, @NotNull String endStop, @NotNull String startTime, @NotNull String endTime) {
        this.busID = busID;
        this.cityID = cityID;
        this.lineID = lineID;
        this.lineName = lineName;
        this.stationID = stationID;
        this.stationName = stationName;
        this.direction = direction;
        this.startStop = startStop;
        this.endStop = endStop;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Generated(hash = 1982642731)
    public BusLineDB() {
    }

    @Override
    public String toString() {
        return "busID:" + busID + ",cityID:" + cityID + ",lineID:" + lineID + ",lineName:" + lineName + ",stationID:" + stationID + ",stationName:" + stationName;
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

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

}
