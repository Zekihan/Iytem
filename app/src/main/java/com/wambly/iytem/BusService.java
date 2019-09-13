package com.wambly.iytem;

import android.os.Parcel;
import android.os.Parcelable;

public class BusService implements Parcelable{
    private String name;
    private String direction0;
    private String direction1;

    public BusService(String name, String direction0, String direction1) {
        this.name = name;
        this.direction0 = direction0;
        this.direction1 = direction1;
    }

    BusService(String name, String direction0) {
        this.name = name;
        this.direction0 = direction0;
        this.direction1 = reverseDir(direction0);
    }

    private BusService(Parcel in){
        name = in.readString();
        direction0 = in.readString();
        direction1 = in.readString();
    }
    public static final Creator<BusService> CREATOR = new Creator<BusService>() {
        @Override
        public BusService createFromParcel(Parcel in) {
            return new BusService(in);
        }

        @Override
        public BusService[] newArray(int size) {
            return new BusService[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(direction0);
        dest.writeString(direction1);
    }

    private String reverseDir(String direction){
        String[] ways = direction.split("_");
        return ways[1] + "_" + ways[0];
    }

    public String getName() {
        return name;
    }

    public String getDirection0() {
        return direction0;
    }

    public String getDirection1() {
        return direction1;
    }

    public String getNameStr(){
        return firstLetterUp(name.replace("_", " - ")).
                replace("Bus","Eshot");
    }

    public String getDirectionTileStr(){
        return direction0.toUpperCase().replace("_", "  <-->  ");
    }

    public String getWay0(){
        return direction0.toUpperCase().split("_")[0];
    }

    public String getWay1(){
        return direction0.toUpperCase().split("_")[1];
    }

    private String firstLetterUp(String str){
        return str.substring(0,1).toUpperCase() + str.substring(1);
    }
}
