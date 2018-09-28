package com.example.jnbcb.qrtextbook;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jnbcb.qrtextbook.listview.FavoriteAdapter;
import com.example.jnbcb.qrtextbook.listview.FavoriteLoader;
import com.example.jnbcb.qrtextbook.query.Result;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Result>> {

    private FavoriteAdapter adapter;
    @BindView(R.id.list_view_favorite)
    ListView listView;
    @BindView(R.id.empty_state_favorite)
    TextView emptyState;
    @BindView(R.id.bar_favorite)
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);
        List<Result> list = new ArrayList<>();
        adapter = new FavoriteAdapter(this, R.layout.list_item_favorite, list);
        listView.setAdapter(adapter);
        listView.setEmptyView(emptyState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Result result = adapter.getItem(position);
                Uri uri = Uri.parse(result.getUrl());
                Log.e("url", result.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(websiteIntent);
            }
        });
        getSupportLoaderManager().initLoader(0, null, this);
    }

    public void dataChanged() {
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    @NonNull
    @Override
    public Loader<List<Result>> onCreateLoader(int i, @Nullable Bundle bundle) {
        bar.setVisibility(View.VISIBLE);
        return new FavoriteLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Result>> loader, List<Result> results) {
        adapter.clear();
        bar.setVisibility(View.INVISIBLE);
        if (results.isEmpty()) {
            emptyState.setText("You have no favorites!");
        } else {
            adapter.addAll(results);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Result>> loader) {
        adapter.clear();
    }
}
