package com.nechaev.loftmoney;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ItemsAdapter itemsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.costsRecyclerView);
        itemsAdapter = new ItemsAdapter();
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        itemsAdapter.setItems(generateExpenses());
        itemsAdapter.addItems(generateIncomes());
    }

    private List<Item> generateExpenses(){
        List<Item> items = new ArrayList<>();
        items.add(new Item("Молоко", 70, R.color.expenseColor));
        items.add(new Item("Зубная щетка", 70, R.color.expenseColor));
        items.add(new Item("Сковородка с антипригарным покрытием", 1670, R.color.expenseColor));
        return items;
    }

    private List<Item> generateIncomes(){
        List<Item> items = new ArrayList<>();
        items.add(new Item("Зарплата. Июнь", 70000, R.color.incomeColor));
        items.add(new Item("Премия", 7000, R.color.incomeColor));
        items.add(new Item("Олег наконец-то вернул долг", 300000, R.color.incomeColor));
        return items;
    }
}