package com.weather.weatherapplication.recycleviewlist;

public class recyclelist {
    String imageurl;
    String day;
    String high;
    String low;

    public recyclelist( String imageurl, String day, String high, String low) {
        this.day = day;
        this.high = high;
        this.low = low;
        this.imageurl=imageurl;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }
}
