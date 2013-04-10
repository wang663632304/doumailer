package com.googolmo.douban.dou4a.model;

import com.googolmo.douban.dou4a.http.Response;

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

    public DoubanException(String msg) {
        super(msg);
    }

    public DoubanException(Response response) {

    }

    public DoubanException(String msg, String request, int errorCode, int statusCode) {
        super(msg);
        this.statusCode = statusCode;
        this.msg = msg;
        this.request = request;
        this.errorCode = errorCode;
    }

    public DoubanException(String json, int statusCode) {

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
