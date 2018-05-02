package com.ansari.smartplug.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ansari.smartplug.R;
import com.ansari.smartplug.activities.ActivityDateTime;
import com.ansari.smartplug.activities.ActivityRelayDetail;
import com.ansari.smartplug.activities.ActivityRelayPlan;
import com.ansari.smartplug.activities.App;
import com.ansari.smartplug.adapter.AdapterRelayPlan;
import com.ansari.smartplug.interfaces.OnDeviceDataReadListener;
import com.ansari.smartplug.network.AsyncClient;
import com.ansari.smartplug.network.NetConfig;
import com.ansari.smartplug.setting.AppSettings;
import com.ansari.smartplug.utils.Tools;
import com.melnykov.fab.FloatingActionButton;

import pl.pawelkleczkowski.customgauge.CustomGauge;


public class RelayPlanFragment extends Fragment implements AdapterView.OnItemClickListener {



    View view;
    ListView listView;
    AdapterRelayPlan adapter;

    String[] array_data = {"Plan 1"};



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_relay_plan, container, false);

        listView = (ListView) view.findViewById(android.R.id.list);
        listView.setOnItemClickListener(this);
        listView.setItemsCanFocus(true);


        adapter = new AdapterRelayPlan(getActivity() , R.layout.relay_plan_item , array_data);

        listView.setAdapter(adapter);

        registerForContextMenu(listView);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.attachToListView(listView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(ActivityRelayDetail.createNewIntent(getActivity()));

            }

        });

        return view;

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        startActivity(ActivityRelayDetail.createNamedIntent(getActivity() , array_data[position]));
    }
}
