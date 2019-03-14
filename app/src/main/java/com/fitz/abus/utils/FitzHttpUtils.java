package com.fitz.abus.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.fitz.abus.FitzApplication;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.utils
 * @ClassName: FitzHttpUtils
 * @Author: Fitz
 * @CreateDate: 2018/12/23 14:58
 */
public class FitzHttpUtils {

    /**
     * 超时时间
     * 5s
     */
    public static final int TIMEOUT = 5 * 1000;
    private static final String TAG = "FitzHttpUtils";
    private static final String URL_SH = "http://apps.eshimin.com/traffic/gjc/";
    private static final String URL_WH = "http://220.180.139.42:8980/SmartBusServer/Main";
    private static final String JSON_TYPE = "application/json;charset=UTF-8";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final String EMPTY = "{ }";
    private static final String ERROR = "error";
    private final boolean isDebug = FitzApplication.Debug;
    private OkHttpClient.Builder mOkHttpClientBuilder;
    private OkHttpClient mOkHttpClient;
    private Handler handler = new Handler(Looper.getMainLooper());

    private static volatile FitzHttpUtils fitzHttpUtils;

    public static FitzHttpUtils getInstance(){
        if(fitzHttpUtils == null){
            synchronized (FitzHttpUtils.class){
                if(fitzHttpUtils == null){
                    fitzHttpUtils = new FitzHttpUtils();
                }
            }
        }
        return fitzHttpUtils;
    }

    private FitzHttpUtils() {
        mOkHttpClientBuilder = new OkHttpClient.Builder().addInterceptor(new FitzLogInterceptor());
        //设置超时
        mOkHttpClient = mOkHttpClientBuilder.connectTimeout(TIMEOUT, TimeUnit.SECONDS).writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                                            .readTimeout(TIMEOUT, TimeUnit.SECONDS).build();
    }


    /**
     * 获取上海公交线路信息
     * 返回示例：
     * {
     * "start_latetime" : "23:50",
     * "line_name" : "763",
     * "end_earlytime" : "06:00",
     * "start_earlytime" : "05:20",
     * "end_stop" : "上海南站(北广场)",
     * "line_id" : "076300",
     * "start_stop" : "莘庄地铁站(北广场)",
     * "end_latetime" : "23:58"
     * }
     */
    public void getBusBaseSH(String busLine, final AbstractHttpCallBack callBack) {
        String q = "getBusBase?name=" + busLine + "%E8%B7%AF\n";
        final Request request = new Request.Builder().url(URL_SH + q).build();
        Log.d(TAG, "getArriveBaseSH q:" + q);
        Call call = mOkHttpClient.newCall(request);
        OnStart(callBack);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                OnError(callBack, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && isJsonData(response)) {
                    onSuccess(callBack, response.body().string());
                } else {
                    OnError(callBack, response.message());
                }
            }

        });
    }

    /**
     * 获取上海公交线路站点列表
     * 返回示例
     * {
     * "lineResults1" : {
     * "stops" : [ {
     * "id" : "1",
     * "zdmc" : "上海南站(北广场)"
     * } ],
     * "direction" : "false"
     * },
     * "lineResults0" : {
     * "stops" : [ {
     * "id" : "1",
     * "zdmc" : "莘庄地铁站(北广场)"
     * }],
     * "direction" : "true"
     * }
     * }
     */
    public void getBusStopSH(String busName, String lineid, final AbstractHttpCallBack callBack) {
        //去掉汉字
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]");
        Matcher matcher = pattern.matcher(busName);
        busName = matcher.replaceAll("");
        String q = "getBusStop?name=" + busName + "%E8%B7%AF&lineid=" + lineid + "\n";
        final Request request = new Request.Builder().url(URL_SH + q).build();
        Log.d(TAG, "getBusStopSH q:" + q);
        Call call = mOkHttpClient.newCall(request);
        OnStart(callBack);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                OnError(callBack, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && isJsonData(response)) {
                    onSuccess(callBack, response.body().string());
                } else {
                    OnError(callBack, response.message());
                }
            }

        });
    }

    /**
     * 获取上海公交到站信息
     * 返回示例
     * {
     * "cars" : [ {
     * "time" : "478",
     * "distance" : "2357",
     * "terminal" : "沪D-G3936",
     * "stopdis" : "4"
     * } ]
     * }
     */
    public void getArriveBaseSH(String busName, String lineid, String stopid, int direction, final AbstractHttpCallBack callBack) {
        //去掉汉字
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]");
        Matcher matcher = pattern.matcher(busName);
        busName = matcher.replaceAll("");
        String q = "getArriveBase?name=" + busName + "%E8%B7%AF&lineid=" + lineid + "&stopid=" + stopid + "&direction=" + direction + "\n";
        final Request request = new Request.Builder()
                .url(URL_SH + q)
                .build();
        Log.d(TAG, "getArriveBaseSH q:" + q);
        Call call = mOkHttpClient.newCall(request);
        OnStart(callBack);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                OnError(callBack, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // response.body().string() 一次性数据
                String data = response.body().string();
                if (response.isSuccessful() && notEmptyArriveBaseSH(data)) {
                    onSuccess(callBack, data);
                } else {
                    OnError(callBack, response.message());
                }
            }
        });
    }

    /**
     * 查询芜湖公交数据
     * {
     * "result": {
     * "list": [
     * "42路",
     * "204214"
     * ]
     * }
     * }
     * <p>
     * {
     * "error": {
     * "code": -103,
     * "message": "未查到记录!"
     * }
     * }
     */
    public void postBusBaseWH(String busLine, final AbstractHttpCallBack callBack) {
        String requestBody = "{\"cmd\": \"searchLine\",\"params\": {\"lineName\": \"" + busLine + "\"}}";
        Log.d(TAG, "postBusBaseWH requestBody:" + requestBody);
        final Request request = new Request.Builder().url(URL_WH).post(RequestBody.create(mediaType, requestBody)).build();
        Call call = mOkHttpClient.newCall(request);
        OnStart(callBack);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                OnError(callBack, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Log.d(TAG, "postBusBaseWH  onResponse data:" + data);
                if (response.isSuccessful() && hasResault(data)) {
                    onSuccess(callBack, data);
                } else {
                    OnError(callBack, response.message());
                }
            }
        });
    }

    public void postBusLineDetails(String busLine, final AbstractHttpCallBack callBack) {
        String requestBody = "{\"cmd\":\"lineDetail\",\"params\":{\"lineName\":\"" + busLine + "\"}}";
        Log.d(TAG, "postBusLineDetails requestBody:" + requestBody);
        final Request request = new Request.Builder().url(URL_WH).post(RequestBody.create(mediaType, requestBody)).build();
        Call call = mOkHttpClient.newCall(request);
        OnStart(callBack);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                OnError(callBack, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Log.d(TAG, "postBusLineDetails  onResponse data:" + data);
                if (response.isSuccessful() && hasResault(data)) {
                    onSuccess(callBack, data);
                } else {
                    OnError(callBack, response.message());
                }
            }
        });

    }

    public void postArriveBaseWH(String busLine, String stationId, int direction, final AbstractHttpCallBack callBack) {
        String requestBody = "{\"cmd\": \"getArriveInfo\",\"params\": {\"lineName\": \"" + busLine + "\",\"stationId\": \"" + stationId + "\"," +
                "\"type\": " + (direction + 1) + "}}";
        Log.d(TAG, "postArriveBaseWH requestBody:" + requestBody);
        final Request request = new Request.Builder().url(URL_WH).post(RequestBody.create(mediaType, requestBody)).build();
        Call call = mOkHttpClient.newCall(request);
        OnStart(callBack);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                OnError(callBack, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Log.d(TAG, "postArriveBaseWH  onResponse data:" + data);
                if (response.isSuccessful() && hasResault(data)) {
                    onSuccess(callBack, data);
                } else {
                    OnError(callBack, response.message());
                }
            }
        });
    }

    /**
     * 吐槽：上海公交查询错误的线路，返回的不是错误码，而是网页……
     */
    private boolean isJsonData(Response response) {
        if (JSON_TYPE.equals(response.headers().get(CONTENT_TYPE))) {
            return true;
        }
        return false;
    }

    private boolean notEmptyArriveBaseSH(String data) {
        if (EMPTY.equals(data)) {
            return false;
        } else {
            return true;
        }
    }

    private boolean hasResault(String data) {
        if (data.contains(ERROR)) {
            return false;
        }
        return true;
    }

    public void OnStart(AbstractHttpCallBack callBack) {
        if (callBack != null) {
            callBack.onCallBefore();
        }
    }

    public void onSuccess(final AbstractHttpCallBack callBack, final String data) {
        if (callBack != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {//在主线程操作
                    Log.d(TAG, "data:" + data);
                    callBack.onCallSuccess(data);
                }
            });
        }
    }

    public void OnError(final AbstractHttpCallBack callBack, final String msg) {
        if (callBack != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callBack.onCallError(msg);
                }
            });
        }
    }

    public void FLOG(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

    public static abstract class AbstractHttpCallBack {
        /**
         * 查询前准备
         */
        public void onCallBefore() {
            Log.d(TAG, "onCallBefore");
        }

        /**
         * 获取成功
         *
         * @param data 结果
         */
        public void onCallSuccess(String data) {
            Log.d(TAG, "onCallSuccess");
        }

        /**
         * 获取失败
         *
         * @param meg 错误
         */
        public void onCallError(String meg) {
            Log.d(TAG, "onCallError");
        }
    }
}
