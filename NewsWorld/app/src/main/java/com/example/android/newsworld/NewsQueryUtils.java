package com.example.android.newsworld;

import android.text.Html;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.newsworld.MainActivity.LOG_TAG;

/**
 * Created by kushaldesai on 31/10/17.
 */

public final class NewsQueryUtils {

    public NewsQueryUtils() {
    }

    public static List<NewsProvider> fetchNewsData(String requestUrl)
    {
        URL url = null;

        try {
            url = new URL(requestUrl);
        }
        catch(MalformedURLException e)
        {
            Log.e(LOG_TAG, "Error with creating URL",e);
        }

        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
            Log.i("JSON: ", jsonResponse);
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG, "Errow while fetching jsonresponse",e);
            e.printStackTrace();
        }

        List<NewsProvider> newsList = extractFeatureFromJson(jsonResponse);
        return newsList;

    }

    private static String makeHttpRequest(URL url) throws IOException
    {
        String jsonResponse = "";

        if (url == null)
        {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try
        {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200)
            {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG, "Error while making HTTP Request", e);
        }
        finally {
            if (inputStream != null)
            {
                inputStream.close();
            }

            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException
    {
        StringBuilder result = new StringBuilder();

        if (inputStream != null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while (line != null)
            {
                result.append(line);
                line = reader.readLine();
            }
        }

        return result.toString();
    }

    private static List<NewsProvider> extractFeatureFromJson(String jsonResponse)
    {
        if (jsonResponse.isEmpty())
        {
            return null;
        }

        List<NewsProvider> newsList = new ArrayList<>();

        try
        {
            JSONObject baseObject = new JSONObject(jsonResponse);

            JSONObject newsResponse = baseObject.getJSONObject("response");

            JSONArray newsArray = newsResponse.getJSONArray("results");

            for (int i = 0; i < newsArray.length(); i++)
            {
                JSONObject currentNews = newsArray.getJSONObject(i);

                String headLine, content = "No content", date, url, imageUrl="https://placeholdit.imgix.net/~text?txtsize=26&txt=No+preview&w=350&h=215", section, contributor = "No Info";

                JSONObject newsDetails = currentNews.getJSONObject("fields");

                headLine = newsDetails.getString("headline");

                if (newsDetails.has("standfirst"))
                {
                    content = newsDetails.getString("standfirst");
                    content = Html.fromHtml(content).toString();
                }

                date = newsDetails.getString("lastModified");
                date = date.substring(0,10);
                date = "Date: "+date;

                url = newsDetails.getString("shortUrl");

                if (newsDetails.has("thumbnail")) {
                    imageUrl = newsDetails.getString("thumbnail");
                }

                if (newsDetails.has("byline")) {
                    contributor = newsDetails.getString("byline");
                }

                section = currentNews.getString("sectionName");
                section = "In "+section;

                NewsProvider news = new NewsProvider(headLine,content,date,url,imageUrl,contributor,section);
                newsList.add(news);

            }
        }
        catch(JSONException e)
        {
            Log.e(LOG_TAG, "Error while parsing JSON", e);
            e.printStackTrace();
        }

        return newsList;
    }
}
