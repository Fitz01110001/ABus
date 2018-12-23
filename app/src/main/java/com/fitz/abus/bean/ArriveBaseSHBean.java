package com.fitz.abus.bean;

import java.util.List;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.bean
 * @ClassName: ArriveBaseSHBean
 * @Author: Fitz
 * @CreateDate: 2018/12/23 17:42
 */
public class ArriveBaseSHBean {

    private List<Cars> cars;
    public void setCars(List<Cars> cars) {
        this.cars = cars;
    }
    public List<Cars> getCars() {
        return cars;
    }

}
