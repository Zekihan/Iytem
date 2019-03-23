package com.wambly.iytem;

import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Parcelable {
    String name;
    String email;
    String phone;
    String department;

    public Contact() {
    }

    public Contact(String name, String email, String phone, String department) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.department = department;
    }

    protected Contact(Parcel in) {
        name = in.readString();
        email = in.readString();
        phone = in.readString();
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

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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
    }
}
