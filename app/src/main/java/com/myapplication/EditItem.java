package com.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;


public class EditItem extends AppCompatActivity {
    Spinner spinner;
    String selectedItem;
    EditText editItemName,editQuantity, editprice;
    Button updateButton;
    SQLiteManager sqLiteManager;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);

        spinner = findViewById(R.id.itemToBeEdited);
        editItemName = findViewById(R.id.editItemName);
        editQuantity =findViewById(R.id.editQuantity);
        editprice=findViewById(R.id.editprice);
        updateButton = findViewById(R.id.additembuttontodatabase);
        sqLiteManager = SQLiteManager.instanceOfDatabase(this);

        // Populate the spinner with item names
        populateSpinner();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Retrieve the selected item
                selectedItem = (String) parent.getItemAtPosition(position);
                // Update the EditText fields with the selected item's information
                updateEditTextFields(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when no item is selected
                showToast("Select Device First");
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateFields()){
                    showLoadingIndicator();
                    // Update the item information in the database
                    updateItemInDatabase();
                    // Simulating a delay of 2 seconds for demonstration purposes.
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            editItemName.setText("");
                            editQuantity.setText("");
                            editprice.setText("");
                            hideLoadingIndicator();

                        }
                    }, 2000);

                }

            }
        });

    }

    private boolean validateFields() {

        String productNameText =  editItemName.getText().toString().trim();
        String quantityText = editQuantity.getText().toString().trim();
        String unitPriceText = editprice.getText().toString().trim();

        if (productNameText.isEmpty() || quantityText.isEmpty() || unitPriceText.isEmpty()) {
            // Check if any field is empty
            showError(editItemName, "Field cannot be empty");
            showError(editQuantity, "Field cannot be empty");
            showError(editprice, "Field cannot be empty");
            return false;
        }


        if (!isInteger(quantityText)) {
            // Check if quantity is an integer
            showError(editQuantity, "Invalid input");
            return false;
        }

        if (!isFloatOrInteger(unitPriceText)) {
            // Check if unitPrice is a float or an integer
            showError(editprice, "Invalid input");
            return false;
        }

        // All fields are valid
        return true;
    }

    private void showError(EditText editText, String errorMessage) {
        editText.setError(errorMessage);
    }

    private boolean isInteger(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isFloatOrInteger(String text) {
        try {
            Float.parseFloat(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showLoadingIndicator() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideLoadingIndicator() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void populateSpinner() {
        HashMap<String, Object> result = sqLiteManager.populateDisplayTable();
        String[][] data = (String[][]) result.get("data");

        // Determine the size of the data array
        int dataSize = data.length;

        // Create a new array to store the second field values
        String[] items = new String[dataSize];

        // Extract the second field values and store them in the items array
        for (int i = 0; i < dataSize; i++) {
            // Assuming the second field is at index 1
            items[i] = data[i][0];
        }

        // Create an ArrayAdapter using the data source and a default layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, items);
        // Set the adapter on the Spinner
        spinner.setAdapter(adapter);
    }



    private void updateEditTextFields(String selectedItem) {
        HashMap<String, Object> result = sqLiteManager.getItemInfo(selectedItem);
        String[][] data = (String[][]) result.get("data");

        if (data.length > 0) {
            String itemName = data[0][2];
            String itemQuantity = data[0][3];
            String itemPrice = data[0][4];

            // Update the EditText fields with the selected item's information
            editItemName.setText(itemName);
            editItemName.setHint("");
            editQuantity.setText(itemQuantity);
            editQuantity.setHint("");
            editprice.setText(itemPrice);
            editprice.setHint("");
        }
    }

    private void updateItemInDatabase() {
        String newItemName = editItemName.getText().toString();
        String newQuantity = editQuantity.getText().toString();
        String newPrice = editprice.getText().toString();

        // Update the item in the database
        boolean isUpdated = sqLiteManager.updateItem(selectedItem, newItemName, Integer.parseInt(newQuantity), Float.parseFloat(newPrice));
        if (isUpdated) {
            showToast("Item updated successfully");
        } else {
            showToast("Failed to update item");
        }
    }


}