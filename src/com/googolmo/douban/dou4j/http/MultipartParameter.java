package com.googolmo.douban.dou4j.http;

/**
 * User: googolmo
 * Date: 13-2-9
 * Time: 上午11:15
 */
public class MultipartParameter {

    /**
     * Constructor
     *
     * @param name name of the parameter
     * @param contentType content type
     * @param content content
     */
    public MultipartParameter(String name, String contentType, byte[] content) {
        this.name = name;
        this.contentType = contentType;
        this.content = content;
    }

    /**
     * Returns content
     *
     * @return content
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * Returns content type
     *
     * @return content type
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Returns parameter name
     *
     * @return parameter name
     */
    public String getName() {
        return name;
    }

    private String name;

    private String contentType;
    private byte[] content;
}
