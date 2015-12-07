package com.bilkent.findnwear;

/**
 * Created by 1 on 05.11.2015.
 */
public class Cloth {
    private String id, type,color,texture,price,brand,url,photoURL;

    public Cloth(String id,String type,String color,String texture,String price,String brand,
                 String url,String photoURL){
        this.photoURL = photoURL;
        this.id = id;
        this.type = type;
        this.color = color;
        this.texture = texture;
        this.brand = brand;
        this.url = url;
        this.price =price;
    }
    public String getPrice() {return price;}
    public String getPhotoURL() {return photoURL;}

    public String getDescription() {
        return color + " " + texture+ " "+ type;
    }

    public String getPhotoId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getUrl() {
        return url;
    }
}
