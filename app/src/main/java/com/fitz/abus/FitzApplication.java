package com.fitz.abus;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.fitz.abus.greendao.DaoMaster;
import com.fitz.abus.greendao.DaoSession;

import java.util.HashMap;

public class FitzApplication extends Application {

    private static FitzApplication application;

    private static DaoSession daoSession;

    public static FitzApplication getInstance() { return application; }




    /** 从Cities中取出对应区号的城市名称*/
    public String getDefaultCityName(String key) {
        return getResources().getString(Cities.get(key));
    }

    public static void setDefaultCity(int defaultCity) {
        FitzApplication.defaultCity = defaultCity;
    }

    private static int defaultCity;

    public int getDefaultCityKey() {
        return defaultCity;
    }

    /** 存储区号-城市，每个城市需要单独适配*/
    public static final HashMap<String, Integer> Cities = new HashMap<String, Integer>() {
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

        setupDatabase();
    }

    private void setupDatabase() {
        //创建数据库shop.db
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "abus.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取dao对象管理者
        daoSession = daoMaster.newSession();
    }


    public static DaoSession getDaoInstant() {
        return daoSession;
    }

    private int readDefaultCityID() {
        // to-do
        // 应该从数据库中读
        return 0;
    }

}
