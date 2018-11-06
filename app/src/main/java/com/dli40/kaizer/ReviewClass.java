package com.dli40.kaizer;

public class ReviewClass {
    private int rating;
    private String name, imgUrl, text, timeCreated, url;

    public ReviewClass(int rating, String name, String imgUrl, String text, String timeCreated, String url) {
        this.rating = rating;
        this.name = name;
        this.imgUrl = imgUrl;
        this.text = text;
        this.timeCreated = timeCreated;
        this.url = url;
    }

    public int getRating() {
        return rating;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public String getUrl() {
        return url;
    }

    public String toString() {
        return name + " " + rating + " " + timeCreated;
    }
}
