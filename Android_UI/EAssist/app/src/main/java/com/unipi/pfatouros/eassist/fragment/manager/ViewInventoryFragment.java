package com.unipi.pfatouros.eassist.fragment.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.pfatouros.eassist.R;
import com.unipi.pfatouros.eassist.adapter.InventoryAdapter;
import com.unipi.pfatouros.eassist.dialog.CustomBottomSheet;
import com.unipi.pfatouros.eassist.model.Inventory;
import com.unipi.pfatouros.eassist.utility.ApiUtil;
import com.unipi.pfatouros.eassist.utility.PreferenceUtil;
import com.unipi.pfatouros.eassist.utility.Retrofit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewInventoryFragment extends Fragment
        implements SearchView.OnQueryTextListener,
        InventoryAdapter.OnItemClickListener{

    // Instance variables
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private InventoryAdapter adapter;
    private List<Inventory> unfilteredInventory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_inventory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize recycler view and layout manager
        recyclerView = view.findViewById(R.id.viewInventoryRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());

        // Set listener to search view
        SearchView searchView = view.findViewById(R.id.viewInventorySearchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(this);

        // Get all existing inventory items and display them on screen

        // Get token
        String token = PreferenceUtil.getToken(getContext());

        // Create retrofit object
        Retrofit api = ApiUtil.getRetrofit(token).create(Retrofit.class);

        // Create call object
        Call<List<Inventory>> call = api.getInventory();

        // Store current context in a variable in order to show toast
        final Context context = getContext();

        // Execute call
        call.enqueue(new Callback<List<Inventory>>() {
            @Override
            public void onResponse(@NonNull Call<List<Inventory>> call,
                                   @NonNull Response<List<Inventory>> response) {

                if(response.isSuccessful()){

                    // Get list of existing inventory items if response was successful
                    unfilteredInventory = response.body();

                    // Setup the recycler view
                    setRecyclerView();
                }
                else {
                    // Show toast otherwise
                    String text = getResources()
                            .getString(R.string.unsuccessful_response_exception_message)
                            + response.code();
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Inventory>> call,
                                  @NonNull Throwable t) {

                // Show toast with error message
                String text = t.getLocalizedMessage();
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRecyclerView() {

        // Create adapter
        adapter = new InventoryAdapter(unfilteredInventory, getContext());

        // Set listener to adapter
        adapter.setOnItemClickListener(this);

        // Setup layout manager and adapter with recycler view
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public boolean onQueryTextChange(String s) {

        // Create a filtered list of inventory items (based on item name, category)

        List<Inventory> filteredInventory = new ArrayList<>();
        for(Inventory inventoryItem: unfilteredInventory){

            //Get item name and category
            String itemName = inventoryItem.getItem_name().toLowerCase();
            String category = inventoryItem.getCategory().toLowerCase();

            // Do the filtering
            if(itemName.contains(s.toLowerCase()) || category.contains(s.toLowerCase())){

                filteredInventory.add(inventoryItem);
            }
        }

        // Update adapter
        if(!Objects.equals(filteredInventory.size(), 0)) {
            adapter.setInventory(filteredInventory);
            adapter.notifyDataSetChanged();
        }

        return true;
    }

    @Override
    public void onButtonClick(int position) {

        // Show bottom sheet in order to edit inventory item on given position
        CustomBottomSheet customBottomSheet = new CustomBottomSheet(adapter, position, 1);
        customBottomSheet.show(((AppCompatActivity) requireContext()).getSupportFragmentManager(),
                getResources().getString(R.string.bottom_sheet_dialog));
    }
}