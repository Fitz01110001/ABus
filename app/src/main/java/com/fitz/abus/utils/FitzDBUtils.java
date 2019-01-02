package com.fitz.abus.utils;

import com.fitz.abus.bean.BusLineDB;
import com.fitz.abus.FitzApplication;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class FitzDBUtils {

    private FitzDBUtils() {
    }

    public static FitzDBUtils getInstance(){
        return ABusDataHolder.ABusData;
    }

    private static class ABusDataHolder{
        private static final FitzDBUtils ABusData = new FitzDBUtils();
    }

    public void insertBus(BusLineDB busLine) {
        //FitzApplication.getDaoInstant().getBusLineDBDao().insertOrReplace(busLine);
        FitzApplication.getDaoInstant().getBusLineDBDao().insert(busLine);
    }

    public void deleteBus(BusLineDB busLine) {
        FitzApplication.getDaoInstant().getBusLineDBDao().delete(busLine);
    }

    public void uodateBus(BusLineDB busLine) {
        FitzApplication.getDaoInstant().getBusLineDBDao().update(busLine);
    }

    public List<BusLineDB> queryAllBus() {
        return FitzApplication.getDaoInstant().getBusLineDBDao().loadAll();
    }

    public List<BusLineDB> queryRawBusWhereCityID(String cityID) {
        return FitzApplication.getDaoInstant().getBusLineDBDao().queryRaw("where CITY_ID = ?", cityID);
    }


}
