package com.example.jnbcb.qrtextbook;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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

/**
 * This class is used to display the textbooks stored in the db
 */
public class HistoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Textbook>>,
        NavigationView.OnNavigationItemSelectedListener {

    private static final int RC_BARCODE_CAPTURE = 9001;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;


    @BindView(R.id.list_view_history)
    ListView mListView;
    @BindView(R.id.empty_state_history)
    TextView mEmptyState;
    @BindView(R.id.bar_history)
    ProgressBar mProgressBar;
    HistoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        List<Textbook> list = new ArrayList<>();
        mAdapter = new HistoryAdapter(this, R.layout.list_item_textbook, list);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mEmptyState);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Textbook textbook = mAdapter.getItem(position);
                ResultsActivity.currentTextbook = textbook;
                Intent intent = ResultsActivity.historyIntent(view.getContext(), textbook.getIsbn());
                startActivity(intent);
            }
        });
        getSupportLoaderManager().initLoader(0, null, this);

        mDrawerLayout = findViewById(R.id.parent_history);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_viewHist);
        navigationView.setNavigationItemSelectedListener(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbarHist);
        setSupportActionBar(mToolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchHistory(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    private void launchFavorite(View view) {
        Intent intent = new Intent(view.getContext(), FavoriteActivity.class);
        startActivity(intent);
    }

    private void launchBarcode(View view) {
        Intent intent = new Intent(this, BarcodeScanner.class);
        intent.putExtra(BarcodeScanner.AutoFocus, true);
        intent.putExtra(BarcodeScanner.UseFlash, false);

        startActivityForResult(intent, RC_BARCODE_CAPTURE);
    }

    private void launchHome(View view) {
        Intent intent = new Intent(mDrawerLayout.getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    private void launchTitleSearch(View view){
        Intent intent = new Intent(view.getContext(), TitleSearchActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.home: {
                launchHome(mDrawerLayout);
                break;
            }
            case R.id.barcode:
                launchBarcode(mDrawerLayout);
                break;

            case R.id.title_search:
                launchTitleSearch(mDrawerLayout);
                break;

            case R.id.history_button:
                launchHistory(mDrawerLayout);
                break;

            case R.id.favorite_button:
                launchFavorite(mDrawerLayout);
                break;
        }
        mDrawerLayout.closeDrawers();
        return true;

    }

    /**
     * Reloads the listview
     */
    public void dataChanged() {
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    @NonNull
    @Override
    public Loader<List<Textbook>> onCreateLoader(int i, @Nullable Bundle bundle) {
        mProgressBar.setVisibility(View.VISIBLE);
        Log.e("loader", "start loader");
        return new HistoryLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Textbook>> loader, List<Textbook> textbooks) {
        mAdapter.clear();
        mProgressBar.setVisibility(View.INVISIBLE);
        if (textbooks.isEmpty()) {
            mEmptyState.setText("Your history is empty!");
        } else {
            Collections.reverse(textbooks);
            mAdapter.addAll(textbooks);
        }
        Log.e("loader", "finish loader");
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Textbook>> loader) {
        mAdapter.clear();
    }
}
