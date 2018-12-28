package com.fitz.abus;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fitz.abus.bean.BusLineDB;
import com.fitz.abus.greendao.DaoMaster;
import com.fitz.abus.greendao.DaoSession;
import com.fitz.abus.utils.FitzDBUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus
 * @ClassName: FitzApplication
 * @Author: Fitz
 * @CreateDate: 2018/12/12 13:10
 */
public class FitzApplication extends Application {

    /** 存储区号-城市，每个城市需要单独适配 */
    public static final HashMap<String, Integer> Cities = new HashMap<String, Integer>() {
        {
            put("021", R.string.city_sh);
            put("0553", R.string.city_wh);
            put("025", R.string.city_nj);
        }
    };
    private static final String KEY = "defaultCityKey";
    /**
     * json 中 direction = true 对应 lineResults0
     * 查询时 direction = true 对应 direction 0
     */
    public static boolean directionSH = true;
    protected static FitzApplication application;
    protected static DaoSession daoSession;
    private final String firstBootDefaultCity = "021";
    private final List<String> cities = new ArrayList<>(
            Arrays.asList("021",
                    "0553",
                    "025"));
    private String TAG = "FitzApplication";
    private String defaultCityKey;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private List<BusLineDB> favoriteBusListSH = new ArrayList<>();
    private List<BusLineDB> favoriteBusListWH = new ArrayList<>();
    private List<BusLineDB> favoriteBusListNJ = new ArrayList<>();

    /**
     * 单例
     */
    public static FitzApplication getInstance() { return application; }

    public static DaoSession getDaoInstant() {
        return daoSession;
    }

    public List<BusLineDB> getFavoriteBusListWH() {
        return favoriteBusListWH;
    }

    public void setFavoriteBusListWH(List<BusLineDB> favoriteBusListWH) {
        this.favoriteBusListWH = favoriteBusListWH;
    }

    public List<BusLineDB> getFavoriteBusListSH() {
        return favoriteBusListSH;
    }

    public void setFavoriteBusListSH(List<BusLineDB> favoriteBusListSH) {
        this.favoriteBusListSH = favoriteBusListSH;
    }

    public String getDefaultCityKey() {
        return defaultCityKey;
    }

    /**
     * 保存用户选择的默认城市
     */
    public void setDefaultCityKey(String defaultCityKey) {
        this.defaultCityKey = defaultCityKey;
        preferences = getApplicationContext().getSharedPreferences(KEY, MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(KEY, defaultCityKey);
        if (!editor.commit()) {
            Log.e(TAG, "set defaultCityKey error");
        }
    }

    /** 从Cities中取出对应区号的城市名称 */
    public String getDefaultCityName() {
        return getResources().getString(Cities.get(defaultCityKey));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        defaultCityKey = readDefaultCityKey();
        setupDatabase();
        favoriteBusListSH = FitzDBUtils.getInstance().queryRawBus(cities.get(0));
        favoriteBusListWH = FitzDBUtils.getInstance().queryRawBus(cities.get(1));
        //favoriteBusListNJ = FitzDBUtils.getInstance().queryRawBus(cities.get(2));
        Log.d(TAG, "favoriteBusListSH" + favoriteBusListSH.toString());
    }

    /**
     * 初始化GreenDao
     */
    private void setupDatabase() {
        //abus.db
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "abus.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取dao对象管理者
        daoSession = daoMaster.newSession();
    }

    /**
     * 应用启动获取默认城市，若是首次打开，选择 firstBootDefaultCity 设定值。
     */
    private String readDefaultCityKey() {
        preferences = getApplicationContext().getSharedPreferences(KEY, MODE_PRIVATE);
        return preferences.getString(KEY, firstBootDefaultCity);
    }

}
