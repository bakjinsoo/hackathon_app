package com.example.hackathon_app_ver_1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hackathon_app_ver_1.databinding.ActivityMainBinding;
import com.example.hackathon_app_ver_1.databinding.ViewMainActivityBinding;
import com.example.hackathon_app_ver_1.ui.menuhome.HomeCoursesFragment;
import com.example.hackathon_app_ver_1.ui.menuhome.PopularCoursesAdapter;
import com.example.hackathon_app_ver_1.ui.model.CourseCard;
import com.example.hackathon_app_ver_1.ui.utils.AppLogger;
import com.example.hackathon_app_ver_1.ui.utils.helpers.BottomNavigationBehavior;
import com.example.hackathon_app_ver_1.ui.utils.helpers.DarkModePrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class ViewMainActivity extends AppCompatActivity {
    private DarkModePrefManager darkModePrefManager;
    private ViewMainActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ViewMainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setAppTheme();
        setSupportActionBar(binding.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            // 초기 프래그먼트 설정
            Fragment homeCoursesFragment = new HomeCoursesFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, homeCoursesFragment); // 여기에서 fragment_container ID를 사용
            fragmentTransaction.commit();
        }

        setupNavigation();
    }

    private void setAppTheme() {
        darkModePrefManager = new DarkModePrefManager(this);
        boolean isDarkModeEnabled = darkModePrefManager.isNightMode();
        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void setupNavigation() {
        // 필요한 경우 네비게이션 관련 설정 추가
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void toggleDarkMode() {
        AppLogger.d("toggleDarkMode init");
        boolean isDarkModeEnabled = darkModePrefManager.isNightMode();
        darkModePrefManager.setDarkMode(!isDarkModeEnabled);

        startActivity(new Intent(ViewMainActivity.this, ViewMainActivity.class));
        finish();
        overridePendingTransition(0, 0);
    }
}
