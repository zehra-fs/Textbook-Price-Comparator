package com.example.jnbcb.qrtextbook.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.jnbcb.qrtextbook.query.Result;

import java.util.List;

/**
 * This class encapsulates the queries for accessing the results table
 */

@Dao
public interface ResultDAO {

    /**
     * Returns a List of the results of a specific textbook
     *
     * @param book_isbn isbn of the book that the desired results belong to
     * @return list of the results for a book
     */
    @Query("Select * From Result Where book_isbn = :book_isbn")
    List<Result> getResults(String book_isbn);

    /**
     * Gets favorited results from database
     *
     * @return list of favorited results
     */
    @Query("Select * From Result Where favorited = 1")
    List<Result> getFavoritedResults();

    /**
     * Inserts a result into the database
     *
     * @param result result to be inserted
     */
    @Insert()
    void insertResult(Result result);

    // These queries are currently unused.
//    @Query("Delete From Result Where book_isbn = :book_isbn")
//    void deleteResultsForTextbook(String book_isbn);
//
//    @Query("Delete From Result Where favorited = 1")
//    void deleteFavorited();
//
//    @Query("Delete From Result Where id = :id")
//    void deleteFavorite(int id);
//
//    @Query("Select * From Result")
//    List<Result> getAll();

    /**
     * Updates a result in the database. IDs must match.
     *
     * @param result result to be updated
     */
    @Update()
    void updateResult(Result result);
}
