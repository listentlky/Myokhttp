package com.www.http.myokhttputils;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.www.http.myokhttputils.okhttp.api.bean.SmsData;
import com.www.http.myokhttputils.okhttp.api.presenter.LoginPresenterImpl;
import com.www.http.myokhttputils.okhttp.api.presenter.SmsPresenter;
import com.www.http.myokhttputils.okhttp.api.presenter.SmsPresenterImpl;
import com.www.http.myokhttputils.okhttp.api.view.LoginView;
import com.www.http.myokhttputils.Utils.ToastUtils;
import com.www.http.myokhttputils.okhttp.api.bean.LoginData;
import com.www.http.myokhttputils.okhttp.api.view.SmsView;

import java.util.HashMap;

public class MainActivity extends RxAppCompatActivity implements LoginView {

    private EditText phone;
    private EditText code;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        phone = ((EditText) findViewById(R.id.phone));
        code = ((EditText) findViewById(R.id.code));
    }

    public void Login(View v) {
        HashMap<String, String> map = new HashMap<>();
        map.put("phone", phone.getText().toString());
        //     map.put("authCode", code.getText().toString());
        map.put("code", "1000");
        //     map.put("jpushId", "21354684526");
        //     LoginPresenterImpl presenter = new LoginPresenterImpl(this);
        //      presenter.onReadyLogin(map);

        SmsPresenterImpl smsPresenter = new SmsPresenterImpl(new SmsView() {
            @Override
            public void onNetworkDisable() {
                ToastUtils.showShort(MainActivity.this, "网络未连接");
            }

            @Override
            public void onPre() {
                dialog = ProgressDialog.show(MainActivity.this, "正在加载中", null);
            }

            @Override
            public void onSuccess(SmsData data) {
                Log.d("Bruce", "-------onSuccess----------------" + data.toString());
            }

            @Override
            public void onError(String err_code, String err_msg) {
                Log.d("Bruce", "-------onError----------------" + err_msg);
            }

            @Override
            public void onFailure(String message) {
                ToastUtils.showShort(MainActivity.this, message);
                Log.d("Bruce", "-------onFailure----------------" + message);
            }

            @Override
            public void onFinish() {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        smsPresenter.onSend(phone.getText().toString());
    }

    @Override
    public void onNetworkDisable() {
        ToastUtils.showShort(MainActivity.this, "网络未连接");
    }

    @Override
    public void onPre() {
        dialog = ProgressDialog.show(MainActivity.this, "正在加载中", null);
    }

    @Override
    public void onSuccess(LoginData ret) {
        Log.d("Bruce", "-------onSuccess----------------" + ret.data.toString());
    }

    @Override
    public void onError(String err_code, String err_msg) {
        Log.d("Bruce", "-------onError----------------" + err_msg);
    }

    @Override
    public void onFailure(String message) {
        ToastUtils.showShort(MainActivity.this, message);
        Log.d("Bruce", "-------onFailure----------------" + message);
    }

    @Override
    public void onFinish() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
