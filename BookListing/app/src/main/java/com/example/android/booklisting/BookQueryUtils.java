package com.example.android.booklisting;

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

import static com.example.android.booklisting.BookListActivity.LOG_TAG;
/**
 * Created by kushaldesai on 01/10/17.
 */

public final class BookQueryUtils {

    private BookQueryUtils() {}

    public static List<Book> fetchBookData(String requestUrl)
    {
        //Create a url object
        URL url = createUrl(requestUrl);

        //Perform HTTP Request to the URL and receive the JSON response back
        String jsonresponse = "";

        try
        {
            jsonresponse = makeHttpRequest(url);
            Log.i("JSON: ", jsonresponse);
        }
        catch (IOException exception)
        {
            Log.e(LOG_TAG, "Error making the HTTP request", exception);
        }

        List<Book> books = extractFeatureFronJSON(jsonresponse);

        return books;
    }

    private static URL createUrl(String requestUrl)
    {
        URL url = null;
        try
        {
            url = new URL(requestUrl);
        }
        catch (MalformedURLException exception)
        {
            Log.e(LOG_TAG, "Error with creating URL");
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException
    {
        String jsonresponse = "";

        if (url == null)
        {
            return jsonresponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try
        {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(10000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200)
            {
                inputStream = urlConnection.getInputStream();
                jsonresponse = readFromStream(inputStream);
            }
        }
        catch (IOException exception)
        {
            Log.e(LOG_TAG, "Error with making URL request: ", exception);
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

        return jsonresponse;

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

    private static List<Book> extractFeatureFronJSON(String jsonresponse)
    {
        if (jsonresponse.isEmpty())
        {
            return null;
        }

        List<Book> books = new ArrayList<>();

        try {

            JSONObject jsonObject = new JSONObject(jsonresponse);

            JSONArray bookArray = jsonObject.optJSONArray("items");

            for (int i = 0; i < bookArray.length(); i++)
            {
                JSONObject c = bookArray.optJSONObject(i);

                JSONObject volumeInfo = c.optJSONObject("volumeInfo");
                String bookTitle = volumeInfo.getString("title");

                JSONArray authors = volumeInfo.optJSONArray("authors");

                List<String> bookAuthors = new ArrayList<String>();

                Log.i("Authors:", ""+authors.length());
                for (int j = 0; j < authors.length() ; j++)
                {
                    bookAuthors.add(authors.getString(j));
                }

                books.add(new Book(bookTitle, bookAuthors));
            }
        }
        catch (JSONException exception)
        {
            Log.e("BookQueryUtils", "Problem parsing the earthquake JSON results", exception);
        }
        catch (NullPointerException exception)
        {
            Log.e("BookQueryUtils", "Null Pointer Exception: " + exception.toString());
        }

        return books;
    }
}
