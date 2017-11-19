package com.example.android.varietystoreinventory;

import android.Manifest;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.varietystoreinventory.ProductContract.ProductEntry;

public class EditActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CURRENT_PRODUCT_LOADER_ID = 1;

    static final int IMAGE_ID = 10;
    public int b1 = 0;
    public int b2 = 0;
    public int b3 = 0;
    public int b4 = 0;
    public int b5 = 0;

    private Uri mCurrentProductUri;
    Uri imageUri;
    private boolean mProductHasChanged = false;
    private boolean mProductImageChanged = false;

    private EditText pNameEditText, pPriceEditText, pQtyEditText, pDealerEditText, pEmailEditText, pImagePathEditText;
    private Button pBrowseButton;
    private ImageView pProductImage;

    public String filepath = "";

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        if (ContextCompat.checkSelfPermission(EditActivity.this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))
            {

            }
            else
            {
                ActivityCompat.requestPermissions(EditActivity.this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},0);
            }
        }

        pNameEditText = (EditText) findViewById(R.id.edit_ProductName);
        pPriceEditText = (EditText) findViewById(R.id.edit_ProductPrice);
        pQtyEditText = (EditText) findViewById(R.id.edit_ProductQuantity);
        pDealerEditText = (EditText) findViewById(R.id.edit_ProductDealer);
        pEmailEditText = (EditText) findViewById(R.id.edit_ProductDealerEmail);
        pProductImage = (ImageView) findViewById(R.id.edit_productImage);
        pImagePathEditText = (EditText) findViewById(R.id.edit_ImgPath);
        pBrowseButton = (Button) findViewById(R.id.btn_Browse);

        pNameEditText.setOnTouchListener(mTouchListener);
        pPriceEditText.setOnTouchListener(mTouchListener);
        pQtyEditText.setOnTouchListener(mTouchListener);
        pDealerEditText.setOnTouchListener(mTouchListener);
        pEmailEditText.setOnTouchListener(mTouchListener);
        pBrowseButton.setOnTouchListener(mTouchListener);

        pBrowseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null)
                {
                    startActivityForResult(intent, IMAGE_ID);
                }
            }
        });

        String quantityString = pQtyEditText.getText().toString().trim();

        Button addQty = (Button) findViewById(R.id.btn_Plus);
        addQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!pQtyEditText.getText().toString().equals(""))
                {
                    int quantity = Integer.parseInt(pQtyEditText.getText().toString());
                    quantity = quantity + 1;
                    pQtyEditText.setText(String.valueOf(quantity));
                }
                else
                {
                    Toast.makeText(EditActivity.this, "Please enter quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button subQty = (Button) findViewById(R.id.btn_Minus);
        subQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!pQtyEditText.getText().toString().equals(""))
                {
                    int quantity = Integer.parseInt(pQtyEditText.getText().toString());
                    quantity = quantity - 1;
                    if (quantity <= 0)
                        quantity = 0;
                    pQtyEditText.setText(String.valueOf(quantity));
                }
                else
                {
                    Toast.makeText(EditActivity.this, "Please enter quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //If the intent is null, then add product and change title to 'Add a product'
        if (mCurrentProductUri == null)
        {
            setTitle("Add a Product");

            //Invalidate the options menu, so the delete button is hidden
            invalidateOptionsMenu();
        }
        else
        {
            setTitle("Edit a Product");
            mProductImageChanged = true;
            getLoaderManager().initLoader(CURRENT_PRODUCT_LOADER_ID, null, this);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == IMAGE_ID && resultCode == RESULT_OK)
        {
            imageUri = data.getData();
            pProductImage.setImageURI(Uri.parse(String.valueOf(imageUri)));

            mProductImageChanged = true;

            String wholeID = DocumentsContract.getDocumentId(Uri.parse(String.valueOf(imageUri)));

            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = getContentResolver().
                    query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel,
                            new String[]{id}, null);

            filepath = "";
            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst())
            {
                filepath = cursor.getString(columnIndex);
            }

            pImagePathEditText.setText(filepath);
            cursor.close();

        }
        else
        {
            mProductImageChanged = false;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveProduct()
    {
        String strName = pNameEditText.getText().toString().trim();
        String strPrice = pPriceEditText.getText().toString().trim();
        String strQty = pQtyEditText.getText().toString().trim();
        String strDealer = pDealerEditText.getText().toString().trim();
        String strEmail = pEmailEditText.getText().toString().trim();
        String imagePathString = "";
        String imageUriString = "";
        if (imageUri != null)
        {
            imageUriString = imageUri.toString();
            imagePathString = pImagePathEditText.getText().toString().toString();
        }

        if (TextUtils.isEmpty(strName))
        {
            Log.i("B1", "1");
            b1 = 1;
        }

        if (TextUtils.isEmpty(strPrice))
        {
            Log.i("B2", "1");
            b2 = 1;
        }

        if (TextUtils.isEmpty(strQty))
        {
            Log.i("B3", "1");
            b3 = 1;
        }

        if (TextUtils.isEmpty(strDealer))
        {
            Log.i("B4", "1");
            b4 = 1;
        }

        if (TextUtils.isEmpty(strEmail))
        {
            Log.i("B5", "1");
            b5 = 1;
        }

        if (mCurrentProductUri == null && b1 == 1 && b2 == 1 && b3 == 1 && b4 == 1 && b5 == 1)
        {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, strName);
        values.put(ProductEntry.COLUMN_PRODUCT_DEALER, strDealer);
        values.put(ProductEntry.COLUMN_PRODUCT_DEALER_EMAIL, strEmail);

        //If price or quantity is not provided, use 0 by default.
        int quantity = 0;
        int price = 0;

        if (!TextUtils.isEmpty(strQty))
        {
            quantity = Integer.parseInt(strQty);
        }

        if (!TextUtils.isEmpty(strPrice))
        {
            price = Integer.parseInt(strPrice);
        }

        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, price);
        values.put(ProductEntry.COLUMN_PRODUCT_QTY, quantity);
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE, imageUriString);
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE_PATH, imagePathString);

        if (mCurrentProductUri == null)
        {
            //New product so add it.
            Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);

            if (newUri == null)
            {
                Toast.makeText(this, "Failed to insert the Product", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            //Existing product so edit it.
            int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);

            if (rowsAffected == 0)
            {
                //No rows affected
                Toast.makeText(this, "Error with updating product", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Product Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Boolean checkForErrors()
    {
        b1 = 0;
        b2 = 0;
        b3 = 0;
        b4 = 0;
        b5 = 0;

        String nameString = pNameEditText.getText().toString().trim();
        String priceString = pPriceEditText.getText().toString().trim();
        String qtyString = pQtyEditText.getText().toString().trim();
        String dealerString = pDealerEditText.getText().toString().trim();
        String dealerEmail = pEmailEditText.getText().toString().trim();


        if (!pNameEditText.getText().toString().equals(""))
        {
            if (!nameString.matches("^[a-zA-Z\\s]*$"))
            {
                pNameEditText.setError("Invalid Product Name");
                b1 = 1;
            }
        }
        else
        {
            pNameEditText.setError("Enter Product Name");
            b1 = 1;
        }

        if (TextUtils.isEmpty(priceString))
        {
            pPriceEditText.setError("Enter Product Price");
            b2 = 1;
        }

        if (TextUtils.isEmpty(qtyString))
        {
            pQtyEditText.setError("Enter Product Quantity");
            b3 = 1;
        }

        if (!pDealerEditText.getText().toString().equals(""))
        {
            if (!dealerString.matches("^[a-zA-Z\\s]*$"))
            {
                pDealerEditText.setError("Invalid Dealer Name");
                b4 = 1;
            }
        }
        else
        {
            pDealerEditText.setError("Enter Dealer Name");
            b4 = 1;
        }

        if (TextUtils.isEmpty(dealerEmail))
        {
            pEmailEditText.setError("Enter Dealer Email");
            b5 = 1;
        }


        if (mProductImageChanged == false)
        {
            Toast.makeText(this, "Please select image\nfor the product", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (b1 == 1 || b2 == 1 || b3 == 1 || b4 == 1 || b5 == 1)
            return false;
        else
            return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mCurrentProductUri == null)
        {
            MenuItem menuItemDelete = menu.findItem(R.id.action_delete_product);
            MenuItem menuItemOrder = menu.findItem(R.id.action_order_product);
            menuItemDelete.setVisible(false);
            menuItemOrder.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_save_product:
                if (checkForErrors())
                {
                    Log.i("Check for errors:","DONE");
                    saveProduct();
                    Log.i("SAVE PRODUCT:","DONE");
                    finish();
                }
                return true;
            case R.id.action_delete_product:
                showDeleteConfirmationDialog();
                return true;
            case R.id.action_order_product:
                String productName = pNameEditText.getText().toString().trim();
                String productPrice = pPriceEditText.getText().toString().trim();
                String productQty = pQtyEditText.getText().toString().trim();
                String dealerEmail = pEmailEditText.getText().toString().trim();

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + dealerEmail));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Product Order");
                intent.putExtra(Intent.EXTRA_TEXT, "Kushal's Shop\nPlease take the order for the Product" +
                        "\nProduct Name: " + productName +
                        "\nQuantity: " + productQty +
                        "\nPrice: " + productPrice +
                        "\nTotal Price: " + (Integer.parseInt(productQty) * Integer.parseInt(productPrice)));

                if (intent.resolveActivity(getPackageManager()) != null)
                {
                    startActivity(intent);
                }

                return true;

            case android.R.id.home:
                if (!mProductHasChanged)
                {
                    NavUtils.navigateUpFromSameTask(EditActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClicklistener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditActivity.this);
                            }
                        };

                //Dialog for unsaved changes
                showUnsavedChangesDialog(discardButtonClicklistener);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mProductHasChanged)
        {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        //Show dialog about unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard your changes and quit editing?");
        builder.setPositiveButton("Discard", discardButtonClickListener);
        builder.setNegativeButton("Keep Editing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (dialog != null)
                {
                    dialog.dismiss();
                }
            }
        });

        //Create and show the alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this Product");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                deleteProduct();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (dialog != null)
                {
                    dialog.dismiss();
                }
            }
        });

        //Create and show hte dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteProduct()
    {
        if (mCurrentProductUri != null)
        {
            int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);

            if (rowsDeleted == 0)
            {
                Toast.makeText(this, "Error Deleting Product", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Product Deleted", Toast.LENGTH_SHORT).show();
            }
        }

        //Close the activity
        finish();
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
                ProductEntry.COLUMN_PRODUCT_IMAGE,
                ProductEntry.COLUMN_PRODUCT_IMAGE_PATH};

        return new CursorLoader(this,
                mCurrentProductUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.getCount() < 1)
        {
            return;
        }

        if (cursor.moveToFirst())
        {
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int priceCoulmnindex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int qtyColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QTY);
            int dealerColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_DEALER);
            int emailColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_DEALER_EMAIL);
            int imagePathColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_IMAGE_PATH);

            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceCoulmnindex);
            int qty = cursor.getInt(qtyColumnIndex);
            String dealer = cursor.getString(dealerColumnIndex);
            String email = cursor.getString(emailColumnIndex);
            String imagePath = cursor.getString(imagePathColumnIndex);

            pNameEditText.setText(name);
            pPriceEditText.setText(String.valueOf(price));
            pQtyEditText.setText(String.valueOf(qty));
            pDealerEditText.setText(dealer);
            pEmailEditText.setText(email);

            String strImageURI = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_IMAGE));
            if (strImageURI.startsWith("content://com.android.providers.media.documents/document/image"))
            {
                Log.i("IMAGE DATA 1","FLEW AWAY " + strImageURI);
                pProductImage.setImageURI(Uri.parse(strImageURI));
                pImagePathEditText.setText(imagePath);
            }
            else
            {
                Log.i("IMAGE DATA 2","FLEW AWAY " + strImageURI);
                pProductImage.setImageResource(R.drawable.demo_product);
                pImagePathEditText.setText("");

            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        pNameEditText.setText("");
        pPriceEditText.setText("");
        pQtyEditText.setText("");
        pDealerEditText.setText("");
        pEmailEditText.setText("");
        pImagePathEditText.setText("");
    }
}
