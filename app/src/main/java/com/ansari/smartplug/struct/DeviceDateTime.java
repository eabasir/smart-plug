package com.ansari.smartplug.struct;

import com.ansari.smartplug.activities.App;
import com.ansari.smartplug.interfaces.OnDeviceDateTimeReadListener;
import com.ansari.smartplug.utils.Tools;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by Eabasir on 3/29/2016.
 */
public class DeviceDateTime {

    final int SKIP_COUNTER = 7;
    final int DEV_DT_YEAR = 2;
    final int DEV_DT_MONTH = 1;
    final int DEV_DT_DAY = 1;
    final int DEV_DT_HOUR = 1;
    final int DEV_DT_MINUTE = 1;
    final int DEV_DT_SEC = 1;

    int counter = 0;

    int Year = -1;
    int Month = -1;
    int Day = -1;
    int Hour = -1;
    int Minute = -1;
    int Second = -1;


    public DeviceDateTime(byte[] input) {

        try {
            counter = SKIP_COUNTER;
            byte[] slice;


            slice = Arrays.copyOfRange(input, counter, counter += DEV_DT_SEC);
            Second = Tools.OneByteToShort(slice);

            slice = Arrays.copyOfRange(input, counter, counter += DEV_DT_MINUTE);
            Minute = Tools.OneByteToShort(slice);

            slice = Arrays.copyOfRange(input, counter, counter += DEV_DT_HOUR);
            Hour = Tools.OneByteToShort(slice);

            slice = Arrays.copyOfRange(input, counter, counter += DEV_DT_DAY);
            Day = Tools.OneByteToShort(slice);

            slice = Arrays.copyOfRange(input, counter, counter += DEV_DT_MONTH);
            Month = Tools.OneByteToShort(slice);

            slice = Arrays.copyOfRange(input, counter, counter += DEV_DT_YEAR);
            Year = Tools.TwoByteToShort(slice);

            Date date = Tools.getDateFromString(Year + "/" + Month + "/" + Day + " " + Hour + ":" + Minute + ":" + Second);

            for (OnDeviceDateTimeReadListener onDeviceDateTimeReadListener : App
                    .getInstance().getUIListeners(
                            OnDeviceDateTimeReadListener.class))
                onDeviceDateTimeReadListener.onDeviceDateTimeRead(date);

            String s = "";

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
