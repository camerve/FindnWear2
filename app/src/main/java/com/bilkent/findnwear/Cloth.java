package com.bilkent.findnwear;

/**
 * Created by 1 on 05.11.2015.
 */
public class Cloth {
    String photoURL, explanation, id;

    public Cloth(String photoURL,String explanation, String photoId){
        this.photoURL = photoURL;
        this.explanation = explanation;
        id = photoId;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getPhotoId() {
        return id;
    }
}
