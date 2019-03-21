package com.wambly.iytem;

import android.os.Parcel;
import android.os.Parcelable;

public class Tip implements Parcelable {
    private String title;
    private String date;
    private String description;
    private String body;

    public Tip() {
    }

    public Tip(String title, String date, String description, String body, String link) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.body = body;
    }

    protected Tip(Parcel in) {
        title = in.readString();
        date = in.readString();
        description = in.readString();
        body = in.readString();
    }

    public static final Creator<Tip> CREATOR = new Creator<Tip>() {
        @Override
        public Tip createFromParcel(Parcel in) {
            return new Tip(in);
        }

        @Override
        public Tip[] newArray(int size) {
            return new Tip[size];
        }
    };

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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
    }
}
