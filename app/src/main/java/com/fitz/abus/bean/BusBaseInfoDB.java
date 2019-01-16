package com.fitz.abus.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;


@Entity
public class BusBaseInfoDB implements Parcelable {


    @Id(autoincrement = true) private Long id;
    @NotNull private String cityID;
    // 仅上海使用
    @NotNull private String lineId;
    @NotNull private String busName;
    @NotNull private String stationID;
    @NotNull private String stationName;
    @NotNull private int direction;
    @NotNull private String startStopDirection0;
    @NotNull private String endStopDirection0;
    @NotNull private String startEndTimeDirection0;
    @NotNull private String startStopDirection1;
    @NotNull private String endStopDirection1;
    @NotNull private String startEndTimeDirection1;
    @Transient private List<Stops> shStationList0;
    @Transient private List<Stops> shStationList1;
    @Transient private List<List<String>> whStationList0;
    @Transient private List<List<String>> whStationList1;

    protected BusBaseInfoDB(Parcel in) {
        cityID = in.readString();
        lineId = in.readString();
        busName = in.readString();
        stationID = in.readString();
        stationName = in.readString();
        direction = in.readInt();
        startStopDirection0 = in.readString();
        endStopDirection0 = in.readString();
        startEndTimeDirection0 = in.readString();
        startStopDirection1 = in.readString();
        endStopDirection1 = in.readString();
        startEndTimeDirection1 = in.readString();
    }

    @Generated(hash = 1461965588)
    public BusBaseInfoDB(Long id, @NotNull String cityID, @NotNull String lineId, @NotNull String busName, @NotNull String stationID,
            @NotNull String stationName, int direction, @NotNull String startStopDirection0, @NotNull String endStopDirection0,
            @NotNull String startEndTimeDirection0, @NotNull String startStopDirection1, @NotNull String endStopDirection1,
            @NotNull String startEndTimeDirection1) {
        this.id = id;
        this.cityID = cityID;
        this.lineId = lineId;
        this.busName = busName;
        this.stationID = stationID;
        this.stationName = stationName;
        this.direction = direction;
        this.startStopDirection0 = startStopDirection0;
        this.endStopDirection0 = endStopDirection0;
        this.startEndTimeDirection0 = startEndTimeDirection0;
        this.startStopDirection1 = startStopDirection1;
        this.endStopDirection1 = endStopDirection1;
        this.startEndTimeDirection1 = startEndTimeDirection1;
    }

    @Generated(hash = 1933659747)
    public BusBaseInfoDB() {
    }
    

    public static final Creator<BusBaseInfoDB> CREATOR = new Creator<BusBaseInfoDB>() {
        @Override
        public BusBaseInfoDB createFromParcel(Parcel in) {
            return new BusBaseInfoDB(in);
        }

        @Override
        public BusBaseInfoDB[] newArray(int size) {
            return new BusBaseInfoDB[size];
        }
    };

    @Override
    public String toString() {
        return "id:" + id + ",cityID:" + cityID + ",lineId:" + lineId + ",busName:" + busName + ",stationID:" + stationID + ",stationName:" +
               stationName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getStationID() {
        return stationID;
    }

    public void setStationID(String stationID) {
        this.stationID = stationID;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }


    public String getStartStopDirection0() {
        return startStopDirection0;
    }

    public void setStartStopDirection0(String startStopDirection0) {
        this.startStopDirection0 = startStopDirection0;
    }

    public String getEndStopDirection0() {
        return endStopDirection0;
    }

    public void setEndStopDirection0(String endStopDirection0) {
        this.endStopDirection0 = endStopDirection0;
    }

    public String getStartEndTimeDirection0() {
        return startEndTimeDirection0;
    }

    public void setStartEndTimeDirection0(String startEndTimeDirection0) {
        this.startEndTimeDirection0 = startEndTimeDirection0;
    }

    public String getStartStopDirection1() {
        return startStopDirection1;
    }

    public void setStartStopDirection1(String startStopDirection1) {
        this.startStopDirection1 = startStopDirection1;
    }

    public String getEndStopDirection1() {
        return endStopDirection1;
    }

    public void setEndStopDirection1(String endStopDirection1) {
        this.endStopDirection1 = endStopDirection1;
    }

    public String getStartEndTimeDirection1() {
        return startEndTimeDirection1;
    }

    public void setStartEndTimeDirection1(String startEndTimeDirection1) {
        this.startEndTimeDirection1 = startEndTimeDirection1;
    }

    public List<Stops> getShStationList0() {
        return shStationList0;
    }

    public void setShStationList0(List<Stops> shStationList0) {
        this.shStationList0 = shStationList0;
    }

    public List<Stops> getShStationList1() {
        return shStationList1;
    }

    public void setShStationList1(List<Stops> shStationList1) {
        this.shStationList1 = shStationList1;
    }

    public List<List<String>> getWhStationList0() {
        return whStationList0;
    }

    public void setWhStationList0(List<List<String>> whStationList0) {
        this.whStationList0 = whStationList0;
    }

    public List<List<String>> getWhStationList1() {
        return whStationList1;
    }

    public void setWhStationList1(List<List<String>> whStationList1) {
        this.whStationList1 = whStationList1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(cityID);
        parcel.writeString(lineId);
        parcel.writeString(busName);
        parcel.writeString(stationID);
        parcel.writeString(stationName);
        parcel.writeInt(direction);
        parcel.writeString(startStopDirection0);
        parcel.writeString(endStopDirection0);
        parcel.writeString(startEndTimeDirection0);
        parcel.writeString(startStopDirection1);
        parcel.writeString(endStopDirection1);
        parcel.writeString(startEndTimeDirection1);
    }
}
