package com.chen.hiweatherproject.net;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OKHttpUtils{
    // 设置单实例
    private static OKHttpUtils instance = new OKHttpUtils();
    private OkHttpClient okHttpClient;

    private OKHttpUtils() {
        // 创建日志拦截类
        // 创建日志拦截类
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // 为这个日志类设置等级
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)   // 设置连接超时时间
                .readTimeout(20, TimeUnit.SECONDS)  // 设置读取超时时间
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    public static OKHttpUtils getInstance() {
        return instance;
    }

    // 同步get
    public String doGet(String url) throws IOException {
        Request req = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(req);
        Response resp = call.execute();

        return resp.body().string();
    }
}
