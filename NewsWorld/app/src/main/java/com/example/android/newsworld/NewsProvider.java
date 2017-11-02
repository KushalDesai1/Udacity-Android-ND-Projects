package com.example.android.newsworld;

/**
 * Created by kushaldesai on 28/10/17.
 */

public class NewsProvider {

    private String newsHeadline;
    private String newsContent;
    private String newsPublishDate;
    private String newsUrl;
    private String newsImage;
    private String newsContributor;
    private String newsSection;

    public NewsProvider(String newsHeadline, String newsContent, String newsPublishDate, String newsUrl, String newsImage, String newsContributor, String newsSection) {
        this.newsHeadline = newsHeadline;
        this.newsContent = newsContent;
        this.newsPublishDate = newsPublishDate;
        this.newsUrl = newsUrl;
        this.newsImage = newsImage;
        this.newsContributor = newsContributor;
        this.newsSection = newsSection;
    }

    public String getNewsHeadline() {
        return newsHeadline;
    }

    public void setNewsHeadline(String newsHeadline) {
        this.newsHeadline = newsHeadline;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public String getNewsPublishDate() {
        return newsPublishDate;
    }

    public void setNewsPublishDate(String newsPublishDate) {
        this.newsPublishDate = newsPublishDate;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public String getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(String newsImage) {
        this.newsImage = newsImage;
    }

    public String getNewsContributor() {
        return newsContributor;
    }

    public void setNewsContributor(String newsContributor) {
        this.newsContributor = newsContributor;
    }

    public String getNewsSection() {
        return newsSection;
    }

    public void setNewsSection(String newsSection) {
        this.newsSection = newsSection;
    }
}
