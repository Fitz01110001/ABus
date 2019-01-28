package com.fitz.abus;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.fitz.abus.activity.MainActivity;
import com.fitz.abus.bean.BusBaseInfoDB;
import com.fitz.abus.greendao.DaoMaster;
import com.fitz.abus.greendao.DaoSession;
import com.fitz.abus.utils.FitzDBUtils;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

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

    public static final String[] FitzEmail = {"fitz361@163.com"};
    /**
     * 存储区号-城市，每个城市需要单独适配
     */
    public static final HashMap<String, Integer> CITY_CODE_NAME_MAP = new HashMap<String, Integer>() {
        {
            put("021", R.string.city_sh);
            put("0553", R.string.city_wh);
            //put("025", R.string.city_nj);
        }
    };
    public static final String city_code_SH = "021";
    public static final String city_code_WH = "0553";
    public static final String city_code_NJ = "025";
    private static final String APP_ID = "5d927adf2b";
    private static final List<String> CITY_CODE = new ArrayList<>(CITY_CODE_NAME_MAP.keySet());
    private static final String DEFAULT_CITY_KEY = "default_city_Key";
    private static final String DEFAULT_REFRESH_STATE_KEY = "default_refresh_state_key";
    private static final String DEFAULT_REFRESH_TIME_KEY = "default_refresh_time_key";
    private static final String CHECK_FIRST_BOOT_KEY = "check_first_boot_key";
    private static final String FIRST_BOOT_DEFAULT_CITY_CODE = "0553";
    /**
     * json 中 direction = true 对应 lineResults0
     * 查询时 direction = true 对应 direction 0
     */
    public static boolean direction = true;
    protected static FitzApplication application;
    protected static DaoSession daoSession;
    private boolean isDebug = true;
    private boolean autoRefresh;
    private long refreshTime;
    private String TAG = "FitzApplication";
    private String defaultCityCode;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private List<BusBaseInfoDB> favoriteBusListSH = new ArrayList<>();
    private List<BusBaseInfoDB> favoriteBusListWH = new ArrayList<>();
    private List<BusBaseInfoDB> favoriteBusListNJ = new ArrayList<>();
    private boolean isFirstBoot;

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
        isFirstBoot = checkFirstBoot();
        setupDatabase();
        initBuglyBeta();
        favoriteBusListSH = FitzDBUtils.getInstance().queryRawBusWhereCityID(CITY_CODE.get(0));
        favoriteBusListWH = FitzDBUtils.getInstance().queryRawBusWhereCityID(CITY_CODE.get(1));
        Log.d(TAG, "favoriteBusListSH" + favoriteBusListSH.toString());
    }

    private boolean checkFirstBoot() {
        preferences = getApplicationContext().getSharedPreferences(CHECK_FIRST_BOOT_KEY, MODE_PRIVATE);
        if (preferences.getBoolean(CHECK_FIRST_BOOT_KEY, true)) {
            editor = preferences.edit();
            editor.putBoolean(CHECK_FIRST_BOOT_KEY, false);
            if (!editor.commit()) {
                Log.e(TAG, "checkFirstBoot error");
            }
            return true;
        } else {
            return false;
        }
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
     *
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

    private void initBuglyBeta() {
        /***** Beta高级设置 *****/
        /**
         * true表示app启动自动初始化升级模块;
         * false不会自动初始化;
         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false，
         * 在后面某个时刻手动调用Beta.init(getApplicationContext(),false);
         */
        Beta.autoInit = true;

        /**
         * true表示初始化时自动检查升级;
         * false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
         */
        Beta.autoCheckUpgrade = false;

        /**
         * 设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);
         */
        Beta.upgradeCheckPeriod = 60 * 1000;

        /**
         * 设置启动延时为1s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
         */
        Beta.initDelay = 3 * 1000;

        /**
         * 设置通知栏大图标，largeIconId为项目中的图片资源;
         */
        //Beta.largeIconId = R.drawable.ic_launcher;

        /**
         * 设置状态栏小图标，smallIconId为项目中的图片资源Id;
         */
        //Beta.smallIconId = R.drawable.ic_launcher;

        /**
         * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
         * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
         */
        //Beta.defaultBannerId = R.drawable.ic_launcher;

        /**
         * 设置sd卡的Download为更新资源保存目录;
         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
         */
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        /**
         * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
         */
        Beta.showInterruptedStrategy = true;

        /**
         * WIFI自动下载
         */
        Beta.autoDownloadOnWifi = false;

        /**
         * 消息通知
         */
        Beta.enableNotification = false;

        /**
         * 热修复
         */
        Beta.enableHotfix = false;

        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
         * 不设置会默认所有activity都可以显示弹窗;
         */
        Beta.canShowUpgradeActs.add(MainActivity.class);

        /***** 统一初始化Bugly产品，包含Beta *****/
        Bugly.init(this, APP_ID, true);
    }

    public boolean isFirstBoot() {
        return isFirstBoot;
        //return true;
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
}
