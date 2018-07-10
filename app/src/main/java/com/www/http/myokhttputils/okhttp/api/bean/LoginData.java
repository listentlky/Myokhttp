package com.www.http.myokhttputils.okhttp.api.bean;


import java.io.Serializable;

/**
 * Created by AED on 2017/9/27.
 */

public class LoginData implements Serializable{

    public String code;
    public GetToken data;
    public String message;

    public class GetToken implements Serializable{
        public String access_token;
        public String error;
        public String error_description;
        public int expires_in;
        public String refresh_token;
        public String token_type;


        @Override
        public String toString() {
            return "GetToken{" +
                    "access_token='" + access_token + '\'' +
                    ", error='" + error + '\'' +
                    ", error_description='" + error_description + '\'' +
                    ", expires_in=" + expires_in +
                    ", refresh_token='" + refresh_token + '\'' +
                    ", token_type='" + token_type + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LoginRetData{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
