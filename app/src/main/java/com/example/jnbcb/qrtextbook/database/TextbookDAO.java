package com.example.jnbcb.qrtextbook.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.jnbcb.qrtextbook.query.Textbook;

import java.util.List;

/**
 * This class encapsulates the queries for accessing the textbook table
 */

@Dao
public interface TextbookDAO {
    /**
     * Returns all the textbooks in the database
     *
     * @return list of all textbooks
     */
    @Query("Select * From Textbook")
    List<Textbook> getAllTextbooks();

    /**
     * Returns a textbook matching a specific isbn
     *
     * @param isbn isbn of desired textbook
     * @return a textbook
     */
    @Query("Select * FROM Textbook Where isbn = :isbn")
    Textbook getTextbook(String isbn);

    /**
     * Inserts a textbook in database
     *
     * @param textbook textbook to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTextbook(Textbook textbook);

    /**
     * Deletes a specific textbook
     *
     * @param isbn isbn of textbook to be deleted
     */
    @Query("Delete From Textbook Where isbn = :isbn")
    void deleteTextbook(String isbn);

    /**
     * Deletes all textbooks. Currently used just for testing
     */
    @Query("Delete From Textbook")
    void deleteAll();
}
