package com.nechaev.loftmoney;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BalanceFragment extends Fragment {
    private TextView myExpences;
    private TextView myIncome;
    private TextView totalFinances;
    private BalancePie balancePie;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public BalanceFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BalanceFragment newInstance(String param1, String param2) {
        BalanceFragment fragment = new BalanceFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_balance, null);
        myExpences = view.findViewById(R.id.my_expences);
        myIncome = view.findViewById(R.id.my_income);
        totalFinances = view.findViewById(R.id.total_finances);
        balancePie = view.findViewById(R.id.diagram_view);
        mSwipeRefreshLayout = view.findViewById(R.id.balance_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadBalance();
            }
        });
        loadBalance();
        return view;
    }

    private void loadBalance(){
        final List<Item> items = new ArrayList<>();
        Single<List<MoneyItem>> singleIncome;
        Single<List<MoneyItem>> singleExpense;

        final int[] totalIncome = {0};
        final int[] totalExpence = {0};

        String token =  ((LoftApp) getActivity().getApplication()).getSharedPreferences(getString(R.string.app_name), 0).getString(LoftApp.TOKEN_KEY, "");

        singleIncome  =((LoftApp) getActivity().getApplication()).getApi().getItems(token,"income");
        singleExpense  =((LoftApp) getActivity().getApplication()).getApi().getItems(token,"expense");

        Disposable disposable = singleIncome
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MoneyItem>>() {
                    @Override
                    public void accept(List<MoneyItem> moneyResponse) throws Exception {

                        Log.e("TAG", "Refreshing ");
                        for (MoneyItem moneyItem: moneyResponse){
                           totalIncome[0] += moneyItem.getPrice();
                        }
                        resetData(totalIncome[0], totalExpence[0]);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        Log.e("TAG", "Error " + throwable);
                    }
                });

        Disposable disposable1 = singleExpense
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MoneyItem>>() {
                    @Override
                    public void accept(List<MoneyItem> moneyResponse) throws Exception {

                        Log.e("TAG", "Refreshing ");
                        for (MoneyItem moneyItem: moneyResponse){
                            totalExpence[0] += moneyItem.getPrice();
                        }
                        resetData(totalIncome[0], totalExpence[0]);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        Log.e("TAG", "Error " + throwable);
                    }
                });

        compositeDisposable.add(disposable);
        compositeDisposable.add(disposable1);
    }

    private void resetData(Integer income, Integer expense){
        myExpences.setText(String.valueOf(expense));
        myIncome.setText(String.valueOf(income));
        totalFinances.setText(String.valueOf(income - expense));
        balancePie.update(expense, income);
        mSwipeRefreshLayout.setRefreshing(false);
    }
}