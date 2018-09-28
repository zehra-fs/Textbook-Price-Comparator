package com.example.jnbcb.qrtextbook.listview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.jnbcb.qrtextbook.database.ApplicationDB;
import com.example.jnbcb.qrtextbook.query.Result;

import java.util.List;


public class FavoriteLoader extends AsyncTaskLoader<List<Result>> {

    private Context context;

    public FavoriteLoader(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Result> loadInBackground() {
        ApplicationDB db = ApplicationDB.getInMemoryDatabase(context);
        List<Result> results = db.resultModel().getFavoritedResults();
        Log.e("fav Loader", "" + results.size());
        return results;
    }
}
