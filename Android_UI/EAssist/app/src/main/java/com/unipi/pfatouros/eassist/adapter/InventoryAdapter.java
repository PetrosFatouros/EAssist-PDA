package com.unipi.pfatouros.eassist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.pfatouros.eassist.R;
import com.unipi.pfatouros.eassist.model.Inventory;

import java.text.MessageFormat;
import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder> {

    // Instance variables
    private List<Inventory> inventory;
    private final Context context;
    private OnItemClickListener listener;

    // Getter
    public List<Inventory> getInventory() {
        return inventory;
    }

    // Setter
    public void setInventory(List<Inventory> inventory) {
        this.inventory = inventory;
    }

    // Constructor
    public InventoryAdapter(List<Inventory> inventory, Context context) {
        this.inventory = inventory;
        this.context = context;
    }

    // Interface for implementing click on edit button of the cardView
    public interface OnItemClickListener {
        void onButtonClick(int position);
    }

    // Set listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // ViewHolder class, corresponds to card_view_inventory
    public static class InventoryViewHolder extends RecyclerView.ViewHolder {

        // Instance  variables
        public TextView nameTextView;
        public TextView categoryTextView;
        public TextView priceTextView;
        public TextView quantityTextView;
        public AppCompatButton editButton;

        // Constructor
        public InventoryViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            // Initialize instance variables
            nameTextView = itemView.findViewById(R.id.inventoryItemNameCardViewTextView);
            categoryTextView = itemView.findViewById(R.id.inventoryItemCategoryCardViewTextView);
            priceTextView = itemView.findViewById(R.id.inventoryItemPriceCardViewTextView);
            quantityTextView = itemView.findViewById(R.id.inventoryItemQuantityCardViewTextView);
            editButton = itemView.findViewById(R.id.editInventoryItemCardViewAppCompatButton);

            // Set listener for edit button
            editButton.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onButtonClick(position);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Create a new recycler view
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_view_inventory, parent, false);
        return new InventoryViewHolder(view, this.listener);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {

        // Get inventory item based on position
        Inventory inventoryItem = inventory.get(position);

        // Add inventory item's info to card view

        // Set item's name
        holder.nameTextView.setText(MessageFormat.format("{0} {1}",
                context.getResources().getString(R.string.item_), inventoryItem.getItem_name()));

        // Set item's category
        holder.categoryTextView.setText(MessageFormat.format("{0} {1}",
                context.getResources().getString(R.string.category_), inventoryItem.getCategory()));

        // Set item's price
        holder.priceTextView.setText(MessageFormat.format("{0} {1}",
                context.getResources().getString(R.string.price_), inventoryItem.getPrice()));

        // Set item's quantity
        holder.quantityTextView.setText(MessageFormat.format("{0} {1}",
                context.getResources().getString(R.string.quantity_), inventoryItem.getQuantity()));
    }

    @Override
    public int getItemCount() {

        // Return inventory's list size
        return inventory.size();
    }
}
