package com.nechaev.loftmoney;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    List<Item> itemList = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_view, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bindItem(itemList.get(position));
    }

    public void setItems(List<Item> items) {
        this.itemList.clear();
        this.itemList.addAll(items);
        notifyDataSetChanged();
    }

    public void addItems(List<Item> items){
        this.itemList.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView moneyNameView;
        private TextView moneyPriceView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            moneyNameView = itemView.findViewById(R.id.nameView);
            moneyPriceView = itemView.findViewById(R.id.priceView);
        }

        public void bindItem(Item item) {
            moneyNameView.setText(item.getName());
            moneyPriceView.setText(
                    moneyPriceView.getContext().getResources().getString(R.string.price_with_currency, String.valueOf(item.getPrice()))
            );
            moneyPriceView.setTextColor(ContextCompat.getColor(moneyPriceView.getContext(), item.getColor()));
        }
    }
}
