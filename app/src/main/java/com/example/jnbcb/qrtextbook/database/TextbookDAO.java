package com.example.jnbcb.qrtextbook.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.jnbcb.qrtextbook.query.Textbook;

import java.util.List;

@Dao
public interface TextbookDAO {
    @Query("Select * From Textbook")
    List<Textbook> getAllTextbooks();

    @Query("Select * FROM Textbook Where isbn = :isbn")
    Textbook getTextbook(String isbn);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTextbook(Textbook textbook);

    @Query("Delete From Textbook Where isbn = :isbn")
    void deleteTextbook(String isbn);

    @Query("Delete From Textbook")
    void deleteAll();
}
