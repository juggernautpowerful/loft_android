package com.nechaev.loftmoney;

public interface ItemsSelectionListener {
    void onItemClicked(Item selectedItem, int position);
    void onItemLongClicked(Item selectedItem, int position);
}
