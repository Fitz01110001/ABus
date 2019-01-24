package com.fitz.abus;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fitz.abus.bean.BusBaseInfoDB;
import com.fitz.abus.greendao.DaoMaster;
import com.fitz.abus.greendao.DaoSession;
import com.fitz.abus.utils.FitzDBUtils;

import java.util.ArrayList;
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

    private boolean isDebug = true;
    /**
     * 存储区号-城市，每个城市需要单独适配
     */
    public static final HashMap<String, Integer> CITY_CODE_NAME_MAP = new HashMap<String, Integer>() {
        {
            put("021", R.string.city_sh);
            put("0553", R.string.city_wh);
            put("025", R.string.city_nj);
        }
    };
    private static final List<String> CITY_CODE = new ArrayList<>(CITY_CODE_NAME_MAP.keySet());
    private static final String DEFAULT_CITY_KEY = "default_city_Key";
    private static final String DEFAULT_REFRESH_STATE_KEY = "default_refresh_state_key";
    private static final String DEFAULT_REFRESH_TIME_KEY = "default_refresh_time_key";
    /**
     * json 中 direction = true 对应 lineResults0
     * 查询时 direction = true 对应 direction 0
     */
    public static boolean direction = true;
    public static final String city_code_SH = "021";
    public static final String city_code_WH = "0553";
    public static final String city_code_NJ = "025";
    protected static FitzApplication application;


    private boolean autoRefresh;
    private long refreshTime;
    protected static DaoSession daoSession;
    private static final String FIRST_BOOT_DEFAULT_CITY_CODE = "021";
    //new ArrayList<>(Arrays.asList("021", "0553", "025"));


    private String TAG = "FitzApplication";
    private String defaultCityCode;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private List<BusBaseInfoDB> favoriteBusListSH = new ArrayList<>();
    private List<BusBaseInfoDB> favoriteBusListWH = new ArrayList<>();
    private List<BusBaseInfoDB> favoriteBusListNJ = new ArrayList<>();

    /**
     * 单例
     */
    public static FitzApplication getInstance() { return application; }

    public static DaoSession getDaoInstant() {
        return daoSession;
    }

    public List<BusBaseInfoDB> getFavoriteBusListWH() {
        return favoriteBusListWH;
    }

    public void setFavoriteBusListWH(List<BusBaseInfoDB> favoriteBusListWH) {
        this.favoriteBusListWH = favoriteBusListWH;
    }

    public List<BusBaseInfoDB> getFavoriteBusListSH() {
        return favoriteBusListSH;
    }

    public void setFavoriteBusListSH(List<BusBaseInfoDB> favoriteBusListSH) {
        this.favoriteBusListSH = favoriteBusListSH;
    }

    public String getDefaultCityCode() {
        FLOG("getDefaultCityCode:" + defaultCityCode);
        return defaultCityCode;
    }

    /**
     * 保存用户选择的默认城市
     */
    public void setDefaultCityCode(String defaultCityCode) {
        this.defaultCityCode = defaultCityCode;
        FLOG("setDefaultCityCode:" + defaultCityCode);
        preferences = getApplicationContext().getSharedPreferences(DEFAULT_CITY_KEY, MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(DEFAULT_CITY_KEY, defaultCityCode);
        if (!editor.commit()) {
            Log.e(TAG, "set defaultCityCode error");
        }
    }

    public boolean isAutoRefresh() {
        return autoRefresh;
    }

    /**
     * 设置自动刷新开关
     *
     * @param autoRefresh
     */
    public void setAutoRefresh(boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
        FLOG("setAutoRefresh:" + autoRefresh);
        preferences = getApplicationContext().getSharedPreferences(DEFAULT_REFRESH_STATE_KEY, MODE_PRIVATE);
        editor = preferences.edit();
        editor.putBoolean(DEFAULT_REFRESH_STATE_KEY, autoRefresh);
        if (!editor.commit()) {
            Log.e(TAG, "set setAutoRefresh error");
        }
    }

    public long getRefreshTime() {
        FLOG("getRefreshTime:" + refreshTime);
        return refreshTime;
    }

    /**
     * 设置默认刷新时间间隔
     *
     * @param refreshTime
     */
    public void setRefreshTime(long refreshTime) {
        this.refreshTime = refreshTime;
        FLOG("setRefreshTime:" + refreshTime);
        preferences = getApplicationContext().getSharedPreferences(DEFAULT_REFRESH_TIME_KEY, MODE_PRIVATE);
        editor = preferences.edit();
        editor.putLong(DEFAULT_REFRESH_TIME_KEY, refreshTime);
        if (!editor.commit()) {
            Log.e(TAG, "set setAutoRefresh error");
        }
    }

    /**
     * 从Cities中取出对应区号的城市名称
     *
     * @return 当前选中城市名称
     */
    public String getDefaultCityName() {
        FLOG("getDefaultCityName:" + getResources().getString(CITY_CODE_NAME_MAP.get(defaultCityCode)));
        return getResources().getString(CITY_CODE_NAME_MAP.get(defaultCityCode));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        autoRefresh = readAutoRefreshTimeState();
        refreshTime = readRefreshTime();
        defaultCityCode = readDefaultCityKey();
        setupDatabase();
        favoriteBusListSH = FitzDBUtils.getInstance().queryRawBusWhereCityID(CITY_CODE.get(0));
        favoriteBusListWH = FitzDBUtils.getInstance().queryRawBusWhereCityID(CITY_CODE.get(1));
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
     * 应用启动获取默认城市，若是首次打开，选择 FIRST_BOOT_DEFAULT_CITY_CODE 设定值。
     */
    private String readDefaultCityKey() {
        preferences = getApplicationContext().getSharedPreferences(DEFAULT_CITY_KEY, MODE_PRIVATE);
        return preferences.getString(DEFAULT_CITY_KEY, FIRST_BOOT_DEFAULT_CITY_CODE);
    }

    /**
     * 每次启动
     *
     * @return 返回自动刷新设定值，默认为 true
     */
    private boolean readAutoRefreshTimeState() {
        preferences = getApplicationContext().getSharedPreferences(DEFAULT_REFRESH_STATE_KEY, MODE_PRIVATE);
        return preferences.getBoolean(DEFAULT_REFRESH_STATE_KEY, true);
    }

    /**
     * 启动时获取默认刷新时间
     * @return
     */
    private Long readRefreshTime() {
        preferences = getApplicationContext().getSharedPreferences(DEFAULT_REFRESH_TIME_KEY, MODE_PRIVATE);
        return preferences.getLong(DEFAULT_REFRESH_TIME_KEY, 10000);
    }

    public void FLOG(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }


}
