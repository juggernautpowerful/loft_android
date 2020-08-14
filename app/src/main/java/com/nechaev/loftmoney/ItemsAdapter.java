package com.nechaev.loftmoney;

import android.content.Context;
import android.util.SparseBooleanArray;
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
    ItemsSelectionListener listener;
    SparseBooleanArray selectedItems = new SparseBooleanArray();

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public void setListener(ItemsSelectionListener listener) {
        this.listener = listener;
    }

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
        holder.bindItem(itemList.get(position), selectedItems.get(position));
        holder.setItemsSelectionListener(listener, itemList.get(position), position);
    }

    public void toggleItem(final int position) {
        selectedItems.put(position, !selectedItems.get(position));
        notifyDataSetChanged();
    }

    public void clearItem(final int position) {
        selectedItems.put(position, false);
        notifyDataSetChanged();
    }

    public void addItem(Item item) {
        this.itemList.add(item);
        notifyDataSetChanged();
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

    public void DataRefresh(){
        notifyDataSetChanged();
    }

    public int getSelectedSize() {
        int result = 0;
        for (int i = 0; i < itemList.size(); i++) {
            if (selectedItems.get(i)) {
                result++;
            }
        }
        return result;
    }

    public List<Integer> getSelectedItemIds() {
        List<Integer> result = new ArrayList<>();
        int i = 0;
        for (Item item: itemList) {
            if (selectedItems.get(i)) {
                result.add(item.getId());
            }
            i++;
        }
        return result;
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void clear() {
        itemList.clear();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameView;
        private TextView mPriceView;
        private View mItemView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemView = itemView;
            mNameView = itemView.findViewById(R.id.nameView);
            mPriceView = itemView.findViewById(R.id.priceView);
        }

        public void bindItem(Item item, final boolean isSelected) {
            mItemView.setSelected(isSelected);
            mNameView.setText(item.getName());
            mPriceView.setText(mPriceView.getContext().getResources().getString(R.string.price_with_currency, String.valueOf(item.getPrice())));
            mPriceView.setTextColor(ContextCompat.getColor(mPriceView.getContext(), item.getColor()));
        }

        public void setItemsSelectionListener(final ItemsSelectionListener listener, final Item item, final int position){
            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClicked(item, position);
                }
            });

            mItemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onItemLongClicked(item, position);
                    return false;
                }
            });
        }
    }
}
