package com.example.jobservice.Models;

public class Posts {
    private String post_name,post_date,post_address,post_price, pid;

    public String getPid() {
        return pid;
    }

    public Posts(){

    }

    public Posts(String post_name, String post_date, String post_address, String post_price, String pid) {
        this.post_name = post_name;
        this.post_date = post_date;
        this.post_address = post_address;
        this.post_price = post_price;

        this.pid = pid;
    }

    public String getPost_name() {
        return post_name;
    }

    public void setPost_name(String post_name) {
        this.post_name = post_name;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getPost_address() {
        return post_address;
    }

    public void setPost_address(String post_address) {
        this.post_address = post_address;
    }

    public String getPost_price() {
        return post_price;
    }

    public void setPost_price(String post_price) {
        this.post_price = post_price;
    }
}
