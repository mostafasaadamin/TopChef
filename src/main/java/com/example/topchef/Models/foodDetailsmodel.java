package com.example.topchef.Models;

/**
 * Created by unknown on 5/13/2018.
 */

public class foodDetailsmodel {
    public foodDetailsmodel(){}
    private String  name,image,price,discount,description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;
    public foodDetailsmodel(String id,String name, String image, String price, String discount, String description) {
        this.id=id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.discount = discount;
        this.description = description;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getDiscount() {
        return discount;
    }
    public void setDiscount(String discount) {
        this.discount = discount;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
