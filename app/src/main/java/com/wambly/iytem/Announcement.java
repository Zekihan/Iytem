package com.wambly.iytem;

public class Announcement {
    private String title;
    private String date;
    private String description;
    private String body;
    private String link;

    public Announcement(String title, String date, String description, String body, String link) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.body = body;
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Announcement : [" + "Title : " + title + ", "
                + "Date : " + date + ", "
                + "Body : " + body + " ]";
    }
}
