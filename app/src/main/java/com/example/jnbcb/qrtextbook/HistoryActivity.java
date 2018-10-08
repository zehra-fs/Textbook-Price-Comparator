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
        NavigationView.OnNavigationItemSelectedListener{

private static final int RC_BARCODE_CAPTURE = 9001;
private DrawerLayout drawerLayout;
        Toolbar toolbar;


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

        drawerLayout = findViewById(R.id.parent_history);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_viewHist);
        navigationView.setNavigationItemSelectedListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbarHist);
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
