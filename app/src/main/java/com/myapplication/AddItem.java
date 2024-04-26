package com.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AddItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // get text from the text inputs then save it to the database
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);

        //TODO: Change this to display saved data from the database
        Stock stock = new Stock(1,"Bento","Testing");
        sqLiteManager.addStockToDatabase(stock);

    }
}