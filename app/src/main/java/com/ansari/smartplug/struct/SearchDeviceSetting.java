package com.ansari.smartplug.struct;

import com.ansari.smartplug.utils.Tools;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by Eabasir on 3/29/2016.
 */
public class SearchDeviceSetting {

    final int SKIP_COUNTER = 7;
    final int DEV_NAME = 24;
    final int DEV_SN = 4;
    final int DEV_VER = 4;
    final int DEV_ICON_TYPE = 1;

    public String Name = "";
    public int SN = -1;
    public String Ver = "";
    public char IconType = '\u0000';

    int counter = 0;


    public SearchDeviceSetting(byte[] input) {

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
