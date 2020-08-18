package com.nechaev.loftmoney.activities.helpers;

import com.nechaev.loftmoney.data.Item;

public interface ItemsSelectionListener {
    void onItemClicked(Item selectedItem, int position);
    void onItemLongClicked(Item selectedItem, int position);
}
