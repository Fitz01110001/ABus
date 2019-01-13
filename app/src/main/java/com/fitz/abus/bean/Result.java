package com.fitz.abus.bean;

import java.util.List;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.bean
 * @ClassName: Result
 * @Author: Fitz
 * @CreateDate: 2019/1/13 18:36
 */
public class Result {

    private List<String> list;

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }

    private String lineName;
    private String startendTime;
    private String interval;
    private String price;
    private String upLine;
    private List<List<String>> upLineStationList;
    private String downLine;
    private List<List<String>> downLineStationList;

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getLineName() {
        return lineName;
    }

    public void setStartendTime(String startendTime) {
        this.startendTime = startendTime;
    }

    public String getStartendTime() {
        return startendTime;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getInterval() {
        return interval;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setUpLine(String upLine) {
        this.upLine = upLine;
    }

    public String getUpLine() {
        return upLine;
    }

    public void setUpLineStationList(List<List<String>> upLineStationList) {
        this.upLineStationList = upLineStationList;
    }

    public List<List<String>> getUpLineStationList() {
        return upLineStationList;
    }

    public void setDownLine(String downLine) {
        this.downLine = downLine;
    }

    public String getDownLine() {
        return downLine;
    }

    public void setDownLineStationList(List<List<String>> downLineStationList) {
        this.downLineStationList = downLineStationList;
    }

    public List<List<String>> getDownLineStationList() {
        return downLineStationList;
    }

    @Override
    public String toString() {
        return getLineName() + " " + getStartendTime() + " " + getUpLine() + " " + getUpLineStationList()
                .toString() + " " + getDownLine() + " " + "" + getDownLineStationList().toString();
    }
}
