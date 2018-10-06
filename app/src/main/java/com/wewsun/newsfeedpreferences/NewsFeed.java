package com.wewsun.newsfeedpreferences;

public class NewsFeed {

    private String title;
    private String date;
    private String url;
    private String section;
    private String author;

    public NewsFeed(String title, String date, String url, String section, String author){
        this.title = title;
        this.date = date;
        this.url = url;
        this.section = section;
        this.author = author;

    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public String getSection() {
        return section;
    }

    public String getAuthor() {
        return author;
    }
}
