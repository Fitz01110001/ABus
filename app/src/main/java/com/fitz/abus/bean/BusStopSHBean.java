package com.fitz.abus.bean;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.bean
 * @ClassName: BusStopSHBean
 * @Author: Fitz
 * @CreateDate: 2018/12/23 17:38
 * GSON数据类，不要改动
 */
public class BusStopSHBean {

    private LineResults lineResults1;
    private LineResults lineResults0;
    public void setLineResults1(LineResults lineResults1) {
        this.lineResults1 = lineResults1;
    }
    public LineResults getLineResults1() {
        return lineResults1;
    }

    public void setLineResults0(LineResults lineResults0) {
        this.lineResults0 = lineResults0;
    }
    public LineResults getLineResults0() {
        return lineResults0;
    }

    public boolean nonNull(){
        return getLineResults1() != null && getLineResults0() != null;
    }
}
