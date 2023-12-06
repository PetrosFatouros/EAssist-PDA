package com.unipi.pfatouros.eassist.fragment.manager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.pfatouros.eassist.R;
import com.unipi.pfatouros.eassist.adapter.StatisticsAdapter;
import com.unipi.pfatouros.eassist.model.response.ItemStats;
import com.unipi.pfatouros.eassist.utility.ApiUtil;
import com.unipi.pfatouros.eassist.utility.PreferenceUtil;
import com.unipi.pfatouros.eassist.utility.Retrofit;

import java.sql.Date;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewStatisticsFragment extends Fragment
        implements View.OnClickListener,
        SearchView.OnQueryTextListener,
        DatePickerDialog.OnDateSetListener {

    // Instance variables
    private TextView textView;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private StatisticsAdapter adapter;
    private List<ItemStats> itemStatsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize textView
        textView = view.findViewById(R.id.viewStatisticsTextView);

        // Initialize recycler view and layout manager
        recyclerView = view.findViewById(R.id.viewStatisticsRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());

        // Set listener for buttons
        AppCompatButton allHistoryButton
                = view.findViewById(R.id.viewStatisticsAllHistoryAppCompatButton);
        allHistoryButton.setOnClickListener(this);
        AppCompatButton selectDateButton
                = view.findViewById(R.id.viewStatisticsSelectDateAppCompatButton);
        selectDateButton.setOnClickListener(this);

        // Set listener to search view
        SearchView searchView = view.findViewById(R.id.viewStatisticsSearchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(this);

        // Get statistics (all orders regardless of date) and display them on screen

        callApi();
    }

    private void callApi() {

        // Get token
        String token = PreferenceUtil.getToken(getContext());

        // Create retrofit object
        Retrofit api = ApiUtil.getRetrofit(token).create(Retrofit.class);

        // Create call object
        Call<List<ItemStats>> call = api.getStatistics();

        // Store current context in a variable in order to show toast
        final Context context = getContext();

        // Execute call
        call.enqueue(new Callback<List<ItemStats>>() {
            @Override
            public void onResponse(@NonNull Call<List<ItemStats>> call,
                                   @NonNull Response<List<ItemStats>> response) {

                if (response.isSuccessful()) {

                    // Get list of statistics
                    itemStatsList = response.body();

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
            public void onFailure(@NonNull Call<List<ItemStats>> call,
                                  @NonNull Throwable t) {
                // Show toast with error message
                String text = t.getLocalizedMessage();
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRecyclerView() {

        // Create adapter
        adapter = new StatisticsAdapter(itemStatsList, getContext());

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

        // Initialize list with item statistics in case it is null
        if (itemStatsList == null) {
            itemStatsList = new ArrayList<>();
        }

        // Create empty array list
        List<ItemStats> filteredItemStats = new ArrayList<>();

        if (s.toLowerCase().equals(getResources().getString(R.string.profit))) {

            // Sort items' statistics based on profit (ascending)
            itemStatsList.sort(Comparator.comparing(ItemStats::getProfit));
            filteredItemStats = itemStatsList;

        } else if (s.toLowerCase().equals(getResources().getString(R.string.sold))) {

            // Sort items' statistics based on times the item has been sold (ascending)
            itemStatsList.sort(Comparator.comparing(ItemStats::getTimes_sold));
            filteredItemStats = itemStatsList;

        } else {

            // Create a filtered list of items' statistics (based on item name, category)

            for (ItemStats itemStats : itemStatsList) {

                //Get item name and category
                String itemName = itemStats.getInventory_item().getItem_name().toLowerCase();
                String category = itemStats.getInventory_item().getCategory().toLowerCase();

                // Do the filtering
                if (itemName.contains(s.toLowerCase()) || category.contains(s.toLowerCase())) {

                    filteredItemStats.add(itemStats);
                }
            }
        }

        // Update adapter
        if (!Objects.equals(filteredItemStats.size(), 0)) {
            adapter.setStatistics(filteredItemStats);
            adapter.notifyDataSetChanged();
        }

        return true;
    }

    @Override
    public void onClick(View view) {

        // All history button clicked
        if (view.getId() == R.id.viewStatisticsAllHistoryAppCompatButton) {

            // Get statistics (all orders regardless of date) and display them on screen

            callApi();

            // Update textView
            textView.setText(getResources().getString(R.string.all_history));
        }
        // Select date button clicked
        else if (view.getId() == R.id.viewStatisticsSelectDateAppCompatButton) {

            // Create calender variable
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Open date picker
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this,
                    year, month, day);
            datePickerDialog.setOnDateSetListener(this);
            datePickerDialog.show();
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        // Create calender variable
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, i);
        calendar.set(Calendar.MONTH, i1);
        calendar.set(Calendar.DAY_OF_MONTH, i2);

        // Create date variable based on calendar variable
        Date date = new Date(calendar.getTimeInMillis());

        // Update textView
        textView.setText(MessageFormat.format("{0} {1}",
                getResources().getString(R.string.date_), date.toString()));

        // Get statistics based on orders of specific date

        // Get token
        String token = PreferenceUtil.getToken(getContext());

        // Create retrofit object
        Retrofit api = ApiUtil.getRetrofit(token).create(Retrofit.class);

        // Create call object
        Call<List<ItemStats>> call = api.getStatistics(date.toString());

        // Store current context in a variable in order to show toast
        final Context context = getContext();

        // Execute call
        call.enqueue(new Callback<List<ItemStats>>() {
            @Override
            public void onResponse(@NonNull Call<List<ItemStats>> call,
                                   @NonNull Response<List<ItemStats>> response) {

                if (response.isSuccessful()) {

                    // Get list of statistics
                    itemStatsList = response.body();

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
            public void onFailure(@NonNull Call<List<ItemStats>> call,
                                  @NonNull Throwable t) {

                // Show toast with error message
                String text = t.getLocalizedMessage();
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}