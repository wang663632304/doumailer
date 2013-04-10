package com.googolmo.douban.dou4a.http;

import com.google.gson.Gson;
import com.googolmo.douban.dou4a.model.DoubanException;
import com.googolmo.douban.dou4a.util.DLog;
import com.googolmo.douban.dou4a.util.JsonUtils;
import org.apache.http.NameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * User: googolmo
 * Date: 13-2-10
 * Time: 上午11:22
 */
public class BaseApi {
    private static final String TAG = "DoubanApi";
    private static final String HOST = "api.douban.com";
    private static final String AUTH_BASE_URL = "https://www.douban.com/service/auth2/";

    private AccessToken accessToken;
    private String token;


    private Gson mGson;
    private String apiKey;
    private String apiSecret;
    private String callbackUrl;

    private String host;
    public BaseApi(String apiKey, String apiSecret, String callbackUrl) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.callbackUrl = callbackUrl;
        init();
    }


    public BaseApi(AccessToken accessToken, String apiKey, String apiSecret, String callbackUrl) {
        this.accessToken = accessToken;
        this.token = this.accessToken.getAccessToken();
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.callbackUrl = callbackUrl;
        init();
    }

    private void init() {
        this.mGson = JsonUtils.getGson();
        this.host = HOST;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
        this.token = this.accessToken.getAccessToken();
    }

    public void setGson(Gson gson) {
//        this.mGson = gson;
        JsonUtils.setGson(gson);
        this.mGson = JsonUtils.getGson();
    }

    public Gson getGson() {
        return this.mGson;
    }

    public String getAuthorizationUrl(){
        StringBuilder url = new StringBuilder(AUTH_BASE_URL);
        url.append("auth");
        url.append("?client_id=");
        url.append(this.apiKey);
        url.append("&redirect_uri=");
        url.append(this.callbackUrl);
        url.append("&response_type=code");
        return url.toString();
    }

    public AccessToken oAuth(String code) throws DoubanException {
        String url = AUTH_BASE_URL + "token";
        List<NameValuePair> p = new ArrayList<NameValuePair>();
        p.add(new RequestParameter("client_id", this.apiKey));
        p.add(new RequestParameter("client_secret", this.apiSecret));
        p.add(new RequestParameter("redirect_uri", this.callbackUrl));
        p.add(new RequestParameter("grant_type", "authorization_code"));
        p.add(new RequestParameter("code", code));
        AccessToken t = new AccessToken(post(url, p, false));
        this.accessToken = t;
        return t;
    }

    public AccessToken refreshToken() throws DoubanException {
        String url = AUTH_BASE_URL + "token";
        List<NameValuePair> p = new ArrayList<NameValuePair>();
        p.add(new RequestParameter("client_id", this.apiKey));
        p.add(new RequestParameter("client_secret", this.apiSecret));
        p.add(new RequestParameter("redirect_uri", this.callbackUrl));
        p.add(new RequestParameter("grant_type", "refresh_token"));
        p.add(new RequestParameter("refresh_token", accessToken.getRefreshToken()));
        AccessToken t = new AccessToken(post(url, p, false));
        this.accessToken = t;
        return t;
    }

    public String url(String path, boolean isHttps) {
        if (isHttps) {
            return "https://" + this.host + "/" + path;
        } else {
            return "http://" + this.host + "/" + path;
        }
    }

    public Response get(String url, List<NameValuePair> params) throws DoubanException {
        return get(url, params, true);
    }

    public Response get(String url, List<NameValuePair> params, boolean isAuth) throws DoubanException {
        try {
            url = getQuery(url, params);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        params.add(new RequestParameter("apikey", this.apiKey));
        BasicHandler handler = new BasicHandler(url, Method.GET);
        handler.setOAuth(accessToken.getAccessToken());
        Response response = handler.fetchData(null);
        if (response.getResponseCode() >= 300) {
            throw new DoubanException(response);
        }
        return response;
    }

    public Response post(String url, List<NameValuePair> params) throws DoubanException {
        return post(url, params, true);
    }

    public Response post(String url, List<NameValuePair> params, boolean isAuth) throws DoubanException {
        if (!isAuth) {
            params.add(new RequestParameter("apikey", this.apiKey));
        }
        BasicHandler handler = new BasicHandler(url, Method.POST);
        handler.setOAuth(accessToken.getAccessToken());
        Response response = handler.fetchData(params);
        DLog.d(TAG, response.getResponseContent());
        if (response.getResponseCode() >= 300) {
            throw new DoubanException(response);
        }
        return response;
    }

    public Response put(String url, List<NameValuePair> params) throws DoubanException {
        return put(url, params, true);
    }

    public Response put(String url, List<NameValuePair> params, boolean isAuth) throws DoubanException {

        BasicHandler handler = new BasicHandler(url, Method.PUT);
        handler.setOAuth(accessToken.getAccessToken());
        Response response = handler.fetchData(params);

        DLog.d(TAG, response.getResponseContent());
        if (response.getResponseCode() >= 300) {
            throw new DoubanException(response);
        }
        return response;
    }

    public Response delete(String url) throws DoubanException {
        BasicHandler handler = new BasicHandler(url, Method.PUT);
        handler.setOAuth(accessToken.getAccessToken());
        Response response = handler.fetchData(null);

        DLog.d(TAG, response.getResponseContent());
        if (response.getResponseCode() >= 300) {
            throw new DoubanException(response);
        }
        return response;
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
