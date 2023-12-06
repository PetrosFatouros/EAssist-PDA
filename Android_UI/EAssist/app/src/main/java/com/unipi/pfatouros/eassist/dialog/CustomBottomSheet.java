package com.unipi.pfatouros.eassist.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.unipi.pfatouros.eassist.R;
import com.unipi.pfatouros.eassist.adapter.InventoryAdapter;
import com.unipi.pfatouros.eassist.adapter.ItemAdapter;

public class CustomBottomSheet extends BottomSheetDialogFragment
        implements View.OnClickListener, DialogInterface.OnClickListener {

    // Instance variables
    private BottomSheetListener bottomSheetListener;
    private EditText editPriceEditText;
    private EditText editQuantityEditText;
    private EditText addQuantityEditText;
    private final Object adapter;
    private final int position;
    private final int type;

    // Constructor
    public CustomBottomSheet(Object adapter, int position, int type) {
        this.adapter = adapter;
        this.position = position;
        this.type = type; // 1 --> ViewInventoryFragment, 2 --> SelectItemsFragment
    }

    // Interface to implement clicks on bottom sheet dialog
    public interface BottomSheetListener {

        // Update inventory
        void onButtonClick(InventoryAdapter adapter, int position, Float price, Integer quantity);

        // Add item to order
        void onButtonClick(ItemAdapter adapter, int position, Integer quantity);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (type == 1) {
            return inflater.inflate(R.layout.bottom_sheet_inventory, container, false);
        } else {
            return inflater.inflate(R.layout.bottom_sheet_items, container, false);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize editTexts
        editPriceEditText = view.findViewById(R.id.newPriceBottomSheetEditText);
        editQuantityEditText = view.findViewById(R.id.newQuantityBottomSheetEditText);
        addQuantityEditText = view.findViewById(R.id.addQuantityBottomSheetEditText);

        // Set listeners to buttons
        if (type == 1) {
            Button editButton = view.findViewById(R.id.editBottomSheetButton);
            editButton.setOnClickListener(this);
        } else {
            Button addButton = view.findViewById(R.id.addBottomSheetButton);
            addButton.setOnClickListener(this);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Associate listener with activity
        bottomSheetListener = (BottomSheetListener) context;
    }

    @Override
    public void onClick(View view) {

        // Edit button was clicked
        if (view.getId() == R.id.editBottomSheetButton) {

            // Ask manager user if he is sure he wants to update the inventory
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder
                    .setMessage(getResources().getString(R.string.are_you_sure))
                    .setPositiveButton(getResources().getString(R.string.yes), this)
                    .setNegativeButton(getResources().getString(R.string.no), this).show();
        }
        // Add button was pressed
        else if (view.getId() == R.id.addBottomSheetButton) {

            // Get selected quantity
            String strQuantity = addQuantityEditText.getText().toString();
            Integer selectedQuantity = strQuantity.isEmpty() ? null : Integer.parseInt(strQuantity);

            if(selectedQuantity != null) {

                // Update selected item's quantity and selected quantity
                bottomSheetListener.onButtonClick((ItemAdapter) adapter, position, selectedQuantity);
            }
            dismiss();
        }
    }

    // Followed stackoverflow article:
    // https://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

        switch (i) {
            case DialogInterface.BUTTON_POSITIVE:

                //Yes button clicked

                // Get price and quantity
                String strPrice = editPriceEditText.getText().toString();
                String strQuantity = editQuantityEditText.getText().toString();

                Float price = strPrice.isEmpty() ? null : Float.parseFloat(strPrice);
                Integer quantity = strQuantity.isEmpty() ? null : Integer.parseInt(strQuantity);

                // Update inventory
                bottomSheetListener.onButtonClick((InventoryAdapter) adapter,
                        position,
                        price,
                        quantity);
                dismiss();

                break;

            case DialogInterface.BUTTON_NEGATIVE:

                //No button clicked
                // Do nothing

                break;
        }
    }
}
