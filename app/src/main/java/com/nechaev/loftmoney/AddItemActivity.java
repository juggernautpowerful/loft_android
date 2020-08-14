package com.nechaev.loftmoney;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AddItemActivity extends AppCompatActivity {

    private EditText mNameEditText;
    private EditText mPriceEditText;
    private Button addButton;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    String name;
    String price;
    String type;

    public void setType(String type){
        this.type = type;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mNameEditText = findViewById(R.id.name);
        mPriceEditText = findViewById(R.id.price);

        addButton = findViewById(R.id.add_btn);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            type = extras.getString("type");
        }else{
            type = "income";
        }

        configureInputViews();
        configureItemAdding();
    }

    private void configureItemAdding(){
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                final String name = mNameEditText.getText().toString();
                String price = mPriceEditText.getText().toString();
                String token =  ((LoftApp) getApplication()).getSharedPreferences(getString(R.string.app_name), 0).getString(LoftApp.TOKEN_KEY, "");
                ((LoftApp)getApplication()).getApi().addMoney(token, price, name, type)
                        .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action() {
                            @Override
                            public void run() throws Exception {
                                Log.e("TAG", "Added" + name);
                                setResult(RESULT_OK, new Intent());
                                finish();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("TAG", "Error " + throwable);
                            }
                        });
            }
        });
    }

    private void configureInputViews(){
        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                name = charSequence.toString();
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPriceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                price = charSequence.toString();
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private  void checkInputs(){
      boolean isEnabled = !TextUtils.isEmpty(name)  && name != null && !TextUtils.isEmpty(price) && price != null;
        addButton.setEnabled(isEnabled);
        addButton.setTextColor(ContextCompat.getColor(getApplicationContext(), isEnabled? R.color.incomeColor : R.color.inactiveColor));
    }
}
