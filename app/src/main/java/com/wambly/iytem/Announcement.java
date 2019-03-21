package com.wambly.iytem;

import android.os.Parcel;
import android.os.Parcelable;

public class Announcement implements Parcelable {
    private String title;
    private String date;
    private String description;
    private String body;
    private String link;

    public Announcement() {
    }

    public Announcement(String title, String date, String description, String body, String link) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.body = body;
        this.link = link;
    }

    public Announcement(Parcel in) {
        title = in.readString();
        date = in.readString();
        description = in.readString();
        body = in.readString();
        link = in.readString();
    }

    public static final Creator<Announcement> CREATOR = new Creator<Announcement>() {
        @Override
        public Announcement createFromParcel(Parcel in) {
            return new Announcement(in);
        }

        @Override
        public Announcement[] newArray(int size) {
            return new Announcement[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(description);
        dest.writeString(body);
        dest.writeString(link);
    }
}
