package com.fitz.abus;

import android.app.Application;

import java.util.HashMap;

public class FitzApplication extends Application {

    private static FitzApplication application;

    public static FitzApplication getInstance() { return application; }

    public static int getDefaultCity() {
        return defaultCity;
    }

    public static void setDefaultCity(int defaultCity) {
        FitzApplication.defaultCity = defaultCity;
    }

    private static int defaultCity;


    public final HashMap<String, Integer> Cities = new HashMap<String, Integer>() {
        {
            put("0553", R.string.menu_cities_wh);
            put("021", R.string.menu_cities_sh);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        defaultCity = readDefaultCityID();
    }

    private int readDefaultCityID() {
        // to-do
        // 应该从数据库中读
        return 0;
    }

}
