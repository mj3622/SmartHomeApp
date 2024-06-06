package com.minjer.smarthome;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.minjer.smarthome.http.DataClient;
import com.minjer.smarthome.utils.ParamUtil;


public class MainActivity extends AppCompatActivity {
    private static BottomNavigationView bottomNavigationView;
    private Fragment currentFragment;
    private final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 开启网络请求的线程策略
        StrictMode.ThreadPolicy.Builder builder = new StrictMode.ThreadPolicy.Builder()
                .permitAll();
        StrictMode.setThreadPolicy(builder.build());

        // 设置顶部状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 设置状态栏的文本和图标为深色
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        // 设置底部导航栏
        bottomNavigationView = findViewById(R.id.nav_view);

        currentFragment = new DeviceFragment(); // 假设HomeFragment是默认显示的Fragment
        displayFragment(currentFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.action_about) {
                fragment = new AboutFragment();
            } else if (itemId == R.id.action_device) {
                fragment = new DeviceFragment();
            } else if (itemId == R.id.action_user) {
                fragment = new UserFragment();
            }

            if (fragment != null) {
                displayFragment(fragment);
                currentFragment = fragment;
            }

            return true;
        });
    }

    private void displayFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

}