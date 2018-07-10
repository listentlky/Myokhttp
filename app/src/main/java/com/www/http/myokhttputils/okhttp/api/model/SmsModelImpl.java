package com.www.http.myokhttputils.okhttp.api.model;

import android.util.Log;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.www.http.myokhttputils.okhttp.RetrofitWrapper;
import com.www.http.myokhttputils.okhttp.api.bean.SmsData;
import com.www.http.myokhttputils.okhttp.api.httpUtils.NetWorkUtils;
import com.www.http.myokhttputils.okhttp.api.httpUtils.StatusUtils;
import com.www.http.myokhttputils.okhttp.api.presenter.SmsPresenter;
import com.www.http.myokhttputils.okhttp.api.service.HttpService;


import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by AED on 2017/9/28.
 */

public class SmsModelImpl implements SmsModel {

    @Override
    public void sendSms(String phone, LifecycleProvider provider, final SmsPresenter smsPresenter) {

        if (!NetWorkUtils.checkNetworkConnect()) {  //需判断 网络是否 连接
            smsPresenter.onNetworkDisable();
            return;
        }
        //在这里 可做非空判断
        smsPresenter.onPre();
        HttpService service = RetrofitWrapper.getInstance().create(HttpService.class);
        service.sendCode(phone)
                .subscribeOn(Schedulers.io())
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SmsData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("Bruce","----------onSubscribe--------------");
                    }

                    @Override
                    public void onNext(SmsData data) {
                        if (data.code.equals(StatusUtils.SUCCESS)) {
                            smsPresenter.onSuccess(data);
                            smsPresenter.onFinish();
                        } else {
                            smsPresenter.onError(data.code, data.message);
                            smsPresenter.onFinish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        smsPresenter.onFailure(e.getMessage());
                        smsPresenter.onFinish();
                    }

                    @Override
                    public void onComplete() {
                        Log.d("Bruce","----------onComplete--------------");
                    }
                });
    }
}
