/*
 * Copyright (c) 2013. By GoogolMo
 */

package com.googolmo.douban.dou4j.http;

import org.apache.http.NameValuePair;

/**
 * User: GoogolMo
 * Date: 13-3-16
 * Time: 上午8:36
 */
public class RequestParameter implements NameValuePair {
    private String name;
    private String value;

    public RequestParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
