package com.unipi.pfatouros.eassist.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.unipi.pfatouros.eassist.R;
import com.unipi.pfatouros.eassist.utility.NavigationUtil;

public class AuthActivity extends AppCompatActivity {

    // Launching activity
    // Responsible for displaying 2 fragments (SignIn and SelectRole)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Setup action bar with navigation component
        NavigationUI.setupActionBarWithNavController(this,
                NavigationUtil.getNavController(R.id.authNavHostFragment, this));
    }

    @Override
    public boolean onSupportNavigateUp() {

        // Setup navigate up with navigation component
        return NavigationUtil.getNavController(R.id.authNavHostFragment, this)
                .navigateUp() || super.onSupportNavigateUp();
    }
}