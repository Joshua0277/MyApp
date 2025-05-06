package com.example.myapp;

import android.widget.EditText;
import android.widget.Spinner;

public class FoodInput {
    private EditText nameInput;
    private EditText amountInput;
    private Spinner unitSpinner;

    public FoodInput(EditText nameInput, EditText amountInput, Spinner unitSpinner) {
        this.nameInput = nameInput;
        this.amountInput = amountInput;
        this.unitSpinner = unitSpinner;
    }

    public String getName() {
        return nameInput.getText().toString().trim();
    }

    public String getAmount() {
        return amountInput.getText().toString().trim();
    }

    public String getUnit() {
        return unitSpinner.getSelectedItem().toString();
    }
}
