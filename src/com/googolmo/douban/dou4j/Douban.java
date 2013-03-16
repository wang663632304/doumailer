package com.googolmo.douban.dou4j;


import com.googolmo.douban.dou4j.http.AccessToken;
import com.googolmo.douban.dou4j.http.BaseApi;
import com.googolmo.douban.dou4j.model.DoubanException;
import com.googolmo.douban.dou4j.model.Doumail;
import com.googolmo.douban.dou4j.model.User;

/**
 * User: googolmo
 * Date: 13-2-8
 * Time: 上午11:18
 */
public class Douban {
    private BaseApi mApi;

    public Douban(BaseApi mApi) {
        this.mApi = mApi;
    }

    public BaseApi getBaseApi() {
        return mApi;
    }

    public Douban(String apiKey, String apiSecret, String callbackUrl) {
        this.mApi = new BaseApi(apiKey, apiSecret, callbackUrl);
    }

    public Douban(AccessToken accessToken, String apiKey, String apiSecret, String callbackUrl) {
        this.mApi = new BaseApi(accessToken, apiKey, apiSecret, callbackUrl);
    }

    public void setAccessToken(AccessToken accessToken) {
        mApi.setAccessToken(accessToken);
    }

    public User getUserInfo(String name) throws DoubanException {
        if (name == null) {
            name = "~me";
        }
        String url = mApi.url("v2/user/" + name, true);
        return mApi.getGson().fromJson(mApi.get(url, null, true).getResponseContent(), User.class);
    }

    public Doumail getInbox() throws DoubanException {
        String url = mApi.url("v2/doumail/inbox", true);
        return mApi.getGson().fromJson(mApi.get(url, null, true).getResponseContent(), Doumail.class);
    }
}
