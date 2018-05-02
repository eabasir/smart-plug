package com.ansari.smartplug.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ansari.smartplug.R;
import com.ansari.smartplug.activities.App;
import com.ansari.smartplug.interfaces.OnDeviceDateTimeReadListener;
import com.ansari.smartplug.interfaces.OnDeviceDateTimeWriteListener;
import com.ansari.smartplug.network.AsyncClient;
import com.ansari.smartplug.network.NetConfig;
import com.ansari.smartplug.setting.AppSettings;
import com.ansari.smartplug.utils.Tools;

import java.util.Calendar;
import java.util.Date;


public class DateTimeSettingFragment extends Fragment implements OnDeviceDateTimeReadListener, OnDeviceDateTimeWriteListener {


    View view;

    TextView txtDate;
    TextView txtTime;
    ImageButton btnSync;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_date_time, container, false);

        txtDate = (TextView) view.findViewById(R.id.txtDate);
        btnSync = (ImageButton) view.findViewById(R.id.btnSync);
        txtTime = (TextView) view.findViewById(R.id.txtTime);


        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateTime();
            }
        });

        new AsyncClient(NetConfig.DEVICE_IP
                , NetConfig.DEVICE_PORT
                , NetConfig.ADD_DEVICE_DATE_TIME_READ
                , Tools.hexStringToByteArray(NetConfig.ADD_DEVICE_DATE_TIME_READ)).execute();

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        App.getInstance().addUIListener(OnDeviceDateTimeReadListener.class, this);
        App.getInstance().addUIListener(OnDeviceDateTimeWriteListener.class, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        App.getInstance().removeUIListener(OnDeviceDateTimeReadListener.class, this);
        App.getInstance().removeUIListener(OnDeviceDateTimeWriteListener.class, this);

    }

    @Override
    public void onDeviceDateTimeWrite() {

            new AsyncClient(NetConfig.DEVICE_IP
                    , NetConfig.DEVICE_PORT
                    , NetConfig.ADD_DEVICE_DATE_TIME_READ
                    , Tools.hexStringToByteArray(NetConfig.ADD_DEVICE_DATE_TIME_READ)).execute();

    }

    @Override
    public void onDeviceDateTimeRead(Date date) {

        txtDate.setText(Tools.getStringFromDate(date).split(" ")[0]);
        txtTime.setText(Tools.getStringFromDate(date).split(" ")[1]);

        if (AppSettings.getInstance(App.getInstance()).isDateTimeSyncedForFirstTime())
            Toast.makeText(getActivity(), "Date & Time have been synchronized", Toast.LENGTH_SHORT).show();


        AppSettings.getInstance(App.getInstance()).syncDateTimeForFirstTime(true);



    }


    void setDateTime() {
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);


        String query = Tools.DateTimeToString8Mode(year, month, day, hour, min, sec);

        new AsyncClient(NetConfig.DEVICE_IP
                , NetConfig.DEVICE_PORT
                , NetConfig.ADD_DEVICE_DATE_TIME_WRITE
                , Tools.hexStringToByteArray(NetConfig.ADD_DEVICE_DATE_TIME_WRITE + query)).execute();

    }


}
