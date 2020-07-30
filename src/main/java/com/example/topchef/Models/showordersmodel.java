package com.example.topchef.Models;

/**
 * Created by unknown on 6/10/2018.
 */

public class showordersmodel {
    public showordersmodel(){}
    String id,phone,Address,status;

    public showordersmodel(String id, String phone, String address, String status) {
        this.id = id;
        this.phone = phone;
        Address = address;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
