package com.example.android.varietystoreinventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.varietystoreinventory.ProductContract.ProductEntry;

/**
 * Created by kushaldesai on 17/11/17.
 */

public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView txtName = (TextView) view.findViewById(R.id.list_Product);
        TextView txtQty = (TextView) view.findViewById(R.id.list_Quantity);
        TextView txtPrice = (TextView) view.findViewById(R.id.list_Price);
        TextView btnSale = (TextView) view.findViewById(R.id.list_btnSale);
        ImageView imgProduct = (ImageView) view.findViewById(R.id.list_imgThumb);

        int nameColumnindex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int qtyColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QTY);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int idColumnIndex = cursor.getColumnIndex(ProductEntry._ID);

        String name = cursor.getString(nameColumnindex);
        final String qty = cursor.getString(qtyColumnIndex);
        String price = cursor.getString(priceColumnIndex);
        final String productID = cursor.getString(idColumnIndex);
        String productImageUri = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_IMAGE));

        if (productImageUri.startsWith("content://com.android.providers.media.documents/document/image"))
        {
            imgProduct.setImageURI(Uri.parse(productImageUri));
        }
        else
        {
            imgProduct.setImageResource(R.drawable.demo_product);
        }

        btnSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ProductCursorAdapter", "Sale button clicked");
                int quantity = Integer.parseInt(qty);

                if (quantity <= 0)
                {
                    Toast.makeText(context, "Out of Stock", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    quantity = quantity - 1;
                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_PRODUCT_QTY, quantity);
                    Uri currentUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, Long.parseLong(productID));
                    context.getContentResolver().update(currentUri, values, null, null);
                }
            }
        });

        txtName.setText(name);
        txtPrice.setText(price);
        txtQty.setText(qty);
    }
}
