package com.zex.web;

/**
 * Created by caspar on 16-5-20.
 */
public class GoodsModel {
    private int id;
    private String name;
    private int stock;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setId(int id) {

        this.id = id;
    }

}
