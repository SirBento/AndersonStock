package com.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private CardView addItems, deleteItems, scanItems, viewInventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addItems = findViewById(R.id.addItems);
        deleteItems = findViewById(R.id.deleteItems);
        scanItems =  findViewById(R.id.scanItems);
        viewInventory =  findViewById(R.id.viewInventory);

        addItems.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this,AddItem.class);
            startActivity(i);
        });
        deleteItems.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this,DeleteItem.class);
            startActivity(i);
        });
        scanItems.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this,ViewItems.class);
            startActivity(i);
        });
        viewInventory.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this,ViewInventory.class);
            startActivity(i);
        });
    }


}