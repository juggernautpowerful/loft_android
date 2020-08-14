package com.nechaev.loftmoney;

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

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabs);
        toolbar = findViewById(R.id.toolbar);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.expences));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.income));
        final ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new BudgetPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));

        FloatingActionButton addItemBtn = findViewById(R.id.call_add_item_activity);
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
            }
            return new BudgetFragment(false);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}