package com.nechaev.loftmoney;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoneyResponse {
    private String status;
    @SerializedName
   ("data") private List<MoneyItem> moneyItems;

    public String getStatus() {
        return status;
    }

    public List<MoneyItem> getMoneyItems() {
        return moneyItems;
    }
}
