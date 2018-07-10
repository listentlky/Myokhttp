package com.www.http.myokhttputils.okhttp.api.bean;

import java.io.Serializable;

/**
 * Created by AED on 2017/9/28.
 */

public class SmsData implements Serializable{
    public String code;
    public String message;

    @Override
    public String toString() {
        return "SmsData{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
