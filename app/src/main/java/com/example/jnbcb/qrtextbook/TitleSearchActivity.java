package com.example.jnbcb.qrtextbook;

import android.content.Intent;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.jnbcb.qrtextbook.query.DirectTextbook;
import com.example.jnbcb.qrtextbook.query.Textbook;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

public class TitleSearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int RC_BARCODE_CAPTURE = 9001;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private String bookTitle;
    private List<Textbook> textbookTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_search);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button titleSearchBtn = findViewById(R.id.titleSearchBtn);
        titleSearchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                //queryForBook();
                Intent intent = new Intent(TitleSearchActivity.this, TitlesActivity.class);
                startActivity(intent);
            }
        });
//        // Verify the action and get the query
//        if (Intent.ACTION_SEARCH == intent.action)
//        {
//            intent.getStringExtra(SearchManager.QUERY)?.also {
//            query ->
//                    //doMySearch(query)
//                    queryForBook(query);
//             }
//        }


        mDrawerLayout = findViewById(R.id.drawer_titleSearch);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_viewTitleSearch);
        navigationView.setNavigationItemSelectedListener(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbarTS);
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

    private void launchTitleSearch(View view) {
        Intent intent = new Intent(view.getContext(), TitleSearchActivity.class);
        startActivity(intent);
    }

    private void launchTitlesActivity(View view)
    {
        Intent intent = new Intent(view.getContext(), TitlesActivity.class);
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

  /*  public void queryForBook() {
        Log.i("SearchTitle", "Search Btn clicked");
        EditText enterTitle = (EditText)findViewById(R.id.enterTitle);
        bookTitle = enterTitle.getText().toString();
        bookTitle = bookTitle.replaceAll("\\s", "+");
        Log.i("SearchTitle", bookTitle);
        try {
            textbookTitles = DirectTextbook.queryTitle(bookTitle);
        } catch (SAXException e) {
            Log.e("ResultLoader SAX", e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("ResultLoader IO", e.getMessage());
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            Log.e("ResultLoader Parser", e.getMessage());
            e.printStackTrace();
        }
        System.out.println(textbookTitles);
        // Log.i("SearchTitle", textbookTitles.toString());
    }*/


}
