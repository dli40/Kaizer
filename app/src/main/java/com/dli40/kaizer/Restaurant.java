package com.dli40.kaizer;//implementation 'com.android.support:appcompat-v7:26.1.0'
//androidTestImplementation 'com.android.support.test:runner:1.0.2'


import java.util.ArrayList;

public class Restaurant {
    private String busId, name, price;
    private double distance, rating;
    private int reviewCount;
    // private String[] displayAddress;
    private String photoUrl;
    private ArrayList hours, categories;



    /*TO IMPLEMENT.....
    phone number, reviews, review url stuff, Hours
     */

    public Restaurant(String busId, String name, String price, double distance, double rating,
                      int reviewCount, String photoUrl, ArrayList hours, ArrayList categories) {
        this.busId = busId;
        this.distance = distance;
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.reviewCount = reviewCount;
        // this.displayAddress = displayAddress;
        this.photoUrl = photoUrl;
        this.hours = hours;
        this.categories = categories;
    }

    public ArrayList getHours() {
        return hours;
    }

    public double getDistance() {
        return distance;
    }

    public double getRating() {
        return rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }


    public String getBusId() {
        return busId;
    }

    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getPrice() {
        return price;
    }

    public ArrayList getCategories() {
        return categories;
    }
    //Name
    //Reviewss -- not yet
    // rating
    //Photo
    //Price
    //distance
    //Address

    //id

}
