package com.wind.fitz.greendaodemo.bean;

import com.wind.fitz.greendaodemo.MyAppliaction;

import java.util.List;

public class BusLineDBUtils {

    private BusLineDBUtils() {
    }

    public static BusLineDBUtils getInstance(){
        return ABusDataHolder.ABusData;
    }

    private static class ABusDataHolder{
        private static final BusLineDBUtils ABusData = new BusLineDBUtils();
    }

    public void insertBus(BusLine busLine) {
        MyAppliaction.getDaoInstant().getBusLineDao().insertOrReplace(busLine);
    }

    public void deleteBus(BusLine busLine) {
        MyAppliaction.getDaoInstant().getBusLineDao().delete(busLine);
    }

    public void uodateBus(BusLine busLine) {
        MyAppliaction.getDaoInstant().getBusLineDao().update(busLine);
    }

    public List<BusLine> queryAllBus() {
        return MyAppliaction.getDaoInstant().getBusLineDao().loadAll();
    }

    public List<BusLine> queryRawBus(String cityID) {
        return MyAppliaction.getDaoInstant().getBusLineDao().queryRaw("where CITY_ID = ?", cityID);
    }

}
