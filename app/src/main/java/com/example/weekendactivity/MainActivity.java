package com.example.weekendactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.weekendactivity.fragments.ActivityFragment;
import com.example.weekendactivity.fragments.GroupFragment;
import com.example.weekendactivity.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity
{
    public static final String TAG = "MainActivity";

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        //bottomNavigationView.setItemIconTintList(null);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                Fragment fragment = null;
                switch (menuItem.getItemId())
                {
                    case R.id.action_activity:
                         fragment = new ActivityFragment();
                        break;
                    case R.id.action_group:
                         fragment = new GroupFragment();
                        break;
                    case R.id.action_create:
                         fragment = new ActivityFragment();
                        break;
                    case R.id.action_schedule:
                         fragment = new ActivityFragment();
                        break;
                    case R.id.action_profile:
                         fragment = new ProfileFragment();
                        break;
                    default:
                        break;
                }

                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_activity);
    }
}