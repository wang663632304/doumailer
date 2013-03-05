package com.googolmo.douban.dou4j.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.NameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * User: googolmo
 * Date: 13-2-10
 * Time: 上午11:22
 */
public class BaseApi {

    private AccessToken accessToken;
    private String token;

    private IOHandler ioHandler;

    private Gson mGson;

    public BaseApi() {
        this.ioHandler = new BasicHandler();
        init();
    }


    public BaseApi(AccessToken accessToken) {
        this.accessToken = accessToken;
        this.token = this.accessToken.getAccessToken();
        this.ioHandler = new BasicHandler();
        init();
    }

    public BaseApi(AccessToken accessToken, IOHandler ioHandler) {
        this.accessToken = accessToken;
        this.token = this.accessToken.getAccessToken();
        this.ioHandler = ioHandler;
        init();
    }

    private void init() {
        this.mGson = new GsonBuilder()
                .disableHtmlEscaping()
                .serializeNulls()
                .create();
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
        this.token = this.accessToken.getAccessToken();
    }

    public IOHandler getIoHandler() {
        return ioHandler;
    }

    public void setIoHandler(IOHandler ioHandler) {
        this.ioHandler = ioHandler;
    }

    public void setGson(Gson gson) {
        this.mGson = gson;
    }

    public Gson getGson() {
        return this.mGson;
    }

    public Response get(String url, List<NameValuePair> params) {
        return get(url, params, true);
    }

    public Response get(String url, List<NameValuePair> params, boolean isAuth) {
        try {
            url = getQuery(url, params);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ioHandler.fetchData(url, Method.GET, null, isAuth ? this.token : null);
    }

    public Response post(String url, List<NameValuePair> params) {
        return post(url, params, true);
    }

    public Response post(String url, List<NameValuePair> params, boolean isAuth) {
        return ioHandler.fetchData(url, Method.POST, params, isAuth ? this.token : null);
    }

    public Response put(String url, List<NameValuePair> params) {
        return put(url, params, true);
    }

    public Response put(String url, List<NameValuePair> params, boolean isAuth) {
        return ioHandler.fetchData(url, Method.PUT, params, isAuth ? this.token : null);
    }

    public Response delete(String url) {
        return ioHandler.fetchData(url, Method.DELETE, null, this.token);
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }


    private String getQuery(String url, List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder(url);
        boolean first = true;
        if (url.contains("?")) {
            first = false;
        }


        for (NameValuePair pair : params)
        {
            if (first) {
                first = false;
                result.append("?");
            } else {
                result.append("&");
            }


            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

}
