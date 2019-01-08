package com.fitz.abus.bean;

import java.util.List;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.bean
 * @ClassName: LineResults
 * @Author: Fitz
 * @CreateDate: 2018/12/23 17:39
 * GSON数据类，不要改动
 */
public class LineResults {

    private List<Stops> stops;
    private String direction;

    public void setStops(List<Stops> stops) {
        this.stops = stops;
    }

    public List<Stops> getStops() {
        return stops;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

}
