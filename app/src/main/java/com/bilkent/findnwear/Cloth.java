package com.bilkent.findnwear;

/**
 * Created by 1 on 05.11.2015.
 */
public class Cloth {
    private String photoURL, description, id,price;

    public Cloth(String description,String photoURL,String price, String photoId){
        this.photoURL = photoURL;
        this.description = description;
        id = photoId;
        this.price =price;
    }
    public String getPrice() {
        return price;
    }
    public String getPhotoURL() {
        return photoURL;
    }

    public String getDescription() {
        return description;
    }

    public String getPhotoId() {
        return id;
    }
}
