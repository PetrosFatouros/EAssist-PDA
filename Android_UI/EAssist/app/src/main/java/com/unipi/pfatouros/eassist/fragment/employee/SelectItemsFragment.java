package com.unipi.pfatouros.eassist.fragment.employee;

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
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.pfatouros.eassist.R;
import com.unipi.pfatouros.eassist.adapter.ItemAdapter;
import com.unipi.pfatouros.eassist.dialog.CustomBottomSheet;
import com.unipi.pfatouros.eassist.model.Inventory;
import com.unipi.pfatouros.eassist.model.Item;
import com.unipi.pfatouros.eassist.utility.ApiUtil;
import com.unipi.pfatouros.eassist.utility.PreferenceUtil;
import com.unipi.pfatouros.eassist.utility.Retrofit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectItemsFragment extends Fragment
        implements SearchView.OnQueryTextListener,
        ItemAdapter.OnItemClickListener,
        View.OnClickListener {

    // Instance variables
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ItemAdapter adapter;
    private List<Item> items;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize items list
        items = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize recycler view and layout manager
        recyclerView = view.findViewById(R.id.selectItemsRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());

        // Empty items list
        items.clear();

        // Set listener for review order button
        AppCompatButton button = view.findViewById(R.id.selectItemsAppCompatButton);
        button.setOnClickListener(this);

        // Set listener to search view
        SearchView searchView = view.findViewById(R.id.selectItemsSearchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(this);

        // Get all available items from the inventory and display them on screen

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

                if (response.isSuccessful()) {

                    // Get list of all inventory items if response was successful
                    List<Inventory> inventory = response.body();

                    // Update items list and display it

                    // Gather all inventory items that are in stock
                    for (Inventory inventoryItem : Objects.requireNonNull(inventory)) {
                        if (inventoryItem.getQuantity() != 0) {
                            items.add(new Item(inventoryItem));
                        }
                    }

                    // Setup the recycler view
                    setRecyclerView();
                } else {
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
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setRecyclerView() {

        // Create adapter
        adapter = new ItemAdapter(items, getContext(), 2);

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

    @SuppressLint("NotifyDataSetChanged") // Update the whole adapter when searchView is used
    @Override
    public boolean onQueryTextChange(String s) {

        // Create empty array list
        List<Item> filteredItems = new ArrayList<>();

        if (!s.equals(getResources().getString(R.string.special_code))) {

            // Create a filtered list of items (based on item name, category)

            for (Item item : items) {

                //Get item name and category
                String itemName = item.getInventory_item().getItem_name().toLowerCase();
                String category = item.getInventory_item().getCategory().toLowerCase();

                // Do the filtering
                if (itemName.contains(s.toLowerCase()) || category.contains(s.toLowerCase())) {

                    filteredItems.add(item);
                }
            }
        } else {

            // Special code "@@" inserted
            // Create a list of items that are currently selected by the customer

            for (Item item : items) {

                if (item.getSelected_quantity() > 0) {
                    filteredItems.add(item);
                }
            }
        }

        // Update adapter
        if (!Objects.equals(filteredItems.size(), 0)) {
            adapter.setItems(filteredItems);
            adapter.notifyDataSetChanged();
        }

        return true;
    }

    @Override
    public void onItemClick(int position) {

        // Show bottom sheet in order to edit item on given position
        CustomBottomSheet customBottomSheet = new CustomBottomSheet(adapter, position, 2);
        customBottomSheet.show(((AppCompatActivity) requireContext()).getSupportFragmentManager(),
                getResources().getString(R.string.bottom_sheet_dialog));
    }

    @Override
    public void onClick(View view) {

        // Review order button was clicked
        if (view.getId() == R.id.selectItemsAppCompatButton) {

            // Review current order

            // Set the items list of the ReviewItemsFragment
            List<Item> selectedItems = new ArrayList<>();
            for (Item item : items) {
                if (item.getSelected_quantity() > 0) {
                    selectedItems.add(item);
                }
            }

            // At least one item must be selected
            if (!Objects.equals(selectedItems.size(), 0)) {

                ReviewItemsFragment.items = selectedItems;

                // Get table id from safe args
                long id = SelectItemsFragmentArgs.fromBundle(getArguments()).getTableId();

                // Define action
                SelectItemsFragmentDirections.ActionSelectItemsFragmentToReviewItemsFragment action =
                        SelectItemsFragmentDirections.actionSelectItemsFragmentToReviewItemsFragment(id);

                // Navigate to corresponding fragment
                Navigation.findNavController(requireView()).navigate(action);
            }
            else {

                // Show toast with corresponding message
                Toast.makeText(getContext(),
                        getResources().getString(R.string.at_least_one_item_must_be_selected),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}