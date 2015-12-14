package com.bilkent.findnwear.Utilities;

/**
 * Created by absolute on 12/14/15.
 */
public class StringUtilities {

    public static String upperCaseEachWord(String a){
        a = a.replaceAll("[ ]+", " ");
        String[] words = a.split(" ");
        String result = "";
        for(int i = 0 ; i < words.length; i++){
            result += Character.toUpperCase(words[i].charAt(0)) + words[i].substring(1) + " ";
        }
        return result.substring(0, result.length()-1);
    }
}
