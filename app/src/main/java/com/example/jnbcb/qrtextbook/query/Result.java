package com.example.jnbcb.qrtextbook.query;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

import android.support.annotation.NonNull;

/**
 * This class contains the individual vendor information for each Textbook. It acts as an entity object in the DAO DB
 */
@Entity(foreignKeys = {
        @ForeignKey(entity = Textbook.class,
                parentColumns = "isbn",
                childColumns = "book_isbn",
                onDelete = CASCADE)})

public class Result {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String url; // url of website to buy
    private String companyName; // EX. Amazon
    private float price; // price
    private String type; // ebook, rental, buy
    private String condition; // condition of book, new or used
    @ColumnInfo(name = "book_isbn")
    private String bookIsbn;
    private boolean favorited; // for use in DB when selecting favorited results

    public Result(String url, String companyName, float price, String type, String condition, String bookIsbn) {
        this.url = url;
        this.companyName = companyName;
        this.price = price;
        this.type = type;
        this.condition = condition;
        this.bookIsbn = bookIsbn;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    @Override
    public String toString() {
        return companyName + " Price: " + price + " Type: " + type + " Condition: " + condition;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }
}
