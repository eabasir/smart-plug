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
import android.view.View;

import com.ansari.smartplug.R;
import com.ansari.smartplug.fragments.RelayDetailFragment;
import com.ansari.smartplug.fragments.SensorFragment;

/**
 * Created by Eabasir on 4/6/2016.
 */
public class ActivityRelayDetail extends AppCompatActivity  {


    public static Intent createNewIntent(Context context) {
        return new Intent(context, ActivityRelayDetail.class);
    }

    public static Intent createNamedIntent(Context context, String name) {

        Intent i = new Intent(context, ActivityRelayDetail.class);
        i.putExtra("Name", name);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        String userName;

        String name = "New Plan";
        if (extras != null) {
            name = extras.getString("Name");
        }

        getSupportActionBar().setTitle(name);


        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new RelayDetailFragment()).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_relay_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
              case R.id.action_set:
                  setRelayData();

                  return true;

        }


        return super.onOptionsItemSelected(item);
    }

    private void setRelayData()
    {
        ((RelayDetailFragment) getFragmentManager().findFragmentById(R.id.fragment_container)).setRelayData();
    }



}
