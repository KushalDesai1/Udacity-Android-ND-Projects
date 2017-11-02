package com.example.android.booklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.empty;

public class BookListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    public static final String LOG_TAG = BookListActivity.class.getName();
    public static final String BOOK_QUERY = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final int BOOK_LOADER_ID = 1;
    public static int maxResults = 10;
    EditText etSearch;
    Button btnSearch;
    ListView bookList;
    TextView emptyText;
    CustomAdapter customAdapter;
    BookListActivity context;
    ProgressBar progress;
    private String stringURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        context = BookListActivity.this;

        emptyText = (TextView) findViewById(R.id.empty_textView);
        emptyText.setText("Enter your search");

        progress = (ProgressBar) findViewById(R.id.progressBar);

        etSearch = (EditText) findViewById(R.id.editText_Search);

        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchQuery = etSearch.getText().toString().toLowerCase();
                if (searchQuery.isEmpty()) {
                    Toast.makeText(BookListActivity.this, "Oops! No text entered", Toast.LENGTH_SHORT).show();
                } else {
                    stringURL = BOOK_QUERY + searchQuery + "&maxResults=" + String.valueOf(maxResults);
                    StartBookSearch(stringURL);
                }

            }
        });

        bookList = (ListView) findViewById(R.id.list_Book);
        customAdapter = new CustomAdapter(BookListActivity.this, new ArrayList<Book>());
        bookList.setAdapter(customAdapter);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();

        boolean isNetworkConnected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

        if (isNetworkConnected == false) {
            progress.setVisibility(View.GONE);
            emptyText.setText("Enter your search");
        } else {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        }
    }

    private void StartBookSearch(String requestUrl) {

        customAdapter.clear();

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();

        boolean isNetworkConnected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

        if (isNetworkConnected == false) {
            progress.setVisibility(View.GONE);
            emptyText.setText("No internet connection");
        } else {
            emptyText.setText("");
            progress.setVisibility(View.VISIBLE);
            getLoaderManager().restartLoader(BOOK_LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        Log.i("URL: ", stringURL);
        return new BookLoader(this, stringURL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        customAdapter.clear();

        if (books != null && !books.isEmpty()) {
            progress.setVisibility(View.GONE);
            customAdapter.addAll(books);
        }
        else {
            progress.setVisibility(View.GONE);
            emptyText.setText("Enter your search");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        customAdapter.clear();
    }
}
