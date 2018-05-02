package com.ansari.smartplug.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ansari.smartplug.R;
import com.ansari.smartplug.fragments.DateTimeSettingFragment;

/**
 * Created by Eabasir on 4/6/2016.
 */
public class ActivityDateTime extends AppCompatActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, ActivityDateTime.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base_layout);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_date_time_title);



        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new DateTimeSettingFragment()).commit();
    }




}
