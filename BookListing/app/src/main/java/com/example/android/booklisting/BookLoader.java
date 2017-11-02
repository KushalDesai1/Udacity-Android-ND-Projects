package com.example.android.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by kushaldesai on 03/10/17.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private static final String LOG_TAG = BookLoader.class.getName();

    private String mUrl;

    public BookLoader(Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if(mUrl == null)
        {
            return null;
        }

        Log.i("LoadInBackground", "STARTS");

        List<Book> bookList = BookQueryUtils.fetchBookData(mUrl);
        return bookList;
    }
}
