package com.example.real_estate_market.Class.Class;

public class DataPost {
    String nameProject,PNumberItem,priceItem,idPost, Address;
    String ImageMain;

    public DataPost(String nameProject, String PNumberItem, String priceItem, String idPost, String imageMain, String address) {
        this.nameProject = nameProject;
        this.PNumberItem = PNumberItem;
        this.priceItem = priceItem;
        this.idPost = idPost;
        this.ImageMain = imageMain;
        Address = address;
    }

    public DataPost() {
    }

    public String getNameProject() {
        return nameProject;
    }

    public void setNameProject(String nameProject) {
        this.nameProject = nameProject;
    }

    public String getPNumberItem() {
        return PNumberItem;
    }

    public void setPNumberItem(String PNumberItem) {
        this.PNumberItem = PNumberItem;
    }

    public String getPriceItem() {
        return priceItem;
    }

    public void setPriceItem(String priceItem) {
        this.priceItem = priceItem;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public String getImageMain() {
        return ImageMain;
    }

    public void setImageMain(String imageMain) {
        this.ImageMain = imageMain;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
