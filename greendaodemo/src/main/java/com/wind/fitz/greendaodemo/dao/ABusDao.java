package com.wind.fitz.greendaodemo.dao;

import com.wind.fitz.greendaodemo.MyAppliaction;
import com.wind.fitz.greendaodemo.bean.City;

public class ABusDao {


    public static void insertShop(City city) {
        MyAppliaction.getDaoInstant().getCityDao().insertOrReplace(city);
    }



}
