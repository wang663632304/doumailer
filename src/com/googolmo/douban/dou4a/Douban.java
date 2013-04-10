package com.googolmo.douban.dou4a;


import com.google.gson.reflect.TypeToken;
import com.googolmo.douban.dou4a.http.AccessToken;
import com.googolmo.douban.dou4a.http.BaseApi;
import com.googolmo.douban.dou4a.http.RequestParameter;
import com.googolmo.douban.dou4a.model.DoubanException;
import com.googolmo.douban.dou4a.model.Doumail;
import com.googolmo.douban.dou4a.model.User;
import org.apache.http.NameValuePair;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    /**
     * 获得用户信息
     * @param name
     * @return
     * @throws DoubanException
     */
    public User getUserInfo(String name) throws DoubanException {
        if (name == null) {
            name = "~me";
        }
        String url = mApi.url("v2/user/" + name, true);
        return mApi.getGson().fromJson(mApi.get(url, null, true).getResponseContent(), User.class);
    }

    /**
     * 获得豆邮列表
     * @return
     * @throws DoubanException
     */
    public List<Doumail> getInbox() throws DoubanException {
        String url = mApi.url("v2/doumail/inbox", true);
        Type type = new TypeToken<List<Doumail>>() {}.getType();
        return mApi.getGson().fromJson(mApi.get(url, null, true).getResponseContent(), type);
    }

    /**
     * 获得某封豆邮
     * @param id
     * @return
     */
    public Doumail getDoumail(String id) throws DoubanException {
        String url = mApi.url("v2/doumail/" + id, true);
        return mApi.getGson().fromJson(mApi.get(url, null, true).getResponseContent(), Doumail.class);
    }

    /**
     * 获得收件箱
     * @return
     */
    public List<Doumail> getOutbox() throws DoubanException {
        String url = mApi.url("v2/doumail/outbox", true);
        Type type = new TypeToken<List<Doumail>>() {}.getType();
        return mApi.getGson().fromJson(mApi.get(url, null, true).getResponseContent(), type);
    }

    /**
     * 获得未读列表
     * @return
     * @throws DoubanException
     */
    public List<Doumail> getUnread() throws DoubanException {
        String url = mApi.url("v2/doumail/inbox/unread", true);
        Type type = new TypeToken<List<Doumail>>() {}.getType();
        return mApi.getGson().fromJson(mApi.get(url, null, true).getResponseContent(), type);
    }

    /**
     * 发送一封豆邮
     * @param title
     * @param content
     * @param receiverId
     * @throws DoubanException
     */
    public void sendDoumail(String title, String content, String receiverId) throws DoubanException {
        String url = mApi.url("v2/doumails", true);
        List<NameValuePair> p = new ArrayList<NameValuePair>();
        p.add(new RequestParameter("title", title));
        p.add(new RequestParameter("content", content));
        p.add(new RequestParameter("receiver_id", receiverId));
        mApi.post(url, p, true);
    }

    /**
     * 标记豆邮可读
     * @param id
     * @throws DoubanException
     */
    public void read(String id) throws DoubanException {
        String url = mApi.url("v2/doumail/" + id, true);
        mApi.put(url, null, true);
    }

    /**
     * 批量标记可读
     * @param ids
     * @throws DoubanException
     */
    public void read(Set<String> ids) throws DoubanException {
        String url = mApi.url("v2/doumail/read", true);
        List<NameValuePair> p = new ArrayList<NameValuePair>();
        StringBuffer sb = new StringBuffer();
        for (String id : ids) {
            sb.append(id);
            sb.append(",");
        }
        p.add(new RequestParameter("ids", sb.toString()));
        mApi.put(url, p, true);
    }


    /**
     * 标记豆邮可读
     * @param id
     * @throws DoubanException
     */
    public void delete(String id) throws DoubanException {
        String url = mApi.url("v2/doumail/" + id, true);
        mApi.delete(url);
    }

    /**
     * 批量标记可读
     * @param ids
     * @throws DoubanException
     */
    public void delete(Set<String> ids) throws DoubanException {
        String url = mApi.url("v2/doumail/delete", true);
        List<NameValuePair> p = new ArrayList<NameValuePair>();
        StringBuffer sb = new StringBuffer();
        for (String id : ids) {
            sb.append(id);
            sb.append(",");
        }
        p.add(new RequestParameter("ids", sb.toString()));
        mApi.post(url, p, true);
    }

}
