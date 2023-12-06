package com.unipi.pfatouros.eassist.fragment.employee;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.pfatouros.eassist.R;
import com.unipi.pfatouros.eassist.adapter.ItemAdapter;
import com.unipi.pfatouros.eassist.model.Item;
import com.unipi.pfatouros.eassist.utility.ApiUtil;
import com.unipi.pfatouros.eassist.utility.PreferenceUtil;
import com.unipi.pfatouros.eassist.utility.Retrofit;

import java.text.MessageFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewItemsOfOrderFragment extends Fragment {

    // Instance variables
    private TextView totalCostTextView;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Item> items;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_items_of_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize textView
        totalCostTextView = view.findViewById(R.id.viewItemsOfOrderTextView);

        // Initialize recycler view and layout manager
        recyclerView = view.findViewById(R.id.viewItemsOfOrderRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());

        // Get all associated items of selected order and display them on screen

        // Get selected order's id from safe args
        Long id = ViewItemsOfOrderFragmentArgs.fromBundle(getArguments()).getOrderId();

        // Get token
        String token = PreferenceUtil.getToken(getContext());

        // Create retrofit object
        Retrofit api = ApiUtil.getRetrofit(token).create(Retrofit.class);

        // Create call object
        Call<List<Item>> call = api.getItemsOfOrder(id);

        // Store current context in a variable in order to show toast
        final Context context = getContext();

        // Execute call
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(@NonNull Call<List<Item>> call,
                                   @NonNull Response<List<Item>> response) {

                if(response.isSuccessful()){

                    // Get list of the items of the selected order if response was successful
                    items = response.body();

                    // Setup total cost textView
                    setTotalCostTextView();

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
            public void onFailure(@NonNull Call<List<Item>> call,
                                  @NonNull Throwable t) {

                // Show toast with error message
                String text = t.getLocalizedMessage();
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTotalCostTextView(){

        // Get total cost of order
        float total_cost = 0F;
        for(Item item: items){
            total_cost += item.getInventory_item().getPrice() * item.getSelected_quantity();
        }

        // Update textView
        totalCostTextView.setText(MessageFormat.format("{0} {1} {2}",
                getResources().getString(R.string.total_cost_of_order_),
                total_cost,
                getResources().getString(R.string.euros)));
    }

    private void setRecyclerView() {

        // Create adapter
        ItemAdapter adapter = new ItemAdapter(items, getContext(), 1);

        // Setup layout manager and adapter with recycler view
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}