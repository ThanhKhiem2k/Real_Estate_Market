package com.example.real_estate_market.Class.Class;

public class ClassPost {
    String Type, Name, Address, Price, PhoneNumber, UserID, Detail, Key ;

    public ClassPost(String type, String name, String address, String price, String phoneNumber, String userID, String detail, String key) {
        Type = type;
        Name = name;
        Address = address;
        Price = price;
        PhoneNumber = phoneNumber;
        UserID = userID;
        Detail = detail;
        Key = key;
    }

    public ClassPost() {
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
