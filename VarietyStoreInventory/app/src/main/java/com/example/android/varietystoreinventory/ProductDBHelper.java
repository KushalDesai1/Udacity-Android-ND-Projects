package com.example.android.varietystoreinventory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.varietystoreinventory.ProductContract.ProductEntry;

/**
 * Created by kushaldesai on 16/11/17.
 */

public class ProductDBHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = ProductDBHelper.class.getName();

    /** Name of the database */
    public static final String DATABASE_NAME = "ProductInventory.db";
    public static final int DATABASE_VERSION = 1;

    public ProductDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_PRODUCT_TABLE = "CREATE TABLE " + ProductEntry.TABLE_NAME + " ("
                + ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL,"
                + ProductEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL,"
                + ProductEntry.COLUMN_PRODUCT_QTY + " INTEGER NOT NULL DEFAULT 0,"
                + ProductEntry.COLUMN_PRODUCT_DEALER + " TEXT NOT NULL,"
                + ProductEntry.COLUMN_PRODUCT_DEALER_EMAIL + " TEXT NOT NULL,"
                + ProductEntry.COLUMN_PRODUCT_IMAGE + " TEXT NOT NULL,"
                + ProductEntry.COLUMN_PRODUCT_IMAGE_PATH + " TEXT NOT NULL);";

        Log.i(LOG_TAG, SQL_CREATE_PRODUCT_TABLE);
        db.execSQL(SQL_CREATE_PRODUCT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
