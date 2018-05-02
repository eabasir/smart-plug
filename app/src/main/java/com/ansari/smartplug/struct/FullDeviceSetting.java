package com.ansari.smartplug.struct;

import com.ansari.smartplug.utils.Tools;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by Eabasir on 3/29/2016.
 */
public class FullDeviceSetting {

    final int SKIP_COUNTER = 7;
    final int DEV_NAME = 24;
    final int DEV_SN = 4;
    final int DEV_VER = 4;
    final int DEV_ICON_TYPE = 1;
    final int DEV_WIFI_NAME = 24;
    final int DEV_WIFI_PASS = 16;
    final int DEV_WIFI_SET = 4;
    final int DEV_WIFI_SERVER = 4;
    final int DEV_WIFI_PORT = 4;
    final int DEV_MCYCLE = 1;
    final int DEV_REC_LEN = 1;
    final int DEV_RES_LEN = 35;

    public String Name = "";
    public int SN = -1;
    public String Ver = "";
    public char IconType = '\u0000';
    public String WifiName = "";
    public String WifiPass = "";

    int counter = 0;


    public FullDeviceSetting(byte[] input) {

        try {
            counter = SKIP_COUNTER;
            byte[] slice;


            slice = Arrays.copyOfRange(input, counter, counter += DEV_NAME );
            Name = new String(slice,  "UTF-8").trim();;

            slice = Arrays.copyOfRange(input, counter, counter += DEV_SN);
            SN = Tools.unsignedIntFromByteArray(slice);

            slice = Arrays.copyOfRange(input, counter, counter += DEV_VER);
            Ver = new String(slice, "UTF-8").trim();

            slice = Arrays.copyOfRange(input, counter, counter += DEV_ICON_TYPE );
            IconType = (char) slice[0];

            slice = Arrays.copyOfRange(input, counter, counter += DEV_WIFI_NAME );
            WifiName = new String(slice, "UTF-8").trim();

            slice = Arrays.copyOfRange(input, counter, counter += DEV_WIFI_PASS );
            WifiPass = new String(slice, "UTF-8").trim();


            String s ="";

        }

        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
