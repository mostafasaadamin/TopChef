package com.example.topchef.Models;

/**
 * Created by unknown on 6/8/2018.
 */

public class ordermodel {
    String  productID;
    String Price;
    String productName;
    String  Quantity;
    String  discount;
    String image_url;
    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }


    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public ordermodel(String productID, String price, String productName, String quantity, String discount, String image_url) {
        this.productID = productID;
        Price = price;
        this.productName = productName;
        Quantity = quantity;
        this.discount = discount;
        this.image_url=image_url;
    }
}
