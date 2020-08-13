package com.nechaev.loftmoney;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BudgetFragment extends Fragment {
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public BudgetFragment(Boolean isExpense) {
        this.isExpense = isExpense;
        if (isExpense){
            this.color = R.color.expenseColor;
        } else {
            this.color = R.color.incomeColor;
        }
    }

    public static final int ADD_ITEM_ACTIVITY_REQUEST_CODE = 100;
    private ItemsAdapter mAdapter;
    private Integer color = R.color.expenseColor;
    private Boolean isExpense = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, null);
        RecyclerView recyclerView = view.findViewById(R.id.budget_item_list);

        mAdapter = new ItemsAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));

        loadItems();
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
            loadItems();
        }
    }

    private void loadItems(){
        final List<Item> items = new ArrayList<>();
        Single<MoneyResponse> singleItems;
        if (this.isExpense) {
            singleItems  =((LoftApp) getActivity().getApplication()).getApi().getItems("expense");
        } else {
            singleItems  =((LoftApp) getActivity().getApplication()).getApi().getItems("income");
        }

        mAdapter.clear();

        Disposable disposable = singleItems
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MoneyResponse>() {
                    @Override
                    public void accept(MoneyResponse moneyResponse) throws Exception {
                        for (MoneyItem moneyItem: moneyResponse.getMoneyItems()){
                            items.add(Item.getInstance(moneyItem));
                        }
                        mAdapter.addItems(items);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("TAG", "Error " + throwable);
                    }
                });

        compositeDisposable.add(disposable);
    }
}
