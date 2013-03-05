package com.googolmo.douban.dou4j;


import com.googolmo.douban.dou4j.http.BaseApi;

/**
 * User: googolmo
 * Date: 13-2-8
 * Time: 上午11:18
 */
public class Douban {
    private BaseApi mApi;

    public Douban() {
        this.mApi = new BaseApi();
    }

    public Douban(BaseApi mApi) {
        this.mApi = mApi;
    }
}
