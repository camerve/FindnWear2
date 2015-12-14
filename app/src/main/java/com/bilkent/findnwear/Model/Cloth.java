package com.bilkent.findnwear.Model;

/**
 * Created by 1 on 05.11.2015.
 */
public class Cloth {
    public int id;
    public String type;
    public String color;
    public String texture;
    public float price;
    public String brand;
    public String url;
    public String photo_url;
    public String thumb_url;

    public String getDescription() {
        return color + " " + texture+ " "+ type;
    }
}
