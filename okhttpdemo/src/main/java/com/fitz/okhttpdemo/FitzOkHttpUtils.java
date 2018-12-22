package com.fitz.okhttpdemo;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.okhttpdemo
 * @ClassName: FitzOkHttpUtils
 * @Author: Fitz
 * @CreateDate: 2018/12/22 16:46
 */
public class FitzOkHttpUtils {

    private static FitzOkHttpUtils mFitzOkHttpUtils;

    public static FitzOkHttpUtils getInstance(){
        if(mFitzOkHttpUtils == null){
            synchronized (FitzOkHttpUtils.class){
                if(mFitzOkHttpUtils == null){
                    mFitzOkHttpUtils = new FitzOkHttpUtils();
                }
            }
        }
        return mFitzOkHttpUtils;
    }

    public FitzOkHttpUtils() {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url("https://github.com/hongyangAndroid")
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback()
        {
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

            }

        });



    }
}
