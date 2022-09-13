package com.example.arslicious;

import java.io.Serializable;

public class CartModelClass implements Serializable {
    String cartImage, cartName, cartQuantity;
    int cartPrice;
    String documentId;

    public CartModelClass() {
    }

    public CartModelClass(String cartImage, String cartName, String cartQuantity, int cartPrice) {
        this.cartImage = cartImage;
        this.cartName = cartName;
        this.cartQuantity = cartQuantity;
        this.cartPrice = cartPrice;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getCartImage() {
        return cartImage;
    }

    public void setCartImage(String cartImage) {
        this.cartImage = cartImage;
    }

    public String getCartName() {
        return cartName;
    }

    public void setCartName(String cartName) {
        this.cartName = cartName;
    }

    public String getCartQuantity() {
        return cartQuantity;
    }

    public void setCartQuantity(String cartQuantity) {
        this.cartQuantity = cartQuantity;
    }

    public int getCartPrice() {
        return cartPrice;
    }

    public void setCartPrice(int cartPrice) {
        this.cartPrice = cartPrice;
    }

    @Override
    public String toString() {
        return "CartModelClass{" +
                "cartImage='" + cartImage + '\'' +
                ", cartName='" + cartName + '\'' +
                ", cartQuantity='" + cartQuantity + '\'' +
                ", cartPrice=" + cartPrice +
                '}';
    }
}
