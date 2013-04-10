/*
 * Copyright (c) 2013. By GoogolMo
 */

package com.googolmo.douban.dou4a.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;

/**
 * User: googolmo
 * Date: 13-2-12
 * Time: 上午9:28
 */
public class Doumail implements Parcelable {

    public static final String STATUS_READ = "R";
    public static final String STATUS_UNREAD = "U";

    @Expose private String status;
    @Expose private String id;
    @Expose private User sender;
    @Expose private User receiver;
    @Expose private String title;
    @Expose private String published;
    @Expose private String content;

    private Doumail(Parcel in) {

        this.status = in.readString();
        this.id = in.readString();
        this.title = in.readString();
        this.published = in.readString();
        this.content = in.readString();
    }


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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.published);
        dest.writeString(this.content);
    }

    public static Parcelable.Creator<Doumail> CREATOR = new Parcelable.Creator<Doumail>() {
        public Doumail createFromParcel(Parcel source) {
            return new Doumail(source);
        }

        public Doumail[] newArray(int size) {
            return new Doumail[size];
        }
    };

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
