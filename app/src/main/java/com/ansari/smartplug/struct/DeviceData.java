package com.ansari.smartplug.struct;

import com.ansari.smartplug.activities.App;
import com.ansari.smartplug.interfaces.OnDeviceDataReadListener;
import com.ansari.smartplug.utils.Tools;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Eabasir on 3/29/2016.
 */
public class DeviceData {

    final int SKIP_COUNTER = 7;
    final int CH0_SENSOR_VALUE = 4;
    final int CH1_SENSOR_VALUE = 4;
    final int CH2_SENSOR_VALUE = 4;
    final int CH3_SENSOR_VALUE = 4;
    final int CH4_SENSOR_VALUE = 4;
    final int CH5_SENSOR_VALUE = 4;
    final int NOW_DEV_TIME_DATE = 8;
    final int DEV_RELAY_VALUES = 1;
    final int DEV_EVENT_ALARM_NUM = 2;
    final int DEV_RECORD_NUM = 4;
    final int RESERV = 1;

    public float Temp = -1f;
    public float Humidity = -1f;
    public float light = -1f;
    public float Gas = -1f;
    public float Res1 = -1f;
    public float Res2 = -1f;

    public Date Dev_DateTime;

    public boolean RL0 = false;
    public boolean RL1 = false;
    public boolean RL2 = false;
    public boolean RL3 = false;
    public boolean RL4 = false;
    public boolean RL5 = false;
    public boolean RL6 = false;
    public boolean RL7 = false;

    public int AlarmNum = -1;
    public int RecordNum = -1;


    int counter = 0;


    public DeviceData(byte[] input) {

        try {
            counter = SKIP_COUNTER;
            byte[] slice;


            slice = Arrays.copyOfRange(input, counter, counter += CH0_SENSOR_VALUE );
            Temp = ByteBuffer.wrap(slice).order(ByteOrder.LITTLE_ENDIAN).getFloat();

            slice = Arrays.copyOfRange(input, counter, counter += CH1_SENSOR_VALUE );
            Humidity = ByteBuffer.wrap(slice).order(ByteOrder.LITTLE_ENDIAN).getFloat();

            slice = Arrays.copyOfRange(input, counter, counter += CH2_SENSOR_VALUE );
            light = ByteBuffer.wrap(slice).order(ByteOrder.LITTLE_ENDIAN).getFloat();

            slice = Arrays.copyOfRange(input, counter, counter += CH3_SENSOR_VALUE );
            Gas = ByteBuffer.wrap(slice).order(ByteOrder.LITTLE_ENDIAN).getFloat();

            slice = Arrays.copyOfRange(input, counter, counter += CH4_SENSOR_VALUE );
            Res1 = ByteBuffer.wrap(slice).order(ByteOrder.LITTLE_ENDIAN).getFloat();

            slice = Arrays.copyOfRange(input, counter, counter += CH5_SENSOR_VALUE );
            Res2 = ByteBuffer.wrap(slice).order(ByteOrder.LITTLE_ENDIAN).getFloat();

            slice = Arrays.copyOfRange(input, counter, counter += NOW_DEV_TIME_DATE );
            Dev_DateTime = Tools.byteArrayToDateTime(slice , 8);


            slice = Arrays.copyOfRange(input, counter, counter += DEV_RELAY_VALUES );
            boolean[] RLStates = Tools.bytesToBooleans(slice);

            if (RLStates.length == 8)
            {
                RL0 = RLStates[0];
                RL1 = RLStates[1];
                RL2 = RLStates[2];
                RL3 = RLStates[3];
                RL4 = RLStates[4];
                RL5 = RLStates[5];
                RL6 = RLStates[6];
                RL7 = RLStates[7];

            }

            slice = Arrays.copyOfRange(input, counter, counter += DEV_EVENT_ALARM_NUM );
            AlarmNum = Tools.unsignedIntFromByteArray(slice);

            slice = Arrays.copyOfRange(input, counter, counter += DEV_RECORD_NUM );
            RecordNum = Tools.unsignedIntFromByteArray(slice);


            for (OnDeviceDataReadListener onDeviceDataReadListener : App
                    .getInstance().getUIListeners(
                            OnDeviceDataReadListener.class))
                onDeviceDataReadListener.onDeviceDataRead(light ,Humidity , Temp , Gas , RL7);




        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
