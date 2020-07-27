package com.nechaev.loftmoney;

public class Item {
    private String name;
    private Integer price;
    private Integer color;

    public Item(String name, Integer price, Integer color) {
        this.name = name;
        this.price = price;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getColor() { return color; }
}
