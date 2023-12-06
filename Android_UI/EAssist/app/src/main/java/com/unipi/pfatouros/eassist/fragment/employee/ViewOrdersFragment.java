package com.unipi.pfatouros.eassist.fragment.employee;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.pfatouros.eassist.R;
import com.unipi.pfatouros.eassist.adapter.OrderAdapter;
import com.unipi.pfatouros.eassist.callback.CustomDiffUtil;
import com.unipi.pfatouros.eassist.model.Order;
import com.unipi.pfatouros.eassist.utility.ApiUtil;
import com.unipi.pfatouros.eassist.utility.Constants;
import com.unipi.pfatouros.eassist.utility.PreferenceUtil;
import com.unipi.pfatouros.eassist.utility.Retrofit;

import java.sql.Date;
import java.text.MessageFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewOrdersFragment extends Fragment implements OrderAdapter.OnItemClickListener, View.OnClickListener {

    // Instance variables
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private OrderAdapter adapter;
    private List<Order> orders;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize recycler view and layout manager
        recyclerView = view.findViewById(R.id.viewOrdersRecycleView);
        layoutManager = new LinearLayoutManager(getContext());

        // Set listener for refresh button
        AppCompatButton button = view.findViewById(R.id.viewOrdersAppCompatButton);
        button.setOnClickListener(this);

        // Call api
        callApi(false);
    }

    private void callApi(boolean refresh) {

        // Get all pending orders and display them on screen

        // Get current date
        Date date = new Date(System.currentTimeMillis());

        // Get token
        String token = PreferenceUtil.getToken(getContext());

        // Create retrofit object
        Retrofit api = ApiUtil.getRetrofit(token).create(Retrofit.class);

        // Initialize call object
        Call<List<Order>> call = api.getPendingOrders(date.toString());

        // Store current context in a variable in order to show toast
        final Context context = getContext();

        // Execute call
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(@NonNull Call<List<Order>> call,
                                   @NonNull Response<List<Order>> response) {

                if (response.isSuccessful()) {

                    // Get list of pending orders if response was successful
                    orders = response.body();

                    // Do not setup recycler view when refreshing adapter
                    if (!refresh) {

                        // Setup the recycler view
                        setRecyclerView();
                    } else {

                        // Refresh recycler view
                        refreshRecyclerView();
                    }

                } else {
                    // Show toast otherwise
                    String text = getResources()
                            .getString(R.string.unsuccessful_response_exception_message)
                            + response.code();
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Order>> call,
                                  @NonNull Throwable t) {

                // Show toast with error message
                String text = t.getLocalizedMessage();
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRecyclerView() {

        // Create adapter
        adapter = new OrderAdapter(orders, getContext(), 1);

        // Set listener to adapter
        adapter.setOnItemClickListener(this);

        // Setup layout manager and adapter with recycler view
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    private void refreshRecyclerView() {

        // Create DiffResult object
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CustomDiffUtil(
                adapter.getOrders(),
                orders,
                getContext()));

        // Update adapter

        diffResult.dispatchUpdatesTo(adapter);
        adapter.getOrders().clear();
        adapter.getOrders().addAll(orders);
    }

    @Override
    public void onClick(View view) {

        // Refresh recycler view
        if(view.getId() == R.id.viewOrdersAppCompatButton){

            callApi(true);
        }
    }

    @Override
    public void onItemClick(int position) {

        // Show the items of the clicked order

        // Get id of clicked order
        Long id = adapter.getOrders().get(position).getId();

        // Define action
        ViewOrdersFragmentDirections.ActionViewOrdersFragmentToViewItemsOfOrderFragment action =
                ViewOrdersFragmentDirections.actionViewOrdersFragmentToViewItemsOfOrderFragment(id);

        // Navigate to corresponding fragment
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onButtonClick(int position) {

        // Change the status of the clicked order

        // Get order based on position
        Order order = adapter.getOrders().get(position);

        // Get next status
        String status;
        switch (order.getStatus().toUpperCase()) {
            case Constants.ACTIVE:
                status = getResources().getString(R.string.ready);
                break;
            case Constants.READY:
                status = getResources().getString(R.string.unpaid);
                break;
            case Constants.UNPAID:
                status = getResources().getString(R.string.complete);
                break;
            case Constants.COMPLETE:
                status = getResources().getString(R.string.active);
                break;
            default:
                throw new IllegalStateException(
                        MessageFormat.format("{0} {1}",
                                getResources().getString(R.string.unexpected_value_),
                                order.getStatus()));
        }

        // Get token
        String token = PreferenceUtil.getToken(getContext());

        // Create retrofit object
        Retrofit api = ApiUtil.getRetrofit(token).create(Retrofit.class);

        // Create call object
        Call<Void> call = api.updateOrder(order.getId(), status.toLowerCase());

        // Store current context in a variable in order to show toast
        final Context context = getContext();

        // Execute call
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,
                                   @NonNull Response<Void> response) {

                String text;
                if (response.isSuccessful()) {
                    // Status updated
                    text = getResources()
                            .getString(R.string.status_updated);

                    // Update adapter
                    order.setStatus(status.toLowerCase());

                    if (status.equalsIgnoreCase(Constants.COMPLETE)) {
                        adapter.getOrders().remove(position);
                        adapter.notifyItemRemoved(position);
                    } else {
                        adapter.notifyItemChanged(position);
                    }

                } else {
                    // Unsuccessful response
                    text = getResources()
                            .getString(R.string.status_could_not_be_updated);
                }

                // Show toast with corresponding message
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call,
                                  @NonNull Throwable t) {

                // Show toast with error message
                String text = t.getLocalizedMessage();
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}