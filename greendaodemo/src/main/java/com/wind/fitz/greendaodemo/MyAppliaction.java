package com.wind.fitz.greendaodemo;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.wind.fitz.greendaodemo.bean.DaoMaster;
import com.wind.fitz.greendaodemo.bean.DaoSession;

public class MyAppliaction extends Application {

    private static DaoSession daoSession;



    @Override
    public void onCreate() {
        super.onCreate();

        //配置数据库
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
}
