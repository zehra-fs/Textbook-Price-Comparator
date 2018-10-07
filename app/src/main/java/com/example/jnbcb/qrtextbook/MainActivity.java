package com.example.jnbcb.qrtextbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

/**
 * This is the home activity of the app
 */
public class MainActivity extends AppCompatActivity {

    private static final int RC_BARCODE_CAPTURE = 9001;
    private DrawerLayout drawerLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

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
}
