package com.unipi.pfatouros.eassist.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.unipi.pfatouros.eassist.R;
import com.unipi.pfatouros.eassist.adapter.InventoryAdapter;
import com.unipi.pfatouros.eassist.adapter.ItemAdapter;
import com.unipi.pfatouros.eassist.dialog.CustomBottomSheet;
import com.unipi.pfatouros.eassist.model.Inventory;
import com.unipi.pfatouros.eassist.utility.ApiUtil;
import com.unipi.pfatouros.eassist.utility.Constants;
import com.unipi.pfatouros.eassist.utility.NavigationUtil;
import com.unipi.pfatouros.eassist.utility.PreferenceUtil;
import com.unipi.pfatouros.eassist.utility.Retrofit;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerActivity extends AppCompatActivity
        implements CustomBottomSheet.BottomSheetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        // Setup action bar with navigation component
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder(R.id.viewInventoryFragment, R.id.viewStatisticsFragment).build();
        NavigationUI.setupActionBarWithNavController(this,
                NavigationUtil.getNavController(R.id.managerNavHostFragment, this),
                appBarConfiguration);

        // Setup the NavController with the BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.managerBottomNavigationView);
        NavigationUI.setupWithNavController(bottomNavigationView,
                NavigationUtil.getNavController(R.id.managerNavHostFragment, this));
    }

    @Override
    public boolean onSupportNavigateUp() {

        // Setup navigate up with navigation component
        return NavigationUtil
                .getNavController(R.id.managerNavHostFragment, this).navigateUp()
                || super.onSupportNavigateUp();
    }

    @Override
    public void onButtonClick(InventoryAdapter adapter, int position, Float price, Integer quantity) {

        // Update inventory

        // Get inventory item
        Inventory inventoryItem = adapter.getInventory().get(position);

        // Get token
        String token = PreferenceUtil.getToken(this);

        // Create retrofit object
        Retrofit api = ApiUtil.getRetrofit(token).create(Retrofit.class);

        // Create call object
        Call<Void> call = api.updateInventory(inventoryItem.getId(), price, quantity);

        // Store current context in a variable in order to show toast
        final Context context = this;

        // Execute call
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,
                                   @NonNull Response<Void> response) {

                String text;
                if (response.isSuccessful()) {

                    // Update adapter
                    boolean updated = false;
                    if (Objects.nonNull(price)) {
                        if (price >= 0 && price <= Constants.MAX_PRICE) {
                            inventoryItem.setPrice(price);
                            updated = true;
                        }
                    }
                    if (Objects.nonNull(quantity)) {
                        if (quantity >= 0 && quantity <= Constants.MAX_QUANTITY) {
                            inventoryItem.setQuantity(quantity);
                            updated = true;
                        }
                    }
                    if (updated) {
                        adapter.notifyItemChanged(position);

                        // Inventory updated
                        text = getResources().getString(R.string.inventory_updated);
                    }
                    else {
                        // Unsuccessful response
                        text = getResources()
                                .getString(R.string.inventory_could_not_be_updated);
                    }
                } else {
                    // Unsuccessful response
                    text = getResources()
                            .getString(R.string.inventory_could_not_be_updated);
                }

                // Show toast with corresponding message
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call,
                                  @NonNull Throwable t) {

                // Show toast with error message
                String text = t.getLocalizedMessage();
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onButtonClick(ItemAdapter adapter, int position, Integer quantity) {

        // Add item to order (not implemented in this activity)
    }
}