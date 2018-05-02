package com.ansari.smartplug.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ansari.smartplug.R;
import com.ansari.smartplug.fragments.RelayPlanFragment;

/**
 * Created by Eabasir on 4/6/2016.
 */
public class ActivityRelayPlan extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{


    DrawerLayout mDrawerLayout;


    public static Intent createIntent(Context context) {
        return new Intent(context, ActivityRelayPlan.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(R.string.activity_relay_plan);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);


        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new RelayPlanFragment()).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_relay_plan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_refresh:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {


        mDrawerLayout.closeDrawers();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (menuItem.getTitle().toString().equals(getResources().getString(R.string.nav_item_sensors))) {
                    startActivity(ActivitySensors.createIntent(ActivityRelayPlan.this));
                } else if (menuItem.getTitle().toString().equals(getResources().getString(R.string.nav_item_date_time))) {
                    startActivity(ActivityDateTime.createIntent(ActivityRelayPlan.this));
                }else if (menuItem.getTitle().toString().equals(getResources().getString(R.string.nav_item_about))) {
                    startActivity(AboutViewer.createIntent(ActivityRelayPlan.this));
                }


            }
        }, 300);


        return true;
    }


}
