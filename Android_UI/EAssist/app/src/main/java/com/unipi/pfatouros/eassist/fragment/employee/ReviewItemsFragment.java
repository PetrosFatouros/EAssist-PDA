package com.unipi.pfatouros.eassist.fragment.employee;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.pfatouros.eassist.R;
import com.unipi.pfatouros.eassist.adapter.ItemAdapter;
import com.unipi.pfatouros.eassist.model.Item;
import com.unipi.pfatouros.eassist.model.request.AddOrderRequest;
import com.unipi.pfatouros.eassist.utility.ApiUtil;
import com.unipi.pfatouros.eassist.utility.PreferenceUtil;
import com.unipi.pfatouros.eassist.utility.Retrofit;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewItemsFragment extends Fragment
        implements View.OnClickListener,
        DialogInterface.OnClickListener {

    // Class variable
    public static List<Item> items;

    // Instance variables
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ItemAdapter adapter;
    private Call<Void> call;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize recycler view and layout manager
        recyclerView = view.findViewById(R.id.reviewItemsRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());

        // Set listener for add order button
        AppCompatButton button = view.findViewById(R.id.reviewItemsAppCompatButton);
        button.setOnClickListener(this);

        // Setup the recycler view
        setRecyclerView();
    }

    private void setRecyclerView() {

        // Create adapter
        adapter = new ItemAdapter(items, getContext(), 2);

        // Setup layout manager and adapter with recycler view
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {

        // Add order button was clicked
        if(view.getId() == R.id.reviewItemsAppCompatButton){

            // Add current order to system

            // Get id of the selected table from safe args
            Long table_id = ReviewItemsFragmentArgs.fromBundle(getArguments()).getTableId();

            // Gather the ids of the selected inventory items and their selected quantity
            List<Long> inventoryIds = new ArrayList<>();
            List<Integer> selectedQuantities = new ArrayList<>();
            for(Item item: items){
                inventoryIds.add(item.getInventory_item().getId());
                selectedQuantities.add(item.getSelected_quantity());
            }

            // Get token
            String token = PreferenceUtil.getToken(getContext());

            // Create retrofit object
            Retrofit api = ApiUtil.getRetrofit(token).create(Retrofit.class);

            // Create request
            AddOrderRequest request = new AddOrderRequest(table_id,
                    inventoryIds,
                    selectedQuantities);

            // Create call object
            call = api.addOrder(request);

            // Ask employee user if he is sure he wants add this order to system
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder
                    .setMessage(getResources().getString(R.string.are_you_sure))
                    .setPositiveButton(getResources().getString(R.string.yes), this)
                    .setNegativeButton(getResources().getString(R.string.no), this).show();
        }
    }

    // Followed stackoverflow article:
    // https://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

        switch (i) {
            case DialogInterface.BUTTON_POSITIVE:

                //Yes button clicked

                // Store current context in a variable in order to show toast
                final Context context = getContext();

                // Execute call
                call.enqueue(new Callback<Void>() {
                    @SuppressLint("NotifyDataSetChanged") // Update the whole adapter
                    @Override
                    public void onResponse(@NonNull Call<Void> call,
                                           @NonNull Response<Void> response) {

                        String text;
                        if (response.isSuccessful()) {
                            // Order added to system
                            text = getResources()
                                    .getString(R.string.order_added_to_system);

                            // Remove items from list and update adapter
                            items.clear();
                            adapter.notifyDataSetChanged();

                        } else {
                            // Unsuccessful response
                            text = getResources()
                                    .getString(R.string.unsuccessful_response_exception_message)
                                    + response.code();
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

                break;

            case DialogInterface.BUTTON_NEGATIVE:

                //No button clicked
                // Do nothing

                break;
        }
    }
}