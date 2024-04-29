package com.myapplication;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.HashMap;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class DeleteItem extends AppCompatActivity {
    String selectedItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_item);
        Spinner spinner = findViewById(R.id.itemToDelete);
        CheckBox deleteAllOrNot = findViewById(R.id.deleteAll);
        EditText numbersToBeDeleted = findViewById(R.id.howManyItemsToDelete);
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
                selectedItem = "";
            }
        });

        deleteAllOrNot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked){

                    numbersToBeDeleted.setEnabled(false);
                    numbersToBeDeleted.setAlpha(0.1f);

                }else{

                    numbersToBeDeleted.setEnabled(true);
                    numbersToBeDeleted.setAlpha(1.0f);
                }

            }
        });


        deleteItemFromDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantity = numbersToBeDeleted.getText().toString();

                if(deleteAllOrNot.isChecked()){
                    sqLiteManager.updateStock(selectedItem,0,true);
                }else {
                    sqLiteManager.updateStock(selectedItem,Integer.parseInt(quantity),false);
                }
            }
        });

    }



}