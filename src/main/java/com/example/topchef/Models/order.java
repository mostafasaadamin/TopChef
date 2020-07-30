package com.example.topchef.Models;

/**
 * Created by unknown on 6/1/2018.
 */

public class order
{
    public static final String TABLE_NAME2 = "Favouritestable";
    public static final String Favourite_id = "Food_ID";
    public static final String Favourite_id_increment = "Favourite_ID";
    public static final String food_name= "FOOD_NAME";
    public static final String food_price= "FOOD_PRICE";
    public static final String food_des= "FOOD_DES";
    public static final String food_discount= "FOOD_DisCount";
    public static final String Favourite_image_url= "IMAGE_URL";
    public static final String TABLE_NAME = "Products";
    public static final String Product_ID = "P_id";
    public static final String product_Name = "name";
    public static final String food_id = "id";
    public static final String Product_price = "price";
    public static final String product_image_url = "FOOD_IMAGE";
    public static final String product_quantity= "quantity";
    public static final String product_discount = "discount";
    String productID;
    String Price;
    String productName;
    String  Quantity;
    String  discount;
    String image_url;
    String id;
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + Product_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + product_Name + " TEXT NOT NULL,"
                    + Product_price + " TEXT NOT NULL,"
                    + product_quantity + " TEXT NOT NULL,"
                    +  product_discount + " TEXT NOT NULL,"
                    + product_image_url+ " TEXT NOT NULL,"
                    +  food_id + " TEXT NOT NULL"
                    + ")";

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public static final String CREATE_TABLE_Favourite =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME2 + "("
                    + Favourite_id_increment + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Favourite_id + " TEXT NOT NULL,"
                    + food_name + " TEXT NOT NULL,"
                    + Favourite_image_url+ " TEXT NOT NULL,"
                    + food_price+ " TEXT NOT NULL,"
                    + food_des+ " TEXT NOT NULL,"
                    + food_discount+ " TEXT NOT NULL"
                    + ")";
    public order(String productID, String price, String productName, String quantity, String discount, String id) {
        this.productID = productID;
        Price = price;
        this.productName = productName;
        Quantity = quantity;
        this.discount = discount;
        this.id = id;
    }

    public  order(){}

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
