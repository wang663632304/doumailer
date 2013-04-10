package com.googolmo.douban.dou4a.http;

/**
 * 临时存储上传图片的内容，格式，文件信息等
 */
public class ImageItem {
    private byte[] content;
    private String name;
    private String contentType;

    public ImageItem(byte[] content, String name, String contentType) {
        this.content = content;
        this.name = name;
        this.contentType = contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public String getContentType() {
        return contentType;
    }
}
