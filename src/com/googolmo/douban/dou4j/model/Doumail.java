/*
 * Copyright (c) 2013. By GoogolMo
 */

package com.googolmo.douban.dou4j.model;

import com.google.gson.annotations.Expose;

/**
 * User: googolmo
 * Date: 13-2-12
 * Time: 上午9:28
 */
public class Doumail {

    public static final String STATUS_READ = "R";
    public static final String STATUS_UNREAD = "U";

    @Expose private String status;
    @Expose private String id;
    @Expose private User sender;
    @Expose private User receiver;
    @Expose private String title;
    @Expose private String published;
    @Expose private String content;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Doumail{" +
                "status='" + status + '\'' +
                ", id='" + id + '\'' +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", title='" + title + '\'' +
                ", published='" + published + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
