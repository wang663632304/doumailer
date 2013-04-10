/*
 * Copyright (c) 2013. By GoogolMo
 */

package com.googolmo.douban.doumailer.data;

import android.content.Context;
import android.content.SharedPreferences;
import com.googolmo.douban.dou4a.Douban;
import com.googolmo.douban.dou4a.http.AccessToken;
import com.googolmo.douban.doumailer.Constants;

import java.util.HashSet;
import java.util.Set;

/**
 * User: GoogolMo
 * Date: 13-3-16
 * Time: 上午9:50
 */
public class Provider {
    private Douban mApi;
    private Context mContext;

    public Provider(Context context) {
        this.mContext = context;
        AccessToken t = getLoginedUserToken();
        if (t != null) {
            this.mApi = new Douban(t, Constants.API_KEY, Constants.API_SECRET, Constants.CALLBACKURL);
        } else {
            this.mApi = new Douban(Constants.API_KEY, Constants.API_SECRET, Constants.CALLBACKURL);
        }
    }

    public void setAccessToken(AccessToken t) {
        mApi.setAccessToken(t);
    }

    public Douban getApi() {
        return mApi;
    }

    public AccessToken getLoginedUserToken(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("sessions", Context.MODE_PRIVATE);
        String uid = getCurrentUid();
        String at = sharedPreferences.getString(getUserString(uid, "at"), null);
        String rt = sharedPreferences.getString(getUserString(uid, "rt"), null);
        int epi = sharedPreferences.getInt(getUserString(uid, "epi"), 0);
        if (at == null) {
            return null;
        }
        AccessToken t = new AccessToken(at, epi, rt, uid);
        return t;
    }

    public void addUser(AccessToken t) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("sessions", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getUserString(t.getUid(), "at"), t.getAccessToken());
        editor.putString(getUserString(t.getUid(), "rt"), t.getRefreshToken());
        editor.putInt(getUserString(t.getUid(), "epi"), t.getExpireIn());
        addUserId(t.getUid());
        editor.commit();
    }

    private String getUserString(String userId, String originString) {
        return userId + "_" + originString;
    }


    /**
     * 获得当前用户ID
     * @return
     */
    public String getCurrentUid() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("sessions", Context.MODE_PRIVATE);
        return sharedPreferences.getString("cuid", null);
    }

    /**
     * 从持久化存储中获得保存的用户ID集合
     * @return
     */
    public Set<String> getUserIds() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("sessions", Context.MODE_PRIVATE);
        String users = sharedPreferences.getString("uss", null);
        Set<String> userIdSet = new HashSet<String>();
        if (users != null) {
            String[] s = users.split("&");
            for (String id : s) {
                userIdSet.add(id);
            }
        }
        return userIdSet;
    }

    public void addUserId(String userId) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("sessions", Context.MODE_PRIVATE);
        String users = sharedPreferences.getString("uss", "");
        Set<String> userIds = getUserIds();

        if (!userIds.contains(userId)) {
            StringBuilder builder = new StringBuilder();
            for (String id: userIds) {
                builder.append(id);
                builder.append("&");
            }
            builder.append(userId);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("uss", builder.toString());
        }
    }

    /**
     * 设置当前账户ID
     * @param uid
     */
    public void setCurrentUid(String uid) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("sessions", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cuid", uid);
        editor.commit();
    }
}
