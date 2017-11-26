package com.example.android.varietystoreinventory;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.example.android.varietystoreinventory.ProductContract.ProductEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PRODUCT_LOADER_ID = 1;

    ProductCursorAdapter pCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up Floating Action Button to add the product
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });

         ListView productListView = findViewById(R.id.list_Products);

         View emptyView = findViewById(R.id.empty_view);
         productListView.setEmptyView(emptyView);

         pCursorAdapter = new ProductCursorAdapter(MainActivity.this, null);
         productListView.setAdapter(pCursorAdapter);

         productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                 Log.i("PRODUCT: ", ""+id);
                 Intent intent = new Intent(MainActivity.this, EditActivity.class);

                 Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);
                 intent.setData(currentProductUri);
                 startActivity(intent);

             }
         });

         //Init loader
        getLoaderManager().initLoader(PRODUCT_LOADER_ID, null, this);
    }

    private void insertDummyProduct()
    {
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Body Lotion");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 75);
        values.put(ProductEntry.COLUMN_PRODUCT_QTY, 10);
        values.put(ProductEntry.COLUMN_PRODUCT_DEALER, "Bonds");
        values.put(ProductEntry.COLUMN_PRODUCT_DEALER_EMAIL, "bonds@gmail.com");

        Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);


    }

    private Uri getDrawableResourceUri(int resource)
    {
        Uri result = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                        getResources().getResourcePackageName(resource) + "/" +
                        getResources().getResourceTypeName(resource) + "/" +
                        getResources().getResourceEntryName(resource));

        return result;
    }

    private void deleteAllProducts()
    {
        int rowsDeleted = getContentResolver().delete(ProductEntry.CONTENT_URI, null, null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_insert_dummy_product:
                insertDummyProduct();
                return true;
            case R.id.action_delete_all_product:
                deleteAllProducts();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QTY,
                ProductEntry.COLUMN_PRODUCT_DEALER,
                ProductEntry.COLUMN_PRODUCT_DEALER_EMAIL,
                ProductEntry.COLUMN_PRODUCT_IMAGE
        };

        //Thos loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,
                ProductEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        pCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        pCursorAdapter.swapCursor(null);
    }
}
