package com.nechaev.loftmoney.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nechaev.loftmoney.data.Item;
import com.nechaev.loftmoney.LoftApp;
import com.nechaev.loftmoney.data.MoneyItem;
import com.nechaev.loftmoney.R;
import com.nechaev.loftmoney.data.Status;
import com.nechaev.loftmoney.activities.helpers.ItemsAdapter;
import com.nechaev.loftmoney.activities.helpers.ItemsSelectionListener;
import com.nechaev.loftmoney.activities.helpers.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetFragment extends Fragment implements ItemsSelectionListener, ActionMode.Callback{
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
    private ActionMode mActionMode;

    private SwipeRefreshLayout refresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, null);
        RecyclerView recyclerView = view.findViewById(R.id.budget_item_list);
        refresh = view.findViewById(R.id.swipe_refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadItems();
            }
        });
        mAdapter = new ItemsAdapter();
        mAdapter.setListener(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));

        //loadItems();
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
            //loadItems();
        }
    }

    @Override
    public void onItemClicked(Item selectedItem, int position) {
        mAdapter.clearItem(position);
        if (mActionMode != null) {
            mActionMode.setTitle(getString(R.string.selected, String.valueOf(mAdapter.getSelectedSize())));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadItems();
    }

    @Override
    public void onItemLongClicked(Item selectedItem, int position) {
        if (mActionMode == null) {
            getActivity().startActionMode(this);
        }
        mAdapter.toggleItem(position);
        if (mActionMode != null) {
            mActionMode.setTitle(getString(R.string.selected, String.valueOf(mAdapter.getSelectedSize())));
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        mActionMode = actionMode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater menuInf = new MenuInflater(getActivity());
        menuInf.inflate(R.menu.delete_menu, menu);
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.deleteItems) {
            new AlertDialog.Builder(getContext())
                    .setMessage(R.string.confirmation)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(final DialogInterface dialogInterface, final int i) {
                            removeItems();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(final DialogInterface dialogInterface, final int i) {

                        }
                    }).show();
        }
        return true;
    }

    private void removeItems() {
        String token = ((LoftApp) getActivity().getApplication()).getSharedPreferences(getString(R.string.app_name), 0).getString(LoftApp.TOKEN_KEY, "");
        List<Integer> selectedItems = mAdapter.getSelectedItemIds();
        for (Integer itemId : selectedItems) {
            Call<Status> call = ((LoftApp) getActivity().getApplication()).getApi().removeItem(String.valueOf(itemId.intValue()), token);
            call.enqueue(new Callback<Status>() {

                @Override
                public void onResponse(
                        final Call<Status> call, final Response<Status> response
                ) {
                    loadItems();
                    mAdapter.clearSelections();
                    mActionMode.setTitle(getString(R.string.selected, String.valueOf(mAdapter.getSelectedSize())));
                }

                @Override
                public void onFailure(final Call<Status> call, final Throwable t) {

                }
            });
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        mActionMode = null;
        mAdapter.clearSelections();
    }

    private void loadItems(){
        final List<Item> items = new ArrayList<>();
        Single<List<MoneyItem>> singleItems;

        String token =  ((LoftApp) getActivity().getApplication()).getSharedPreferences(getString(R.string.app_name), 0).getString(LoftApp.TOKEN_KEY, "");

        if (this.isExpense) {
            singleItems  =((LoftApp) getActivity().getApplication()).getApi().getItems(token,"expense");
        } else {
            singleItems  =((LoftApp) getActivity().getApplication()).getApi().getItems(token,"income");
        }

        mAdapter.clear();

        Disposable disposable = singleItems
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MoneyItem>>() {
                    @Override
                    public void accept(List<MoneyItem> moneyResponse) throws Exception {
                        refresh.setRefreshing(false);
                        Log.e("TAG", "Refreshing ");
                        for (MoneyItem moneyItem: moneyResponse){
                            items.add(Item.getInstance(moneyItem));
                        }
                        mAdapter.addItems(items);
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        refresh.setRefreshing(false);
                        Log.e("TAG", "Error " + throwable);
                    }
                });

        compositeDisposable.add(disposable);
    }
}
