package com.fitz.abus.utils;

import android.util.Log;

import com.fitz.abus.FitzApplication;
import com.fitz.abus.bean.BusBaseInfoDB;

import java.util.List;

public class FitzDBUtils {

    private static final String TAG = "FitzDBUtils";

    private FitzDBUtils() {
    }

    public static FitzDBUtils getInstance() {
        return ABusDataHolder.ABusData;
    }

    public void insertBus(BusBaseInfoDB busBaseInfo) {
        Log.d(TAG,"insertBus busBaseInfo:"+busBaseInfo.toString());
        //FitzApplication.getDaoInstant().getBusBaseInfoDBDao().insertOrReplace(busBaseInfo);
        if (isNewData(busBaseInfo)) {
            FitzApplication.getDaoInstant().getBusBaseInfoDBDao().insert(busBaseInfo);
            FitzApplication.getDaoInstant().clear();
        }
    }

    public void deleteBus(BusBaseInfoDB busBaseInfo) {
        FitzApplication.getDaoInstant().getBusBaseInfoDBDao().delete(busBaseInfo);
    }

    public void updateBus(BusBaseInfoDB busBaseInfo) {
        FitzApplication.getDaoInstant().getBusBaseInfoDBDao().update(busBaseInfo);
    }

    public List<BusBaseInfoDB> queryAllBus() {
        return FitzApplication.getDaoInstant().getBusBaseInfoDBDao().loadAll();
    }

    public List<BusBaseInfoDB> queryRawBusWhereCityID(String cityID) {
        return FitzApplication.getDaoInstant().getBusBaseInfoDBDao().queryRaw("where CITY_ID = ?", cityID);
    }

    public boolean isNewData(BusBaseInfoDB busBaseInfo) {
        return FitzApplication.getDaoInstant()
                              .getBusBaseInfoDBDao()
                              .queryRaw("where CITY_ID = ? AND BUS_NAME = ? AND BUS_NAME = ? AND STATION_NAME = ?",
                                        new String[]{busBaseInfo.getCityID() + busBaseInfo.getBusName() + busBaseInfo.getStationName()}).size() == 0;
    }

    private static class ABusDataHolder {
        private static final FitzDBUtils ABusData = new FitzDBUtils();
    }


}
