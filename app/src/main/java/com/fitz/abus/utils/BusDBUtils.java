package com.fitz.abus.utils;

import com.fitz.abus.Bean.BusLineBean;
import com.fitz.abus.FitzApplication;

import java.util.List;

public class BusDBUtils {

    private BusDBUtils() {
    }

    public static BusDBUtils getInstance(){
        return ABusDataHolder.ABusData;
    }

    private static class ABusDataHolder{
        private static final BusDBUtils ABusData = new BusDBUtils();
    }

    public void insertBus(BusLineBean busLine) {
        FitzApplication.getDaoInstant().getBusLineBeanDao().insertOrReplace(busLine);
    }

    public void deleteBus(BusLineBean busLine) {
        FitzApplication.getDaoInstant().getBusLineBeanDao().delete(busLine);
    }

    public void uodateBus(BusLineBean busLine) {
        FitzApplication.getDaoInstant().getBusLineBeanDao().update(busLine);
    }

    public List<BusLineBean> queryAllBus() {
        return FitzApplication.getDaoInstant().getBusLineBeanDao().loadAll();
    }

    public List<BusLineBean> queryRawBus(String cityID) {
        return FitzApplication.getDaoInstant().getBusLineBeanDao().queryRaw("where CITY_ID = ?", cityID);
    }
}
