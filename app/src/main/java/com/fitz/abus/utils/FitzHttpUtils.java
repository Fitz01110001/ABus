package com.fitz.abus.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Timeout;

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
    private final boolean isDebug = true;
    private static final String URL_SH = "http://apps.eshimin.com/traffic/gjc/";
    private static final String URL_WH = "http://220.180.139.42:8980/SmartBusServer/Main";
    private static final String JSON_TYPE = "application/json;charset=UTF-8";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final String EMPTY = "{ }";
    private static final String ERROR = "error";
    private OkHttpClient.Builder mOkHttpClientBuilder;
    private OkHttpClient mOkHttpClient;
    private Handler handler = new Handler(Looper.getMainLooper());

    public FitzHttpUtils() {
        init();
    }

    private void init() {
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
        final Request request = new Request.Builder().url(URL_SH + "getBusBase?name=" + busLine + "%E8%B7%AF\n").build();
        Call call = mOkHttpClient.newCall(request);
        OnStart(callBack);
        call.enqueue(new Callback() {
            /**
             * Called when the request could not be executed due to cancellation, a connectivity problem or
             * timeout. Because networks can fail during an exchange, it is possible that the remote server
             * accepted the request before the failure.
             *
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call call, IOException e) {
                OnError(callBack, e.getMessage());
            }

            /**
             * Called when the HTTP response was successfully returned by the remote server. The callback may
             * proceed to read the response body with {@link Response#body}. The response is still live until
             * its response body is {@linkplain ResponseBody closed}. The recipient of the callback may
             * consume the response body on another thread.
             *
             * <p>Note that transport-layer success (receiving a HTTP response code, headers and body) does
             * not necessarily indicate application-layer success: {@code response} may still indicate an
             * unhappy HTTP response code like 404 or 500.
             *
             * @param call
             * @param response
             */
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
    public void getBusStopSH(String busLine, String lineid, final AbstractHttpCallBack callBack) {
        final Request request = new Request.Builder().url(URL_SH + "getBusStop?name=" + busLine + "%E8%B7%AF&lineid=" + lineid + "\n").build();
        Call call = mOkHttpClient.newCall(request);
        OnStart(callBack);
        call.enqueue(new Callback() {
            /**
             * Called when the request could not be executed due to cancellation, a connectivity problem or
             * timeout. Because networks can fail during an exchange, it is possible that the remote server
             * accepted the request before the failure.
             *
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call call, IOException e) {
                OnError(callBack, e.getMessage());
            }

            /**
             * Called when the HTTP response was successfully returned by the remote server. The callback may
             * proceed to read the response body with {@link Response#body}. The response is still live until
             * its response body is {@linkplain ResponseBody closed}. The recipient of the callback may
             * consume the response body on another thread.
             *
             * <p>Note that transport-layer success (receiving a HTTP response code, headers and body) does
             * not necessarily indicate application-layer success: {@code response} may still indicate an
             * unhappy HTTP response code like 404 or 500.
             *
             * @param call
             * @param response
             */
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
        final Request request = new Request.Builder()
                .url(URL_SH + "getArriveBase?name=" + busName + "%E8%B7%AF&lineid=" + lineid + "&stopid=" + stopid + "&direction=" + direction + "\n")
                .build();
        Call call = mOkHttpClient.newCall(request);
        OnStart(callBack);
        call.enqueue(new Callback() {
            /**
             * Called when the request could not be executed due to cancellation, a connectivity problem or
             * timeout. Because networks can fail during an exchange, it is possible that the remote server
             * accepted the request before the failure.
             *
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call call, IOException e) {
                OnError(callBack, e.getMessage());
            }

            /**
             * Called when the HTTP response was successfully returned by the remote server. The callback may
             * proceed to read the response body with {@link Response#body}. The response is still live until
             * its response body is {@linkplain ResponseBody closed}. The recipient of the callback may
             * consume the response body on another thread.
             *
             * <p>Note that transport-layer success (receiving a HTTP response code, headers and body) does
             * not necessarily indicate application-layer success: {@code response} may still indicate an
             * unhappy HTTP response code like 404 or 500.
             *
             * @param call
             * @param response
             */
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
        final Request request = new Request.Builder().url(URL_WH).post(RequestBody.create(mediaType, requestBody)).build();
        Call call = mOkHttpClient.newCall(request);
        OnStart(callBack);
        call.enqueue(new Callback() {
            /**
             * Called when the request could not be executed due to cancellation, a connectivity problem or
             * timeout. Because networks can fail during an exchange, it is possible that the remote server
             * accepted the request before the failure.
             *
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call call, IOException e) {
                OnError(callBack, e.getMessage());
            }

            /**
             * Called when the HTTP response was successfully returned by the remote server. The callback may
             * proceed to read the response body with {@link Response#body}. The response is still live until
             * its response body is {@linkplain ResponseBody closed}. The recipient of the callback may
             * consume the response body on another thread.
             *
             * <p>Note that transport-layer success (receiving a HTTP response code, headers and body) does
             * not necessarily indicate application-layer success: {@code response} may still indicate an
             * unhappy HTTP response code like 404 or 500.
             *
             * @param call
             * @param response
             */
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

    public void postBusLineDetails(String lineName, final AbstractHttpCallBack callBack) {
        String requestBody = "{\"cmd\":\"lineDetail\",\"params\":{\"lineName\":\"" + lineName + "\"}}";
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

    public void FLOG(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }
}
