package com.unipi.pfatouros.eassist.utility;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.unipi.pfatouros.eassist.R;

public class NavigationUtil {

    public static NavController getNavController(int id, Context context) {

        // Return the navigation controller
        // from the navigation host fragment corresponding to the given id

        // Get fragment manager from context
        FragmentManager fragmentManager =
                ((FragmentActivity) context).getSupportFragmentManager();

        // Get navigation host fragment from fragment manager
        NavHostFragment navHostFragment =
                (NavHostFragment) fragmentManager.findFragmentById(id);

        // Check if navigation host fragment is null
        if (navHostFragment != null) {

            // Get navigation controller from navigation host fragment and return it
            return navHostFragment.getNavController();
        } else { // Otherwise throw exception
            throw new RuntimeException(
                    context.getResources().getString(R.string.nav_host_fragment_exception_message));
        }
    }
}
