package com.example.topchef.Models;

/**
 * Created by unknown on 5/3/2018.
 */

public class categorymodel {
    public String getType() {
        return type;
    }
public  categorymodel(){}
    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    String type,Image;

    public categorymodel(String type, String image) {
        this.type = type;
        Image = image;
    }
}
