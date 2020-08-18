package com.nechaev.loftmoney.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;
import androidx.appcompat.widget.Toolbar;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.nechaev.loftmoney.R;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton addItemBtn = findViewById(R.id.call_add_item_activity);

        tabLayout = findViewById(R.id.tabs);
        toolbar = findViewById(R.id.toolbar);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.expences));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.income));
        final ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new BudgetPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    addItemBtn.hide();
                } else {
                    addItemBtn.show();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final int activeFragmentIndex = viewPager.getCurrentItem();
                Fragment activeFragment = getSupportFragmentManager().getFragments().get(activeFragmentIndex);

                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);

                intent.putExtra("type", activeFragmentIndex == 0 ? "expense": "income");

                activeFragment.startActivityForResult(intent,
                        BudgetFragment.ADD_ITEM_ACTIVITY_REQUEST_CODE);
            }
        });

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText(R.string.expences);
        tabLayout.getTabAt(1).setText(R.string.income);
        tabLayout.getTabAt(2).setText(R.string.balance);
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        super.onActionModeStarted(mode);

        tabLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.dark_gray_blue));
        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.dark_gray_blue));
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);

        tabLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));
        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));
    }

    static class BudgetPagerAdapter extends FragmentPagerAdapter {

        public BudgetPagerAdapter(@NonNull final FragmentManager fm, final int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position == 0){
                return new BudgetFragment(true);
            }if (position == 1){
            return new BudgetFragment(false);
            }
            return new BalanceFragment();
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}