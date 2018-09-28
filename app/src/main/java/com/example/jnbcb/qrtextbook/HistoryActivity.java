package com.example.jnbcb.qrtextbook;


import android.content.Intent;
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

import com.example.jnbcb.qrtextbook.listview.HistoryAdapter;
import com.example.jnbcb.qrtextbook.listview.HistoryLoader;
import com.example.jnbcb.qrtextbook.query.Textbook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Textbook>> {

    @BindView(R.id.list_view_history)
    ListView listView;
    @BindView(R.id.empty_state_history)
    TextView emptyState;
    @BindView(R.id.bar_history)
    ProgressBar bar;
    HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        List<Textbook> list = new ArrayList<>();
        adapter = new HistoryAdapter(this, R.layout.list_item_textbook, list);
        listView.setAdapter(adapter);
        listView.setEmptyView(emptyState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Textbook textbook = adapter.getItem(position);
                ResultsActivity.currentTextbook = textbook;
                Intent intent = new Intent(view.getContext(), ResultsActivity.class);
                intent.putExtra("history", true);
                intent.putExtra("textbook", textbook);
                intent.putExtra("barcode", textbook.getIsbn());
                startActivity(intent);
            }
        });
        getSupportLoaderManager().initLoader(0, null, this);
    }

    public void dataChanged() {
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    @NonNull
    @Override
    public Loader<List<Textbook>> onCreateLoader(int i, @Nullable Bundle bundle) {
        bar.setVisibility(View.VISIBLE);
        Log.e("loader", "start loader");
        return new HistoryLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Textbook>> loader, List<Textbook> textbooks) {
        adapter.clear();
        bar.setVisibility(View.INVISIBLE);
        if (textbooks.isEmpty()) {
            emptyState.setText("Your history is empty!");
        } else {
            Collections.reverse(textbooks);
            adapter.addAll(textbooks);
        }
        Log.e("loader", "finish loader");
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Textbook>> loader) {
        adapter.clear();
    }
}
