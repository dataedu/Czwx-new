package com.dk.mp.tzgg.entity;

/**
 * 通知公告实体
 * 作者：janabo on 2017/1/3 17:21
 */
public class Tzgg {
    private String publishTime; //时间
    private String url;//链接
    private String title;//标题
    private String author;//副标题

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
