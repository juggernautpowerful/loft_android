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

    public static Item getInstance(MoneyItem moneyItem){
        return new Item(moneyItem.getName(),
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
}
