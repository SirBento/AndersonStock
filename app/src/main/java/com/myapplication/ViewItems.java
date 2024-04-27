package com.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class ViewItems extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);

        TableView tableView = findViewById(R.id.stockTable);
        String[] headers = {"ID","DESC","U/PRICE","QTY","$TOTAL"};
        String[][] data = {{"1","Nokia","10","5","50"}, {"2","GTEL","15","5","75"}};

        tableView.setHeaderAdapter( new SimpleTableHeaderAdapter(this,headers));
        tableView.setDataAdapter( new SimpleTableDataAdapter(this,data));
    }
}