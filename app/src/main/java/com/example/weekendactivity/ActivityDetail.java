package com.example.weekendactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.weekendactivity.fragments.ActivityCommentsFragment;
import com.example.weekendactivity.fragments.ActivityDetailFragment;
import com.example.weekendactivity.fragments.ActivityFragment;
import com.example.weekendactivity.fragments.ActivityPollFragment;
import com.example.weekendactivity.fragments.ActivityRegisteredFragment;
import com.example.weekendactivity.fragments.GroupFragment;
import com.example.weekendactivity.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ActivityDetail extends AppCompatActivity
{
    public static final String TAG = "ActivityDetail";

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitydetail);

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
                        fragment = new ActivityDetailFragment();
                        break;
                    case R.id.action_registeredMembers:
                        fragment = new ActivityRegisteredFragment();
                        break;
                    case R.id.action_comment:
                        fragment = new ActivityCommentsFragment();
                        break;
                    case R.id.action_poll:
                        fragment = new ActivityPollFragment();
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
    public void setActionBarTitle(String title)
    {
        getSupportActionBar().setTitle(title);
    }
}