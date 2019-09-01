package com.wambly.iytem;

import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Parcelable{
    private String name;
    private String email;
    private String phone;
    private String department;
    private String title;


    public Contact(){

    }

    public Contact(String name, String email, String phone, String department, String title) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.title = title;
    }



    private Contact(Parcel in) {
        name = in.readString();
        email = in.readString();
        phone = in.readString();
        department = in.readString();
        title = in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getDepartment() {
        return department;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(department);
        dest.writeString(title);
    }
}

