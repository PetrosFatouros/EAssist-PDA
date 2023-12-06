package com.unipi.pfatouros.eassist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.pfatouros.eassist.R;
import com.unipi.pfatouros.eassist.model.response.ItemStats;

import java.text.MessageFormat;
import java.util.List;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.StatisticsViewHolder>{

    // Instance variables
    private List<ItemStats> statistics;
    private final Context context;

    // Setter
    public void setStatistics(List<ItemStats> statistics) {
        this.statistics = statistics;
    }

    // Constructor
    public StatisticsAdapter(List<ItemStats> statistics, Context context) {
        this.statistics = statistics;
        this.context = context;
    }

    // ViewHolder class, corresponds to card_view_statistics
    public static class StatisticsViewHolder extends RecyclerView.ViewHolder{

        // Instance variables
        public TextView itemNameTextView;
        public TextView priceTextView;
        public TextView soldTextView;
        public TextView profitTextView;

        // Constructor
        public StatisticsViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize instance variables
            itemNameTextView = itemView.findViewById(R.id.itemNameCardViewStatisticsTextView);
            priceTextView = itemView.findViewById(R.id.priceCardViewStatisticsTextView);
            soldTextView = itemView.findViewById(R.id.soldCardViewStatisticsTextView);
            profitTextView = itemView.findViewById(R.id.profitCardViewStatisticsTextView);
        }
    }

    @NonNull
    @Override
    public StatisticsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Create a new recycler view
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_view_statistics, parent, false);
        return new StatisticsAdapter.StatisticsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticsViewHolder holder, int position) {

        // Get statistics of item based on position
        ItemStats itemStats = statistics.get(position);

        // Add statistics of item to card view

        // Set item's name
        holder.itemNameTextView.setText(MessageFormat.format("{0} {1}",
                context.getResources().getString(R.string.item_),
                itemStats.getInventory_item().getItem_name()));

        // Set item's price
        holder.priceTextView.setText(MessageFormat.format("{0} {1}",
                context.getResources().getString(R.string.price_),
                itemStats.getInventory_item().getPrice()));

        // Set times the item was sold
        holder.soldTextView.setText(MessageFormat.format("{0} {1} {2}",
                context.getResources().getString(R.string.sold_),
                itemStats.getTimes_sold(),
                context.getResources().getString(R.string.times)));

        // Set profit the item has made
        holder.profitTextView.setText(MessageFormat.format("{0} {1} {2}",
                context.getResources().getString(R.string.profit_),
                itemStats.getProfit(),
                context.getResources().getString(R.string.euros)));
    }

    @Override
    public int getItemCount() {

        // Return statistics list size
        return statistics.size();
    }
}
