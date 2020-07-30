package com.example.topchef.Models;

/**
 * Created by unknown on 7/23/2018.
 */

public class rating {
    String foodid,userphone,ratingvalue,comment;
    public rating(){}
    public String getFoodid() {
        return foodid;
    }

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getRatingvalue() {
        return ratingvalue;
    }

    public void setRatingvalue(String ratingvalue) {
        this.ratingvalue = ratingvalue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public rating(String foodid, String userphone, String ratingvalue, String comment) {
        this.foodid = foodid;
        this.userphone = userphone;
        this.ratingvalue = ratingvalue;
        this.comment = comment;
    }
}
