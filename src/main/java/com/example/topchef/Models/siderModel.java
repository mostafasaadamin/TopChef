package com.example.topchef.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by unknown on 8/4/2018.
 */

public class siderModel  {
     String foodid,foodname,foodimage;
     public siderModel(){}
    public String getFoodid() {
        return foodid;
    }

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getFoodimage() {
        return foodimage;
    }

    public void setFoodimage(String foodimage) {
        this.foodimage = foodimage;
    }

    public siderModel(String foodid, String foodname, String foodimage) {

        this.foodid = foodid;
        this.foodname = foodname;
        this.foodimage = foodimage;
    }

}
