package com.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AddItem extends AppCompatActivity {

    private EditText productName, quantity, unitPrice;
    private Button addItem;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        productName = findViewById(R.id.edititemname);
        quantity = findViewById(R.id.editcategory);
        unitPrice = findViewById(R.id.editprice);
        addItem = findViewById(R.id.additembuttontodatabase);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateFields()){
                    showLoadingIndicator();
                    getEnteredData();

                    // Simulating a delay of 2 seconds for demonstration purposes.
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            productName.setText("");
                            quantity.setText("");
                            unitPrice.setText("");
                            hideLoadingIndicator();
                            showToast("Product Saved Successfully");
                        }
                    }, 2000);

                }
            }
        });

        productName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                 if(hasFocus){
                     productName.setHint("");
                 }
            }
        });

        quantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    quantity.setHint("");
                }
            }
        });

        unitPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                   unitPrice.setHint("");
                }
            }
        });



    }




    private void getEnteredData() {
        String itemName = productName.getText().toString();
        int itemQuantity = Integer.parseInt(quantity.getText().toString());
        float itemPrice = Float.parseFloat(unitPrice.getText().toString());
        Phones phones = new Phones(itemName,itemQuantity,itemPrice);

        // get text from the text inputs then save it to the database
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.addStockToDatabase(phones,this);
    }



    private boolean validateFields() {
        String productNameText = productName.getText().toString().trim();
        String quantityText = quantity.getText().toString().trim();
        String unitPriceText = unitPrice.getText().toString().trim();

        if (productNameText.isEmpty() || quantityText.isEmpty() || unitPriceText.isEmpty()) {
            // Check if any field is empty
            showError(productName, "Field cannot be empty");
            showError(quantity, "Field cannot be empty");
            showError(unitPrice, "Field cannot be empty");
            return false;
        }


        if (!isInteger(quantityText)) {
            // Check if quantity is an integer
            showError(quantity, "Invalid input");
            return false;
        }

        if (!isFloatOrInteger(unitPriceText)) {
            // Check if unitPrice is a float or an integer
            showError(unitPrice, "Invalid input");
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}








