package com.unipi.pfatouros.eassist.fragment.employee;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.pfatouros.eassist.R;
import com.unipi.pfatouros.eassist.adapter.TableAdapter;
import com.unipi.pfatouros.eassist.model.Table;
import com.unipi.pfatouros.eassist.utility.ApiUtil;
import com.unipi.pfatouros.eassist.utility.PreferenceUtil;
import com.unipi.pfatouros.eassist.utility.Retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewTablesFragment extends Fragment implements TableAdapter.OnItemClickListener {

    // Instance variables
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TableAdapter adapter;
    private List<Table> tables;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_tables, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize recycler view and layout manager
        recyclerView = view.findViewById(R.id.viewTablesRecycleView);
        layoutManager = new LinearLayoutManager(getContext());

        // Get all existing tables and display them on screen

        // Get token
        String token = PreferenceUtil.getToken(getContext());

        // Create retrofit object
        Retrofit api = ApiUtil.getRetrofit(token).create(Retrofit.class);

        // Create call object
        Call<List<Table>> call = api.getTables();

        // Execute call
        call.enqueue(new Callback<List<Table>>() {
            @Override
            public void onResponse(@NonNull Call<List<Table>> call,
                                   @NonNull Response<List<Table>> response) {

                // Get list of all existing tables if response was successful
                tables = response.body();

                // Setup the recycler view
                setRecyclerView();
            }

            @Override
            public void onFailure(@NonNull Call<List<Table>> call,
                                  @NonNull Throwable t) {

                // Show toast with error message
                String text = t.getLocalizedMessage();
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRecyclerView() {

        // Create adapter
        adapter = new TableAdapter(tables, getContext(), 1);

        // Set listener to adapter
        adapter.setOnItemClickListener(this);

        // Setup layout manager and adapter with recycler view
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {

        // Show the orders of the clicked table

        // Get clicked table
        Table table = tables.get(position);

        // Define action
        ViewTablesFragmentDirections.ActionViewTablesFragmentToViewOrdersOfTableFragment action =
                ViewTablesFragmentDirections
                        .actionViewTablesFragmentToViewOrdersOfTableFragment(table.getId(),
                                table.getName());

        // Navigate to corresponding fragment
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onSwitchClick(int position) {

        // Update table's availability

        // Get table based on position
        Table table = adapter.getTables().get(position);

        // Get token
        String token = PreferenceUtil.getToken(getContext());

        // Create retrofit object
        Retrofit api = ApiUtil.getRetrofit(token).create(Retrofit.class);

        // Create call object
        Call<Void> call = api.updateTable(table.getId(), !table.getIs_available());

        // Execute call
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,
                                   @NonNull Response<Void> response) {

                String text;
                if (response.isSuccessful()) {
                    // Status updated
                    text = getResources()
                            .getString(R.string.table_updated);

                    // Update adapter
                    table.setIs_available(!table.getIs_available());
                    adapter.notifyItemChanged(position);

                } else {
                    // Unsuccessful response
                    text = getResources()
                            .getString(R.string.table_could_not_be_updated);
                }

                // Show toast with corresponding message
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
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