package com.ansari.smartplug.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ansari.smartplug.R;

import java.util.ArrayList;

// here's our beautiful adapter
public class AdapterRelayPlan extends ArrayAdapter<String> {

    Context mContext;
    int layoutResourceId;
    String[] data = null;


    public AdapterRelayPlan(Context mContext, int layoutResourceId, String[] data) {

        super(mContext, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        String name = data[position];

        TextView txtPlanName = (TextView) convertView.findViewById(R.id.txtPlanName);


        txtPlanName.setText(name);
        return convertView;

    }

}

