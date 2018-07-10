package com.www.http.myokhttputils;

import android.app.Application;

import com.www.http.myokhttputils.okhttp.api.httpUtils.NetWorkUtils;

/**
 * Created by AED on 2017/9/27.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetWorkUtils.context = getApplicationContext();
    }
}
