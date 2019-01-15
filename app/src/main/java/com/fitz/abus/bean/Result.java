package com.fitz.abus.bean;

import java.util.List;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.bean
 * @ClassName: Result
 * @Author: Fitz
 * @CreateDate: 2019/1/13 18:36
 * GSON数据类，不要改动
 */
public class Result {

    private List<String> list;
    private String lineName;
    private String startendTime;
    private String interval;
    private String price;
    private String upLine;
    private List<List<String>> upLineStationList;
    private String downLine;
    private List<List<String>> downLineStationList;
    private String willArriveTime;
    private String distance;
    private int index;
    private String plate;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public String getWillArriveTime() {
        return willArriveTime;
    }

    public void setWillArriveTime(String willArriveTime) {
        this.willArriveTime = willArriveTime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getStartendTime() {
        return startendTime;
    }

    public void setStartendTime(String startendTime) {
        this.startendTime = startendTime;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUpLine() {
        return upLine;
    }

    public void setUpLine(String upLine) {
        this.upLine = upLine;
    }

    public List<List<String>> getUpLineStationList() {
        return upLineStationList;
    }

    public void setUpLineStationList(List<List<String>> upLineStationList) {
        this.upLineStationList = upLineStationList;
    }

    public String getDownLine() {
        return downLine;
    }

    public void setDownLine(String downLine) {
        this.downLine = downLine;
    }

    public List<List<String>> getDownLineStationList() {
        return downLineStationList;
    }

    public void setDownLineStationList(List<List<String>> downLineStationList) {
        this.downLineStationList = downLineStationList;
    }

    @Override
    public String toString() {
        return getLineName() + " " + getStartendTime() + " " + getUpLine() + " " + getUpLineStationList()
                .toString() + " " + getDownLine() + " " + "" + getDownLineStationList().toString();
    }
}
