package com.fitz.abus.bean;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.bean
 * @ClassName: Cars
 * @Author: Fitz
 * @CreateDate: 2018/12/23 17:42
 * GSON数据类，不要改动
 */
public class Cars {

    private String time;
    private String distance;
    private String terminal;
    private String stopdis;
    public void setTime(String time) {
        this.time = time;
    }
    public String getTime() {
        return time;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
    public String getDistance() {
        return distance;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }
    public String getTerminal() {
        return terminal;
    }

    public void setStopdis(String stopdis) {
        this.stopdis = stopdis;
    }
    public String getStopdis() {
        return stopdis;
    }

}
