package com.example.android.booklisting;

import java.util.List;

/**
 * Created by kushaldesai on 01/10/17.
 */

public class Book {

    private String bookTitle;
    private List<String> bookAuthor;
    private float bookPrice;

    public Book(String bookTitle, List<String> bookAuthor) {
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public List<String> getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(List<String> bookAuthor) {
        this.bookAuthor = bookAuthor;
    }
}
