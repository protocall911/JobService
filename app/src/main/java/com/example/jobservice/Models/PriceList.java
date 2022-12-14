package com.example.jobservice.Models;

public class PriceList {
    String price;

    public PriceList(String price){
        this.price = price;
    }
    public String getPrice(){
        return  price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
}
