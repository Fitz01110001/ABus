package com.fitz.abus.bean;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.bean
 * @ClassName: ArriveBusInfo
 * @Author: Fitz
 * @CreateDate: 2019/1/15 22:22
 */
public class ArriveBusInfo {
    private String busName;
    private String stationId;

    public ArriveBusInfo(String busName, String stationId) {
        setBusName(busName);
        setStationId(stationId);
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }
}
