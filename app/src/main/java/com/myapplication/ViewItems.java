package com.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import java.util.HashMap;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class ViewItems extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);

        loadFromDb();
    }


    private void loadFromDb() {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        //String[][] data = sqLiteManager.populateDisplayTable();

        HashMap<String, Object> result = sqLiteManager.populateDisplayTable();
        String[][] data = (String[][]) result.get("data");
        int totalUnits = (int) result.get("totalUnits");
        double totalPrice = (double) result.get("totalPrice");



        String[] headers = {"DESCRIPTION","U/Price","QTY","$TOTAL"};
        TableView tableView = findViewById(R.id.stockTable);
        TextView totalnoitem = findViewById(R.id.totalnoitem);
        TextView totalsum = findViewById(R.id.totalsum);

        totalnoitem.setText(String.valueOf(totalUnits));
        totalsum.setText(String.valueOf(totalPrice));

        TableColumnWeightModel columnModel = new TableColumnWeightModel(4);
        columnModel.setColumnWeight(0, 3);
        //columnModel.setColumnWeight(1, 1);
        //columnModel.setColumnWeight(2, 1);
        //columnModel.setColumnWeight(3, 1);
        //columnModel.setColumnWeight(4, 1);
        tableView.setColumnModel(columnModel);
        tableView.setHeaderAdapter( new SimpleTableHeaderAdapter(this,headers));
        tableView.setDataAdapter( new SimpleTableDataAdapter(this,data));



    }
}