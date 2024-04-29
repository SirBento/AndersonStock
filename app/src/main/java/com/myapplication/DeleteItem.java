package com.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class DeleteItem extends AppCompatActivity {
    String selectedItem;
    private ProgressDialog progressDialog;
    EditText numbersToBeDeleted;
    CheckBox deleteAllOrNot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_item);
        Spinner spinner = findViewById(R.id.itemToDelete);
        deleteAllOrNot = findViewById(R.id.deleteAll);
        numbersToBeDeleted= findViewById(R.id.howManyItemsToDelete);
        Button deleteItemFromDB = findViewById(R.id.deleteItemFromDB);

        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, items);
       // Set the adapter on the Spinner
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Retrieve the selected item
                selectedItem = (String) parent.getItemAtPosition(position);
                // Perform actions based on the selected item
                // ...
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when no item is selected
                showToast("Select Device First");
            }
        });

        deleteAllOrNot.setOnCheckedChangeListener((compoundButton, isChecked) -> {

            if(isChecked){

                numbersToBeDeleted.setEnabled(false);
                numbersToBeDeleted.setAlpha(0.1f);

            }else{

                numbersToBeDeleted.setEnabled(true);
                numbersToBeDeleted.setAlpha(1.0f);
            }

        });

        numbersToBeDeleted.setOnFocusChangeListener((view, hasFocus) -> {
            if(hasFocus){
                numbersToBeDeleted.setHint("");
            }
        });


        deleteItemFromDB.setOnClickListener(view -> {

            if(validateFields() && spinner.getSelectedItem()!=null  ){

            String quantity = numbersToBeDeleted.getText().toString();
            showLoadingIndicator();


            if(deleteAllOrNot.isChecked()){
                sqLiteManager.updateStock(selectedItem,0,true);
                // Simulating a delay of 2 seconds for demonstration purposes.
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        numbersToBeDeleted.setText("");
                        deleteAllOrNot.setChecked(false);
                        hideLoadingIndicator();
                        showToast("Deleted Successfully");
                    }
                }, 2000);

            }else {

                sqLiteManager.updateStock(selectedItem,Integer.parseInt(quantity),false);
                // Simulating a delay of 2 seconds for demonstration purposes.
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        numbersToBeDeleted.setText("");
                        deleteAllOrNot.setChecked(false);
                        hideLoadingIndicator();
                        showToast("Deleted Successfully");
                    }
                }, 2000);
            }
        }
        });

    }


    private boolean validateFields() {
        String productCount = numbersToBeDeleted.getText().toString().trim();
        if(!deleteAllOrNot.isChecked()){


        if (productCount.isEmpty()) {
            // Check if the field is empty
            showError(numbersToBeDeleted, "Field cannot be empty");
            return false;
        }

        if (!isInteger(productCount)) {
            // Check if quantity is an integer
            showError(numbersToBeDeleted, "Invalid input");
            return false;
        }

        }
        // All fields are valid
        return true;
    }


    private boolean isInteger(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showError(EditText editText, String errorMessage) {
        editText.setError(errorMessage);
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



}