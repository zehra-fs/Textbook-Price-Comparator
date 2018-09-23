package com.example.jnbcb.qrtextbook.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.jnbcb.qrtextbook.query.Result;
import com.example.jnbcb.qrtextbook.query.Textbook;

@Database(entities = {Textbook.class, Result.class}, version = 1)
public abstract class ApplicationDB extends RoomDatabase
{
    private static ApplicationDB INSTANCE;

    public abstract TextbookDAO textbookModel();
    public abstract ResultDAO resultModel();

    public static ApplicationDB getInMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.inMemoryDatabaseBuilder(context.getApplicationContext(), ApplicationDB.class)
                            //.allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
