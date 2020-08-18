package com.nechaev.loftmoney.data;

import com.nechaev.loftmoney.R;

public class Item {
    private Integer id;
    private String name;
    private Integer price;
    private Integer color;

    public Item(Integer id, String name, Integer price, Integer color) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.color = color;
    }

    public static Item getInstance(MoneyItem moneyItem){
        return new Item(moneyItem.getItemId(), moneyItem.getName(),
                moneyItem.getPrice(),
                moneyItem.getType().equals("expense")? R.color.expenseColor: R.color.incomeColor);
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getColor() { return color; }

    public Integer getId() {
        return id;
    }
}
