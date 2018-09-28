package com.example.jnbcb.qrtextbook;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jnbcb.qrtextbook.database.ApplicationDB;
import com.example.jnbcb.qrtextbook.listview.ResultListAdapter;
import com.example.jnbcb.qrtextbook.listview.ResultLoader;
import com.example.jnbcb.qrtextbook.query.*;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This activity displays the results of a query in a listview with the option to sort by type
 */
public class ResultsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Result>> {

    public static Textbook currentTextbook;

    private String barcode;
    private ResultListAdapter adapter;
    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.empty_state)
    TextView emptyState;
    @BindView(R.id.bar)
    ProgressBar bar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        return true;
    }


    public void sortByBuy(MenuItem item) {
        ArrayList<Result> filterResults = new ArrayList<>();
        Log.e("sort buy", true + "");
        for (Result result : currentTextbook.getResults()) {
            if (result.getType().equals("buy")) {
                filterResults.add(result);
            }
        }
        adapter.clear();
        adapter.addAll(filterResults);
    }

    public void sortByRent(MenuItem item) {
        ArrayList<Result> filterResults = new ArrayList<>();
        Log.e("sort rent", true + "");
        for (Result result : currentTextbook.getResults()) {
            if (result.getType().equals("rental")) {
                filterResults.add(result);
            }
        }
        adapter.clear();
        adapter.addAll(filterResults);
    }

    public void sortByEbook(MenuItem item) {
        ArrayList<Result> filterResults = new ArrayList<>();
        Log.e("sort ebook", true + "");
        for (Result result : currentTextbook.getResults()) {
            if (result.getType().equals("ebook")) {
                filterResults.add(result);
            }
        }
        adapter.clear();
        adapter.addAll(filterResults);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("menu item", item.getItemId() + "");
        Log.e("buy botton", "" + R.id.buy_button);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_activity);
        ButterKnife.bind(this);
        barcode = getIntent().getExtras().getString("barcode");
        List<Result> list = new ArrayList<>();
        adapter = new ResultListAdapter(this, R.layout.list_item, list);
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
        if (getIntent().getExtras().getBoolean("history")) {
            final ApplicationDB db = ApplicationDB.getInMemoryDatabase(this);
            Thread thread = new Thread() {
                public void run() {
                    bar.setVisibility(View.VISIBLE);
                    Textbook textbook = ((Textbook) getIntent().getSerializableExtra("textbook"));
                    List<Result> results = db.resultModel().getResults(textbook.getIsbn());
                    ResultsActivity.currentTextbook.setResults(results);
                    adapter.addAll(results);
                    bar.setVisibility(View.INVISIBLE);
                }
            };
            thread.start();
            return;
        }
        if (checkConnection()) {
            getSupportLoaderManager().initLoader(0, null, this); // replace this?
        } else {
            emptyState.setText("No connection"); // add string to strings.xml
        }

    }

    public void dataChanged() {
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    private boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    @NonNull
    @Override
    public Loader<List<Result>> onCreateLoader(int i, @Nullable Bundle bundle) {
        bar.setVisibility(View.VISIBLE);
        return new ResultLoader(this, barcode);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Result>> loader, List<Result> resultList) {
        adapter.clear();
        bar.setVisibility(View.INVISIBLE);
        if (!currentTextbook.isSuccess()) {
            emptyState.setText("Invalid barcode");
            return;
        }
        adapter.addAll(resultList);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Result>> loader) {
        adapter.clear();
    }


}
