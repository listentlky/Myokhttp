package com.www.http.myokhttputils.okhttp;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by AED on 2017/9/27.
 */

public class RetrofitWrapper {

    private static RetrofitWrapper instance;
    private OkHttpClient okHttpClient;
    private Retrofit mRetrofit;

    public RetrofitWrapper() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .followRedirects(true)
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static RetrofitWrapper getInstance() {

        if (instance == null) {
            synchronized (RetrofitWrapper.class) {
                if (instance == null) {
                    instance = new RetrofitWrapper();
                }
            }
        }

        return instance;
    }

    /**
     * 转换为对象的Service
     * @param service
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service){
        return mRetrofit.create(service);
    }

    /**
     * 常量类 基本的URL
     */
    public class Constant {
        //BASE_URL 可以自行替换
        public static final String BASE_URL = "http://116.62.165.70:8802//api/v1/";
    }
}
