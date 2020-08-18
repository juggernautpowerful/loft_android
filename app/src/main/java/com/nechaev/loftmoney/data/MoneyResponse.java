package com.nechaev.loftmoney.data;

import com.google.gson.annotations.SerializedName;
import com.nechaev.loftmoney.data.MoneyItem;

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
