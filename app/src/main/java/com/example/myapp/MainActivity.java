package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Fragment homeFragment, recordFragment,historyFragment, exploreFragment, communityFragment, accountFragment;
    private Fragment activeFragment;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_MyApp);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();
        // 初始化 Fragment（只加載一次）
        homeFragment = new HomeFragment();
        recordFragment = new RecordFragment();
        historyFragment = new HistoryFragment();
        exploreFragment = new ExploreFragment();
        communityFragment = new CommunityFragment();
        accountFragment = new AccountFragment();
        activeFragment = homeFragment;

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frame_container, homeFragment, "home");
        transaction.add(R.id.frame_container, recordFragment, "record").hide(recordFragment);
        transaction.add(R.id.frame_container, historyFragment, "history").hide(historyFragment);
        transaction.add(R.id.frame_container, exploreFragment, "explore").hide(exploreFragment);
        transaction.add(R.id.frame_container, communityFragment, "community").hide(communityFragment);
        transaction.add(R.id.frame_container, accountFragment, "account").hide(accountFragment);
        transaction.commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_record) selectedFragment = recordFragment;
            else if (id == R.id.nav_explore) selectedFragment = exploreFragment;
            else if (id == R.id.nav_home) selectedFragment = homeFragment;
            else if (id == R.id.nav_community) selectedFragment = communityFragment;
            else if (id == R.id.nav_account) selectedFragment = accountFragment;
            else if (id == R.id.nav_history) selectedFragment = historyFragment;

            if (selectedFragment != null && selectedFragment != activeFragment) {
                fragmentManager.beginTransaction()
                        .hide(activeFragment)
                        .show(selectedFragment)
                        .commit();
                activeFragment = selectedFragment;
            }
            return true;
        });
            bottomNavigationView.setSelectedItemId(R.id.nav_home);  // 預設選中 Home
    }
}
