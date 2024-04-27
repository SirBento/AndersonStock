package com.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SQLiteManager extends SQLiteOpenHelper {

    private  static  SQLiteManager sqLiteManager;
    private static  final  String DATABASE_NAME = "StockDB";
    private static  final  int DATABASE_VERSION = 1;
    private static  final  String TABLE_NAME = "Stock";
    private static  final  String COUNTER = "Counter";


    private static  final  String ID_FIELD = "id";
    private static  final  String TITLE_FIELD = "title";
    private static  final  String DESC_FIELD = "desc";
    private static  final  String DELETED_FIELD = "deleted";

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

        StringBuilder sql;
        sql = new StringBuilder()
                .append(" CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ID_FIELD)
                .append(" INT,")
                .append(TITLE_FIELD)
                .append(" TEXT, ")
                .append(DESC_FIELD)
                .append(" TEXT, ")
                .append(DELETED_FIELD)
                .append(" TEXT)");
        sqLiteDatabase.execSQL(sql.toString());
    }



    /**
     *    public void onCreate(SQLiteDatabase sqLiteDatabase) {
     *     StringBuilder sql = new StringBuilder()
     *             .append("CREATE TABLE ")
     *             .append(TABLE_NAME)
     *             .append(" (")
     *             .append(COUNTER)
     *             .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
     *             .append(ITEM_ID)      ITEM_ID  =itemID
     *             .append(" TEXT, ")
     *             .append(ITEM_NAME)    ITEM_NAME= itemName
     *             .append(" TEXT, ")
     *             .append(ITEM_QUANTITY)   ITEM_QUANTITY= itemQuantity
     *             .append(" INTEGER, ")
     *             .append(ITEM_PRICE)    ITEM_PRICE= itemPrice
     *             .append(" REAL, ")
     *             .append(TOTAL_PRICE)    TOTAL_PRICE= totalPrice
     *             .append(" REAL)");
     *
     *     sqLiteDatabase.execSQL(sql.toString());
     * }
     *
     *
     *
     *
     *
     *
     *
     *
     * // Assuming you have an instance of SQLiteDatabase named 'database'
     *
     * ContentValues values = new ContentValues();
     * values.put(ITEM_ID, itemID);
     * values.put(ITEM_NAME, itemName);
     * values.put(ITEM_QUANTITY, itemQuantity);
     * values.put(ITEM_PRICE, itemPrice);
     * values.put(TOTAL_PRICE, totalPrice);
     *
     * long newRowId = database.insert(TABLE_NAME, null, values);
     *
     * if (newRowId != -1) {
     *     // Data inserted successfully
     * } else {
     *     // Failed to insert data
     * }
     *
     *
     *
     *
     *
     *
     *
     *
     * */

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public  void addStockToDatabase(Stock stock ){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, stock.getId());
        contentValues.put(TITLE_FIELD, stock.getTitle());
        contentValues.put(DESC_FIELD, stock.getDescription());
        contentValues.put(DELETED_FIELD, getStringFromDate(stock.getDeleted()));
        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
    }

    public  void populateDisplayTable(){

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        try(Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null)){

            if (result.getCount()!=0){

                while(result.moveToNext()){
                    int id = result.getInt(1);
                    String title = result.getString(2);
                    String desc = result.getString(3);
                    String stringDeleted = result.getString(4);
                    Date deleted = getDateFromString(stringDeleted);

                    Stock stock = new Stock(id,title,desc,deleted);
                    Stock stock2 = stock;
                }
            }


        }
    }


    public void updateStock( Stock stock){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, stock.getId());
        contentValues.put(TITLE_FIELD, stock.getTitle());
        contentValues.put(DESC_FIELD, stock.getDescription());
        contentValues.put(DELETED_FIELD, getStringFromDate(stock.getDeleted()));
        sqLiteDatabase.update(TABLE_NAME,null,ID_FIELD +" =? ", new String[]{String.valueOf(stock.getId())});
    }


    private String getStringFromDate(Date date) {

        if(date == null){
            return  null;
        }else{
            return dateformat.format(date);
        }
    }

    private Date getDateFromString(String string){

        try {
            return dateformat.parse(string);

        }catch (ParseException | NullPointerException e){

            return null;
        }
    }
}
