package com.wambly.iytem;

public enum TransportationType {

    bus_982(R.string.bus_982,"iyte_izmir","izmir_iyte"),
    bus_882(R.string.bus_882,"iyte_urla","urla_iyte"),
    teneke(R.string.teneke,"iyte_urla","urla_iyte");

    private int titleVal;
    private String direction0;
    private String direction1;

    TransportationType(int titleVal, String direction0, String direction1){
        this.titleVal = titleVal;
        this.direction0 = direction0;
        this.direction1 = direction1;
    }

    public int getTitleVal() {
        return titleVal;
    }

    public String getDirection0() {
        return direction0;
    }

    public String getDirection1() {
        return direction1;
    }

    public String getStrDirection0() {
        return direction0.toUpperCase().replace("_", " --> ");
    }

    public String getStrDirection1() {
        return direction1.toUpperCase().replace("_", " --> ");
    }
}
