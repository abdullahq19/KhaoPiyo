package com.example.arslicious;


import java.io.Serializable;

public class ModelClass implements Serializable {
    private String imageUrl, currencyType, name;
    private int price;

    public ModelClass() {

    }

    public ModelClass(String imageUrl, String currencyType, String name, int price) {
        this.imageUrl = imageUrl;
        this.currencyType = currencyType;
        this.name = name;
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ModelClass{" +
                "imageUrl='" + imageUrl + '\'' +
                ", currencyType='" + currencyType + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}