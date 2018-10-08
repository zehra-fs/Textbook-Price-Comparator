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
        NavigationView.OnNavigationItemSelectedListener{

private static final int RC_BARCODE_CAPTURE = 9001;
private DrawerLayout drawerLayout;
        Toolbar toolbar;

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
        drawerLayout = findViewById(R.id.parent_favorite);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
    }

    public void launchBarcode(View view) {
        Intent intent = new Intent(this, BarcodeScanner.class);
        intent.putExtra(BarcodeScanner.AutoFocus, true);
        intent.putExtra(BarcodeScanner.UseFlash, false);

        startActivityForResult(intent, RC_BARCODE_CAPTURE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void launchHistory(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    public void launchFavorite(View view) {
        Intent intent = new Intent(view.getContext(), FavoriteActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId())
        {
            case R.id.home:
            {
                Intent intent = new Intent(drawerLayout.getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            }
            case R.id.barcode:
                launchBarcode(drawerLayout);
                break;
            case R.id.history_button:
                launchHistory(drawerLayout);
                break;
            case R.id.favorite_button:
                launchFavorite(drawerLayout);
                break;
        }
        drawerLayout.closeDrawers();
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
