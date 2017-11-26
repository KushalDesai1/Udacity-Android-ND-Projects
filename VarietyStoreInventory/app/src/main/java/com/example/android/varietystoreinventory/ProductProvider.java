package com.example.android.varietystoreinventory;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.CursorAdapter;

import com.example.android.varietystoreinventory.ProductContract.ProductEntry;
/**
 * Created by kushaldesai on 17/11/17.
 */

public class ProductProvider extends ContentProvider {

    /** URI Matcher code for the content URI for the products table */
    private static final int PRODUCT = 100;

    /** URI Matcher code for the content URI for a single product in the products table */
    private static final int PRODUCT_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCTS, PRODUCT);
        sUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCTS + "/#", PRODUCT_ID);
    }

    /** Database helper object */
    private ProductDBHelper productDBHelper;

    @Override
    public boolean onCreate() {

        productDBHelper = new ProductDBHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        SQLiteDatabase database = productDBHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match)
        {
            case PRODUCT:
                cursor = database.query(ProductEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(ProductEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
                default:
                    throw new IllegalArgumentException("Unable to query on unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case PRODUCT:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertProduct(Uri uri, ContentValues values)
    {
        String name = values.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
        if (name == null)
        {
            throw new IllegalArgumentException("Product name is required");
        }

        Integer price = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_PRICE);
        if (price != null)
        {
            if (price < 0)
            {
                throw new IllegalArgumentException("Product requires valid price");
            }
        }
        else
        {
            throw new IllegalArgumentException("Product price is required");
        }

        Integer quantity = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_QTY);
        if (quantity == null)
        {
            throw new IllegalArgumentException("Product quantity is required");
        }

        String dealerName = values.getAsString(ProductEntry.COLUMN_PRODUCT_DEALER);
        if (dealerName == null)
        {
            throw new IllegalArgumentException("Dealer name is required");
        }

        String dealerEmail = values.getAsString(ProductEntry.COLUMN_PRODUCT_DEALER_EMAIL);
        if (dealerEmail == null)
        {
            throw new IllegalArgumentException("Dealer email is required");
        }

        Log.d("NAME: ", name);
        Log.d("PRICE: ", ""+price);
        Log.d("QUANTITY: ", ""+quantity);
        Log.d("DEALER NAME: ", dealerName);
        Log.d("DEALER EMAIL: ", dealerEmail);

        //Get writable database
        SQLiteDatabase database = productDBHelper.getWritableDatabase();

        //Insert the new content with content values
        long id = database.insert(ProductEntry.TABLE_NAME, null, values);

        if (id == -1)
        {
            Log.i("Error:", "Insertion failed for " + uri);
            return  null;
        }

        //Notify all listeners that the data has changed for the product content uri
        getContext().getContentResolver().notifyChange(uri, null);

        //Return the URI of the newly inserted row
        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);

        switch (match)
        {
            case PRODUCT:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updateProduct(uri, contentValues, selection, selectionArgs);
                default:
                    throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {

        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_NAME))
        {
            String name = values.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
            if (name == null)
            {
                throw new IllegalArgumentException("Product name is required");
            }
        }

        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_QTY))
        {
            Integer quantity = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_QTY);
            if (quantity == null)
            {
                throw new IllegalArgumentException("Product quantity is required");
            }
        }

        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_PRICE))
        {
            Integer price = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_PRICE);
            if (price == null)
            {
                throw new IllegalArgumentException("Product price is required");
            }
        }

        if (values.size() == 0)
        {
            return 0;
        }

        SQLiteDatabase database = productDBHelper.getWritableDatabase();

        int rowsUpdated = database.update(ProductEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        if (rowsUpdated != 0)
        {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase database = productDBHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match)
        {
            case PRODUCT:
                rowsDeleted = database.delete(ProductEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};

                rowsDeleted = database.delete(ProductEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Delete is not supported for " + uri);
        }

        if (rowsDeleted != 0)
        {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match)
        {
            case PRODUCT:
                return ProductEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return ProductEntry.CONTENT_ITEM_TYPE;
                default:
                    throw new IllegalStateException("Unknown URI " + uri);
        }
    }
}
