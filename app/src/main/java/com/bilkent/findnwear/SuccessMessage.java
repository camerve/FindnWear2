package com.bilkent.findnwear;

/**
 * Created by 1 on 07.12.2015.
 */
public class SuccessMessage {
    int success;
    String message;
    public SuccessMessage(int success,String message){
        this.success = success;
        this.message = message;
    }

    public int getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
