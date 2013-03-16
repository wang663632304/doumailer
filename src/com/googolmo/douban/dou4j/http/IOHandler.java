package com.googolmo.douban.dou4j.http;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * User: googolmo
 * Date: 13-2-9
 * Time: 上午11:13
 */
public abstract class IOHandler {

    public abstract Response fetchData(String url, Method method, List<NameValuePair> params, List<NameValuePair> headers);

    public abstract Response fetchDataMultipartMime(String url, Method method, List<NameValuePair> params, List<NameValuePair> headers, MultipartParameter... parameters);
}
