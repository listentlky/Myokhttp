package com.www.http.myokhttputils.okhttp.api.presenter;

import com.www.http.myokhttputils.okhttp.api.bean.LoginData;

import java.util.HashMap;

/**
 * Created by AED on 2017/9/28.
 */

public interface LoginPresenter {
    void onReadyLogin(HashMap<String,String> map);
    void onNetworkDisable();
    void onPre();
    void onFailure(String msg);
    void onSuccess(LoginData ret);
    void onError(String code, String message);
    void onFinish();
}
