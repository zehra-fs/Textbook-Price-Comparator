package com.example.jnbcb.qrtextbook;

import android.content.Intent;
import android.net.Uri;
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

import com.example.jnbcb.qrtextbook.listview.FavoriteAdapter;
import com.example.jnbcb.qrtextbook.listview.FavoriteLoader;
import com.example.jnbcb.qrtextbook.query.Result;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This activity is used to display the favorited results
 */
public class FavoriteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Result>>,
        NavigationView.OnNavigationItemSelectedListener {

    private static final int RC_BARCODE_CAPTURE = 9001;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;

    private FavoriteAdapter mAdapter;
    @BindView(R.id.list_view_favorite)
    ListView mListView;
    @BindView(R.id.empty_state_favorite)
    TextView mEmptyState;
    @BindView(R.id.bar_favorite)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);
        List<Result> list = new ArrayList<>();
        mAdapter = new FavoriteAdapter(this, R.layout.list_item_favorite, list);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mEmptyState);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Result result = mAdapter.getItem(position);
                Uri uri = Uri.parse(result.getUrl());
                Log.e("url", result.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(websiteIntent);
            }
        });
        getSupportLoaderManager().initLoader(0, null, this);

        mDrawerLayout = findViewById(R.id.parent_favorite);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
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
    public Loader<List<Result>> onCreateLoader(int i, @Nullable Bundle bundle) {
        mProgressBar.setVisibility(View.VISIBLE);
        return new FavoriteLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Result>> loader, List<Result> results) {
        mAdapter.clear();
        mProgressBar.setVisibility(View.INVISIBLE);
        if (results.isEmpty()) {
            mEmptyState.setText("You have no favorites!");
        } else {
            mAdapter.addAll(results);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Result>> loader) {
        mAdapter.clear();
    }
}
