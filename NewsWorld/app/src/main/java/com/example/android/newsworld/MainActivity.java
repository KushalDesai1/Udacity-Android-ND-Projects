package com.example.android.newsworld;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.icu.text.Normalizer2;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.N;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsProvider>>{

    public static String LOG_TAG = MainActivity.class.getName();
    public static final int NEWS_LOADER_ID = 1;
    public String NEWS_REQUEST_URL = "http://content.guardianapis.com/search?api-key=517ff302-cd15-4c9f-a2fa-0cffceaa61fe&page-size=10&order-by=newest&show-fields=standfirst,starRating,headline,thumbnail,short-url,lastModified,byline";

    ProgressBar progress;
    TextView emptyText;

    private List<NewsProvider> listOfNews;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private NewsAdapter recycleAdapter;
    MainActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;

        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        emptyText = (TextView) findViewById(R.id.emptyText);
        emptyText.setText("");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));

        listOfNews = new ArrayList<>();
        recycleAdapter = new NewsAdapter(listOfNews);
        recyclerView.setAdapter(recycleAdapter);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();

        boolean isNetworkConnected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

        if (isNetworkConnected == false)
        {
            progress.setVisibility(View.GONE);
            emptyText.setText("No Internet");
            emptyText.setVisibility(View.VISIBLE);
        }
        else
        {
            emptyText.setVisibility(View.GONE);
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, MainActivity.this);
        }
    }

    private void StartNewsSearch()
    {
        recycleAdapter.clearData();

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();

        boolean isNetworkConnected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

        if (isNetworkConnected == false)
        {
            progress.setVisibility(View.GONE);
            emptyText.setText("No Internet");
            emptyText.setVisibility(View.VISIBLE);
        }
        else
        {
            progress.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
            getLoaderManager().restartLoader(NEWS_LOADER_ID, null, MainActivity.this);
        }
    }

    @Override
    public Loader<List<NewsProvider>> onCreateLoader(int id, Bundle args) {
        Log.i("URL: ", NEWS_REQUEST_URL);

        Uri baseUri = Uri.parse(NEWS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("page-size", "10");
        uriBuilder.appendQueryParameter("show-fields", "standfirst,starRating,headline,thumbnail,short-url,lastModified,byline");

        return new NewsLoader(this, uriBuilder.toString() );
    }

    @Override
    public void onLoadFinished(Loader<List<NewsProvider>> loader, List<NewsProvider> newsList) {
        recycleAdapter.clearData();

        if (newsList != null && !newsList.isEmpty())
        {
            progress.setVisibility(View.GONE);
            recycleAdapter.addAll(newsList);
        }
        else
        {
            progress.setVisibility(View.GONE);
            emptyText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsProvider>> loader) {
        recycleAdapter.clearData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_Refresh:
                StartNewsSearch();
                return true;
            case R.id.action_Home:
                NEWS_REQUEST_URL = "http://content.guardianapis.com/search?api-key=517ff302-cd15-4c9f-a2fa-0cffceaa61fe&page-size=10&order-by=newest&show-fields=standfirst,starRating,headline,thumbnail,short-url,lastModified,byline";
                StartNewsSearch();
                return true;
            case R.id.action_World:
                NEWS_REQUEST_URL = "http://content.guardianapis.com/search?api-key=517ff302-cd15-4c9f-a2fa-0cffceaa61fe&section=world";
                StartNewsSearch();
                return true;
            case R.id.action_Politics:
                NEWS_REQUEST_URL = "http://content.guardianapis.com/search?api-key=517ff302-cd15-4c9f-a2fa-0cffceaa61fe&section=politics";
                StartNewsSearch();
                return true;
            case R.id.action_Technology:
                NEWS_REQUEST_URL = "http://content.guardianapis.com/search?api-key=517ff302-cd15-4c9f-a2fa-0cffceaa61fe&section=technology";
                StartNewsSearch();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
