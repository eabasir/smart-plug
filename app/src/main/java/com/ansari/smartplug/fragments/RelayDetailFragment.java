package com.ansari.smartplug.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ansari.smartplug.R;
import com.ansari.smartplug.activities.ActivityDateTime;
import com.ansari.smartplug.activities.App;
import com.ansari.smartplug.interfaces.OnRelayConfigReadListener;
import com.ansari.smartplug.interfaces.OnRelayConfigWriteListener;
import com.ansari.smartplug.network.AsyncClient;
import com.ansari.smartplug.network.NetConfig;
import com.ansari.smartplug.setting.AppSettings;
import com.ansari.smartplug.struct.Relay;
import com.ansari.smartplug.utils.Tools;

import java.util.Calendar;
import java.util.Date;


public class RelayDetailFragment extends Fragment implements OnRelayConfigReadListener, OnRelayConfigWriteListener {


    enum Status {
        Start,
        Finish
    }

    LinearLayout btnNext;


    TextView txtStartDate;
    TextView txtStartTime;
    TextView txtFinishDate;
    TextView txtFinishTime;
    RadioGroup rgRepType;


    View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_relay_detail, container, false);

        RelativeLayout btnEditStart = (RelativeLayout) view.findViewById(R.id.btnEditStart);
        RelativeLayout btnEditFinish = (RelativeLayout) view.findViewById(R.id.btnEditFinish);
        txtStartDate = (TextView) view.findViewById(R.id.txtStartDate);
        txtStartTime = (TextView) view.findViewById(R.id.txtStartTime);
        txtFinishDate = (TextView) view.findViewById(R.id.txtFinishDate);
        txtFinishTime = (TextView) view.findViewById(R.id.txtFinishTime);
        rgRepType = (RadioGroup) view.findViewById(R.id.rgRepType);


        btnEditStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDatePeaker(Status.Start);
            }
        });
        btnEditFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDatePeaker(Status.Finish);
            }
        });

        if (AppSettings.getInstance(App.getInstance()).isDateTimeSyncedForFirstTime()) {
            new AsyncClient(NetConfig.DEVICE_IP
                    , NetConfig.DEVICE_PORT
                    , NetConfig.ADD_RELAY_READ
                    , Tools.hexStringToByteArray(NetConfig.ADD_RELAY_READ)).execute();
        } else {
            startActivity(ActivityDateTime.createIntent(getActivity()));
        }

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        App.getInstance().addUIListener(OnRelayConfigReadListener.class, this);
        App.getInstance().addUIListener(OnRelayConfigWriteListener.class, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        App.getInstance().removeUIListener(OnRelayConfigReadListener.class, this);
        App.getInstance().removeUIListener(OnRelayConfigWriteListener.class, this);

    }

    void ShowDatePeaker(final Status status) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.date_peaker);
        btnNext = (LinearLayout) dialog.findViewById(R.id.btnNext);
        final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);

        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePicker.updateDate(year, month, day);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (status) {
                    case Start:
                        txtStartDate.setText(datePicker.getYear() + "/" + (datePicker.getMonth() + 1) + "/" + datePicker.getDayOfMonth());
                        break;
                    case Finish:
                        txtFinishDate.setText(datePicker.getYear() + "/" + (datePicker.getMonth() + 1) + "/" + datePicker.getDayOfMonth());
                        break;
                }


                dialog.dismiss();
                ShowTimePeaker(status);


            }
        });

        dialog.show();
    }

    void ShowTimePeaker(final Status status) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.time_peaker);
        btnNext = (LinearLayout) dialog.findViewById(R.id.btnNext);
        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);


        Calendar cal = Calendar.getInstance();

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);

        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(min);


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (status) {
                    case Start:
                        txtStartTime.setText(timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute() + ":00");
                        break;
                    case Finish:
                        txtFinishTime.setText(timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute() + ":00");
                        break;
                }


                dialog.dismiss();

            }
        });
        dialog.show();


    }


    public void setRelayData() {

        try {
            int selectedId = rgRepType.getCheckedRadioButtonId();
            RadioButton rbType = (RadioButton) view.findViewById(selectedId);


            Date[] dates = getDateTimes();
            if (dates != null) {


                String typeCode = "";

                if (rbType.getText().toString().equals(Relay.Type.Once.toString())) {

                    if (dates[1].getTime() - dates[0].getTime() < 1000 * 3600 * 24) {
                        typeCode = "4F";
                    } else {
                        Toast.makeText(getActivity(), "The Time period is not set correctly", Toast.LENGTH_SHORT).show();
                        return;
                    }


                } else if (rbType.getText().toString().equals(Relay.Type.Daily.toString())) {

                    if (dates[1].getTime() - dates[0].getTime() < 1000 * 3600 * 24) {
                        typeCode = "44";
                    } else {
                        Toast.makeText(getActivity(), "The Time period is not set correctly", Toast.LENGTH_SHORT).show();
                        return;
                    }

                } else if (rbType.getText().toString().equals(Relay.Type.Weekly.toString())) {
                    if (dates[1].getTime() - dates[0].getTime() < 1000 * 3600 * 24 * 7) {
                        typeCode = "57";
                    } else {
                        Toast.makeText(getActivity(), "The Time period is not set correctly", Toast.LENGTH_SHORT).show();
                        return;
                    }

                } else if (rbType.getText().toString().equals(Relay.Type.Monthly.toString())) {

                    Calendar start = Calendar.getInstance();
                    start.setTime(dates[0]);

                    Calendar finish = Calendar.getInstance();
                    finish.setTime(dates[0]);

                    if (start.get(Calendar.MONTH) == finish.get(Calendar.MONTH)) {
                        typeCode = "4D";
                    } else {
                        Toast.makeText(getActivity(), "The Time period is not set correctly", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }

                String[] hexs = getDateTimesHex(dates);

                String query = Relay.relayDefult + Relay.Time + hexs[0] + hexs[1] + typeCode;
                byte[] input = Tools.hexStringToByteArray(NetConfig.ADD_RELAY_WRITE + query);
                if (input != null) {
                    new AsyncClient(NetConfig.DEVICE_IP
                            , NetConfig.DEVICE_PORT
                            , NetConfig.ADD_RELAY_WRITE
                            , input).execute();

                } else
                    Toast.makeText(getActivity(), "input data is not in correct structure", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getActivity(), "Date and Times are not set correctly", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    Date[] getDateTimes() {

        Date[] result = null;


        try {


            String startDate = txtStartDate.getText().toString();
            String startTime = txtStartTime.getText().toString();
            String finishDate = txtFinishDate.getText().toString();
            String finishTime = txtFinishTime.getText().toString();


            if (!startDate.contains("/") || startDate.split("/").length != 3 ||
                    !finishDate.contains("/") || finishDate.split("/").length != 3 ||
                    !startTime.contains(":") || startTime.split(":").length != 3 ||
                    !finishTime.contains(":") || finishTime.split(":").length != 3)
                return null;


            Date startDateTime = Tools.getDateFromString(startDate + " " + startTime);
            Date finishDateTime = Tools.getDateFromString(finishDate + " " + finishTime);


            if (startDateTime != null && finishDateTime != null) {
                result = new Date[2];
                result[0] = startDateTime;
                result[1] = finishDateTime;

            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }


    String[] getDateTimesHex(Date[] dates) {
        String[] result = null;

        try {

            Calendar start = Calendar.getInstance();
            start.setTime(dates[0]);

            Calendar finish = Calendar.getInstance();
            finish.setTime(dates[1]);


            result = new String[2];


            result[0] = Tools.DateTimeToString6Mode(start.get(Calendar.YEAR), start.get(Calendar.MONTH) + 1, start.get(Calendar.DAY_OF_MONTH), start.get(Calendar.HOUR_OF_DAY), start.get(Calendar.MINUTE), start.get(Calendar.SECOND));
            result[1] = Tools.DateTimeToString6Mode(finish.get(Calendar.YEAR), finish.get(Calendar.MONTH) + 1, finish.get(Calendar.DAY_OF_MONTH), finish.get(Calendar.HOUR_OF_DAY), finish.get(Calendar.MINUTE), finish.get(Calendar.SECOND));

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public void onRelayConfigRead(Date startDate, Date finishDate, String repType) {
        if (startDate != null && finishDate != null) {
            String[] start = Tools.getStringFromDate(startDate).split(" ");
            String[] finish = Tools.getStringFromDate(finishDate).split(" ");

            txtStartDate.setText(start[0]);
            txtStartTime.setText(start[1]);
            txtFinishDate.setText(finish[0]);
            txtFinishTime.setText(finish[1]);

            if (repType.equals(Relay.Once.toString())) {
                rgRepType.check(R.id.rbOnce);
            } else if (repType.equals(Relay.Daily.toString())) {
                rgRepType.check(R.id.rbDaily);

            } else if (repType.equals(Relay.Weekly.toString())) {
                rgRepType.check(R.id.rbWeekly);

            } else if (repType.equals(Relay.Monthly.toString())) {
                rgRepType.check(R.id.rbMonthly);

            }


        }
    }

    @Override
    public void onRelayConfigWrite() {
        Toast.makeText(getActivity(), "Relay data has been set", Toast.LENGTH_SHORT).show();
    }


}
