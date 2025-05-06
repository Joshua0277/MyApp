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
    private Fragment homeFragment, recordFragment, exploreFragment, communityFragment, accountFragment;
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
        exploreFragment = new ExploreFragment();
        communityFragment = new CommunityFragment();
        accountFragment = new AccountFragment();
        activeFragment = homeFragment;

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frame_container, homeFragment, "home").commit();
        transaction.add(R.id.frame_container, recordFragment, "record").hide(recordFragment).commit();
        transaction.add(R.id.frame_container, exploreFragment, "explore").hide(exploreFragment).commit();
        transaction.add(R.id.frame_container, communityFragment, "community").hide(communityFragment).commit();
        transaction.add(R.id.frame_container, accountFragment, "account").hide(accountFragment).commit();


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int id = item.getItemId();  //  確保 `getItemId()` 結果是整數常數

            if (id == R.id.nav_record) {
                selectedFragment = new RecordFragment();
            } else if (id == R.id.nav_explore) {
                selectedFragment = new ExploreFragment();
            } else if (id == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (id == R.id.nav_community) {
                selectedFragment = new CommunityFragment();
            } else if (id == R.id.nav_account) {
                selectedFragment = new AccountFragment();
            } else if (id == R.id.nav_history){
                    selectedFragment = new HistoryFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, selectedFragment)
                        .commit();
            }
            return true;
        });
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, new HomeFragment())
                    .commit();
            bottomNavigationView.setSelectedItemId(R.id.nav_home);  // 預設選中 Home
        }

    }
}
