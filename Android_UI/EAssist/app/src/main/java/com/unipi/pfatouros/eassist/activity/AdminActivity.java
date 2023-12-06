package com.unipi.pfatouros.eassist.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.unipi.pfatouros.eassist.R;
import com.unipi.pfatouros.eassist.utility.NavigationUtil;

public class AdminActivity extends AppCompatActivity {

    // Admin activity (used by users who have the admin role)
    // Responsible for displaying 2 fragments (ViewUsers and AddUser)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Setup action bar with navigation component
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder(R.id.viewUsersFragment, R.id.addUserFragment).build();
        NavigationUI.setupActionBarWithNavController(this,
                NavigationUtil.getNavController(R.id.adminNavHostFragment, this),
                appBarConfiguration);

        // Setup the NavController with the BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.adminBottomNavigationView);
        NavigationUI.setupWithNavController(bottomNavigationView,
                NavigationUtil.getNavController(R.id.adminNavHostFragment, this));
    }

    @Override
    public boolean onSupportNavigateUp() {

        // Setup navigate up with navigation component
        return NavigationUtil.getNavController(R.id.adminNavHostFragment, this)
                .navigateUp() || super.onSupportNavigateUp();
    }
}