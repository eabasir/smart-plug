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
public class AdapterWifi extends ArrayAdapter<AdapterWifi.Data> {

    Context mContext;
    int layoutResourceId;
    ArrayList<Data> data = null;

    public static class Data {
        public String Name;
        public boolean State;

    }

    public AdapterWifi(Context mContext, int layoutResourceId,ArrayList<Data>  data) {

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

        Data obj = data.get(position);

        TextView txtSSID = (TextView) convertView.findViewById(R.id.txtSSID);
        ImageView imgState = (ImageView) convertView.findViewById(R.id.imgState);


        txtSSID.setText(obj.Name);

        if (obj.State)
            imgState.setVisibility(View.VISIBLE);
        else
        imgState.setVisibility(View.GONE);

        return convertView;

    }

}

