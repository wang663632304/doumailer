package com.googolmo.douban.dou4a.http;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.googolmo.douban.dou4a.model.DoubanException;
import com.googolmo.douban.dou4a.util.JsonUtils;

import java.io.Serializable;
import java.io.StringReader;


public class AccessToken implements Serializable {

    private static final long serialVersionUID = 6986530164134648944L;
    private String accessToken;
    private int expireIn = 0;
    private String refreshToken;
    private String uid;

    public AccessToken(Response res) throws DoubanException {

        if (res.getResponseContent() != null) {
            JsonReader reader = new JsonReader(new StringReader(res.getResponseContent()));
            JsonElement element = JsonUtils.getJsonParser().parse(reader);
            if (element != null) {
                JsonObject object = element.getAsJsonObject();
                JsonElement access_token_element = object.get("access_token");
                if (access_token_element != null) {
                    this.accessToken = access_token_element.getAsString();
                }
                JsonElement refresh_token_element = object.get("refresh_token");
                if (refresh_token_element != null) {
                    this.refreshToken = refresh_token_element.getAsString();
                }

                JsonElement expires_in_element = object.get("expires_in");
                if (expires_in_element != null) {
                    this.expireIn = expires_in_element.getAsInt();
                }
                JsonElement uid_element = object.get("uid");
                if (uid_element != null) {
                    this.uid = uid_element.getAsString();
                }
            }


        } else {
            throw  new DoubanException("no accesstoken");
        }
    }

    public AccessToken(String accessToken, int expireIn, String refreshToken, String uid) {
        this.accessToken = accessToken;
        this.expireIn = expireIn;
        this.refreshToken = refreshToken;
        this.uid = uid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AccessToken other = (AccessToken) obj;
        if (accessToken == null) {
            if (other.accessToken != null)
                return false;
        } else if (!accessToken.equals(other.accessToken))
            return false;
        if (expireIn != other.expireIn)
            return false;
        if (refreshToken == null) {
            if (other.refreshToken != null)
                return false;
        } else if (!refreshToken.equals(other.refreshToken))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AccessToken [" +
                "accessToken=" + accessToken +
                ", expireIn=" + expireIn +
                ", refreshToken=" + refreshToken +
                ",uid=" + uid +
                "]";
    }

    public boolean isAvailable(){
        if (this.accessToken == null) {
            return false;
        } else {
            return true;
        }
    }


}
