package com.www.http.myokhttputils.okhttp.api.service;

import com.www.http.myokhttputils.okhttp.api.bean.LoginData;
import com.www.http.myokhttputils.okhttp.api.bean.SmsData;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by AED on 2017/9/28.
 */

public interface HttpService {

    //用户端发送code
    @GET("sms/{phone}/1000")
    Observable<SmsData> sendCode(@Path("phone") String phone);


    //用户端登录
    @Headers("client: CLIENT")
    @POST("account/login")
    Observable<LoginData> login(@QueryMap HashMap<String, String> map);
}
