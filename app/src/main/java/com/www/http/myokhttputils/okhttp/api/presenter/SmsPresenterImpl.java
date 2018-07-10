package com.www.http.myokhttputils.okhttp.api.presenter;

import android.content.Context;

import com.www.http.myokhttputils.okhttp.api.bean.SmsData;
import com.www.http.myokhttputils.okhttp.api.model.SmsModel;
import com.www.http.myokhttputils.okhttp.api.model.SmsModelImpl;
import com.www.http.myokhttputils.okhttp.api.view.SmsView;

/**
 * Created by AED on 2017/9/28.
 */

public class SmsPresenterImpl extends BaseImpl implements SmsPresenter{

    private SmsModel smsModel;
    private SmsView smsView;

    public SmsPresenterImpl(Context context,SmsView smsView) {
        super(context);
        this.smsModel = new SmsModelImpl();
        this.smsView = smsView;
    }

    @Override
    public void onSend(String phone) {
        smsModel.sendSms(phone,getActivityLifecycleProvider(),this);
    }

    @Override
    public void onNetworkDisable() {
        smsView.onNetworkDisable();
    }

    @Override
    public void onPre() {
        smsView.onPre();
    }

    @Override
    public void onFailure(String msg) {
        smsView.onFailure(msg);
    }

    @Override
    public void onSuccess(SmsData data) {
        smsView.onSuccess(data);
    }

    @Override
    public void onError(String code, String message) {
        smsView.onError(code,message);
    }

    @Override
    public void onFinish() {
        smsView.onFinish();
        doDestroy();
    }
}
