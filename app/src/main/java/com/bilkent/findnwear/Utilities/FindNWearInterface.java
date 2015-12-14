package com.bilkent.findnwear.Utilities;

import com.bilkent.findnwear.Model.Response.Base;
import com.bilkent.findnwear.Model.Response.ClothList;
import com.bilkent.findnwear.Model.Response.Login;
import com.bilkent.findnwear.Model.Response.Wishlist;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Created by absolute on 12/8/15.
 */
public interface FindNWearInterface {

    @GET("/login")
    void login(@Query("email") String email,
               @Query("password") String password,
               Callback<Login> cb);

    @GET("/fbLogin")
    void fbLogin(@Query("email") String email,
                 @Query("token") String password,
                 Callback<Login> cb);

    @FormUrlEncoded
    @POST("/register")
    void register(@Field("email") String email,
                  @Field("password") String password,
                  @Field("name")  String name,
                  @Field("surname")  String surname,
                  Callback<Login> cb);

    @GET("/getReco")
    void getReco(@Query("user_id") int user_id,
                 Callback<ClothList> cb);

    @Multipart
    @POST("/search")
    void search(@Part("file") TypedFile file,
                @Part("type") String type,
                Callback<ClothList> cb);

    @GET("/searchKeyword")
    void searchKeyword(@Query("type") String type,
                       @Query("color") String color,
                       @Query("texture") String texture,
                       Callback<ClothList> cb);

    @GET("/getWishlist")
    void getWishlist(@Query("user_id") int user_id,
                     Callback<Wishlist> cb);

    @GET("/addWishlist")
    void addWishlist(@Query("user_id") int user_id,
                     @Query("item_id") int item_id,
                     Callback<Base> cb);

    @GET("/removeWishlist")
    void removeWishlist(@Query("user_id") int user_id,
                        @Query("item_id") int item_id,
                        Callback<Base> cb);
}
