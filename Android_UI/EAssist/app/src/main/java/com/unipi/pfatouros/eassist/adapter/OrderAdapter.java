package com.unipi.pfatouros.eassist.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.pfatouros.eassist.R;
import com.unipi.pfatouros.eassist.model.Order;
import com.unipi.pfatouros.eassist.utility.Constants;

import java.text.MessageFormat;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter {

    // Instance  variables
    private final List<Order> orders;
    private final Context context;
    // 1 --> OrderViewHolderOne, 2 --> OrderViewHolderTwo
    private final int type;
    private OnItemClickListener listener;

    // Getter
    public List<Order> getOrders() {
        return orders;
    }

    // Constructor
    public OrderAdapter(List<Order> orders, Context context, int type) {
        this.orders = orders;
        this.context = context;
        this.type = type;
    }

    // Interface for implementing click on the cardView and on status button
    public interface OnItemClickListener {
        void onItemClick(int position);

        void onButtonClick(int position);
    }

    // Set listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // ViewHolder class, corresponds to card_view_orders
    public static class OrderViewHolderOne extends RecyclerView.ViewHolder {

        // Instance  variables
        public TextView orderIdTextView;
        public TextView tableNameTextView;
        public AppCompatButton statusAppCompatButton;

        // Constructor
        public OrderViewHolderOne(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            // Initialize instance variables
            orderIdTextView = itemView.findViewById(R.id.orderIdCardViewOrdersTextView);
            tableNameTextView = itemView.findViewById(R.id.tableNameCardViewOrdersTextView);
            statusAppCompatButton = itemView.findViewById(R.id.orderStatusCardViewOrdersAppCompatButton);

            // Set listener for clicking on the cardView itself
            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });

            // Set listener for button
            statusAppCompatButton.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onButtonClick(position);
                    }
                }
            });
        }
    }

    // ViewHolder class, corresponds to card_view_orders_of_table
    public static class OrderViewHolderTwo extends RecyclerView.ViewHolder {

        // Instance  variables
        public TextView orderIdTextView;
        public AppCompatButton statusAppCompatButton;

        // Constructor
        public OrderViewHolderTwo(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            // Initialize instance variables
            orderIdTextView = itemView
                    .findViewById(R.id.orderIdCardViewOrdersOfTableTextView);
            statusAppCompatButton = itemView
                    .findViewById(R.id.orderStatusCardViewOrdersOfTableAppCompatButton);

            // Set listener for clicking on the cardView itself
            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });

            // Set listener for button
            statusAppCompatButton.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onButtonClick(position);
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

        if (type == 1) {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.card_view_orders, parent, false);
            return new OrderViewHolderOne(view, this.listener);
        } else {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.card_view_orders_of_table, parent, false);
            return new OrderViewHolderTwo(view, this.listener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        // Get order based on position
        Order order = orders.get(position);

        // Get next button's color based on status
        int colorID = R.color.button_default;
        switch (order.getStatus().toUpperCase()) {
            case Constants.ACTIVE:
                colorID = R.color.active;
                break;
            case Constants.READY:
                colorID = R.color.ready;
                break;
            case Constants.UNPAID:
                colorID = R.color.unpaid;
                break;
            case Constants.COMPLETE:
                colorID = R.color.complete;
                break;
        }

        // Add order's info to card view

        if (type == 1) {

            // Get associated ViewHolder
            OrderViewHolderOne holderOne = (OrderViewHolderOne) holder;

            // Set order's id
            holderOne.orderIdTextView.setText(MessageFormat.format("{0} {1}",
                    context.getResources().getString(R.string.order_id_), order.getId()));

            // Set order's table name
            holderOne.tableNameTextView.setText(MessageFormat.format("{0} {1}",
                    context.getResources().getString(R.string.table_),
                    order.getTable().getName()));

            // Set order's availability
            holderOne.statusAppCompatButton.setText(order.getStatus());

            // Set button's color based on status
            holderOne.statusAppCompatButton.setBackgroundColor(context.getColor(colorID));
        } else {

            // Get associated ViewHolder
            OrderViewHolderTwo holderTwo = (OrderViewHolderTwo) holder;

            // Set order's id
            holderTwo.orderIdTextView.setText(MessageFormat.format("{0} {1}",
                    context.getResources().getString(R.string.order_id_), order.getId()));

            // Set order's availability
            holderTwo.statusAppCompatButton.setText(order.getStatus());

            // Set button's color based on status
            holderTwo.statusAppCompatButton.setBackgroundColor(context.getColor(colorID));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,
                                 int position,
                                 @NonNull List payloads) {

        if (type == 1) {
            if (payloads.isEmpty()) {
                super.onBindViewHolder(holder, position, payloads);
            } else {

                // Get bundle from payloads
                Bundle bundle = (Bundle) payloads.get(0);

                for (String key : bundle.keySet()) {

                    if (key.equals(context.getResources().getString(R.string.status))) {

                        // Get associated ViewHolder
                        OrderViewHolderOne holderOne = (OrderViewHolderOne) holder;

                        // Get order based on position
                        Order order = orders.get(position);

                        // Get next button's color based on status
                        int colorID = R.color.button_default;
                        switch (order.getStatus().toUpperCase()) {
                            case Constants.ACTIVE:
                                colorID = R.color.active;
                                break;
                            case Constants.READY:
                                colorID = R.color.ready;
                                break;
                            case Constants.UNPAID:
                                colorID = R.color.unpaid;
                                break;
                            case Constants.COMPLETE:
                                colorID = R.color.complete;
                                break;
                        }

                        // Update order's info

                        // Set order's availability
                        holderOne.statusAppCompatButton.setText(order.getStatus());

                        // Set button's color based on status
                        holderOne.statusAppCompatButton.setBackgroundColor(context.getColor(colorID));
                    }
                }
            }
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public int getItemCount() {

        // Return orders' list size
        return orders.size();
    }
}
