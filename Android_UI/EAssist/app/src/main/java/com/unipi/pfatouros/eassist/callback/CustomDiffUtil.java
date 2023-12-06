package com.unipi.pfatouros.eassist.callback;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.unipi.pfatouros.eassist.R;
import com.unipi.pfatouros.eassist.model.Order;

import java.util.List;
import java.util.Objects;

// Followed YouTube video
// https://www.youtube.com/watch?v=i1AjlGywGFU
public class CustomDiffUtil extends DiffUtil.Callback {

    // Instance variables
    final List<Order> oldList;
    final List<Order> newList;
    final Context context;

    // Constructor
    public CustomDiffUtil(List<Order> oldList, List<Order> newList, Context context) {

        this.oldList = oldList;
        this.newList = newList;
        this.context = context;
    }

    @Override
    public int getOldListSize() {

        return oldList != null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {

        return newList != null ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

        // Get old order
        Order oldOrder = (Order) oldList.get(oldItemPosition);

        // Get new order
        Order newOrder = (Order) newList.get(newItemPosition);

        // Return True if both orders have the same id, False otherwise
        return Objects.equals(oldOrder.getId(), newOrder.getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {

        // Get old order
        Order oldOrder = (Order) oldList.get(oldItemPosition);

        // Get new order
        Order newOrder = (Order) newList.get(newItemPosition);

        // Return True if both orders have the same status, False otherwise
        return Objects.equals(oldOrder.getStatus(), newOrder.getStatus());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {

        // Get old order
        Order oldOrder = (Order) oldList.get(oldItemPosition);

        // Get new order
        Order newOrder = (Order) newList.get(newItemPosition);

        // Create bundle
        Bundle bundle = new Bundle();

        // Insert status into the bundle
        if (!Objects.equals(oldOrder.getStatus(), newOrder.getStatus())) {
            bundle.putString(context.getResources().getString(R.string.status),
                    newOrder.getStatus());
        }

        // Return bundle
        return bundle.size() != 0 ? bundle : null;
    }
}
