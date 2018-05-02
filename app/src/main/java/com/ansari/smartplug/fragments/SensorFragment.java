package com.ansari.smartplug.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.ansari.smartplug.R;
import com.ansari.smartplug.activities.ActivityDateTime;
import com.ansari.smartplug.activities.App;
import com.ansari.smartplug.interfaces.OnDeviceDataReadListener;
import com.ansari.smartplug.network.AsyncClient;
import com.ansari.smartplug.network.NetConfig;
import com.ansari.smartplug.setting.AppSettings;
import com.ansari.smartplug.utils.Tools;

import pl.pawelkleczkowski.customgauge.CustomGauge;


public class SensorFragment extends Fragment implements OnDeviceDataReadListener {


    private TextView txtLight;
    private TextView txtHumidity;
    private TextView txtTemprature;
    private TextView txtGas;
    private TextView txtGasState;

    private CustomGauge gasGauge;

    private Switch swRefresh;
    private CheckBox chkRelay;

    View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_sensor, container, false);

        txtLight = (TextView) view.findViewById(R.id.txtLight);
        txtHumidity = (TextView) view.findViewById(R.id.txtHumidity);
        txtTemprature = (TextView) view.findViewById(R.id.txtTemprature);
        txtGas = (TextView) view.findViewById(R.id.txtGas);
        txtGas = (TextView) view.findViewById(R.id.txtGas);
        txtGasState = (TextView) view.findViewById(R.id.txtGasState);
        chkRelay = (CheckBox) view.findViewById(R.id.chkRelay);

        gasGauge = (CustomGauge) view.findViewById(R.id.gasGauge);
        swRefresh = (Switch) view.findViewById(R.id.swRefresh);

        swRefresh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    AppSettings.getInstance(getActivity()).setSensorAutoRefresh(true);
                    startAutoRefresh();

                } else {
                    AppSettings.getInstance(getActivity()).setSensorAutoRefresh(false);
                    stopAutoRefresh();

                }
            }
        });


        if (!AppSettings.getInstance(App.getInstance()).isDateTimeSyncedForFirstTime()) {
            startActivity(ActivityDateTime.createIntent(getActivity()));
            getActivity().finish();

        }

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        App.getInstance().addUIListener(OnDeviceDataReadListener.class, this);

        new AsyncClient(NetConfig.DEVICE_IP
                , NetConfig.DEVICE_PORT
                , NetConfig.ADD_DEVICE_DATA_READ
                , Tools.hexStringToByteArray(NetConfig.ADD_DEVICE_DATA_READ)).execute();

        if (AppSettings.getInstance(getActivity()).getSensorAutoRefresh()) {
            if (swRefresh.isChecked()) {
                AppSettings.getInstance(getActivity()).setSensorAutoRefresh(true);
                startAutoRefresh();
            } else
                swRefresh.setChecked(true);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        App.getInstance().removeUIListener(OnDeviceDataReadListener.class, this);

        stopAutoRefresh();
    }


    @Override
    public void onDeviceDataRead(float light, float humidity, float temprature, float gas, boolean relay) {


        if (gas < 150) {
            txtGasState.setText(R.string.alarm_ok);
            txtGasState.setTextColor(ContextCompat.getColor(getActivity(), R.color.alarem_green));
        } else if (gas >= 150 && gas < 200) {
            txtGasState.setText(R.string.alarm_caution);
            txtGasState.setTextColor(ContextCompat.getColor(getActivity(), R.color.alarem_orange));

        } else if (gas > 200) {
            txtGasState.setText(R.string.alarm_danger);
            txtGasState.setTextColor(ContextCompat.getColor(getActivity(), R.color.alarem_red));

        }
        ViewAnimation a1 = new ViewAnimation(txtTemprature, 0, temprature);
        a1.setDuration(1000);
        txtTemprature.startAnimation(a1);

        ViewAnimation a2 = new ViewAnimation(txtLight, 0, (int) ((light / 255f) * 100));
        a2.setDuration(1000);
        txtLight.startAnimation(a2);

        ViewAnimation a3 = new ViewAnimation(txtHumidity, 0, humidity);
        a3.setDuration(1000);
        txtHumidity.startAnimation(a3);

        ViewAnimation a4 = new ViewAnimation(txtGas, 0, gas);
        a4.setDuration(1000);
        txtGas.startAnimation(a4);

        ViewAnimation a5 = new ViewAnimation(gasGauge, 0, gas);
        a5.setDuration(1000);
        gasGauge.startAnimation(a5);


        chkRelay.setChecked(relay);


    }

    public class ViewAnimation extends Animation {
        private View view;
        private float from;
        private float to;

        public ViewAnimation(View view, float from, float to) {
            super();
            this.view = view;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = from + (to - from) * interpolatedTime;

            if (view instanceof CustomGauge)
                ((CustomGauge) view).setValue((int) value);
            if (view instanceof TextView)
                ((TextView) view).setText((int) value + "");

        }

    }

    Handler handler = new Handler();

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {

            new AsyncClient(NetConfig.DEVICE_IP
                    , NetConfig.DEVICE_PORT
                    , NetConfig.ADD_DEVICE_DATA_READ
                    , Tools.hexStringToByteArray(NetConfig.ADD_DEVICE_DATA_READ)).execute();

            handler.postDelayed(runnableCode, 10000);
        }
    };


    private void startAutoRefresh() {

        handler.post(runnableCode);

    }

    private void stopAutoRefresh() {
        handler.removeCallbacks(runnableCode);
    }

}
