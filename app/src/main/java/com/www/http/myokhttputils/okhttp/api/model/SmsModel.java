package com.www.http.myokhttputils.okhttp.api.model;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.www.http.myokhttputils.okhttp.api.presenter.SmsPresenter;


/**
 * Created by AED on 2017/9/28.
 */

public interface SmsModel {

    void sendSms(String phone, LifecycleProvider provider, SmsPresenter smsPresenter);
}
