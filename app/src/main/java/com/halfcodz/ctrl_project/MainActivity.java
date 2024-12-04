package com.halfcodz.ctrl_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.inhatc.real_project.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigation = findViewById(R.id.navigationView);
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.page_category) {
                selectedFragment = new FragmentCategory();
            } else if (itemId == R.id.page_todolist) {
                selectedFragment = new FragmentTodolist();
            } else if (itemId == R.id.page_complete) {
                selectedFragment = new FragmentComplete();
            } else if (itemId == R.id.page_pet) {
                selectedFragment = new FragmentPet();
            } else if (itemId == R.id.page_stopwatch) {
                selectedFragment = new FragmentStopwatch();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });
    }
    private void loadFragment(Fragment fragment) { // 프래그먼트 로드 메서드
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }
}