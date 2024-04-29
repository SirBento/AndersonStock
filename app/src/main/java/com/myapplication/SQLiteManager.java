package com.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class SQLiteManager extends SQLiteOpenHelper {

    private  static  SQLiteManager sqLiteManager;
    private static  final  String DATABASE_NAME = "PhoneStockDB";
    private static  final  int DATABASE_VERSION = 1;
    private static  final  String TABLE_NAME = "PhoneStock";
    private static  final  String COUNTER = "Counter";

    // Table columns
    private static  final  String  ITEM_ID  = "itemID";
    private static  final  String ITEM_NAME= "itemName";
    private static  final  String ITEM_QUANTITY= "itemQuantity";
    private static  final  String ITEM_PRICE= "itemPrice";
    private static  final  String TOTAL_PRICE= "totalPrice";

    private static  final DateFormat dateformat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");


    public SQLiteManager(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteManager instanceOfDatabase(Context context){

        if(sqLiteManager == null){
            sqLiteManager = new SQLiteManager(context);
        }
        return sqLiteManager;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        StringBuilder sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append(" (")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ITEM_ID)
                .append(" TEXT, ")
                .append(ITEM_NAME)
                .append(" TEXT, ")
                .append(ITEM_QUANTITY)
                .append(" INTEGER, ")
                .append(ITEM_PRICE)
                .append(" REAL, ")
                .append(TOTAL_PRICE)
                .append(" REAL)");

        sqLiteDatabase.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public  void addStockToDatabase(Phones phones, Context context ){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_ID, phones.getItemID());
        contentValues.put(ITEM_NAME, phones.getItemName());
        contentValues.put(ITEM_QUANTITY, phones.getItemQuantity());
        contentValues.put(ITEM_PRICE, phones.getItemPrice());
        contentValues.put(TOTAL_PRICE, phones.getTotalPrice());

        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);

    }



    public HashMap<String, Object> populateDisplayTable() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        HashMap<String, Object> resultData = new HashMap<>();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null)) {
            int rowCount = result.getCount();
            int columnCount = result.getColumnCount();

            String[][] data = new String[rowCount][columnCount - 1]; // Adjust column count
            double totalPrice = 0.0;
            int totalUnits = 0;

            if (result.moveToFirst()) {
                int row = 0;
                do {
                    for (int column = 2; column < columnCount; column++) { // Start from index 1
                        if (column == columnCount - 1) {
                            double price = Double.parseDouble(result.getString(column));
                            totalPrice += price;
                        } else if (column == columnCount - 2) {
                            int units = Integer.parseInt(result.getString(column));
                            totalUnits += units;
                        }
                        data[row][column - 2] = result.getString(column); // Adjust column index
                    }
                    row++;
                } while (result.moveToNext());
            }

            resultData.put("data", data);
            resultData.put("totalPrice", totalPrice);
            resultData.put("totalUnits", totalUnits);
        }

        return resultData;
    }


    public void updateStock(String itemName, int itemQuantity, boolean deleteAll) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        if (deleteAll) {
            deleteItems(sqLiteDatabase, itemName);
        } else {
            // Retrieve the current item quantity from the database
            int currentQuantity = getCurrentQuantity(sqLiteDatabase, itemName);

            // Calculate the updated item quantity
            int updatedQuantity = currentQuantity - itemQuantity;
            if (updatedQuantity < 0) {
                updatedQuantity = 0; // Ensure the quantity doesn't go below zero
            }

            ContentValues contentValues = new ContentValues();
            contentValues.put(ITEM_QUANTITY, updatedQuantity);

            String whereClause = ITEM_NAME + " = ?";
            String[] whereArgs = { itemName };

            sqLiteDatabase.update(TABLE_NAME, contentValues, whereClause, whereArgs);
        }
    }

    private void deleteItems(SQLiteDatabase sqLiteDatabase, String itemName) {
        String whereClause = ITEM_NAME + " = ?";
        String[] whereArgs = { itemName };

        sqLiteDatabase.delete(TABLE_NAME, whereClause, whereArgs);
    }

    private int getCurrentQuantity(SQLiteDatabase sqLiteDatabase, String itemName) {
        String[] columns = { ITEM_QUANTITY };
        String selection = ITEM_NAME + " = ?";
        String[] selectionArgs = { itemName };
        String limit = "1";

        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null, limit);

        int currentQuantity = 0;
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(ITEM_QUANTITY);
            currentQuantity = cursor.getInt(columnIndex);
        }

        cursor.close();
        return currentQuantity;
    }


}
