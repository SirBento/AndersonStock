package com.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ViewInventory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);
        loadFromDb();
    }

    private void loadFromDb() {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);

        //TODO: Change this to display saved data from the database
        sqLiteManager.populateDisplayTable();

    }
}