package com.example.jnbcb.qrtextbook.listview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.jnbcb.qrtextbook.database.ApplicationDB;
import com.example.jnbcb.qrtextbook.query.Textbook;

import java.util.List;

public class TitlesLoader extends AsyncTaskLoader<List<Textbook>> {

    private Context context;

    public TitlesLoader(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Nullable
    @Override
    public List<Textbook> loadInBackground() {
        ApplicationDB db = ApplicationDB.getInMemoryDatabase(context.getApplicationContext());
        List<Textbook> list = db.textbookModel().getAllTextbooks();
        return list;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
