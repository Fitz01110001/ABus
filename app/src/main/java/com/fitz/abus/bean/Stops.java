package com.fitz.abus.bean;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.bean
 * @ClassName: Stops
 * @Author: Fitz
 * @CreateDate: 2018/12/23 17:39
 * GSON数据类，不要改动
 */
public class Stops {

    private String id;
    private String zdmc;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setZdmc(String zdmc) {
        this.zdmc = zdmc;
    }

    public String getZdmc() {
        return zdmc;
    }

    @Override
    public String toString() {
        return getZdmc();
    }
}
