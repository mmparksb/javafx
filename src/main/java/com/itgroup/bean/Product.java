package com.itgroup.bean;

import com.itgroup.utility.Utility;
import javafx.beans.property.SimpleIntegerProperty;
import oracle.security.crypto.asn1.ASN1Date;

import javax.print.attribute.HashDocAttributeSet;

public class Product {
    private int barcode;
    private String category;
    private String name;
    private int price;
    private int stock;
    private String manufactureDate;
    private String expirationDate;
    private String best;
    private String event;
    private String image;
    private SimpleIntegerProperty count;// = new SimpleIntegerProperty(1);
    private SimpleIntegerProperty shoppingCartPrice;// = new SimpleIntegerProperty(price*count.get());


    public String getBest() {
        return best;
    }

    public int getCount() {
        return count.get();
    }

    public SimpleIntegerProperty countProperty() {
        return count;
    }

    public void setCount(int count) {
        this.count.set(count);
    }

    public int getShoppingCartPrice() {
        return shoppingCartPrice.get();
    }

    public SimpleIntegerProperty shoppingCartPriceProperty() {
        return shoppingCartPrice;
    }

    public void setShoppingCartPrice(int shoppingCartPrice) {
        this.shoppingCartPrice.set(shoppingCartPrice);
    }

    public void setBest(String best) {
        this.best = best;
    }

    public Product() {
        count = new SimpleIntegerProperty(0);
        shoppingCartPrice = new SimpleIntegerProperty(price * count.get());
    }

    public String getImage() {
        return image;
    }


    public void setImage(String image) {
        this.image = image;
    }

    public int getBarcode() {
        return barcode;
    }

    public void setBarcode(int barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "Product{" +
                "barcode=" + barcode +
                ", category=" + category +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", manufactureDate='" + manufactureDate + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", best=" + best +
                ", event=" + event +
                ", image='" + image + '\'' +
                '}';
    }
}
