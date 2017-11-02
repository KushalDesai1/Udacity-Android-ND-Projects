package com.example.android.newsworld;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by kushaldesai on 31/10/17.
 */

public class NewsLoader extends AsyncTaskLoader<List<NewsProvider>> {

    private static final String LOG_TAG = NewsLoader.class.getName();
    private String mUrl;

    public NewsLoader(Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsProvider> loadInBackground() {
        if (mUrl == null)
        {
            return null;
        }

        List<NewsProvider> newsList = NewsQueryUtils.fetchNewsData(mUrl);
        return newsList;
    }
}
