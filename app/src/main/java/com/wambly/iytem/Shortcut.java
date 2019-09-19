package com.wambly.iytem;

class Shortcut {

    private String name;
    private String url;
    private int img;

    public Shortcut(String name, String url, int img) {
        this.name = name;
        this.url = url;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public int getImg() {
        return img;
    }

}
