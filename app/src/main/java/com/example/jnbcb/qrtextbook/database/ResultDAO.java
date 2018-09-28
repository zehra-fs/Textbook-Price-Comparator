package com.example.jnbcb.qrtextbook.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.jnbcb.qrtextbook.query.Result;

import java.util.List;

@Dao
public interface ResultDAO {
    @Query("Select * From Result Where book_isbn = :book_isbn")
    List<Result> getResults(String book_isbn);

    @Query("Select * From Result Where favorited = 1")
    List<Result> getFavoritedResults();

    @Insert()
    void insertResult(Result result);

    @Query("Delete From Result Where book_isbn = :book_isbn")
    void deleteResultsForTextbook(String book_isbn);

    @Query("Delete From Result Where favorited = 1")
    void deleteFavorited();

    @Query("Delete From Result Where id = :id")
    void deleteFavorite(int id);

    @Query("Select * From Result")
    List<Result> getAll();

    @Update()
    void updateResult(Result result);
}
