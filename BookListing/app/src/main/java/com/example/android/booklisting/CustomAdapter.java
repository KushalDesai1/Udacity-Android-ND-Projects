package com.example.android.booklisting;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import static android.R.attr.author;
import static android.R.attr.resource;

/**
 * Created by kushaldesai on 03/10/17.
 */

public class CustomAdapter extends ArrayAdapter<Book> {
    public CustomAdapter(Activity context, List<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.custom_book_layout, parent, false);
        }

        Book currentItem = getItem(position);

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.bookTitle_text_view);
        titleTextView.setText(currentItem.getBookTitle());

        List<String> bookAuthorArray = currentItem.getBookAuthor();
        String bookAuthorName = "";
        for (int i = 0; i < bookAuthorArray.size(); i++) {
            bookAuthorName = bookAuthorName + bookAuthorArray.get(i);

            if (bookAuthorArray.size() > 1 && i < bookAuthorArray.size() - 1) {
                bookAuthorName = bookAuthorName + ", ";
            }

        }

        TextView authorTextView = (TextView) listItemView.findViewById(R.id.bookAuthor_text_view);
        authorTextView.setText("Author: " + bookAuthorName);

        return listItemView;
    }
}
