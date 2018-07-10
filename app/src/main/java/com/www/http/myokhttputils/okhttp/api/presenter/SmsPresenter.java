package com.www.http.myokhttputils.okhttp.api.presenter;

import com.www.http.myokhttputils.okhttp.api.bean.SmsData;

import java.util.HashMap;

/**
 * Created by AED on 2017/9/28.
 */

public interface SmsPresenter {
    void onSend(String phone);
    void onNetworkDisable();
    void onPre();
    void onFailure(String msg);
    void onSuccess(SmsData data);
    void onError(String code, String message);
    void onFinish();
}
