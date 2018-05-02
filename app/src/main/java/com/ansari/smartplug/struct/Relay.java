package com.ansari.smartplug.struct;

import com.ansari.smartplug.activities.App;
import com.ansari.smartplug.interfaces.OnRelayConfigReadListener;
import com.ansari.smartplug.utils.Tools;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by Eabasir on 3/29/2016.
 */
public class Relay {

    public static enum Type {
        Once,
        Daily,
        Weekly,
        Monthly
    }

    final int SKIP_COUNTER = 7;
    final int REL_DEF = 1; // 0=>Open(disconnect),1=>Close(connect)
    public static String relayDefult = "00";

    final int REL_TYPE = 1; // M: manually (1 byte of startTime will be used as on 00 , off 01),  T: dateTime (start and stop time will be used completely) ,
    // S: Sensor (2 bytes of startTime will be used: 1 byte indicates that wich sensor is used (i.e 0-4) , 1 byte indicates state of working sensor
    // (L: lower than lower value , N: normal => in the range , H: upper than uppper value , A: anormal => out of range)

    // Assci Codes :
    public static String Manulal = "4D";
    public static String Time = "54";
    public static String Sensor = "53";


    final int REL_START_TIME = 6;
    final int REL_STOP_TIME = 6;
    final int REL_REP_TYPE = 1; // O=>Once,D>Daily,W=>Weekly,M=>Monthly

    // Assci Codes :
    public static String Once = "4F";
    public static String Daily = "44";
    public static String Weekly = "57";
    public static String Monthly = "4D";


    final int REL_RES = 1;

    int counter = 0;

    int defult = -1;
    String type = "";


    public Date startDate = null;
    public Date finishDate = null;

    public String repType = "";



    public Relay(byte[] input) {

        try {
            counter = SKIP_COUNTER;
            byte[] slice;


            slice = Arrays.copyOfRange(input, counter, counter += REL_DEF);
            defult = Tools.OneByteToShort(slice);

            slice = Arrays.copyOfRange(input, counter, counter += REL_TYPE);
            type = Tools.byteArrayToHexString(slice);

            if (type.equals(Time)) {

                slice = Arrays.copyOfRange(input, counter, counter += REL_START_TIME);
                startDate = Tools.byteArrayToDateTime(slice, 6);

                slice = Arrays.copyOfRange(input, counter, counter += REL_STOP_TIME);
                finishDate = Tools.byteArrayToDateTime(slice, 6);

                slice = Arrays.copyOfRange(input, counter, counter += REL_REP_TYPE);
                repType = Tools.byteArrayToHexString(slice);

                for (OnRelayConfigReadListener onRelayConfigReadListener : App
                        .getInstance().getUIListeners(
                                OnRelayConfigReadListener.class))
                    onRelayConfigReadListener.onRelayConfigRead(startDate, finishDate, repType);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
