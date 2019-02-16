package com.fitz.abus.utils;

import android.util.Log;

import com.fitz.abus.FitzApplication;
import com.fitz.abus.bean.BusBaseInfoDB;
import com.fitz.abus.greendao.BusBaseInfoDBDao;

import java.util.List;

public class FitzDBUtils {

    private static final String TAG = "FitzDBUtils";

    private FitzDBUtils() {
    }

    public static FitzDBUtils getInstance() {
        return ABusDataHolder.ABusData;
    }

    public void insertBus(BusBaseInfoDB busBaseInfo) {
        Log.d(TAG, "insertBus busBaseInfo:" + busBaseInfo.toString());
        if (isNewData(busBaseInfo)) {
            busBaseInfo.setId(null);
            FitzApplication.getDaoInstant().getBusBaseInfoDBDao().insertOrReplace(busBaseInfo);
            FitzApplication.getDaoInstant().clear();
        }
    }

    public void deleteBus(BusBaseInfoDB busBaseInfo) {
        FitzApplication.getDaoInstant().getBusBaseInfoDBDao().delete(busBaseInfo);
    }

    public void updateBus(BusBaseInfoDB busBaseInfo) {
        FitzApplication.getDaoInstant().getBusBaseInfoDBDao().update(busBaseInfo);
    }

    public int getDataSize() {
        return FitzApplication.getDaoInstant().getBusBaseInfoDBDao().loadAll().size();
    }

    public List<BusBaseInfoDB> queryRawBusWhereCityID(String cityID) {
        return FitzApplication.getDaoInstant().getBusBaseInfoDBDao().queryRaw("where CITY_ID = ?", cityID);
    }

    public boolean isNewData(BusBaseInfoDB busBaseInfo) {
        BusBaseInfoDBDao busBaseInfoDBDao = FitzApplication.getDaoInstant().getBusBaseInfoDBDao();
        List<BusBaseInfoDB> l = busBaseInfoDBDao.queryBuilder().where(BusBaseInfoDBDao.Properties.BusName.eq(busBaseInfo.getBusName()))
                                                .where(BusBaseInfoDBDao.Properties.CityID.eq(busBaseInfo.getCityID()))
                                                .where(BusBaseInfoDBDao.Properties.Direction.eq(busBaseInfo.getDirection()))
                                                .where(BusBaseInfoDBDao.Properties.StationName.eq(busBaseInfo.getStationName())).build().list();

        Log.d(TAG, "l:" + l.size());
        return l.size() == 0;
    }

    private static class ABusDataHolder {
        private static final FitzDBUtils ABusData = new FitzDBUtils();
    }


}
