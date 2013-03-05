package com.googolmo.douban.dou4j.model;

import org.json.JSONObject;

/**
 * User: googolmo
 * Date: 13-2-8
 * Time: 上午11:25
 */
public class DoubanException extends Exception {
    private int statusCode = -1;
    private int errorCode = -1;
    private String request;
    private String msg;
    private static final long serialVersionUID = -2623309261327598087L;

    public DoubanException(String msg) {
        super(msg);
    }

    public DoubanException(Exception cause) {
        super(cause);
    }

    public DoubanException(String msg, int statusCode) {
        super(msg);
        this.statusCode = statusCode;
    }

    public DoubanException(JSONObject json, int statusCode) {
        super("\n msg:" + json.optString("msg", "") + " code:" + json.optInt("code", -1) + " request:" + json.optString("request", ""));
        this.statusCode = statusCode;
        this.errorCode = json.optInt("code", -1);
        this.msg = json.optString("msg", "");
        this.request = json.optString("request", "");
    }

    public DoubanException(String msg, Exception cause) {
        super(msg, cause);
    }

    public DoubanException(String msg, Exception cause, int statusCode) {
        super(msg, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getRequest() {
        return request;
    }

    public String getMsg() {
        return msg;
    }
}
