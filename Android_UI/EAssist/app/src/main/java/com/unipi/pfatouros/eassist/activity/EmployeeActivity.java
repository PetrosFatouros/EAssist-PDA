package com.unipi.pfatouros.eassist.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.unipi.pfatouros.eassist.R;
import com.unipi.pfatouros.eassist.adapter.InventoryAdapter;
import com.unipi.pfatouros.eassist.adapter.ItemAdapter;
import com.unipi.pfatouros.eassist.dialog.CustomBottomSheet;
import com.unipi.pfatouros.eassist.model.Item;
import com.unipi.pfatouros.eassist.utility.Constants;
import com.unipi.pfatouros.eassist.utility.NavigationUtil;

import java.util.Objects;

public class EmployeeActivity extends AppCompatActivity
        implements CustomBottomSheet.BottomSheetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        // Setup action bar with navigation component
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder(R.id.viewOrdersFragment,
                R.id.selectTableFragment,
                R.id.viewTablesFragment).build();
        NavigationUI.setupActionBarWithNavController(this,
                NavigationUtil.getNavController(R.id.employeeNavHostFragment, this),
                appBarConfiguration);

        // Setup the NavController with the BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.employeeBottomNavigationView);
        NavigationUI.setupWithNavController(bottomNavigationView,
                NavigationUtil.getNavController(R.id.employeeNavHostFragment, this));
    }

    @Override
    public boolean onSupportNavigateUp() {

        // Setup navigate up with navigation component
        return NavigationUtil
                .getNavController(R.id.employeeNavHostFragment, this).navigateUp()
                || super.onSupportNavigateUp();
    }

    @Override
    public void onButtonClick(InventoryAdapter adapter, int position, Float price, Integer quantity) {

        // Edit inventory (not implemented in this activity)
    }

    @Override
    public void onButtonClick(ItemAdapter adapter, int position, Integer selectedQuantity) {

        // Get selected item
        Item item = adapter.getItems().get(position);

        // Get item's quantity
        Integer currentQuantity = item.getInventory_item().getQuantity();

        // Get item's selected quantity
        Integer currentSelectedQuantity = item.getSelected_quantity();

        // Calculate new quantity and selected quantity
        int newQuantity = currentQuantity
                - Objects.requireNonNull(selectedQuantity);
        int newSelectedQuantity = currentSelectedQuantity
                + Objects.requireNonNull(selectedQuantity);

        // Validate new quantity
        if (newQuantity < 0 || newQuantity > Constants.MAX_QUANTITY) {

            // Show toast with corresponding message
            Toast.makeText(this,
                    getResources().getString(R.string.item_not_available_in_that_quantity),
                    Toast.LENGTH_SHORT).show();
        }
        // Validate new selected quantity
        else if (newSelectedQuantity < 0 || newSelectedQuantity > Constants.MAX_SELECTED_QUANTITY) {

            // Show toast with corresponding message
            Toast.makeText(this,
                    getResources().getString(R.string.item_not_available_in_that_quantity),
                    Toast.LENGTH_SHORT).show();
        }
        else {

            // Update item
            item.getInventory_item().setQuantity(newQuantity);
            item.setSelected_quantity(newSelectedQuantity);

            // Update adapter
            adapter.notifyItemChanged(position);
        }
    }
}