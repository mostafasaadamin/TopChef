package com.example.topchef.retrofit;


import com.example.topchef.Models.foodDetailsmodel;
import com.example.topchef.Models.showordersmodel;
import com.example.topchef.UserPanel.responseModel;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
public interface connection {
    @GET
    Call<ArrayList<foodDetailsmodel>> getAllFoods(@Url String url);
    @GET
    Call<ArrayList<showordersmodel>> getAllorders(@Url String url);
    @FormUrlEncoded
    @POST("update_Customer_Token.php")
    Call<ResponseBody>sendToken(
            @Field("token") String token,
            @Field("phone") String phone);
    @FormUrlEncoded
    @POST("cancel_order.php")
    Call<ResponseBody>cancelOrder(@Field("id") String id);
    @FormUrlEncoded
    @POST("sendnotificationtoserver.php")
    Call<responseModel>sendNotification(
            @Field("title") String title,
            @Field("message") String message);

    @FormUrlEncoded
    @POST("eateRegister.php")
    Call<ResponseBody>Register(@Field("name") String name,@Field("phonenumber") String phonenumber,@Field("password") String password,@Field("token") String token);
}
