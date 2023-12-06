package com.unipi.pfatouros.eassist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.pfatouros.eassist.R;
import com.unipi.pfatouros.eassist.model.Item;

import java.text.MessageFormat;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter{

    // Instance  variables
    private List<Item> items;
    private final Context context;
    private final int type; // 1 --> ItemViewHolderOne, 2 --> ItemViewHolderTwo
    private OnItemClickListener listener;

    // Getter
    public List<Item> getItems() {
        return items;
    }

    // Setter
    public void setItems(List<Item> items) {
        this.items = items;
    }

    // Constructor
    public ItemAdapter(List<Item> items, Context context, int type) {
        this.items = items;
        this.context = context;
        this.type = type;
    }

    // Interface for implementing click on the cardView itself
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    // Set listener
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    // ViewHolder class, corresponds to card_view_items
    public static class ItemViewHolderOne extends RecyclerView.ViewHolder{

        // Instance  variables
        public TextView itemNameTextView;
        public TextView priceTextView;
        public TextView selectedQuantityTextView;

        // Constructor
        public ItemViewHolderOne(@NonNull View itemView) {
            super(itemView);

            // Initialize instance variables
            itemNameTextView = itemView.findViewById(R.id.itemNameCardViewItemsTextView);
            priceTextView = itemView.findViewById(R.id.itemPriceCardViewItemsTextView);
            selectedQuantityTextView = itemView
                    .findViewById(R.id.itemSelectedQuantityCardViewItemsTextView);
        }
    }

    // ViewHolder class, corresponds to card_view_select_items
    public static class ItemViewHolderTwo extends RecyclerView.ViewHolder{

        // Instance  variables
        public TextView itemNameTextView;
        public TextView priceTextView;
        public TextView quantityTextView;
        public TextView selectedQuantityTextView;

        // Constructor
        public ItemViewHolderTwo(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            // Initialize instance variables
            itemNameTextView = itemView.findViewById(R.id.itemNameCardViewSelectItemsTextView);
            priceTextView = itemView.findViewById(R.id.itemPriceCardViewSelectItemsTextView);
            quantityTextView = itemView.findViewById(R.id.itemQuantityCardViewSelectItemsTextView);
            selectedQuantityTextView = itemView
                    .findViewById(R.id.itemSelectedQuantityCardViewSelectItemsTextView);

            // Set listener for clicking on the cardView itself
            itemView.setOnClickListener(view -> {
                if (listener != null){
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {

        // Return which ViewHolder will be displayed at the recycler view
        return type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Create a new recycler view based on type

        if(type == 1) {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.card_view_items, parent, false);
            return new ItemViewHolderOne(view);
        }
        else {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.card_view_select_items, parent, false);
            return new ItemViewHolderTwo(view, this.listener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        // Get item based on position
        Item item = items.get(position);

        // Add item's info to card view

        if(type == 1){

            // Get associated ViewHolder
            ItemViewHolderOne holderOne = (ItemViewHolderOne) holder;

            // Set item's name
            holderOne.itemNameTextView.setText(MessageFormat.format("{0} {1}",
                    context.getResources().getString(R.string.item_),
                    item.getInventory_item().getItem_name()));

            // Set's item's price
            holderOne.priceTextView.setText(MessageFormat.format("{0} {1}",
                    context.getResources().getString(R.string.price_),
                    item.getInventory_item().getPrice()));

            // Set item's selected quantity
            holderOne.selectedQuantityTextView.setText(MessageFormat.format("{0} {1}",
                    context.getResources().getString(R.string.selected_),
                    item.getSelected_quantity()));
        }
        else {

            // Get associated ViewHolder
            ItemViewHolderTwo holderTwo = (ItemViewHolderTwo) holder;

            // Set item's name
            holderTwo.itemNameTextView.setText(MessageFormat.format("{0} {1}",
                    context.getResources().getString(R.string.item_),
                    item.getInventory_item().getItem_name()));

            // Set's item's price
            holderTwo.priceTextView.setText(MessageFormat.format("{0} {1}",
                    context.getResources().getString(R.string.price_),
                    item.getInventory_item().getPrice()));

            // Set's item's quantity
            holderTwo.quantityTextView.setText(MessageFormat.format("{0} {1}",
                    context.getResources().getString(R.string.quantity_),
                    item.getInventory_item().getQuantity()));

            // Set item's selected quantity
            holderTwo.selectedQuantityTextView.setText(MessageFormat.format("{0} {1}",
                    context.getResources().getString(R.string.selected_),
                    item.getSelected_quantity()));
        }
    }

    @Override
    public int getItemCount() {

        // Return items' list size
        return items.size();
    }
}
