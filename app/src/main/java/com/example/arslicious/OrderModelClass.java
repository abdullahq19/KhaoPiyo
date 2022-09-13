package com.example.arslicious;

public class OrderModelClass {
    String orderName, orderQuantity, orderPhoneNo, orderAddons, orderAddress, orderImage;
    int orderPrice;

    public OrderModelClass() {
    }

    public OrderModelClass(String orderName, String orderQuantity, String orderPhoneNo, String orderAddons, String orderAddress, String orderImage, int orderPrice) {
        this.orderName = orderName;
        this.orderQuantity = orderQuantity;
        this.orderPhoneNo = orderPhoneNo;
        this.orderAddons = orderAddons;
        this.orderAddress = orderAddress;
        this.orderImage = orderImage;
        this.orderPrice = orderPrice;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getOrderPhoneNo() {
        return orderPhoneNo;
    }

    public void setOrderPhoneNo(String orderPhoneNo) {
        this.orderPhoneNo = orderPhoneNo;
    }

    public String getOrderAddons() {
        return orderAddons;
    }

    public void setOrderAddons(String orderAddons) {
        this.orderAddons = orderAddons;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getOrderImage() {
        return orderImage;
    }

    public void setOrderImage(String orderImage) {
        this.orderImage = orderImage;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    @Override
    public String toString() {
        return "OrderModelClass{" +
                "orderName='" + orderName + '\'' +
                ", orderQuantity='" + orderQuantity + '\'' +
                ", orderPhoneNo='" + orderPhoneNo + '\'' +
                ", orderAddons='" + orderAddons + '\'' +
                ", orderAddress='" + orderAddress + '\'' +
                ", orderImage='" + orderImage + '\'' +
                ", orderPrice=" + orderPrice +
                '}';
    }
}