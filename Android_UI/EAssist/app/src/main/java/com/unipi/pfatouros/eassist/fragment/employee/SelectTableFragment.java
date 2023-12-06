package com.unipi.pfatouros.eassist.fragment.employee;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectTableFragment extends Fragment implements TableAdapter.OnItemClickListener{

    // Instance variables
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TableAdapter adapter;
    private List<Table> tables;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize tables list
        tables = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_table, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize recycler view and layout manager
        recyclerView = view.findViewById(R.id.selectTableRecycleView);
        layoutManager = new LinearLayoutManager(getContext());

        // Empty tables list (in order to remove duplicates)
        tables.clear();

        // Get all existing tables that are available and display them on screen

        // Get token
        String token = PreferenceUtil.getToken(getContext());

        // Create retrofit object
        Retrofit api = ApiUtil.getRetrofit(token).create(Retrofit.class);

        // Create call object
        Call<List<Table>> call = api.getTables();

        // Store current context in a variable in order to show toast
        final Context context = getContext();

        // Execute call
        call.enqueue(new Callback<List<Table>>() {
            @Override
            public void onResponse(@NonNull Call<List<Table>> call,
                                   @NonNull Response<List<Table>> response) {

                // Get list of all existing tables if response was successful
                List<Table> all_tables = response.body();

                // Remove tables that are not available from the list
                for(Table table: Objects.requireNonNull(all_tables)){
                    if (table.getIs_available()){
                        tables.add(table);
                    }
                }

                // Setup the recycler view
                setRecyclerView();
            }

            @Override
            public void onFailure(@NonNull Call<List<Table>> call,
                                  @NonNull Throwable t) {

                // Show toast with error message
                String text = t.getLocalizedMessage();
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRecyclerView() {

        // Create adapter
        adapter = new TableAdapter(tables, getContext(), 2);

        // Set listener to adapter
        adapter.setOnItemClickListener(this);

        // Setup layout manager and adapter with recycler view
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {

        // Proceed with order creation and select items

        // Get clicked table
        Table table = adapter.getTables().get(position);

        // Define action
        SelectTableFragmentDirections.ActionSelectTableFragmentToSelectItemsFragment action =
                SelectTableFragmentDirections
                        .actionSelectTableFragmentToSelectItemsFragment(table.getId(),
                                table.getName());

        // Navigate to corresponding fragment
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onSwitchClick(int position) {

        // Update table's availability (not implemented in this class)
    }
}