package com.unipi.pfatouros.eassist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.unipi.pfatouros.eassist.R;
import com.unipi.pfatouros.eassist.model.Table;

import java.text.MessageFormat;
import java.util.List;

public class TableAdapter extends RecyclerView.Adapter{

    // Instance  variables
    private final List<Table> tables;
    private final Context context;
    private final int type; // 1 --> TableViewHolderOne, 2 --> TableViewHolderTwo
    private OnItemClickListener listener;

    // Getter
    public List<Table> getTables() {
        return tables;
    }

    // Constructor
    public TableAdapter(List<Table> tables, Context context, int type) {
        this.context = context;
        this.tables = tables;
        this.type = type;
    }

    // Interface for implementing click on the cardView and on switch
    public interface OnItemClickListener{
        void onItemClick(int position);
        void onSwitchClick(int position);
    }

    // Set listener
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    // ViewHolder class, corresponds to card_view_tables
    public static class TableViewHolderOne extends RecyclerView.ViewHolder {

        // Instance variables
        public TextView textView;
        public SwitchMaterial switchMaterial;

        // Constructor
        public TableViewHolderOne(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            // Initialize instance variables
            textView = itemView.findViewById(R.id.tableNameCardViewTablesTextView);
            switchMaterial = itemView.findViewById(R.id.tableAvailableCardViewTablesTextView);

            // Set listener for cardView itself
            itemView.setOnClickListener(view -> {
                if (listener != null){
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            });

            // Set listener for switch
            switchMaterial.setOnClickListener(view -> {
                if (listener != null){
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onSwitchClick(position);
                    }
                }
            });
        }
    }

    // ViewHolder class, corresponds to card_view_select_table
    public static class TableViewHolderTwo extends RecyclerView.ViewHolder {

        // Instance variable
        public TextView textView;

        // Constructor
        public TableViewHolderTwo(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            // Initialize instance variable
            textView = itemView.findViewById(R.id.tableNameCardViewSelectTableTextView);

            // Set listener for cardView itself
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
                    .inflate(R.layout.card_view_tables, parent, false);
            return new TableViewHolderOne(view, this.listener);
        }
        else {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.card_view_select_table, parent, false);
            return new TableViewHolderTwo(view, this.listener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        // Get table based on position
        Table table = tables.get(position);

        if(type == 1){

            // Get associated ViewHolder
            TableViewHolderOne holderOne = (TableViewHolderOne) holder;

            // Add table's info to card view

            // Set table name
            holderOne.textView.setText(MessageFormat.format("{0} {1}",
                    context.getResources().getString(R.string.table_), table.getName()));

            // Set availability
            holderOne.switchMaterial.setChecked(table.getIs_available());
        }
        else {

            // Get associated ViewHolder
            TableViewHolderTwo holderTwo = (TableViewHolderTwo) holder;

            // Add table's info to card view

            // Set table name
            holderTwo.textView.setText(MessageFormat.format("{0} {1}",
                    context.getResources().getString(R.string.table_), table.getName()));
        }
    }

    @Override
    public int getItemCount() {

        // Return tables' list size
        return tables.size();
    }
}
