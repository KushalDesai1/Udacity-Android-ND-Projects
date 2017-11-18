package com.example.android.varietystoreinventory;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by kushaldesai on 16/11/17.
 */

public class ProductContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.varietystoreinventory";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PRODUCTS = "productinventory";

    public ProductContract() {
    }

    public static class ProductEntry implements BaseColumns
    {

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);

        public static final String TABLE_NAME = "productinventory";

        //Table Columns
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "productName";
        public static final String COLUMN_PRODUCT_QTY = "productQuantity";
        public static final String COLUMN_PRODUCT_PRICE = "productPrice";
        public static final String COLUMN_PRODUCT_IMAGE = "productImage";
        public static final String COLUMN_PRODUCT_IMAGE_PATH = "productImagePath";
        public static final String COLUMN_PRODUCT_DEALER = "productDealer";
        public static final String COLUMN_PRODUCT_DEALER_EMAIL = "productDealerEmail";


    }
}
