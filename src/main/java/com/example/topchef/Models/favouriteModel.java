package com.example.topchef.Models;

/**
 * Created by unknown on 8/4/2018.
 */

public class favouriteModel {
    String foodID,FoodName,ImageURl,price,description,discount;

    public favouriteModel(){}

    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public String getImageURl() {
        return ImageURl;
    }

    public void setImageURl(String imageURl) {
        ImageURl = imageURl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public favouriteModel(String foodID, String foodName, String imageURl, String price, String description, String discount) {

        this.foodID = foodID;
        FoodName = foodName;
        ImageURl = imageURl;
        this.price = price;
        this.description = description;
        this.discount = discount;
    }
}
