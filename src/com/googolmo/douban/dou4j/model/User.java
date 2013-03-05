/*
 * Copyright (c) 2013. By GoogolMo
 */

package com.googolmo.douban.dou4j.model;

import com.google.gson.annotations.Expose;
import org.json.JSONObject;

/**
 * User: googolmo
 * Date: 13-2-12
 * Time: 上午9:28
 */
public class User {

    @Expose private String id;
    @Expose private String uid;
    @Expose private String name;
    @Expose private String alt;
    @Expose private String avatar;
    @Expose private String loc;
    @Expose private String status;
    @Expose private String created;
    @Expose private String desc;
    @Expose private String signature;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "User{" +
                ", id='" + id + '\'' +
                ", uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", alt='" + alt + '\'' +
                ", avatar='" + avatar + '\'' +
                ", loc='" + loc + '\'' +
                ", status='" + status + '\'' +
                ", created='" + created + '\'' +
                ", desc='" + desc + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }




}
