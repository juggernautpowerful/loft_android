package com.nechaev.loftmoney;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BudgetFragment extends Fragment {

    public BudgetFragment(Boolean isExpense) {
        this.isExpense = isExpense;
        if (isExpense){
            this.color = R.color.expenseColor;
        } else {
            this.color = R.color.incomeColor;
        }
    }

    private static final int ADD_ITEM_ACTIVITY_REQUEST_CODE = 100;
    private ItemsAdapter mAdapter;
    private Integer color = R.color.expenseColor;
    private Boolean isExpense = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, null);
        Button callAddButton = view.findViewById(R.id.call_add_item_activity);
        callAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startActivityForResult(new Intent(getActivity(), AddItemActivity.class),
                        ADD_ITEM_ACTIVITY_REQUEST_CODE);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.budget_item_list);

        mAdapter = new ItemsAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        if (this.isExpense ){
            mAdapter.addItems(generateExpenses());
        } else {
            mAdapter.addItems(generateIncomes());
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int price;
        try {
            price = Integer.parseInt(data.getStringExtra("price"));
        } catch (NumberFormatException e) {
            price = 0;
        }
        if (requestCode == ADD_ITEM_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mAdapter.addItem(new Item(data.getStringExtra("name"), price, color));
        }
    }

    private List<Item> generateExpenses(){
        List<Item> items = new ArrayList<>();
        items.add(new Item("Молоко", 70, this.color));
        items.add(new Item("Зубная щетка", 70, this.color));
        items.add(new Item("Сковородка с антипригарным покрытием", 1670, this.color));
        return items;
    }

    private List<Item> generateIncomes(){
        List<Item> items = new ArrayList<>();
        items.add(new Item("Зарплата. Июнь", 70000, this.color));
        items.add(new Item("Премия", 7000, this.color));
        items.add(new Item("Олег наконец-то вернул долг", 300000, this.color));
        return items;
    }
}
